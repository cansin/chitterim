package com.chitter.web.state;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.chitter.external.BitlyAPI;
import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.persistence.UserStatistic;
import com.chitter.persistence.UserTwitterTimeline;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.users.User;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

public class BotInvitationState extends AbstractState {
	
	private static final long serialVersionUID = 3894996960567629624L;
	
	private static final XMPPService xmppService =
		XMPPServiceFactory.getXMPPService();

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		User user = (User)session.getAttribute("user");

		/**
		 * If request does not have necessary parameters
		 * or it has a 'denied' as a parameter, it means
		 * something went wrong at twitter authorization state so
		 * we should get back to login page;
		 */
		String denied = request.getParameter("denied");
		String oauthToken= request.getParameter("oauth_token");
		String oauthVerifier= request.getParameter("oauth_verifier");
		if(denied!=null || (oauthToken==null && oauthVerifier==null)){
			System.err.println("I redirected "+user.getEmail().toLowerCase()+" from /twitter to /login page");
			if(denied!=null)
				System.err.println("Because denied!=null");
			else
				System.err.println("Because oauthToken==null && oauthVerifier==null");
			
			try {
				response.sendRedirect("/login");
			} catch (IOException e) {
				ExceptionPrinter.print(System.err, e, "I couldn't send redirect to /login at BotInvitationState");
			}
			
			return;
		}
		
		try {
			RequestToken requestToken =(RequestToken)session.getAttribute("requestToken");
			AccessToken accessToken = TwitterAPI.getAccessTokenFor(requestToken, request.getParameter("oauth_verifier"));
			Twitter twitter = TwitterAPI.getInstanceFor(accessToken.getToken(), accessToken.getTokenSecret());
	
			
			try {
				new UserAccount(user.getEmail().toLowerCase(), accessToken.getToken(), accessToken.getTokenSecret());
				new UserStatistic(user.getEmail().toLowerCase(),new float[UserStatistic.statisticsLabels.length]);
				new UserTwitterTimeline(user.getEmail().toLowerCase(), (long) 1, (long) 1);
				
				/**
				 *  Adjust UserTwitterTimeline object so that new registered user
				 *  will only get last tweet from timeline when he first interacts w/ bot.
				 */
				long ttSinceId = 1;
				long dmSinceId = 1;
				ResponseList<twitter4j.Status> tts=twitter.getHomeTimeline();
				/*
				ResponseList<DirectMessage> dms=twitter.getDirectMessages();
				*/
				
				if(tts.size()>1)
					ttSinceId = tts.get(1).getId();
				/*
				if(dms.size()>1)
					dmSinceId = dms.get(1).getId();
				*/
				new UserTwitterTimeline(user.getEmail().toLowerCase(), ttSinceId, dmSinceId);
					 
			} catch(Exception e) {
				ExceptionPrinter.print(System.err, e, "I couldn't create persistence objects for the new user: "+user.getEmail());		
			}
	
			xmppService.sendInvitation(new JID(user.getEmail().toLowerCase()));

			try {
				if(!twitter.existsFriendship(twitter.getScreenName(), TwitterAPI.getChitterScreenName())){
					twitter.createFriendship(TwitterAPI.getChitterScreenName());
				}
			} catch (TwitterException e) {
				try {
					twitter.createFriendship(TwitterAPI.getChitterScreenName());
				} catch(Exception e1) {
					ExceptionPrinter.print(System.err, e1, "I couldn't follow chitterim from newly registered user: "+user.getEmail());
				}
			}
			
			try {
				if(!twitter.existsFriendship(twitter.getScreenName(), TwitterAPI.getCansinScreenName())){
					twitter.createFriendship(TwitterAPI.getCansinScreenName());
				}
			} catch (TwitterException e) {
				try {
					twitter.createFriendship(TwitterAPI.getCansinScreenName());
				} catch(Exception e1) {
					ExceptionPrinter.print(System.err, e1, "I couldn't follow chitterim from newly registered user: "+user.getEmail());
				}
			}
			
			try {
				twitter.updateStatus("I started using @"+
									 TwitterAPI.getChitterScreenName()+
									 ". It's a bot that enables you to use Twitter from Gtalk! Get it at "+
									 BitlyAPI.shortenUrl("http://chitter.im/?utm_source=twitter&utm_medium=auto-tweet&utm_campaign=user-"+twitter.getScreenName())+
									 "!");
			} catch (TwitterException e) {
				ExceptionPrinter.print(System.err, e, "I couldn't update newly registered user's status: "+user.getEmail());
			}
			
			String newUserScreenName = "";
			try{
				newUserScreenName = twitter.getScreenName();
				request.setAttribute("twitterScreenName", twitter.getScreenName());
			} catch (TwitterException e) {
				ExceptionPrinter.print(System.err, e, "I couldn't get the screen name of newly registered user: "+user.getEmail());
			} 
			
			twitter.shutdown();
	
			twitter = TwitterAPI.getInstanceForChitter();
			try{
				if(!twitter.existsFriendship(twitter.getScreenName(), newUserScreenName)){
					twitter.createFriendship(newUserScreenName);
				}
			} catch (TwitterException e) {
				/**
				 *  If existsFriendship throws an exception, 
				 *  we know that new subscribed user is a protected non-friend.
				 */
				try {
					twitter.createFriendship(newUserScreenName);
				} catch(Exception e1) {
					ExceptionPrinter.print(System.err, e1, "I couldn't send follow request from chitterim to "+newUserScreenName);
				}
			}

			/** Send an email to cansin@chitter.im to notify me */
			Properties props = new Properties();
	        Session ses = Session.getInstance(props);
	        String msgBody = "...";
	        try {
	            Message msg = new MimeMessage(ses);
	            msg.setFrom(new InternetAddress("cansin@chitter.im", "Cansin Yildiz"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress("cansin@chitter.im", "Cansin Yildiz"));
	            msg.setSubject("New User w/ Twitter: "+newUserScreenName+" Gmail: "+user.getEmail().toLowerCase());
	            msg.setText(msgBody);
	            Transport.send(msg);
	        } catch (Exception e) {
	        	ExceptionPrinter.print(System.err, e, "I couldn't send cansin@chitter.im an email");
	        }
	
		} catch(Exception e){
			ExceptionPrinter.print(System.err, e, "An exception occured for "+user.getEmail().toLowerCase()+" at BotInvitationState");
		}
		
	}

	public void forward(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher rd = request.getRequestDispatcher("final.jsp");
		try {
			rd.forward(request, response);	
		} catch (Exception e) {
			ExceptionPrinter.print(System.err, e, "I couldn't forwarded BotInvitation's response to final.jsp");
		}
	}
}
