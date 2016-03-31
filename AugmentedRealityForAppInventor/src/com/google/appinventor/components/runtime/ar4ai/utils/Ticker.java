package com.google.appinventor.components.runtime.ar4ai.utils;

public class Ticker {
	private int rate;
	private long s2;
	
	public Ticker(int tickrateMS) {
		rate = tickrateMS;
		s2 = Ticker.getTime();
	}

	public static long getTime() {
		return System.currentTimeMillis();
	}

	public int getTicks() {
		long i = Ticker.getTime();
		if (i - s2 > rate) {
			int ticks = (int) ((i - s2) / (long) rate);
			s2 += (long) rate * ticks;
			return ticks;
		}
		return 0;
	}
}
