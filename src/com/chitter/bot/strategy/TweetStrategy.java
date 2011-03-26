package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.BitlyAPI;
import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class TweetStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message)  throws TwitterException {
		String messageBody = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);

		try {
			twitter.updateStatus(BitlyAPI.shortenUrls(messageBody));
		} catch (TwitterException e) {
			System.err.println("Boss, I couldn't tweet "+userAccount.getGtalkId()+"'s message. ");
			System.err.println("-----------TweetStrategy-Exception-----------------");
			for(int i=0;i<e.getStackTrace().length;i++)
				System.err.println(e.getStackTrace()[i].toString());
			System.err.println("---------------------------------------------------");
		}
		replyToMessage(message, "Your tweet has been sent.");	
	}

}
