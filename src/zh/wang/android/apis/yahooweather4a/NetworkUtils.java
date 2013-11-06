package zh.wang.android.apis.yahooweather4a;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	public static boolean isConnected(final Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

		return (networkInfo != null && networkInfo.isConnected());
	}
}
