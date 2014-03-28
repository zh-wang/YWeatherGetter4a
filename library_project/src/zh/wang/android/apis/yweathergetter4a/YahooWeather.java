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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketTimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import zh.wang.android.apis.yweathergetter4a.UserLocationUtils.LocationResult;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo.ForecastInfo;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

/**
 * A wrapper for accessing Yahoo weather informations. 
 * @author Zhenghong Wang
 */
public class YahooWeather implements LocationResult {
    
    private static final int CONNECT_TIMEOUT_DEFAULT = 20 * 1000;
    private static final int SOCKET_TIMEOUT_DEFAULT = 20 * 1000;

	public enum SEARCH_MODE {
		GPS,
		PLACE_NAME
	}
	
	public static final String YAHOO_WEATHER_ERROR = "Yahoo! Weather - Error";

	public static final int FORECAST_INFO_MAX_SIZE = 5;
	
	private String mWoeidNumber;
	private YahooWeatherInfoListener mWeatherInfoResult;
	private boolean mNeedDownloadIcons;
	private SEARCH_MODE mSearchMode;
	
	private Context mContext;
	private static YahooWeather mInstance = new YahooWeather();
	
	public SEARCH_MODE getSearchMode() {
		return mSearchMode;
	}

	public void setSearchMode(SEARCH_MODE searchMode) {
		mSearchMode = searchMode;
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
	 * @param isDebbugable set if you want some debug log in Logcat
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
        	YahooWeatherLog.shortToast(context, "Network connection is unavailable!!");
        	return;
        }
        final String convertedlocation = AsciiUtils.convertNonAscii(cityAreaOrLocation);
		mWeatherInfoResult = result;
		final WeatherQueryByPlaceTask task = new WeatherQueryByPlaceTask();
		task.execute(new String[]{convertedlocation});
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
	public void queryYahooWeatherByLatLon(final Context context, final String lat, final String lon, 
			final YahooWeatherInfoListener result) {
		YahooWeatherLog.d("query yahoo weather by lat lon");
		mContext = context;
        if (!NetworkUtils.isConnected(context)) {
        	YahooWeatherLog.shortToast(context, "Network connection is unavailable!!");
        	return;
        }
		mWeatherInfoResult = result;
		final WeatherQueryByLatLonTask task = new WeatherQueryByLatLonTask();
		task.execute(new String[]{lat, lon});
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
        	YahooWeatherLog.shortToast(context, "Network connection is unavailable!!");
        	return;
        }
		mContext = context;
		mWeatherInfoResult = result;
		(new UserLocationUtils()).findUserLocation(context, this);
	}
	
	@Override
	public void gotLocation(Location location) {
	    if (location == null) {
	        YahooWeatherLog.shortToast(mContext, "GPS location cannot be found!");
	        return;
	    }
		final String lat = String.valueOf(location.getLatitude());
		final String lon = String.valueOf(location.getLongitude());
		final WeatherQueryByLatLonTask task = new WeatherQueryByLatLonTask();
		task.execute(new String[]{lat, lon});
	}

	private String getWeatherString(Context context, String woeidNumber) {
		YahooWeatherLog.d("query yahoo weather with WOEID number : " + woeidNumber);

		String qResult = "";
		String queryUrl = "http://weather.yahooapis.com/forecastrss?w=" + woeidNumber;
		
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
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, e.toString());
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, e.toString());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, e.toString());
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
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, e.toString());
		} catch (SAXException e) {
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, e.toString());
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
			weatherInfo.setCurrentTempF(
					Integer.parseInt(
							currentConditionNode.getAttributes().getNamedItem("temp").getNodeValue()
							));
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
			e.printStackTrace();
			YahooWeatherLog.shortToast(context, "Parse XML failed - Unrecognized Tag");
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
		forecastInfo.setForecastTempHighF(
				Integer.parseInt(
						forecast1ConditionNode.getAttributes().getNamedItem("high").getNodeValue()
						));
		forecastInfo.setForecastTempLowF(
				Integer.parseInt(
						forecast1ConditionNode.getAttributes().getNamedItem("low").getNodeValue()
						));
		if (mNeedDownloadIcons) {
			forecastInfo.setForecastConditionIcon(
					ImageUtils.getBitmapFromWeb(forecastInfo.getForecastConditionIconURL()));
		}
	}
	
	private class WeatherQueryByPlaceTask extends AsyncTask<String, Void, WeatherInfo> {
		@Override
		protected WeatherInfo doInBackground(String... cityName) {
			if (cityName == null || cityName.length > 1) {
				throw new IllegalArgumentException("Parameter of WeatherQueryByPlaceTask is illegal");
			}
			WOEIDUtils woeidUtils = WOEIDUtils.getInstance();
			mWoeidNumber = woeidUtils.getWOEID(mContext, cityName[0]);
			if(!mWoeidNumber.equals(WOEIDUtils.WOEID_NOT_FOUND)) {
				String weatherString = getWeatherString(mContext, mWoeidNumber);
				Document weatherDoc = convertStringToDocument(mContext, weatherString);
				WeatherInfo weatherInfo = parseWeatherInfo(mContext, weatherDoc);
    			/*
    			 * pass some woied info
    			 */
				weatherInfo.mWOEIDneighborhood = woeidUtils.getWoeidInfo().mNeighborhood;
				weatherInfo.mWOEIDCounty = woeidUtils.getWoeidInfo().mCounty;
				weatherInfo.mWOEIDState = woeidUtils.getWoeidInfo().mState;
				weatherInfo.mWOEIDCountry = woeidUtils.getWoeidInfo().mCountry;
				return weatherInfo;
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mWeatherInfoResult.gotWeatherInfo(result);
			mContext = null;
		}
	}

	private class WeatherQueryByLatLonTask extends AsyncTask<String, Void, WeatherInfo> {

        @Override
		protected WeatherInfo doInBackground(String... params) {
			if (params == null || params.length != 2) {
				throw new IllegalArgumentException("Parameter of WeatherQueryByLatLonTask is illegal");
			}
			final String lat = params[0];
			final String lon = params[1];
			WOEIDUtils woeidUtils = WOEIDUtils.getInstance();
			mWoeidNumber = woeidUtils.getWOEID(mContext, lat, lon);
			if (!mWoeidNumber.equals(WOEIDUtils.WOEID_NOT_FOUND)) {
				String weatherString = getWeatherString(mContext, mWoeidNumber);
				Document weatherDoc = convertStringToDocument(mContext, weatherString);
				WeatherInfo weatherInfo = parseWeatherInfo(mContext, weatherDoc);
    			/*
    			 * pass some woied info
    			 */
				weatherInfo.mWOEIDneighborhood = woeidUtils.getWoeidInfo().mNeighborhood;
				weatherInfo.mWOEIDCounty = woeidUtils.getWoeidInfo().mCounty;
				weatherInfo.mWOEIDState = woeidUtils.getWoeidInfo().mState;
				weatherInfo.mWOEIDCountry = woeidUtils.getWoeidInfo().mCountry;
				return weatherInfo;
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			super.onPostExecute(result);
			mWeatherInfoResult.gotWeatherInfo(result);
			mContext = null;
		}
	}

}
