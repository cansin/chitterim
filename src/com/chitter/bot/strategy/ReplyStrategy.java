package com.chitter.bot.strategy;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.BitlyAPI;
import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class ReplyStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		String messageBody = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();
		String receiverScreenName = messageBody.substring(0, messageBody.indexOf(' ')).trim();
		messageBody = messageBody.substring(messageBody.indexOf(' '));

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
		try {
			messageBody = "@"+receiverScreenName+" "+BitlyAPI.shortenUrls(messageBody);
			if(twitter.existsFriendship(receiverScreenName,twitter.getScreenName())){
				if(messageBody.length()<140) {
					StatusUpdate statusUpdate=new StatusUpdate(messageBody);
					statusUpdate.setInReplyToStatusId(twitter.getUserTimeline(receiverScreenName).get(0).getId());
					twitter.updateStatus(statusUpdate);
					replyToMessage(message, "You sent reply to _*" + receiverScreenName +"*_ 's last status.");
				} else {
					replyToMessage(message, "Your message has "+(messageBody.length()-140)+" extra characters.");
				}
			} else {
				replyToMessage(message, "You cannot reply to _*" + receiverScreenName +"*_ 's status.");
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
