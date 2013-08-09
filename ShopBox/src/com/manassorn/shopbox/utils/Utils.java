package com.manassorn.shopbox.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.manassorn.shopbox.SettingsFragment;

public class Utils {

	public static String sha1(String toHash) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		String hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = toHash.getBytes("UTF-8");
			digest.update(bytes, 0, bytes.length);
			bytes = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02X", b));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		return hash;
	}
	
	public static boolean verifyPasscode(Context context, String text) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String passcodeSha1 = prefs.getString(SettingsFragment.PREF_PASSCODE, SettingsFragment.PREF_PASSCODE_DEFAULT_SHA1);
		
		try {
			return Utils.sha1(text).equals(passcodeSha1);
		} catch (Exception e) {
			Toast.makeText(context, "มือถือไม่สนับสนุน SHA1", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	public static boolean changePasscode(Context context, String text) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		try {
			return prefs.edit().putString(SettingsFragment.PREF_PASSCODE, Utils.sha1(text)).commit();
		} catch (Exception e) {
			Toast.makeText(context, "มือถือไม่สนับสนุน SHA1", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}
