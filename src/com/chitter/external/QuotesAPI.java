package com.chitter.external;

import java.net.URL;

import org.json.JSONObject;

public class QuotesAPI extends AbstractAPI {
	
	private static final String url = "http://www.iheartquotes.com/api/v1/random?";
	private static final String params = "max_characters=140&" +
			"show_permalink=false&" +
			"show_source=false&" +
			"format=json";

	public static String getQuote(String source){
		source = source.trim().replace(' ', '_');
		String response="";
		try {
			String apiUrl = url+params+"&source="+source;
			URL url = new URL(apiUrl);
	        response = convertStreamToString(url.openStream());
	        response = new JSONObject(response).getString("quote");
	        response = response
	        .trim()
	        .replace('\n', ' ')
	        .replace('\r', ' ')
	        .replace('\t', '-')
	        .replaceAll(" +", " ")
	        .replaceAll("--+", "--");
		} catch (Exception e) {
			return "";
		}
		return response;
	}
}
