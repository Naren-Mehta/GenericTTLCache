
/**
 * Title:        Caching
 * Description:  A Generic Cache Object wrapper.  Implements the Cacheable
 interface
 *               uses a TimeToLive stategy for CacheObject expiration.
 * Copyright:    Copyright (c) 2001
 * Company:  JavaWorld
 * Filename: CacheManagerTestProgram.java
 * @author Jonathan Lurie
 * @version 1.0
 */
package com;


import java.util.Calendar;

public class CachedObject implements Cacheable {
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/*
	 * This variable will be used to determine if the object is expired.
	 */
	private java.util.Date dateofExpiration = null;
	/*
	 * This contains the real "value". This is the object which needs to be
	 * shared.
	 */
	int timetolive = 0;
	public Object object = null;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public CachedObject(Object obj, int minutesToLive) {
		this.object = obj;

		// minutesToLive of 0 means it lives on indefinitely.
		if (minutesToLive != 0) {
			dateofExpiration = new java.util.Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateofExpiration);
			cal.add(Calendar.MINUTE, minutesToLive);
			dateofExpiration = cal.getTime();
			timetolive = minutesToLive;
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	@Override
	public boolean isExpired() {
		// Remember if the minutes to live is zero then it lives forever!
		if (dateofExpiration != null) {
			// date of expiration is compared.
			if (dateofExpiration.before(new java.util.Date())) {
				// System.out.println(
				// "CachedResultSet.isExpired:  Expired from Cache! EXPIRE TIME : "
				// +
				// dateofExpiration.toString() + " CURRENT TIME : " +
				// (new java.util.Date() ).toString()
				// );
				return true;
			} else {
				// System.out.println(
				// "CachedResultSet.isExpired:  Expired not from Cache!");
				return false;
			}
		} else // This means it lives forever!
		{
			return false;
		}
	}

	@Override
	public boolean equals(Object obj) {
		return this.object.equals(((CachedObject) obj).object);
	}

	@Override
	public int compareTTL(CachedObject object) {
		if (timetolive > object.timetolive)
			return 1;
		else if (timetolive == object.timetolive)
			return 0;
		return -1;
	}

}
