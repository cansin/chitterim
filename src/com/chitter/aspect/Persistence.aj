package com.chitter.aspect;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.jdo.annotations.PersistenceCapable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chitter.persistence.UserAccount;
import com.chitter.persistence.UserStatistic;

public aspect Persistence {	
	/** 
	 * captures ctors with more than one arg and
	 * all setters of @PersistenceCapable objects.
	 */
	pointcut pushObject(Object o): target(o) && (call(* (@PersistenceCapable *).set*(..)) || execution((@PersistenceCapable *).new(*,*,..)));
	pointcut pullObject(Object pk): args(pk) && call((@PersistenceCapable *).new(*));
	pointcut pullList(): call(List (@PersistenceCapable *).get*List());
	
	pointcut pmShouldClose() : call(* com.chitter..*Servlet.processRequest(HttpServletRequest,HttpServletResponse) throws *);
	
	pointcut withinEscapePersistence(): withincode(@EscapePersistence * *(..));
	
	@SuppressWarnings("all")
	public static List UserAccount.getUserAccountList(){
		return new ArrayList<UserAccount>();
	}

	@SuppressWarnings("all")
	public static List UserStatistic.getUserStatisticList() {
		return new ArrayList<UserStatistic> ();
	}

	private static final PersistenceManagerFactory PMF =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");
    private static final ThreadLocal<PersistenceManager> perThreadPM
    	= new ThreadLocal<PersistenceManager>();

    private Persistence() {};
    
    private static PersistenceManager getPM() {
    	PersistenceManager PM = perThreadPM.get();
        if (PM == null) {
          PM = PMF.getPersistenceManager();
          perThreadPM.set(PM);
        }
        return PM;
    }
    
    after(Object o) : pushObject(o) && !withinEscapePersistence(){
		System.out.println("I should persist an instance of "+o.getClass());
    	PersistenceManager pm = getPM();
    	try{
    		UserStatistic statistic=(UserStatistic) o;
    		System.out.println("which is a statistic object btw with pk "+statistic.getGtalkId());
    		JDOHelper.makeDirty(statistic, "statistics");
    	} catch(Exception e) {
    		System.err.println("THIS COULD BE AN EXPECTED EXCEPTION!");
    	} finally {
        	pm.makePersistent(o.getClass().cast(o));
    		pm.flush();
    	}
    }    
    
    @SuppressWarnings("all")
	Object around(Object pk) : pullObject(pk) && !withinEscapePersistence(){
    	PersistenceManager pm = getPM();
		try{
			Object r = pm.getObjectById(thisJoinPoint.getSignature().getDeclaringType(),pk);
			return r;
		} catch (Exception e) {
    		System.err.println("THIS COULD BE AN EXPECTED EXCEPTION!");
			return null;
		} 
    }
    
    @SuppressWarnings("all")
	List around() : pullList() {
		PersistenceManager pm = getPM();
		
		try {
			System.out.println("I am trying to pull a list of "+getClass()+" from db with ");
			List list = (List) pm.newQuery(thisJoinPoint.getSignature().getDeclaringType()).execute();
			System.out.println("Yes I did");
			return list;
		} catch (Exception e) {
			System.err.println("No I am returning an empty list");
			return (List)(new ArrayList<Object>());
		} 
    }
	
	after() : pmShouldClose() {
		PersistenceManager PM = perThreadPM.get();
		if (PM != null) {
			System.out.println("PM should definitely close");
			perThreadPM.remove();
			Transaction tx = PM.currentTransaction();
			if (tx.isActive()) {
				tx.rollback();
			}
			PM.close();
		}
	}
	
}