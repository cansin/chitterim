package com.chitter.bot.strategy;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class TimelineOffStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		userAccount.setIsTimelineActive(false);
		replyToMessage(message,"I will not talk to you anymore :-/.");
	}

}
