/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author CODE
 */
public class FileLogger {

	static List log = Collections.synchronizedList(new LinkedList<String>());
	static String file = "LogFile.txt";
	static boolean opened = false;
	static FileWriter FW = null;

	static void copytoLog(String message) {
		log.add(message);
	}

	static boolean openNewHandle() throws IOException {
		System.out.println("Generating " + file + " ... ");
		FW = new FileWriter(file);
		return true;
	}

	static private boolean openAppendHandle() throws IOException {

		FW = new FileWriter(file, true);
		return true;
	}

	static void flush() throws IOException {

		try {
			for (Object obj : log) {

				String Entry = (String) obj;
				FW.write(Entry);
			}
		} catch (IOException ex) {

		} finally {
			FW.close();
			openAppendHandle();
		}
		log = new LinkedList<String>();
	}

	static void closeLogHandle() throws IOException {
		FW.close();
	}
}
