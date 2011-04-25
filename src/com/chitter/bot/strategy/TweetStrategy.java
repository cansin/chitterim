package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.BitlyAPI;
import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.Message;

public class TweetStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		String messageBody = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);

		try {
			messageBody = BitlyAPI.shortenUrls(messageBody);
			if(messageBody.length()<140) {
				twitter.updateStatus(messageBody);
				replyToMessage(message, "Your tweet has been sent.");
			} else {
				replyToMessage(message, "Your message has "+(messageBody.length()-140)+" extra characters.");
			}
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "Boss, I couldn't tweet "+userAccount.getGtalkId()+"'s message.");
			replyToMessage(message, "I couldn't send your tweet.");	
		}	
	}

}
