package zh.wang.android.apis.yweathergetter4a;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    
    /** default timeout is 20 seconds */
    private int mConnectTimeout = 20 * 1000;
    private int mSocketTimeout = 20 * 1000;
    
    private static NetworkUtils sInstance = new NetworkUtils();
    
    public static NetworkUtils getInstance() {
        return sInstance;
    }
    
	public static boolean isConnected(final Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

		return (networkInfo != null && networkInfo.isConnected());
	}
	
	public static HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, getInstance().mConnectTimeout);
		HttpConnectionParams.setSoTimeout(params, getInstance().mSocketTimeout);
		HttpClient httpClient = new DefaultHttpClient(params);
	    return httpClient;
	}

    public void setConnectTimeout(int connectTimeout) {
        mConnectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        mSocketTimeout = socketTimeout;
    }
}
