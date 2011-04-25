package com.chitter.bot.strategy;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class TimelineOnStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		userAccount.setIsTimelineActive(true);
		replyToMessage(message,"Yeay, can I talk to you now? :)");
	}

}
