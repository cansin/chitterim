package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class RetweetStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message)  throws TwitterException {
		String retweetedScreenName = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);

		twitter.retweetStatus(twitter.getUserTimeline(retweetedScreenName).get(0).getId());
		replyToMessage(message, "You retweeted _*"+retweetedScreenName+"*_ last status update.");
		
	}

}
