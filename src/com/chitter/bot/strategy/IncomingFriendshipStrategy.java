package com.chitter.bot.strategy;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class IncomingFriendshipStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) throws TwitterException {
		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);

		IDs ids = twitter.getIncomingFriendships(-1);
		int[] idsArr=ids.getIDs();
		if(idsArr.length!=0){
			String msgBody="Your pending follow requests are:\n";
			for(int id : idsArr){
				User user=twitter.showUser(id);
				msgBody+="_"+user.getName()+"_"+" a.k.a _*"+user.getScreenName()+"*_\n";
			}
			replyToMessage(message,msgBody);
		} else {
			replyToMessage(message,"There are no pending requests.");
		}
	}

}
