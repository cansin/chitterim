package com.chitter.bot;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.persistence.UserTwitterTimeline;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class StreamCronjobServlet extends HttpServlet {

	private static final XMPPService xmppService =
			XMPPServiceFactory.getXMPPService();
	private static final MessageBuilder messageBuilder =
			new MessageBuilder();

	/*private static final Queue queue = 
		QueueFactory.getDefaultQueue();
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		processRequest(request,response);
	}

	@SuppressWarnings("unchecked")
	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Will start stream cronjob process");

		List<UserAccount> userAccounts = UserAccount.getTimelineActiveAndOnlineUsers();
		userAccounts.size();

		for(UserAccount userAccount: userAccounts){
			try {
				sendTimelineUpdates(userAccount.getGtalkId(),userAccount.getTwitterAccessToken(),userAccount.getTwitterAccessTokenSecret());
			} catch (TwitterException e) {
				ExceptionPrinter.print(System.out, e, "I couldn't send timeline updates to "+userAccount.getGtalkId());
			}
		}

		System.out.println("Done");
	}

	public static void sendTimelineUpdates(String gtalkId, String accessToken, String accessTokenSecret) throws TwitterException {
		UserTwitterTimeline userTT = new UserTwitterTimeline(gtalkId);
		Long ttSinceId = userTT.getTwitterTimelineSinceId();
		Long dmSinceId = userTT.getTwitterDirectMessageSinceId();

		System.out.println("Starting routine task for user " + gtalkId + " ... ttSince " + ttSinceId + " dmSince " + dmSinceId);
		System.out.println("with accessToken " + accessToken + " and accessTokenSecret " + accessTokenSecret);
		if ((dmSinceId != null) && (ttSinceId != null) && (gtalkId != null) && (accessToken != null) && (accessTokenSecret != null)) {
			System.out.println("started");
			Twitter twitter = TwitterAPI.getInstanceFor(accessToken, accessTokenSecret);
			System.out.println("Twitter authentication is set for " + twitter.getScreenName() + ". ");

			ResponseList<Status> timeline = twitter.getHomeTimeline(new Paging(ttSinceId.longValue()));
			System.out.println("Timeline fetched for " + twitter.getScreenName() + " with sizes " + timeline.size());

			String fullMessageBody = "---";
			String fullMessageBodyHTML = "<p>---</p>";
			for (int i = timeline.size() - 1; i >= 0; i--) {
				System.out.println("Trying to get " + i + "th wall post.");
				Status status = (Status)timeline.get(i);
				System.out.println("Yes, we're in");
				if (!status.getUser().getScreenName().equals(twitter.getScreenName())) {
					String messageBody = "_*" + status.getUser().getScreenName() + ":*_ ";
					String messageBodyHTML = "<p><b><i>" + status.getUser().getScreenName() + ":</i></b> ";
					if (status.isRetweet()) {
						messageBody = messageBody + "_rt_ _" + status.getRetweetedStatus().getUser().getScreenName() + "_: " + status.getRetweetedStatus().getText();
						messageBodyHTML = messageBodyHTML + "<i>rt</i> <i>" + status.getRetweetedStatus().getUser().getScreenName() + "</i>: " + status.getRetweetedStatus().getText() + "</p>";
					} else {
						messageBody = messageBody + status.getText();
						messageBodyHTML = messageBodyHTML + status.getText() + "</p>";
					}

					fullMessageBody = fullMessageBody + "\n" + messageBody;
					fullMessageBodyHTML = fullMessageBodyHTML + messageBodyHTML;
				}
			}
			if (fullMessageBody != "---")
			{
				Message message = messageBuilder
						.withRecipientJids(new JID[] { 
								new JID(gtalkId) })
								.withMessageType(MessageType.CHAT)
								.withBody(fullMessageBody)
								.build();

				xmppService.sendMessage(message);
			}

			boolean isSinceIdsChanged = false;
			if (timeline.size() > 0) {
				ttSinceId = Long.valueOf(((Status)timeline.get(0)).getId());
				isSinceIdsChanged = true;
			}

			if (isSinceIdsChanged)
				new UserTwitterTimeline(gtalkId, ttSinceId, dmSinceId);
		}
	}
}
