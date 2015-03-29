/**
 * Title:        Caching
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:  JavaWorld
 * Filename:  CacheManager.java
 * @author Jonathan Lurie
 * @version 1.0
 */
package com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.PortableServer.THREAD_POLICY_ID;

public class CacheManager {
	/* This is the HashMap that contains all objects in the cache. */

	private List<Cacheable> Cache = null;
	private List<Cacheable> data = null;
	private int cache_entries = 0;
	private int cache_size = 0;
	private int refresh_rate = 0;
	private String cache_file;
	String logfile = "";
	private int log_rate = 0;
	private int hits = 0;
	private int QueueDrop = 0;
	private int miss = 0;
	private int replacing_policy = 0; // 1 - LRU , 2- LFU , 3- FIFO

	private Thread logger_thread = null;
	private Thread refresher = null;
	private int total_Entries = 0;

	/* This object acts as a semaphore, which protects the HashMap */
	/* RESERVED FOR FUTURE USE private static Object lock = new Object(); */

	private CacheManager(int cachesize, String file, int refresh, int log,
			int replacing) {
		Cache = Collections.synchronizedList(new LinkedList<Cacheable>());
		data = Collections.synchronizedList(new LinkedList<Cacheable>());
		cache_size = cachesize;
		cache_file = file;
		replacing_policy = replacing;
		refresh_rate = refresh;
		log_rate = log;

	}

	public static boolean ManagerInstance = false;

	public static CacheManager GetCacheManagerInstance(int cache_size,
			String cache_file, int refresh, int log, int replacing)
			throws FileNotFoundException {
		if (!ManagerInstance) {
			ManagerInstance = true;
			CacheManager CM = new CacheManager(cache_size, cache_file, refresh,
					log, replacing);
			CM.getfromcachefile();
			CM.start_threads();
			return CM;
		}
		return null;
	}

	private void start_threads() {

		logger_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(log_rate * 1000);
						System.out.println("Logger Thread : Updating Log File");
						LogDetails();
						FileLogger.flush();
					}
				} catch (IOException ex) {
					Logger.getLogger(CacheManager.class.getName()).log(
							Level.SEVERE, null, ex);
				} catch (InterruptedException ex) {
				}
				System.out.println("Logger Thread : stopped");
			}
		});
		logger_thread.start();

		refresher = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						cleanTheCache();
						cleanTheQueue();
						if (cache_entries < cache_size) {
							putIntoCache();
						}
						System.out
								.println("Refresher Thread : Updating Cache File ... "
										+ cache_file);
						DumptoCacheFile(Cache);
						Thread.sleep(refresh_rate * 1000);
					}
				} catch (InterruptedException ex) {

				} catch (IOException ex) {
					Logger.getLogger(CacheManager.class.getName()).log(
							Level.SEVERE, null, ex);
				}
				System.out.println("Cache Refresher Thread : stopped\n");

			}
		});
		refresher.start();
	}

	private void cleanTheCache() {
		LinkedList Del = new LinkedList();
		synchronized (Cache) {
			for (Cacheable Entry : Cache) {
				if (Entry.isExpired()) {
					Del.add(Entry);
				}
			}
			Cache.removeAll(Del);
			cache_entries -= Del.size();
		}
	}

	private void cleanTheQueue() {
		LinkedList Del = new LinkedList();
		synchronized (data) {
			for (Cacheable Entry : data) {
				if (Entry.isExpired()) {
					Del.add(Entry);
				}
			}

			QueueDrop += Del.size();
			data.removeAll(Del);
		}
	}

	private void putIntoCache() {
		LinkedList<Cacheable> list = new LinkedList<Cacheable>();
		synchronized (data) {
			for (Cacheable obj : data) {
				if (obj.isExpired()) {
					list.add(obj);
				} else {
					break;
				}
			}
			QueueDrop += list.size();
			data.removeAll(list);
			if (data.isEmpty()) {
				return;
			}
			synchronized (Cache) {
				while (!data.isEmpty() && cache_entries < cache_size) {
					Cacheable obj = data.remove(0);
					Cache.add(obj);
					cache_entries++;
				}
			}
		}
	}

	public boolean putEntry(Cacheable object) throws FileNotFoundException,
			IOException {
		total_Entries++;
		// first check if entry in cache then if we can replace it ( it's a hit
		// )
		// else place it in the queue
		synchronized (Cache) {

			for (Cacheable entry : Cache) {
				if (entry.equals(object)) {
					if (entry.compareTTL((CachedObject) object) < 0) {
						((CachedObject) entry).timetolive = ((CachedObject) object).timetolive;
						hits++;
						return true;
					} else if (entry.compareTTL((CachedObject) object) == 0) {
						hits++;
						return true;
					}
					hits++;
					return false; // Cache not updated
				}
			}
			miss++;
			// put directly into the cache if cache is not full
			if (cache_entries < cache_size) {
				Cache.add(object);
				cache_entries++;
				return false;
			}
		}

		CachedObject Entrytochange = null;
		synchronized (data) {
			for (Cacheable obj : data) {
				CachedObject entry = (CachedObject) obj;
				if (entry.equals(object)) {
					// we don't need to change cache
					if (entry.compareTTL((CachedObject) object) == 0
							|| entry.compareTTL((CachedObject) object) < 0) {
						return false;
					}
					Entrytochange = entry;
				}
			}
			if (replacing_policy == 3) {
				data.add(data.size(), object);
				return false;
			}
			if (Entrytochange != null) {
				data.remove(Entrytochange);
			}
			// FIFO
			if (replacing_policy == 1) {
				// LRU
				data.add(data.size(), object);

			}
		}
		return false;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private void DumptoCacheFile(List list) throws IOException {
		FileWriter FW = null;
		try {

			FW = new FileWriter(cache_file);
			synchronized (list) {
				for (Object obj : list) {
					CachedObject Entry = (CachedObject) obj;
					FW.write(((ipAddress) Entry.object)._ip + " "
							+ Entry.timetolive + "\n");
				}
			}
		} catch (IOException ex) {
		} finally {
			FW.close();
		}
	}

	void LogDetails() {
		Date D = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MMM d hh:mm:ss");
		FileLogger.copytoLog("\n" + format.format(D));
		FileLogger.copytoLog("\t" + hits);
		FileLogger.copytoLog("\t" + miss);
		FileLogger.copytoLog("\t" + data.size());
		FileLogger.copytoLog("\t" + QueueDrop);
		FileLogger.copytoLog("\t" + total_Entries);

	}

	void killThreads() throws InterruptedException {
		refresher.interrupt();
		logger_thread.interrupt();

	}

	public static void refresh() {
		ManagerInstance = false;
	}

	private void getfromcachefile() throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(cache_file));
		LinkedList list = new LinkedList();
		try {
			// first use a Scanner to get each line
			int i = 0;
			while (scanner.hasNextLine() && i < cache_size) {
				String line = scanner.nextLine();
				String[] tokens = line.split("[\\s|\\t]");
				tokens[0] = tokens[0].trim();
				if (ipAddress.isvalid(tokens[0])) {

					try {
						Integer.parseInt(tokens[1]);
						CachedObject CO = new CachedObject(new ipAddress(
								tokens[0]), Integer.parseInt(tokens[1]));
						if (!list.contains(CO)) {
							list.add(CO);
						}
					} catch (NumberFormatException ex) {
						continue;
					}
				}
				i++;
			}
		} finally {
			// ensure the underlying stream is always closed
			// this only has any effect if the item passed to the Scanner
			// constructor implements Closeable (which it does in this case).
		}
		synchronized (Cache) {
			Cache.addAll(list);
		}
	}

}