package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//C:\Users\Ashok\workspace\TTLCache_Generic\src\files\C.txt
//C:\Users\Ashok\workspace\TTLCache_Generic\src\files\IPTTL1.txt

public class TTLCache {
	static Scanner scanner = null;
	static int EntriesAtATime = 100;
	static boolean quit = false;
	static boolean back = false;
	static Thread MainThread = Thread.currentThread();
	private static int LogFileUpdate = 0;

	static public void main(String[] argv) throws InterruptedException {

		println("**************** Generic TTL Cache ****************");
		CacheManager Cm = null;

		while (true) {
			Thread InputGetter = new Thread(new Runnable() {

				@Override
				public void run() {

					while (true) {
						String sig = null;
						try {
							sig = readConsole();
							println("You Entered : " + sig);
							if (sig.equalsIgnoreCase("back")) {
								back = true;
								MainThread.interrupt();
								return;
							}
						} catch (IOException ex) {
							Logger.getLogger(TTLCache.class.getName()).log(
									Level.SEVERE, null, ex);
						} catch (quitException ex) {
							println("You said to quit ... ");
							quit = true;
							MainThread.interrupt();

							break;
						}
					}
					println("Input Taker Thread : stopped");
				}
			});

			int Refresh = 0;
			String Sample_File = null;
			String Cache_file = null;
			int Cache_Size = 0;
			int Replacing_Scheme = 0;
			int InputFileRead = 0;

			try {

				println("****************     MAIN MENU     ****************");
				println("Enter the name/path of Cache file ( e.g. cache.txt ) :  ");

				while (true) {
					Cache_file = readConsole();
					if (FileExists(Cache_file)) {
						break;
					}
					println("File doesnot Exists.");
				}
				while (true) {
					println("Enter the size ( number of entries ) of the Cache ");
					try {
						Cache_Size = Integer.parseInt(readConsole());
						if (Cache_Size > 0) {
							break;
						} else {
							println("Wrong Size.Value Must be greatter than 0.");
						}
					} catch (NumberFormatException e) {
						println("Wrong integer input.");
					}
				}
				while (true) {
					println("How Often do you want to update CacheFile ( Cache Refresh Rate - Seconds ) :");
					try {
						Refresh = Integer.parseInt(readConsole());
						if (Refresh > 0) {
							break;
						} else {
							println("Wrong Input.Value  Must be greatter than 0.");
						}
					} catch (NumberFormatException e) {
						println("Wrong integer input.");
					}
				}

				while (true) {
					println("Enter the first file from which program will start reading data (e.g. sample1.txt ) : ");
					Sample_File = readConsole();
					if (FileExists(Sample_File)) {
						break;
					}
					println("File doesnot Exists.");
				}

				while (true) {
					println("How Often do you want to check next Sample file in the sequence ( seconds ) :");
					try {
						InputFileRead = Integer.parseInt(readConsole());
						if (Refresh > 0) {
							break;
						} else {
							println("Wrong Input.Value  Must be greatter than 0.");
						}
					} catch (NumberFormatException e) {
						println("Wrong integer input.");
					}
				}
				while (true) {
					println("Number of entries to read from Sample file AT A TIME :");
					try {
						EntriesAtATime = Integer.parseInt(readConsole());
						if (EntriesAtATime >= 50) {
							break;
						} else {
							println("Wrong Input.Value  Value must be equal to 50 atleast.");
						}
					} catch (NumberFormatException e) {
						println("Wrong integer input.");
					}
				}

				while (true) {
					println("How Often do you want update LOG FILE ( seconds ) :");
					try {
						LogFileUpdate = Integer.parseInt(readConsole());
						if (LogFileUpdate > 0) {
							break;
						} else {
							println("Wrong Input.Value  Must be greatter than 0.");
						}
					} catch (NumberFormatException e) {
						println("Wrong integer input.");
					}
				}

				while (true) {

					try {
						println("Enter Replacing Sceheme . 1 for LRU and 2 for FIFO");
						Replacing_Scheme = Integer.parseInt(readConsole());
						if (Replacing_Scheme > 0 && Replacing_Scheme < 3) {
							break;
						} else {
							println("Wrong Replacing Scheme. Value Must be greatter than 0 and less than 3.");
						}
					} catch (NumberFormatException e) {
						println("Wrong integer input.");
					}
				}
				println("From Now ONWARDS ......");
				println("type 'quit' or 'exit' to terminate the program. OR type 'back' to Go To Main Menu (AnyTIme) ");
				InputGetter.start();
				FileLogger.copytoLog("Cache File : " + Cache_file
						+ " -- Sample Data File :" + Sample_File
						+ " -- Cache Size : " + Cache_Size
						+ " -- Cache Refresh Rate : " + Refresh
						+ " -- Cache Replacing Policy : "
						+ (Replacing_Scheme == 1 ? "LRU" : "FIFO"));
				FileLogger
						.copytoLog("\n\nTime\tTotal_Hits\tTotal_Miss\tEntries_in_Queue\t"
								+ "Entries_Expired_in_Queue\tTotal_Entries\n");

				Cm = CacheManager.GetCacheManagerInstance(Cache_Size,
						Cache_file, Refresh, LogFileUpdate, Replacing_Scheme);
				boolean end = false;
				FileLogger.openNewHandle();
				int entries = 0;
				while (!end) {
					start_Reading(Sample_File);
					println("Reading data from File : " + Sample_File);
					while (true) {

						LinkedList LIST = getNorLessEntriesFromFile();
						if (LIST.size() == 0) {
							/*
							 * while (LIST.size() == 0) { println(
							 * "File seems Empty : Waiting for user to input data . Program will check the file after "
							 * + InputFileRead + " Seconds .");
							 * Thread.sleep(InputFileRead * 1000);
							 * println("Reading data from File : " +
							 * Sample_File); start_Reading(Sample_File); LIST =
							 * getNorLessEntriesFromFile(); }
							 */

						}
						List list = Collections.synchronizedList(LIST);
						for (Object obj : list) {
							CachedObject entry = (CachedObject) obj;
							Cm.putEntry(entry);
							entries++;
						}

						if (list.size() < EntriesAtATime) {
							break;
						}

						// remove data from sample.txt
					}
					println("\nFile Completely Read -> " + Sample_File);
					println("***********************************************************************************");
					Sample_File = getNextFileName(Sample_File);
					while (!FileExists(Sample_File)) {
						println("Reading in Sequence . File not found ->  "
								+ Sample_File);
						println("Program will check after " + InputFileRead
								+ " seconds");
						Thread.sleep(InputFileRead * 1000);
					}
					start_Reading(Sample_File);

				}
				FileLogger.closeLogHandle();
			} catch (InterruptedException ex) {
				println("Got the signal");
				Cm.refresh();
				Cm.killThreads();
				if (quit) {
					println("Program Stopping ...");
					return;

				}
				back = false;
				quit = false;
				Thread.sleep(2 * 1000);
			} catch (IOException ex) {
				Logger.getLogger(TTLCache.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (quitException ex) {
				break;
			}
		}

	}

	// function to check if user has input the 'quit' keyword
	public static boolean isQuitSignal(String sig) {
		if (sig.compareToIgnoreCase("quit") == 0
				|| sig.compareToIgnoreCase("exit") == 0) {
			return true;
		}
		return false;
	}

	public static String readConsole() throws IOException, quitException {
		BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
		String line = BR.readLine();
		if (isQuitSignal(line)) {
			throw new quitException();
		}
		return line;

	}

	static public void println(Object v) {
		System.out.println(v);
	}

	static public void print(Object v) {
		System.out.print(v);
	}

	private static boolean FileExists(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	static void start_Reading(String _fFile) throws FileNotFoundException {
		scanner = new Scanner(new FileReader(_fFile));
		// get file lock
	}

	static LinkedList getNorLessEntriesFromFile() throws FileNotFoundException {
		LinkedList list = new LinkedList();
		try {
			// first use a Scanner to get each line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split("[\\s|\\t]");
				tokens[0] = tokens[0].trim();
				if (ipAddress.isvalid(tokens[0])) {

					try {
						Integer.parseInt(tokens[1]);
						list.add(new CachedObject(new ipAddress(tokens[0]),
								Integer.parseInt(tokens[1])));
					} catch (NumberFormatException ex) {
						continue;
					}
					if (list.size() == EntriesAtATime)
						return list;
				}

			}
		} finally {
			// ensure the underlying stream is always closed
			// this only has any effect if the item passed to the Scanner
			// constructor implements Closeable (which it does in this case).
		}
		return list;

	}

	static void endReading(String sampleFile) throws IOException,
			InterruptedException {
		scanner.close();
		FileWriter FW = new FileWriter(new File(sampleFile));
		FW.write("");
		FW.close();
	}

	public static String getNextFileName(String Sample_File) {
		Sample_File = Sample_File.replace(".", ",");
		String[] tokens = Sample_File.split(",");
		String name = tokens[0];
		String extension = tokens.length == 2 ? tokens[1] : "";
		int i = name.length() - 1;
		for (; i >= 0; --i) {
			if (!(name.charAt(i) >= '1' && name.charAt(i) <= '9')) {
				break;
			}
		}
		i++;
		String numberPart = null;
		String namePart = null;
		if (i != 0)
			namePart = String.copyValueOf(name.toCharArray(), 0, i);
		else
			namePart = "";

		if (i != name.length()) {
			numberPart = String.copyValueOf(name.toCharArray(), i,
					name.length() - i);
			namePart += Integer.parseInt(numberPart) + 1;
		} else
			namePart += "1";
		return namePart + "." + extension;
	}

}
