package com.chitter.external;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.chitter.utility.ExceptionPrinter;

public class BitlyAPI extends AbstractAPI {
	
	private static final String login = "chitterim";
		
	public static String shortenUrl (String longUrl) {
		String response="";
		String apiUrl="";
		try {
			apiUrl = "http://api.bit.ly/v3/shorten?domain=j.mp&login="+login+"&apiKey="+Config.bitlyApiKey+"&longUrl="+URLEncoder.encode(longUrl,"UTF-8")+"&format=json";
			URL url = new URL(apiUrl);
	        response = convertStreamToString(url.openStream());
	        JSONObject json = new JSONObject(response).getJSONObject("data");
	        response = json.getString("url");
			return response;
		} catch (Exception e) {
			/**
			 * If for any reason Bit.ly api cannot shorten url, 
			 * just use the lon version of it.
			 */
			ExceptionPrinter.print(System.err, e, "I couldn't shorten url via Bit.ly. I sent:\n"+apiUrl+"\n and Bit.ly returned\n"+response);
			return longUrl;
		}
	}
	
	public static String shortenUrls(String input) {
		String output = "";
		while(input.indexOf("http://")!=-1) {
			int firstPos = input.indexOf("http://");
			int lastPos = input.indexOf(" ", firstPos);
			if(lastPos==-1) lastPos=input.length();
			String longUrl = input.substring(firstPos, lastPos);
			String shortUrl = longUrl;
			if(longUrl.length()>21)
				shortUrl = shortenUrl(longUrl);
			output += input.substring(0,firstPos)+shortUrl;
			input = input.substring(lastPos);
		}
		output += input;
        return output;
	}
}