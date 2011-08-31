package com.chitter.bot.strategy;

import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class HelpStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		replyToMessage(message, "You can order me:\n" +
				"_*/h*_ to show this help.\n" +
				"_*/on*_ to receive timeline updates\n" +
				"_*/off*_ to _not_ receive timeline updates\n" +
				"_*/t*_ to send a tweet.\n"+
				"_*/r*_ _*user*_ to send a tweet in reply to a user's _last_ tweet.\n"+
				"_*/q*_ _*category*_ to send quotes.\n\tavailable categories at http://j.mp/fbSRSr\n"+
				"_*/rt*_ _*user*_ to retweet a user's _last_ tweet.\n" +
				"_*/fav*_ _*user*_ to mark a user's _last_ tweet as _favorite_.\n" +
				"_*/d*_ _*user*_ to send direct message.\n" +
				"_*/f*_ _*user*_ to follow someone.\n" +
				"_*/u*_ _*user*_ to unfollow someone.\n" +
				"_*/i*_ to show incoming friendship requests.\n");
	}

}
