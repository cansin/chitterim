package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class UnfollowStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		String unfolloweeScreenName = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
		try {
			if(!twitter.existsFriendship(twitter.getScreenName(), unfolloweeScreenName)){
				replyToMessage(message, "You are already _not_ following _*"+unfolloweeScreenName+"*_.");
			} else {
				twitter.destroyFriendship(unfolloweeScreenName);
				replyToMessage(message, "You stop following _*"+unfolloweeScreenName+"*_.");
			}
		} catch(TwitterException e) {
			/**
			 *  If existsFriendship throws an exception, 
			 *  we know that unfollowee is a protected non-friend.
			 */
			replyToMessage(message, "You are already _not_ following _*"+unfolloweeScreenName+"*_.");
		}
	}

}
