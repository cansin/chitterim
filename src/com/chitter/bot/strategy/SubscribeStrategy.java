package com.chitter.bot.strategy;

import twitter4j.TwitterException;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class SubscribeStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message)  throws TwitterException {
		replyToMessage(message, "You should subscribe me at http://aspectjchitter.appspot.com !");
	}

}
