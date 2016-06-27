/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2014 Zhenghong Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zh.wang.android.apis.yweathergetter4a;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import zh.wang.android.apis.yweathergetter4a.UserLocationUtils.LocationResult;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo.ForecastInfo;

/**
 * A wrapper for accessing Yahoo weather informations. 
 * @author Zhenghong Wang
 */
public class YahooWeather implements LocationResult {

    private static final String YQL_WEATHER_ENDPOINT_AUTHORITY = "query.yahooapis.com";
    private static final String YQL_WEATHER_ENDPOINT_PATH = "/v1/public/yql";

    private static final int CONNECT_TIMEOUT_DEFAULT = 20 * 1000;
    private static final int SOCKET_TIMEOUT_DEFAULT = 20 * 1000;

	public enum SEARCH_MODE {
		GPS,
		PLACE_NAME
	}
	
	public enum UNIT {
	    FAHRENHEIT,
	    CELSIUS,
	}
	
	public static final String YAHOO_WEATHER_ERROR = "Yahoo! Weather - Error";

	public static final int FORECAST_INFO_MAX_SIZE = 5;
	
	private String mWoeidNumber;
	private YahooWeatherInfoListener mWeatherInfoResult;
	private YahooWeatherExceptionListener mExceptionListener;
	private boolean mNeedDownloadIcons;
	private SEARCH_MODE mSearchMode;
	
	// Use Metric units by default
	private UNIT mUnit = UNIT.CELSIUS;
	
	private Context mContext;
	private static YahooWeather mInstance = new YahooWeather();
	
	public SEARCH_MODE getSearchMode() {
		return mSearchMode;
	}

	public void setSearchMode(SEARCH_MODE searchMode) {
		mSearchMode = searchMode;
	}
	
	/**
	 * Necessary only if Imperial units to be used instead of Metric
	 * @param unit, should be {@link UNIT#CELSIUS} or {@link UNIT#FAHRENHEIT}. {@link UNIT#CELSIUS} in default.
	 * Units for temperature Fahrenheit or Celsius
	 * See {@link YahooWeather#turnCtoF(int)} and {@link YahooWeather#turnFtoC(int)}
     */
	public void setUnit(UNIT unit) {
        mUnit = unit;
	}
	
	public UNIT getUnit() {
		return mUnit;
	}

	/**
	 * Get the YahooWeather instance.
	 * Use this to query weather information from Yahoo.
	 * @return YahooWeather instance
	 */
	public static YahooWeather getInstance() {
	    getInstance(CONNECT_TIMEOUT_DEFAULT, SOCKET_TIMEOUT_DEFAULT);
		return mInstance;
	}
	
	/**
	 * Get the YahooWeather instance.
	 * Use this to query weather information from Yahoo.
	 * @param connectTimeout in milliseconds, 5 seconds in default
	 * @param socketTimeout in milliseconds, 5 seconds in default
	 * @return YahooWeather instance
	 */
	public static YahooWeather getInstance(int connectTimeout, int socketTimeout) {
	    return getInstance(connectTimeout, socketTimeout, false);
	}
	
	/**
	 * Get the YahooWeather instance.
	 * Use this to query weather information from Yahoo.
	 * @param connectTimeout in milliseconds, 5 seconds in default
	 * @param socketTimeout in milliseconds, 5 seconds in default
	 * @param isDebuggable set if you want some debug log in Logcat
	 * @return YahooWeather instance
	 */
	public static YahooWeather getInstance(int connectTimeout, int socketTimeout, boolean isDebuggable) {
	    YahooWeatherLog.setDebuggable(isDebuggable);
	    NetworkUtils.getInstance().setConnectTimeout(connectTimeout);
	    NetworkUtils.getInstance().setSocketTimeout(socketTimeout);
		return mInstance;
	}
	
	/**
	 * Set it to true will enable downloading the default weather icons.
	 * The Default icons are too tiny, so in most cases, you don't need them.
	 * @param needDownloadIcons Weather it will enable downloading the default weather icons
	 */
	public void setNeedDownloadIcons(final boolean needDownloadIcons) {
		mNeedDownloadIcons = needDownloadIcons;
	}
	
	/**
	 * Set exception listener. 
	 * If this is not set, stack info will be printed in logcat if 'isDebuggable' is set to true.
	 * Remember, these methodas may be called on background thread. Therefore, any UI related
	 * activities must be post to UI thread, using {@link Handler} or something else.
	 * @param exceptionListener
	 */
	public void setExceptionListener(final YahooWeatherExceptionListener exceptionListener) {
	    this.mExceptionListener = exceptionListener;
	}
	
	/**
	 * Use a name of place to query Yahoo weather apis for weather information. 
	 * Querying will be run on a separated thread to accessing Yahoo's apis.
	 * When it is completed, a callback will be fired.
	 * See {@link YahooWeatherInfoListener} for detail.
	 * @param context app's context
	 * @param cityAreaOrLocation A city name, like "Shanghai"; an area name, like "Mountain View";
	 * a pair of city and country, like "Tokyo, Japan"; a location or view spot, like "Eiffel Tower";
	 * Yahoo's apis will find a closest position for you.
	 * @param result A {@link WeatherInfo} instance.
	 */
	public void queryYahooWeatherByPlaceName(final Context context, final String cityAreaOrLocation, 
			final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by name of place");
		mContext = context;
        if (!NetworkUtils.isConnected(context)) {
            if (mExceptionListener != null) mExceptionListener.onFailConnection(
                    new Exception("Network is not avaiable"));
        	return;
        }
        final String convertedlocation = AsciiUtils.convertNonAscii(cityAreaOrLocation);
		mWeatherInfoResult = result;
		final WeatherQueryByPlaceTask task = new WeatherQueryByPlaceTask();
		task.execute(new String[] {convertedlocation});
	}
	
	/** 
	 * Use lat & lon pair to query Yahoo weather apis for weather information.
	 * Querying will be run on a separated thread to accessing Yahoo's apis.
	 * When it is completed, a callback will be fired.
	 * See {@link YahooWeatherInfoListener} for detail.
	 * @param context app's context
	 * @param lat A string of latitude value
	 * @param lon A string of longitude value
	 * @param result A {@link WeatherInfo} instance
	 */
	public void queryYahooWeatherByLatLon(final Context context, final Double lat, final Double lon, 
			final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by lat lon");
		mContext = context;
        if (!NetworkUtils.isConnected(context)) {
            if (mExceptionListener != null) mExceptionListener.onFailConnection(
                    new Exception("Network is not avaiable"));
        	return;
        }
		mWeatherInfoResult = result;
		final WeatherQueryByLatLonTask task = new WeatherQueryByLatLonTask();
		task.execute(new Double[]{lat, lon});
	}
	
	/**
	 * Use your device's GPS to automatically detect where you are, then query Yahoo weather apis
	 * for weather information.
	 * @param context app's context
	 * @param result A {@link WeatherInfo} instance
	 */
	public void queryYahooWeatherByGPS(final Context context, final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by gps");
        if (!NetworkUtils.isConnected(context)) {
            if (mExceptionListener != null) mExceptionListener.onFailConnection(
                    new Exception("Network is not avaiable"));
        	return;
        }
		mContext = context;
		mWeatherInfoResult = result;
		(new UserLocationUtils()).findUserLocation(context, this);
	}
	
	@Override
	public void gotLocation(final Location location) {
	    if (location == null) {
	        if (mExceptionListener != null) mExceptionListener.onFailFindLocation(
                    new Exception("Location cannot be found"));
	        return;
	    }
	    final Double lat = location.getLatitude();
	    final Double lon = location.getLongitude();
	    final WeatherQueryByLatLonTask task = new WeatherQueryByLatLonTask();
	    task.execute(new Double[] {lat, lon});
	}

	public static int turnFtoC(int tempF) {
		return (int) ((tempF - 32) * 5.0f / 9);
	}
	
	public static int turnCtoF(int tempC) {
	    return (int) (tempC * 9.0f / 5 + 32);
	}
	
	public static String addressToPlaceName(final Address address) {
	    String result = "";
	    if (address.getLocality() != null) {
	        result += address.getLocality();
	        result += " ";
	    }
	    if (address.getAdminArea() != null) {
	        result += address.getAdminArea();
	        result += " ";
	    }
	    if (address.getCountryName() != null) {
	        result += address.getCountryName();
	        result += " ";
	    }
	    return result;
	}

    private String getWeatherString(Context context, String placeName) {
        YahooWeatherLog.d("query yahoo weather with placeName : " + placeName);

        String qResult = "";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority(YQL_WEATHER_ENDPOINT_AUTHORITY);
        builder.path(YQL_WEATHER_ENDPOINT_PATH);
        builder.appendQueryParameter("q", "select * from weather.forecast where woeid in" +
                        "(select woeid from geo.places(1) where text=\"" +
                        placeName +
                        "\")");
        String queryUrl = builder.build().toString();

        YahooWeatherLog.d("query url : " + queryUrl);

        HttpClient httpClient = NetworkUtils.createHttpClient();

        HttpGet httpGet = new HttpGet(queryUrl);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();

                String readLine = null;

                while ((readLine = bufferedreader.readLine()) != null) {
                    YahooWeatherLog.d(readLine);
                    stringBuilder.append(readLine + "\n");
                }

                qResult = stringBuilder.toString();
            }

        } catch (ClientProtocolException e) {
            YahooWeatherLog.printStack(e);
            if (mExceptionListener != null) mExceptionListener.onFailConnection(e);
        } catch (ConnectTimeoutException e) {
            YahooWeatherLog.printStack(e);
            if (mExceptionListener != null) mExceptionListener.onFailConnection(e);
        } catch (SocketTimeoutException e) {
            YahooWeatherLog.printStack(e);
            if (mExceptionListener != null) mExceptionListener.onFailConnection(e);
        } catch (IOException e) {
            YahooWeatherLog.printStack(e);
            if (mExceptionListener != null) mExceptionListener.onFailConnection(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return qResult;
    }

	private Document convertStringToDocument(Context context, String src) {
		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e) {
			YahooWeatherLog.printStack(e);
			if (mExceptionListener != null) mExceptionListener.onFailParsing(e);
		} catch (SAXException e) {
			YahooWeatherLog.printStack(e);
			if (mExceptionListener != null) mExceptionListener.onFailParsing(e);
		} catch (IOException e) {
			YahooWeatherLog.printStack(e);
			if (mExceptionListener != null) mExceptionListener.onFailParsing(e);
		}

		return dest;
	}
	
	private WeatherInfo parseWeatherInfo(Context context, Document doc) {
		WeatherInfo weatherInfo = new WeatherInfo();
		try {
			
			Node titleNode = doc.getElementsByTagName("title").item(0);
			
			if(titleNode.getTextContent().equals(YAHOO_WEATHER_ERROR)) {
				return null;
			}
			
			weatherInfo.setTitle(titleNode.getTextContent());
			weatherInfo.setDescription(doc.getElementsByTagName("description").item(0).getTextContent());
			weatherInfo.setLanguage(doc.getElementsByTagName("language").item(0).getTextContent());
			weatherInfo.setLastBuildDate(doc.getElementsByTagName("lastBuildDate").item(0).getTextContent());
			
			Node locationNode = doc.getElementsByTagName("yweather:location").item(0);
			weatherInfo.setLocationCity(locationNode.getAttributes().getNamedItem("city").getNodeValue());
			weatherInfo.setLocationRegion(locationNode.getAttributes().getNamedItem("region").getNodeValue());
			weatherInfo.setLocationCountry(locationNode.getAttributes().getNamedItem("country").getNodeValue());
			
			Node windNode = doc.getElementsByTagName("yweather:wind").item(0);
			weatherInfo.setWindChill(windNode.getAttributes().getNamedItem("chill").getNodeValue());
			weatherInfo.setWindDirection(windNode.getAttributes().getNamedItem("direction").getNodeValue());
			weatherInfo.setWindSpeed(windNode.getAttributes().getNamedItem("speed").getNodeValue());
			
			Node atmosphereNode = doc.getElementsByTagName("yweather:atmosphere").item(0);
			weatherInfo.setAtmosphereHumidity(atmosphereNode.getAttributes().getNamedItem("humidity").getNodeValue());
			weatherInfo.setAtmosphereVisibility(atmosphereNode.getAttributes().getNamedItem("visibility").getNodeValue());
			weatherInfo.setAtmospherePressure(atmosphereNode.getAttributes().getNamedItem("pressure").getNodeValue());
			weatherInfo.setAtmosphereRising(atmosphereNode.getAttributes().getNamedItem("rising").getNodeValue());
			
			Node astronomyNode = doc.getElementsByTagName("yweather:astronomy").item(0);
			weatherInfo.setAstronomySunrise(astronomyNode.getAttributes().getNamedItem("sunrise").getNodeValue());
			weatherInfo.setAstronomySunset(astronomyNode.getAttributes().getNamedItem("sunset").getNodeValue());
			
			weatherInfo.setConditionTitle(doc.getElementsByTagName("title").item(2).getTextContent());
			weatherInfo.setConditionLat(doc.getElementsByTagName("geo:lat").item(0).getTextContent());
			weatherInfo.setConditionLon(doc.getElementsByTagName("geo:long").item(0).getTextContent());
			
			Node currentConditionNode = doc.getElementsByTagName("yweather:condition").item(0);
			weatherInfo.setCurrentCode(
					Integer.parseInt(
							currentConditionNode.getAttributes().getNamedItem("code").getNodeValue()
							));
			weatherInfo.setCurrentText(
					currentConditionNode.getAttributes().getNamedItem("text").getNodeValue());
			int curTempF = Integer.parseInt(currentConditionNode.getAttributes().getNamedItem("temp").getNodeValue());
            int curTempC = YahooWeather.turnFtoC(curTempF);
			weatherInfo.setCurrentTemp(mUnit == UNIT.CELSIUS ? curTempC : curTempF);
			weatherInfo.setCurrentConditionDate(
					currentConditionNode.getAttributes().getNamedItem("date").getNodeValue());
			
			if (mNeedDownloadIcons) {
				weatherInfo.setCurrentConditionIcon(ImageUtils.getBitmapFromWeb(
						weatherInfo.getCurrentConditionIconURL()));
			}
			
			for (int i = 0; i < FORECAST_INFO_MAX_SIZE; i++) {
				this.parseForecastInfo(weatherInfo.getForecastInfoList().get(i), doc, i);
			}

		} catch (NullPointerException e) {
		    YahooWeatherLog.printStack(e);
			if (mExceptionListener != null) mExceptionListener.onFailParsing(e);
			weatherInfo = null;
		}
		
		return weatherInfo;
	}
	
	private void parseForecastInfo(final ForecastInfo forecastInfo, final Document doc, final int index) {
		Node forecast1ConditionNode = doc.getElementsByTagName("yweather:forecast").item(index);
		forecastInfo.setForecastCode(Integer.parseInt(
				forecast1ConditionNode.getAttributes().getNamedItem("code").getNodeValue()
				));
		forecastInfo.setForecastText(
				forecast1ConditionNode.getAttributes().getNamedItem("text").getNodeValue());
		forecastInfo.setForecastDate(
				forecast1ConditionNode.getAttributes().getNamedItem("date").getNodeValue());
		forecastInfo.setForecastDay(
				forecast1ConditionNode.getAttributes().getNamedItem("day").getNodeValue());
        int highF = Integer.parseInt(forecast1ConditionNode.getAttributes().getNamedItem("high").getNodeValue());
        int highC = YahooWeather.turnFtoC(highF);
		forecastInfo.setForecastTempHigh(mUnit == UNIT.CELSIUS ? highC : highF);
        int lowF = Integer.parseInt(forecast1ConditionNode.getAttributes().getNamedItem("low").getNodeValue());
        int lowC = YahooWeather.turnFtoC(lowF);
		forecastInfo.setForecastTempLow(mUnit == UNIT.CELSIUS ? lowC : lowF);
		if (mNeedDownloadIcons) {
			forecastInfo.setForecastConditionIcon(
					ImageUtils.getBitmapFromWeb(forecastInfo.getForecastConditionIconURL()));
		}
	}
	
	private class WeatherQueryByPlaceTask extends AsyncTask<String, Void, WeatherInfo> {
		@Override
		protected WeatherInfo doInBackground(String... placeName) {
			if (placeName == null || placeName.length > 1) {
				throw new IllegalArgumentException("Parameter of WeatherQueryByPlaceTask is illegal. "
                        + "No place name exists.");
			}
            String weatherString = getWeatherString(mContext, placeName[0]);
            Document weatherDoc = convertStringToDocument(mContext, weatherString);
            WeatherInfo weatherInfo = parseWeatherInfo(mContext, weatherDoc);
            return weatherInfo;
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mWeatherInfoResult.gotWeatherInfo(result);
			mContext = null;
		}
	}

	private class WeatherQueryByLatLonTask extends AsyncTask<Double, Void, WeatherInfo> {

        @Override
		protected WeatherInfo doInBackground(Double... params) {
			if (params == null || params.length != 2) {
				throw new IllegalArgumentException("Parameter of WeatherQueryByLatLonTask is illegal."
                        + "No Lat Lon exists.");
			}
			final Double lat = params[0];
			final Double lon = params[1];
			// Get city name or place name
			if (mContext != null) {
                final Geocoder geocoder = new Geocoder(mContext);
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat.doubleValue(), lon.doubleValue(), 1);
                    for (Address address : addresses) {
                        YahooWeatherLog.d("latlon : " + lat + ", " + lon);
                        int n = address.getMaxAddressLineIndex();
                        for (int i = 0; i < n; i++) {
                            YahooWeatherLog.d("address line : " + address.getAddressLine(i));
                        }

                        YahooWeatherLog.d("adminarea : " + address.getAdminArea());
                        YahooWeatherLog.d("subAdminArea : " + address.getSubAdminArea());
                        YahooWeatherLog.d("countryName : " + address.getCountryName());
                        YahooWeatherLog.d("feature name : " + address.getFeatureName());
                        YahooWeatherLog.d("locality : " + address.getLocality());
                        YahooWeatherLog.d("sublocality : " + address.getSubLocality());
                        YahooWeatherLog.d("postCode : " + address.getPostalCode());
                        YahooWeatherLog.d("premises : " + address.getPremises());
                        YahooWeatherLog.d("thoroughfare : " + address.getThoroughfare());

                        String weatherString = getWeatherString(mContext, addressToPlaceName(address));
                        Document weatherDoc = convertStringToDocument(mContext, weatherString);
                        WeatherInfo weatherInfo = parseWeatherInfo(mContext, weatherDoc);
                        if(weatherInfo != null)
                        	weatherInfo.setAddress(address);
                        return weatherInfo;
                    }

                } catch (IOException e) {
                    YahooWeatherLog.printStack(e);
                }
			}
            return null;
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			super.onPostExecute(result);
			mWeatherInfoResult.gotWeatherInfo(result);
			mContext = null;
		}
	}

}
