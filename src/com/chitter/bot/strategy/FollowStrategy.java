package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class FollowStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) throws TwitterException {
		String followeeScreenName = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
		if(!twitter.existsFriendship(twitter.getScreenName(), followeeScreenName)){
			twitter.createFriendship(followeeScreenName);
			replyToMessage(message, "You start following _*"+followeeScreenName+"*_.");	
		} else {
			replyToMessage(message, "You are already following _*"+followeeScreenName+"*_.");
		}
	}

}
