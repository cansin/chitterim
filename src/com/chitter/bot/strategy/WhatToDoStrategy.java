package com.chitter.bot.strategy;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class WhatToDoStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		replyToMessage(message,"Hey there! Don't sure what to do? Type _*/h*_ for help.");
	}

}
