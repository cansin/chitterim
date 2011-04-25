package com.chitter.bot.strategy;

import twitter4j.Twitter;

import com.chitter.external.BitlyAPI;
import com.chitter.external.QuotesAPI;
import com.chitter.external.TwitterAPI;
import com.chitter.persistence.UserAccount;
import com.chitter.utility.ExceptionPrinter;
import com.google.appengine.api.xmpp.Message;

public class QuoteStrategy extends AbstractStrategy {

	@Override
	public void handleMessage(UserAccount userAccount, Message message) {
		
		// If no category has been written just inform user on available categories.
		if( message.getBody().equalsIgnoreCase("/quote") ||
			message.getBody().equalsIgnoreCase("/q") ||
			message.getBody().equalsIgnoreCase(".quote") ||
			message.getBody().equalsIgnoreCase(".q") ) {
			replyToMessage(message, "Try giving a category name ( e.g. _*/q love*_ ). " +
					"You can look available categories at http://j.mp/fbSRSr");
			return;
		}
		
		
		String source = message.getBody().trim().substring(
				message.getBody().indexOf(' '), 
				message.getBody().length()).
				trim();

		Twitter twitter = TwitterAPI.getInstanceFor(userAccount);
		String quote = "";
		try {
			quote = BitlyAPI.shortenUrls(QuotesAPI.getQuote(source));
			if(!quote.equals("")) {
				twitter.updateStatus(quote);
				replyToMessage(message, "Your quote has been sent:\n_*"+quote+"*_");	
			} else {
				replyToMessage(message, "There is no _*"+source.replace('_', ' ')+"*_ category. " +
						"You can look available categories at http://j.mp/fbSRSr");
			}
		} catch (Exception e) {
			ExceptionPrinter.print(System.err, e, "Boss, I couldn't tweet "+userAccount.getGtalkId()+"'s quote:\n "+quote);
			replyToMessage(message, "I couldn't send your quote.");
		}
	}

}
