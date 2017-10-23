package com.xingyanping.util;

public class NumberFunctions {
	public static boolean isEQ(Long l1, Long l2, boolean bothNullEqual) {
		if (l1 == null && l2 == null) {
			return bothNullEqual;
		}
		if (l1 == null || l2 == null) {
			return false;
		}
		return l1.equals(l2);
	}
}
