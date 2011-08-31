package com.chitter.bot.strategy;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.Message;

public class FavoriteStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		String favoritedScreenName = message.getBody().substring(message.getBody().indexOf(' '), message.getBody().length()).trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);

		try {
			twitter.createFavorite(twitter.getUserTimeline(favoritedScreenName).get(0).getId());
			replyToMessage(message, "You mark _*"+favoritedScreenName+"*_ 's last status as _favorite_.");
		} catch (TwitterException e) {
			ExceptionPrinter.print(System.err, e, "Boss, I couldn't retweet "+favoritedScreenName+"'s tweet for "+userAccount.getGtalkId());
			replyToMessage(message, "I couldn't mark _*"+favoritedScreenName+"*_ 's last status as _favorite_.");
		}
		
	}

}
