package com.chitter.aspect;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import com.chitter.bot.strategy.AbstractStrategy;
import com.chitter.persistence.UserAccount;
import com.chitter.persistence.UserStatistic;
import com.google.appengine.api.xmpp.Message;

public aspect Statistics {
	
	pointcut commandCall (AbstractStrategy strategy, UserAccount user): target(strategy) && args(user,..) && execution(* handleMessage(UserAccount, Message) );
	
	private enum AnalyticEnum { SUM, AVG, MIN, MAX }
	
	public static UserStatistic UserStatistic.getSumAnalytic() {
		return getAnalyticFor(AnalyticEnum.SUM);
	}
	
	public static UserStatistic UserStatistic.getAvgAnalytic() {
		return getAnalyticFor(AnalyticEnum.AVG);
	}
	
	public static UserStatistic UserStatistic.getMinAnalytic() {
		return getAnalyticFor(AnalyticEnum.MIN);
	}
	
	public static UserStatistic UserStatistic.getMaxAnalytic() {
		return getAnalyticFor(AnalyticEnum.MAX);
	}
	
	@EscapePersistence
	private static UserStatistic getAnalyticFor(AnalyticEnum analyticEnum){
		System.out.println("Boss, I'm at to get getting UserAnalytic "+analyticEnum);
		
		try {
			@SuppressWarnings("unchecked")
			List<UserStatistic> analytics = UserStatistic.getUserStatisticList();
			Iterator<UserStatistic> analyticsIt = analytics.iterator();
			UserStatistic totalAnalytic = new UserStatistic("");
			if(analyticEnum.equals(AnalyticEnum.SUM)){
				totalAnalytic.setGtalkId("sum");
			}else if(analyticEnum.equals(AnalyticEnum.MAX)){
				totalAnalytic.setGtalkId("max");
			}else if(analyticEnum.equals(AnalyticEnum.MIN)){
				totalAnalytic.setGtalkId("min");
			}else if(analyticEnum.equals(AnalyticEnum.AVG)){
				totalAnalytic.setGtalkId("avg");
			}
			while(analyticsIt.hasNext()){
				UserStatistic analytic = analyticsIt.next();
				float[] analyticArr = analytic.getStatistics();
				for(int i=0;i<analyticArr.length;i++){
					if(	analytic.getGtalkId().equals("cansinyildiz@gmail.com")||
						analytic.getGtalkId().equals("chitter.im.tester@gmail.com"))
						continue;
					if(analyticEnum.equals(AnalyticEnum.SUM) || analyticEnum.equals(AnalyticEnum.AVG)) {
						totalAnalytic.setStatistic(i, totalAnalytic.getStatistic(i)+analyticArr[i]);
					} else if(analyticEnum.equals(AnalyticEnum.MAX)){
						totalAnalytic.setStatistic(i, totalAnalytic.getStatistic(i)>analyticArr[i]?totalAnalytic.getStatistic(i):analyticArr[i]);
					} else if(analyticEnum.equals(AnalyticEnum.MIN)){
						totalAnalytic.setStatistic(i, totalAnalytic.getStatistic(i)<analyticArr[i]?totalAnalytic.getStatistic(i):analyticArr[i]);
					}
				}
			}
			if(analyticEnum.equals(AnalyticEnum.AVG)) {
				for(int i=0;i<totalAnalytic.getStatistics().length;i++){
					totalAnalytic.setStatistic(i, totalAnalytic.getStatistic(i)/analytics.size());
				}
			}
			System.out.println("I will return fetched total user analytic");
			return totalAnalytic;
		} catch (Exception e) {
			System.out.println("I will return null for total analytic ");
			for(int i=0;i<e.getStackTrace().length;i++)
				System.out.println(e.getStackTrace()[i].toString());
			return null;
		}	
	}
	
	after(AbstractStrategy strategy, UserAccount user) : commandCall(strategy, user) {
		System.out.println("Boss, I will capture analytic data for "+user.getGtalkId()+ " , " + strategy.getClass().getName());

		try{
			//Prepare field's name that we use at UserStatistic (e.g. DIRECTMESSAGE)
		    String fieldName = strategy.getClass().getName().toUpperCase();
		    fieldName = fieldName.substring(fieldName.lastIndexOf(".")+1, fieldName.lastIndexOf("STRATEGY"));
		    Field field = UserStatistic.class.getDeclaredField(fieldName);
		    //Get corresponding value using the name (e.g. 0 for DIRECTMESSAGE)
		    int id = field.getInt(null);

		    //Fetch the user's statistic object
		    UserStatistic userStatistic = new UserStatistic(user.getGtalkId());
		    
		    //Check whether the user's statistic object has enough statistic columns. 
		    //(i.e. a new bot command could be added to the system after the user registered.
		    // Hence, he will not have a column for that bot command.)
		    if(userStatistic.getStatistics().length<UserStatistic.statisticsLabels.length) {
		    	float[] arr=new float[UserStatistic.statisticsLabels.length];
		    	System.arraycopy(userStatistic.getStatistics(), 0, arr, 0, userStatistic.getStatistics().length);
		    	userStatistic.setStatistics(arr);
		    }
		    
		    //Increment i-th statistic by 1 (e.g. i=0 for DIRECTMESSAGE)
		    userStatistic.setStatistic(id, userStatistic.getStatistic(id)+1);
		} catch (Exception e) {
			System.err.println("Boss, I couldn't capture analytic data");
		}
	    
	}
}
