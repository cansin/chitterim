package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.Message;

public class RetweetStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		String retweetedScreenName = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);

		try {
			twitter.retweetStatus(twitter.getUserTimeline(retweetedScreenName).get(0).getId());
			replyToMessage(message, "You retweeted _*"+retweetedScreenName+"*_ last status update.");
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "Boss, I couldn't retweet "+retweetedScreenName+"'s tweet for "+userAccount.getGtalkId());
			replyToMessage(message, "I couldn't retweeted _*"+retweetedScreenName+"*_ last status update.");
		}
		
	}

}
