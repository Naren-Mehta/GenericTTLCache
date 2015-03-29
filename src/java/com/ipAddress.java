/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.util.Scanner;

/**
 * 
 * @author CODE
 */
public class ipAddress implements Comparable {
	String _ip = null;
	long value = 0;

	public String getip() {
		return _ip;
	}

	public void setip(String ip) {
		_ip = ip;
	}

	public ipAddress(String ip) {
		_ip = ip;
		value = toNumeric(ip);
	}

	public long getvalue() {
		return value;
	}

	public static Long toNumeric(String ip) {
		Scanner sc = new Scanner(String.valueOf(ip)).useDelimiter("\\.");
		long value = (sc.nextLong() << 24) + (sc.nextLong() << 16)
				+ (sc.nextLong() << 8) + (sc.nextLong());
		return value;
	}

	@Override
	public int compareTo(Object ip2) {
		if (value > ((ipAddress) ip2).value)
			return 1;
		if (value < ((ipAddress) ip2).value)
			return -1;
		return 0;

	}

	@Override
	public boolean equals(Object ip2) {
		if (!(ip2 instanceof ipAddress) || _ip == null)
			return false;
		return SimpleEquals((ipAddress) ip2);
	}

	public boolean SimpleEquals(Object ip2) {
		Long ip2Value = ((ipAddress) ip2).value;
		if (ip2Value.equals(value))
			return true;
		return false;
	}

	/*
	 * public boolean fullEquals( Object ip2 ) {
	 * 
	 * if ( _ip.equals(((ipAddress)ip2)._ip) ) if ( ( _timeStamp != null &&
	 * ((ipAddress)ip2)._timeStamp != null &&
	 * _timeStamp.equals(((ipAddress)ip2)._timeStamp) ) || _timeStamp == null ||
	 * ((ipAddress)ip2)._timeStamp == null ) // specially for null return true;
	 * 
	 * return false; }
	 */
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + (this._ip != null ? this._ip.hashCode() : 0);
		return hash;
	}

	public void print() {
		System.out.println(_ip);
	}

	@Override
	public String toString() {
		return _ip;
	}

	/*
	 * public String toString(int flag) { SimpleDateFormat format = new
	 * SimpleDateFormat("MMM d hh:mm:ss"); return flag == 2 ?
	 * format.format(_timeStamp) + " " + _ip : _ip ; }
	 */

	public static boolean isvalid(String ip) {
		String[] tokens = ip.split("\\.");
		if (tokens.length != 4) {
			return false;
		}

		try {
			for (String str : tokens) {
				int i = Integer.parseInt(str);
				if ((i < 0) || (i > 255)) {
					return false;
				}
			}
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

}
