package com.timerchina.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyeclipseKeyMaker {

	public MyeclipseKeyMaker() {
	}

	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String userId = null;
		while (userId == null || "".equals(userId.trim())) {
			System.out.print("Subscriber: ");
			try {
				userId = br.readLine();
			} catch (IOException ioexception) {
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 2);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String seed = "000-" + sdf.format(cal.getTime()) + "0";

		// for pro version
		String need = userId.substring(0, 1) + "YE3MP-300" + seed;
		String dx = need + LICENSE + userId;
		int suf = decode(dx);
		String code = need + suf;
		System.out.println("Subscription Code: \n" + change(code));

		// for blue version
		need = userId.substring(0, 1) + "YE3MB-300" + seed;
		dx = need + LICENSE + userId;
		suf = decode(dx);
		code = need + suf;
		System.out
				.println("\nBlue Editon Subscription Code: \n" + change(code));

		// for Spring version
		need = userId.substring(0, 1) + "YE3MS-300" + seed;
		dx = need + LICENSE + userId;
		suf = decode(dx);
		code = need + suf;
		System.out.println("\nSpring Editon Subscription Code: \n"
				+ change(code));
	}

	private final static String LICENSE = "Decompiling this copyrighted software is a violation of both your license agreement and the Digital Millenium Copyright Act of 1998 (http://www.loc.gov/copyright/legislation/dmca.pdf). Under section 1204 of the DMCA, penalties range up to a $500,000 fine or up to five years imprisonment for a first offense. Think about it; pay for a license, avoid prosecution, and feel better about yourself.";

	static int decode(String s) {
		int i = 0;
		char ac[] = s.toCharArray();
		int j = 0;
		for (int k = ac.length; j < k; j++)
			i = 31 * i + ac[j];
		return Math.abs(i);
	}

	static String change(String s) {
		if (s == null || s.length() == 0)
			return s;
		byte abyte0[] = s.getBytes();
		char ac[] = new char[s.length()];
		int i = 0;
		for (int k = abyte0.length; i < k; i++) {
			int j = abyte0[i];
			if (j >= 48 && j <= 57)
				j = ((j - 48) + 5) % 10 + 48;
			else if (j >= 65 && j <= 90)
				j = ((j - 65) + 13) % 26 + 65;
			else if (j >= 97 && j <= 122)
				j = ((j - 97) + 13) % 26 + 97;
			ac[i] = (char) j;
		}
		return String.valueOf(ac);
	}
}
