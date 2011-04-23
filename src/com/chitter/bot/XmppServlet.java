package com.chitter.bot;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.bot.strategy.AbstractStrategy;
import com.chitter.bot.strategy.DirectMessageStrategy;
import com.chitter.bot.strategy.FollowStrategy;
import com.chitter.bot.strategy.HelpStrategy;
import com.chitter.bot.strategy.IncomingFriendshipStrategy;
import com.chitter.bot.strategy.QuoteStrategy;
import com.chitter.bot.strategy.RetweetStrategy;
import com.chitter.bot.strategy.SubscribeStrategy;
import com.chitter.bot.strategy.TimelineOffStrategy;
import com.chitter.bot.strategy.TimelineOnStrategy;
import com.chitter.bot.strategy.TweetStrategy;
import com.chitter.bot.strategy.UnfollowStrategy;
import com.chitter.bot.strategy.WhatToDoStrategy;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class XmppServlet extends HttpServlet {
	private static final XMPPService xmppService =
		XMPPServiceFactory.getXMPPService();
		
	private static AbstractStrategy strategy;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Message message;
		try {
			message = xmppService.parseMessage(request);

			String userName = message.getFromJid().getId();
			userName=userName.substring(0, userName.indexOf('/'));
	
			UserAccount userAccount = new UserAccount(userName);
			if(userAccount!=null && userAccount.getGtalkId()!=null){
				if (message.getBody().startsWith("/tweet ") ||
						message.getBody().startsWith("/t ") ||
						message.getBody().startsWith(".tweet ") ||
						message.getBody().startsWith(".t ") ) {
					strategy=new TweetStrategy();
				} else if (message.getBody().startsWith("/direct ") ||
						message.getBody().startsWith("/d ") ||
						message.getBody().startsWith(".direct ") ||
						message.getBody().startsWith(".d ") ) {
					strategy=new DirectMessageStrategy();
				} else if (message.getBody().startsWith("/retweet ") ||
						message.getBody().startsWith("/rt ") ||
						message.getBody().startsWith(".retweet ") ||
						message.getBody().startsWith(".rt ") ) {
					strategy=new RetweetStrategy();
				} else if (message.getBody().startsWith("/follow ") ||
						message.getBody().startsWith("/f ") ||
						message.getBody().startsWith(".follow ") ||
						message.getBody().startsWith(".f ") ) {
					strategy=new FollowStrategy();
				} else if (message.getBody().startsWith("/unfollow ") ||
						message.getBody().startsWith("/u ") ||
						message.getBody().startsWith(".unfollow ") ||
						message.getBody().startsWith(".u ") ) {
					strategy=new UnfollowStrategy();
				}  else if (message.getBody().equalsIgnoreCase("/help") ||
						message.getBody().equalsIgnoreCase("/h") ||
						message.getBody().equalsIgnoreCase(".help") ||
						message.getBody().equalsIgnoreCase(".h") ) {
					strategy=new HelpStrategy();
				} else if (message.getBody().equalsIgnoreCase("/incoming") ||
						message.getBody().equalsIgnoreCase("/i") ||
						message.getBody().equalsIgnoreCase(".incoming") ||
						message.getBody().equalsIgnoreCase(".i") ) {
					strategy=new IncomingFriendshipStrategy();
				} else if (message.getBody().startsWith("/quote ") ||
						message.getBody().startsWith("/q ") ||
						message.getBody().startsWith(".quote ") ||
						message.getBody().startsWith(".q ") ||
						message.getBody().equalsIgnoreCase("/quote") ||
						message.getBody().equalsIgnoreCase("/q") ||
						message.getBody().equalsIgnoreCase(".quote") ||
						message.getBody().equalsIgnoreCase(".q")) {
					strategy=new QuoteStrategy();
				} else if (message.getBody().equalsIgnoreCase("/on") ||
						message.getBody().equalsIgnoreCase(".on") ) {
					strategy=new TimelineOnStrategy();
				} else if (message.getBody().equalsIgnoreCase("/off") ||
						message.getBody().equalsIgnoreCase(".off") ) {
					strategy=new TimelineOffStrategy();
				} else {
					strategy=new WhatToDoStrategy();
				}
			} else {
				strategy=new SubscribeStrategy();
			}
			
			strategy.handleMessage(userAccount, message);
		} catch (IOException e) {
			ExceptionPrinter.print(System.err, e);
		}
	}
	
}
