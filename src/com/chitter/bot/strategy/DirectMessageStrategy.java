package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class DirectMessageStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		String messageBody = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();
		String receiverScreenName = messageBody.substring(0, messageBody.indexOf(' ')).trim();
		messageBody = messageBody.substring(messageBody.indexOf(' '));

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
		try {
			if(twitter.existsFriendship(receiverScreenName,twitter.getScreenName())){
				twitter.sendDirectMessage(receiverScreenName, messageBody);
				replyToMessage(message, "You sent direct message to _*" + receiverScreenName +"*_.");
			} else {
				replyToMessage(message, "You cannot send direct message to _*" + receiverScreenName +"*_.");
			}
		} catch(TwitterException e) {
			/**
			 *  If existsFriendship throws an exception, 
			 *  we know that receiver is a protected non-friend.
			 */
			replyToMessage(message, "You cannot send direct message to _*" + receiverScreenName +"*_.");
		}
	}

}
