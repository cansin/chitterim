package com.chitter.bot.strategy;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class SubscribeStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		replyToMessage(message, "You should subscribe me at http://chitterim.appspot.com !");
	}

}
