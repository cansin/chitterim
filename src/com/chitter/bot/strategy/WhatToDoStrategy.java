package com.chitter.bot.strategy;

import twitter4j.TwitterException;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class WhatToDoStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message)  throws TwitterException {
		replyToMessage(message,"Hey there! Don't sure what to do? Type _*/h*_ for help.");
	}

}
