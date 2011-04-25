package com.chitter.bot.strategy;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.Message;

public class IncomingFriendshipStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		try {
			Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
	
			IDs ids;
			ids = twitter.getIncomingFriendships(-1);
			long[] idsArr=ids.getIDs();
			if(idsArr.length!=0){
				String msgBody="Your pending follow requests are:\n";
				for(long id : idsArr){
					User user=twitter.showUser(id);
					msgBody+="_"+user.getName()+"_"+" a.k.a _*"+user.getScreenName()+"*_\n";
				}
				replyToMessage(message,msgBody);
			} else {
				replyToMessage(message,"There are no pending requests.");
			}
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "Boss, I couldn't get incoming friendships for "+userAccount.getGtalkId());
			replyToMessage(message, "I couldn't get your incoming friendships.");
		}
	}

}
