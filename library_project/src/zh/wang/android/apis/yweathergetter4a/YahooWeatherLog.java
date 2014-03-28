package zh.wang.android.apis.yweathergetter4a;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

class YahooWeatherLog {
	
	public static final String TAG = "YWeatherGetter4a";
	public static boolean isDebuggable = true;
	
	public static void setDebuggable(final boolean isDebuggable) {
	    YahooWeatherLog.isDebuggable = isDebuggable;
	}
	
	public static void d(final String tag, final String message) {
		if (!isDebuggable) return;
		Log.d(tag, message);
	}
	
	public static void d(final String message) {
		if (!isDebuggable) return;
		Log.d(TAG, message);
	}
	
	public static void shortToast(final Context context, final String s) {
	    Toast.makeText(context, TAG + s, Toast.LENGTH_SHORT).show();
	}

}
