package com.chitter.aspect;

import com.chitter.persistence.UserAccount;

import com.chitter.web.state.*;
import com.chitter.bot.strategy.*;

import twitter4j.TwitterException;
import com.google.appengine.api.xmpp.Message;
import javax.servlet.http.HttpSession;


public aspect Development {

	pointcut sessionSetAttribute(String name, AbstractState state) : 
		call(* HttpSession.setAttribute(String, Object))
		&& args(name, state); 
	
	pointcut processRequest() :
		call(* *.processRequest(..));
	
	pointcut twitterExceptionLog(TwitterException e) :
		handler(TwitterException) && args(e);
	
	pointcut persistenceExceptionLog(Exception e) :
		handler(Exception) && args(e) && this(Persistence);
	
	pointcut persistenceProcessLog() :
		within(Persistence) &&
		(!call(* *.*(..)) && !call(*.new(..)) 
		&& !execution(* *.*(..)) && !execution(*.new(..))
		&& !set(* *.*) && !get(* *.*)
		&& !handler(*)); 
	
	// state change logs
	before(String name, AbstractState state) :
		sessionSetAttribute(name, state) && if(name.equals("state")) {
		System.out.println(name + " is changed to " + state.getClass().getName());
	}
	
	// request process logs
	before(AbstractState state) : processRequest() && target(state){
		System.out.println("Process started for request " + state.getClass().getName() );
	}
	
	// Exception Logger when in web.state
	// TODO not sure the way to trace stack is correct
	before(TwitterException e) : 
		twitterExceptionLog(e) && this(AbstractState) {
		StackTraceElement last = e.getStackTrace()[0];
		System.err.println("Boss, exception when trying to " + last.getMethodName() + ":" + e.toString()) ;
	}
	
	// Exception Logger when in aspect.Persistence
	// TODO not sure the way to trace stack is correct
	before(Exception e) : 
		persistenceExceptionLog(e){
		StackTraceElement last = e.getStackTrace()[0];
		System.err.println("Boss, exception when trying to " + last.getMethodName() + ":" + e.toString()) ;
	}
	
	// Exception Logger when returning from handleMessage
	after(UserAccount user) throwing(TwitterException e):
		target(AbstractStrategy) && call(public void handleMessage(UserAccount, Message)) && args(user, ..){
		System.err.println("Boss, from User: " + user.getGtalkId() + "exception:" + e.toString());
	}
	
	// Notify logger that the execution is now in aspect.Persistence
	before() : 
		persistenceProcessLog(){
		System.out.println("Boss, I am at Persistence Aspect");
	}
	
	
	
	
}
