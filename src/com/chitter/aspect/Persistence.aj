package com.chitter.aspect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.annotations.PersistenceCapable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.chitter.persistence.Announcement;
import com.chitter.persistence.UserAccount;
import com.chitter.persistence.UserStatistic;
import com.chitter.persistence.UserTwitterTimeline;
import com.chitter.utility.ExceptionPrinter;

public aspect Persistence {	
	private static Cache cache;
	static
	{
		try
		{
			CacheManager cacheManager = CacheManager.getInstance();
			CacheFactory cacheFactory = cacheManager.getCacheFactory();
			cache = cacheFactory.createCache(Collections.EMPTY_MAP);
		} catch (CacheException e) {
			ExceptionPrinter.print(System.err, e, "Couldn't create cache at PersistenceAspect");
		}
	}

	/** 
	 * captures ctors with more than one arg and
	 * all setters of @PersistenceCapable objects.
	 */
	pointcut pushObject(Object o): target(o) && (call(* (@PersistenceCapable *).set*(..)) || execution((@PersistenceCapable *).new(*,*,..)));
	pointcut pullObject(Object pk): args(pk) && call((@PersistenceCapable *).new(*));
	pointcut pullList(): call(List (@PersistenceCapable *).get*List());
	pointcut pullExtent(): call(Extent (@PersistenceCapable *).get*Extent());

	pointcut pmShouldClose() : call(* com.chitter..*Servlet.processRequest(HttpServletRequest,HttpServletResponse));

	pointcut withinEscapePersistence(): withincode(@EscapePersistence * *(..));

	@SuppressWarnings("all")
	public static Extent UserAccount.getUserAccountExtent(){
		return null;
	}

	@SuppressWarnings("all")
	public static List UserAccount.getUserAccountList(){
		return new ArrayList<UserAccount>();
	}
	
	@SuppressWarnings("all")
	public static Set<String> UserAccount.getOnlineUserGtalkIds(){
		HashSet<String> onlineUserGtalkIdsSet = (HashSet<String>) cache.peek("onlineUserGtalkIdsSet");
		if (onlineUserGtalkIdsSet == null) {
			onlineUserGtalkIdsSet = new HashSet<String>();
			cache.put("onlineUserGtalkIdsSet",onlineUserGtalkIdsSet);
		}
		return onlineUserGtalkIdsSet;
	}
	
	@SuppressWarnings("all")
	public static List UserAccount.getTimelineActiveUsers(){
		PersistenceManager pm = getPM();

		try {
			Query q = pm.newQuery(UserAccount.class, "this.isTimelineActive == true");
			return (List) q.execute();
		} catch (Exception e) {
			ExceptionPrinter.print(System.err,e,"I couldn't fetch timeline active users");
			return (List)(new ArrayList<Object>());
		} 
	}

	@SuppressWarnings("all")
	public static List UserAccount.getOnlineUsers(){
		PersistenceManager pm = getPM();

		try {
			Query q = pm.newQuery(UserAccount.class, "this.isOnline == true");
			return (List) q.execute();
		} catch (Exception e) {
			ExceptionPrinter.print(System.err,e,"I couldn't fetch online users");
			return (List)(new ArrayList<Object>());
		} 
	}

	@SuppressWarnings("all")
	public static List UserStatistic.getUserStatisticList() {
		return new ArrayList<UserStatistic> ();
	}

	@SuppressWarnings("all")
	public static List UserTwitterTimeline.getUserTwitterTimelineList() {
		return new ArrayList<UserTwitterTimeline> ();
	}

	@SuppressWarnings("all")
	public static List Announcement.getAnnouncementList() {
		return new ArrayList<Announcement> ();
	}

	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private static final ThreadLocal<PersistenceManager> perThreadPM
	= new ThreadLocal<PersistenceManager>();

	private Persistence() {};

	public static PersistenceManager getPM() {
		PersistenceManager PM = perThreadPM.get();
		if (PM == null) {
			PM = PMF.getPersistenceManager();
			perThreadPM.set(PM);
		}
		return PM;
	}

	after(Object o) : pushObject(o) && !withinEscapePersistence(){
		PersistenceManager pm = getPM();
		/**
		 * First try to convert object to UserStatistic.
		 * If you can, that means the persistent object was UserStatistic,
		 * so you should flag statistics array as dirty.
		 * (o.w. it will not persist changes to the array.)
		 */
		if(o.getClass().equals(UserStatistic.class)) {
			UserStatistic statistic=(UserStatistic) o;
			JDOHelper.makeDirty(statistic, "statistics");
		}
		pm.makePersistent(o.getClass().cast(o));
		cache.put(o.getClass().getName() + "_" + pm.getObjectId(o), o);
		pm.flush();
	}    

	@SuppressWarnings("all")
	Object around(Object pk) : pullObject(pk) && !withinEscapePersistence(){
		PersistenceManager pm = getPM();
		try{
			Object r = cache.peek(thisJoinPointStaticPart.getSignature().getDeclaringTypeName() + "_" + pk);
			if (r == null) {
				r = pm.getObjectById(thisJoinPoint.getSignature().getDeclaringType(),pk);
				cache.put(thisJoinPointStaticPart.getSignature().getDeclaringTypeName() + "_" + pk, r);
			}
			return r;
		} catch (Exception e) {
			cache.remove(thisJoinPointStaticPart.getSignature().getDeclaringTypeName() + "_" + pk);
			ExceptionPrinter.print(System.err, e, "I couldn't fetch persistent object for " + 
					thisJoinPointStaticPart.getSignature().getDeclaringTypeName() + " " + pk);
			return null;
		} 
	}

	@SuppressWarnings("all")
	List around() : pullList() {
		PersistenceManager pm = getPM();

		try {
			List list = (List) pm.newQuery(thisJoinPoint.getSignature().getDeclaringType()).execute();
			return list;
		} catch (Exception e) {
			ExceptionPrinter.print(System.err,e,"I couldn't fetch persistent list for "+getClass());
			return (List)(new ArrayList<Object>());
		} 
	}

	@SuppressWarnings("all")
	/*Extent around() : pullExtent() {
		PersistenceManager pm = getPM();

		try {
			Extent extent = pm.getExtent(thisJoinPoint.getSignature().getDeclaringType(), false);
			return extent;
		} catch (Exception e) {
			ExceptionPrinter.print(System.err, e, "I couldn't fetch persistent extent for "+getClass());
			return null;
		} 
    }*/

	after() : pmShouldClose() {
		PersistenceManager PM = perThreadPM.get();
		if (PM != null) {
			perThreadPM.remove();
			Transaction tx = PM.currentTransaction();
			if (tx.isActive()) {
				tx.rollback();
			}
			PM.close();
		}
	}

}
