package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.google.appengine.api.xmpp.Message;

public class UnfollowStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) throws TwitterException {
		String unfolloweeScreenName = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
		if(!twitter.existsFriendship(twitter.getScreenName(), unfolloweeScreenName)){
			replyToMessage(message, "You are already _not_ following _*"+unfolloweeScreenName+"*_.");
		} else {
			twitter.destroyFriendship(unfolloweeScreenName);
			replyToMessage(message, "You stop following _*"+unfolloweeScreenName+"*_.");
		}
	}

}
