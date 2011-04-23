package com.chitter.utility;

import java.io.PrintStream;

public class ExceptionPrinter {
	public static void print(PrintStream out, Exception e) {
		print(out,e,""); 
	}
	
	public static void print(PrintStream out, Exception e, String customMessage) {
		out.println("********************");
		if(!customMessage.equals("")) {
			out.println(customMessage);
			out.println("--------------------");
		}
		out.println(e);
		out.println("--------------------");
		boolean foundFirstChitter = false;
		for(int i=0;i<e.getStackTrace().length;i++) {
			String currentLine = e.getStackTrace()[i].toString();
			if(!foundFirstChitter)
				out.println(currentLine);
			else {
				if(!currentLine.contains("com.chitter."))
					break;
				out.println(currentLine);
			}
			if(currentLine.contains("com.chitter."))
				foundFirstChitter = true;
		}
		out.println("********************");
	}
}
