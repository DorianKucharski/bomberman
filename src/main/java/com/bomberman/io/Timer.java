package com.bomberman.io;

/**
 * Timer petli gry
 */
public class Timer {
	/**
	 * Zwraca czas w sekundach
	 * @return czas w sekundach
	 */
	public static double getTime() {
		return (double) System.nanoTime() / (double) 1000000000L;
	}
}
