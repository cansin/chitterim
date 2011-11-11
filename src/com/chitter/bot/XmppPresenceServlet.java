package com.chitter.bot;

import java.util.Collections;
import java.util.HashSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class XmppPresenceServlet extends HttpServlet {
	private static final XMPPService xmppService =
			XMPPServiceFactory.getXMPPService();

	private static Cache cache;
	static
	{
		try
		{
			CacheManager cacheManager = CacheManager.getInstance();
			CacheFactory cacheFactory = cacheManager.getCacheFactory();
			cache = cacheFactory.createCache(Collections.EMPTY_MAP);
		} catch (CacheException e) {
			ExceptionPrinter.print(System.err, e, "Couldn't create cache at XmppPresence");
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		String from = "";
		try {
			// Parse Incoming Presence
			Presence presence = xmppService.parsePresence(request);
			Boolean isAvailable = presence.isAvailable();
			// Parse Gtalk Id
			from = presence.getFromJid().getId().split("/")[0];
			@SuppressWarnings("unchecked")
			HashSet<String> onlineUserGtalkIdsSet = (HashSet<String>) cache.peek("onlineUserGtalkIdsSet");
			if (onlineUserGtalkIdsSet == null) {
				onlineUserGtalkIdsSet = new HashSet<String>();
				cache.put("onlineUserGtalkIdsSet",onlineUserGtalkIdsSet);
			}
			if(isAvailable) {
				onlineUserGtalkIdsSet.add(from);
			} else {
				onlineUserGtalkIdsSet.remove(from);
			}
			cache.put("onlineUserGtalkIdsSet", onlineUserGtalkIdsSet);
		} catch (Exception e) {
			ExceptionPrinter.print(System.err,e,"Unknown error at XmppPresence for user "+from);
		}
	}

}
