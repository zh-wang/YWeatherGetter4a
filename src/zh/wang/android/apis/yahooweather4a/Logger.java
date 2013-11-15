package zh.wang.android.apis.yahooweather4a;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

public class Logger {
	
	public static boolean isDebuggable = false;

	public static void init(final Context context) {
		isDebuggable =  ( 0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
	}
	
	public static void d(final String tag, final String message) {
		Log.d(tag, message);
	}

}
