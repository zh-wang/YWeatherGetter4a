package zh.wang.android.yweathergetter4a;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.TimerTask;

/*
 * Acquiring User location need permission
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 */

class UserLocationUtils {

    public enum UserLocationErrorType {
        FIND_LOCATION_NOT_PERMITTED,
        LOCATION_SERVICE_IS_NOT_AVAILABLE,
        Unknown,
    }

    private LocationManager mLocationManager;
    private LocationResult mLocationResult;
    private boolean mGpsEnabled = false;
    private boolean mNetworkEnabled = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private GetLastLocation mGetLastLocationTask;
    private UserLocationErrorType mErrorType = null;

    public void findUserLocation(Context context, LocationResult result)
    {
        mLocationResult = result;
        if(mLocationManager == null) {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        //exceptions will be thrown if provider is not permitted.
        try{
            mGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            mErrorType = UserLocationErrorType.LOCATION_SERVICE_IS_NOT_AVAILABLE;
        }
        try{
            mNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            mErrorType = UserLocationErrorType.LOCATION_SERVICE_IS_NOT_AVAILABLE;
        }

        //don't start listeners if no provider is enabled
        if(!mGpsEnabled && !mNetworkEnabled) {
            result.gotLocation(null, mErrorType);
            return;
        }

        if(mGpsEnabled) {
            try {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            } catch (SecurityException e) {
                mErrorType = UserLocationErrorType.FIND_LOCATION_NOT_PERMITTED;
            }
        }
        if(mNetworkEnabled) {
            try {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            } catch (SecurityException e) {
                mErrorType = UserLocationErrorType.FIND_LOCATION_NOT_PERMITTED;
            }
        }

        if (mErrorType != null) {
            mLocationResult.gotLocation(null, mErrorType);
        } else {
            mGetLastLocationTask = new GetLastLocation();
            mHandler.postDelayed(mGetLastLocationTask, 20000);
        }

        return;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            mLocationResult.gotLocation(location, mErrorType);

            try {
                mLocationManager.removeUpdates(this);
                mLocationManager.removeUpdates(locationListenerNetwork);
            } catch (SecurityException e) { }

            if (mGetLastLocationTask != null) {
                mHandler.removeCallbacks(mGetLastLocationTask);
                mGetLastLocationTask = null;
            }
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            mLocationResult.gotLocation(location, mErrorType);
            try {
                mLocationManager.removeUpdates(this);
                mLocationManager.removeUpdates(locationListenerGps);
            } catch (SecurityException e) {}
            if (mGetLastLocationTask != null) {
                mHandler.removeCallbacks(mGetLastLocationTask);
                mGetLastLocationTask = null;
            }
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            YahooWeatherLog.d("20 secs timeout for GPS. GetLastLocation is executed.");
            try {
                mLocationManager.removeUpdates(locationListenerGps);
                mLocationManager.removeUpdates(locationListenerNetwork);
            } catch (SecurityException e) {}

            Location net_loc=null, gps_loc=null;
            if(mGpsEnabled) {
                try {
                    gps_loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException e) {
                    mErrorType = UserLocationErrorType.FIND_LOCATION_NOT_PERMITTED;
                }
            }
            if(mNetworkEnabled) {
                try {
                    net_loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } catch (SecurityException e) {
                    mErrorType = UserLocationErrorType.FIND_LOCATION_NOT_PERMITTED;
                }
            }

            //if there are both values use the latest one
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                    mLocationResult.gotLocation(gps_loc, mErrorType);
                else
                    mLocationResult.gotLocation(net_loc, mErrorType);
                return;
            }

            if(gps_loc!=null){
                mLocationResult.gotLocation(gps_loc, mErrorType);
                return;
            }
            if(net_loc!=null){
                mLocationResult.gotLocation(net_loc, mErrorType);
                return;
            }
            mErrorType = UserLocationErrorType.Unknown;
            mLocationResult.gotLocation(null, mErrorType);
        }
    }

    public interface LocationResult{
        void gotLocation(Location location, UserLocationErrorType errorType);
    }
}