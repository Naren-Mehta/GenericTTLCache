/**
 * Title:        Caching
 Description:  This interface defines the methods, which must be implemented
 by
 all objects wishing to be placed in the cache.
 *
 * Copyright:    Copyright (c) 2001
 * Company:  JavaWorld
 * FileName:     Cacheable.java
 @author Jonathan Lurie
 @version 1.0
 */
package com;

public interface Cacheable {
	/*
	 * By requiring all objects to determine their own expirations, the
	 * algorithm is abstracted from the caching service, thereby providing
	 * maximum flexibility since each object can adopt a different expiration
	 * strategy.
	 */
	@Override
	public boolean equals(Object obj);

	public boolean isExpired();

	public int compareTTL(CachedObject object);

}