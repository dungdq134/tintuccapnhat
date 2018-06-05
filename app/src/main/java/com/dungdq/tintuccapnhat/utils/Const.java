package com.dungdq.tintuccapnhat.utils;

public class Const {
//	public static String urlAPI = "https://api.recsys.opera.com/api/1.0/suggestions/list?count=10&country=vn&language=vi&vcat=vn%3Avi%3A0%2F6&vco=0%2F19";
	
	public static String urlAPI = "https://tinhot24h.firebaseio.com/opera.json";

	public static String urlParser = "https://readability.com/api/content/v1/parser?token=07e298ca25db2e52f54aa39f07607eb6c25c2034&url=";

	public static String TAG_ID = "id";
	public static String TAG_TITLE = "title";
	public static String TAG_SUMMARY = "summary";
	public static String TAG_THUMBNAIL = "thumbnail";
	public static String TAG_AUTHOR = "author";
	public static String TAG_URL = "url";

	// Rate application
	public static int DAYS_UNTIL_PROMPT = 1;
	public static int LAUNCH_UNTIL_PROMPT = 2;
	public static String APP_PACKAGE_NAME = "";

	// Time to push notification
	public static String morning = "8:00";
	public static String afternoon = "13:00";
	public static String evening = "22:00";

	// Banner Ad
	public static boolean adBanner = false;

	// Ad test device id
	public static String adTestDeviceID = "588C116E875BB11C57E186CCAF5FCC1A";
}
