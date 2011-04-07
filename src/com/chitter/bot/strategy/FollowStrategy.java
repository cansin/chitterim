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
		try {
			if(!twitter.existsFriendship(twitter.getScreenName(), followeeScreenName)){
				twitter.createFriendship(followeeScreenName);
				replyToMessage(message, "You start following _*"+followeeScreenName+"*_.");	
			} else {
				replyToMessage(message, "You are already following _*"+followeeScreenName+"*_.");
			}
		} catch (TwitterException e) {
			/**
			 *  If existsFriendship throws an exception, 
			 *  we know that followee is a protected non-friend.
			 */
			twitter.createFriendship(followeeScreenName);
			replyToMessage(message, "You send a request to follow _*"+followeeScreenName+"*_.");		
		}
	}

}
