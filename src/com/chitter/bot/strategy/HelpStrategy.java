package com.chitter.bot.strategy;

import twitter4j.TwitterException;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class HelpStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) throws TwitterException {
		replyToMessage(message, "You can order me:\n" +
				"_*/h*_ to show this help.\n" +
				"_*/on*_ to receive timeline updates\n" +
				"_*/off*_ to _not_ receive timeline updates\n" +
				"_*/t*_ to send tweets.\n"+
				"_*/rt*_ _*user*_ to retweet a user's _last_ message.\n" +
				"_*/d*_ _*user*_ to send direct message.\n" +
				"_*/f*_ _*user*_ to follow someone.\n" +
				"_*/u*_ _*user*_ to unfollow someone.\n" +
		"_*/i*_ to show incoming friendships.\n");
	}

}
