package com.chitter.web.state;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.persistence.UserStatistic;
import com.chitter.persistence.UserTwitterTimeline;
import com.google.appengine.api.users.User;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

public class BotInvitationState extends AbstractState {
	
	private static final long serialVersionUID = 3894996960567629624L;
	
	private static final XMPPService xmppService =
		XMPPServiceFactory.getXMPPService();

	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			System.out.println("Boss, current state is "+toString());
			HttpSession session = request.getSession();
			String token=(String)session.getAttribute("token");
			String tokenSecret=(String)session.getAttribute("tokenSecret");
			
			System.out.println(token+" "+tokenSecret);
			AccessToken accessToken = TwitterAPI.getAccessTokenFor(token, tokenSecret, request.getParameter("oauth_verifier"));
			Twitter twitter = TwitterAPI.getInstanceFor(accessToken.getToken(), accessToken.getTokenSecret());
	
			User user = (User)session.getAttribute("user");
			
			try {
				new UserAccount(user.getEmail(), accessToken.getToken(), accessToken.getTokenSecret());
				new UserStatistic(user.getEmail(),new float[]{0,0,0,0,0,0,0,0,0,0,0});
				new UserTwitterTimeline(user.getEmail(), (long) 1, (long) 1);
			} catch(Exception e) {
				System.err.println("Boss, THIS IS SERIOUS! I couldn't create persistence objects for the new user !\n" + e);		
			}
	
			xmppService.sendInvitation(new JID(user.getEmail()));
	
			try {
				if(!twitter.existsFriendship(twitter.getScreenName(), TwitterAPI.getChitterScreenName())){
					twitter.createFriendship(TwitterAPI.getChitterScreenName());
				}
			} catch (TwitterException e) {
				System.err.println("Boss, I couldn't follow chitter from registered user !\n" + e);
			}
			try {
				twitter.updateStatus("I started using @"+TwitterAPI.getChitterScreenName()+". It's a bot that enables you to use Twitter from Gtalk! Get it at http://j.mp/ed0h7C!");
			} catch (TwitterException e) {
				System.err.println("Boss, I couldn't update registered user's status !\n" + e);
			}
			String newUserScreenName = "";
			try{
				newUserScreenName = twitter.getScreenName();
				request.setAttribute("twitterScreenName", twitter.getScreenName());
			} catch (TwitterException e) {
				System.err.println("Boss, I couldn't get the screen name of the user !\n" + e);
			}
			
			/** Send an email to cansinyildiz@gmail.com to notify me */
			Properties props = new Properties();
	        Session ses = Session.getDefaultInstance(props, null);
	        String msgBody = "...";
	        try {
	            Message msg = new MimeMessage(ses);
	            msg.setFrom(new InternetAddress("cansinyildiz@gmail.com", "Chitter.im"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress("cansinyildiz@gmail.com", "Cansin Yildiz"));
	            msg.setSubject("New User w/ Twitter: "+newUserScreenName+" Gmail: "+user.getEmail());
	            msg.setText(msgBody);
	            Transport.send(msg);
	        } catch (AddressException e) {
	        	System.err.println("Boss, I couldn't send you an email !\n" + e);
	        } catch (MessagingException e) {
	        	System.err.println("Boss, I couldn't send you an email !\n" + e);
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
				twitter.createFriendship(newUserScreenName);
			}
			/*
			try{
				twitter.updateStatus("hey there @"+newUserScreenName+" !");
			} catch (TwitterException e) {
				System.err.println("Boss, I couldn't update chitter's status!\n" + e);
			}
			*/
	
		} catch(Exception e){
			System.err.println("------------Bot-Invitation-State-------------------");
			for(int i=0;i<e.getStackTrace().length;i++)
				System.err.println(e.getStackTrace()[i].toString());
			System.err.println("---------------------------------------------------");
			//response.sendRedirect("/");
		}
		
	}

	public void forward(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("final.jsp");
		rd.forward(request, response);	
	}
}
