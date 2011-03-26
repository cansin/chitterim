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
	
	pointcut statisticsExceptionLog(Exception e) :
		handler(Exception) && args(e) && this(Statistics);
	
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

	// Exception Logger when in aspect.Persistence
	before(Exception e) : 
		persistenceExceptionLog(e){
		System.err.println("-------------Persistence-Exception-----------------");
		for(int i=0;i<e.getStackTrace().length;i++)
			System.err.println(e.getStackTrace()[i].toString());
		System.err.println("---------------------------------------------------");
	}

	// Exception Logger when in aspect.Statistics
	before(Exception e) : 
		statisticsExceptionLog(e){
		System.err.println("-------------Statistics-Exception------------------");
		for(int i=0;i<e.getStackTrace().length;i++)
			System.err.println(e.getStackTrace()[i].toString());
		System.err.println("---------------------------------------------------");
	}
	
	// Exception Logger when returning from handleMessage
	after(UserAccount user) throwing(TwitterException e):
		target(AbstractStrategy) && call(public void handleMessage(UserAccount, Message)) && args(user, ..){
		System.err.println("-------------Exp-For-"+user.getGtalkId()+"---------");
		for(int i=0;i<e.getStackTrace().length;i++)
			System.err.println(e.getStackTrace()[i].toString());
		System.err.println("---------------------------------------------------");
	}
	
	// Notify logger that the execution is now in aspect.Persistence
	before() : 
		persistenceProcessLog(){
		System.out.println("Boss, I am at Persistence Aspect");
	}
	
	
	
	
}
