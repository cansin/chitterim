package com.chitter.external;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.chitter.persistence.UserAccount;


public class TwitterAPI extends AbstractAPI  {	
	private static final TwitterFactory tf = new TwitterFactory();

	public static String getChitterScreenName() {
		return Config.twChitterScreenName;
	}
	
	public static Twitter getInstanceForChitter() {
		return getInstanceFor(Config.twChitterToken,Config.twChitterSecret);
	}
	
	public static Twitter getInstanceFor(UserAccount userAccount){
		return getInstanceFor(userAccount.getTwitterAccessToken(),userAccount.getTwitterAccessTokenSecret());
	}

	public static Twitter getInstanceFor(String accessToken, String accessTokenSecret){
		ConfigurationBuilder cb= new ConfigurationBuilder();
		cb.setOAuthConsumerKey(Config.twConsumerKey);
		cb.setOAuthConsumerSecret(Config.twConsumerSecret);
		cb.setOAuthAccessToken(accessToken);
		cb.setOAuthAccessTokenSecret(accessTokenSecret);
		Twitter twitter = tf.getInstance(new OAuthAuthorization(cb.build()));
		return twitter;
	}

	public static AccessToken getAccessTokenFor(RequestToken requestToken,
			String oauthVerifier) throws TwitterException {
		ConfigurationBuilder cb= new ConfigurationBuilder();
		cb.setOAuthConsumerKey(Config.twConsumerKey);
		cb.setOAuthConsumerSecret(Config.twConsumerSecret);
		Twitter twitter = tf.getInstance(new OAuthAuthorization(cb.build()));	
		AccessToken accessToken;
		if(oauthVerifier!=null)
			accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
		else
			accessToken = twitter.getOAuthAccessToken(requestToken);
		return accessToken;
	}
	
	public static RequestToken getRequestToken() throws TwitterException {
		ConfigurationBuilder cb= new ConfigurationBuilder();
		cb.setOAuthConsumerKey(Config.twConsumerKey);
		cb.setOAuthConsumerSecret(Config.twConsumerSecret);
		Twitter twitter = tf.getInstance(new OAuthAuthorization(cb.build()));
		RequestToken requestToken  = twitter.getOAuthRequestToken();
		return requestToken;
	}
}
