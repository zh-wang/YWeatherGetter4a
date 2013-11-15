/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2012 Zhenghong Wang
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

package zh.wang.android.yweathergetter;

import zh.wang.android.apis.yahooweather4a.AsciiUtils;
import zh.wang.android.apis.yahooweather4a.UserLocationUtils.LocationResult;
import zh.wang.android.apis.yahooweather4a.WeatherInfo;
import zh.wang.android.apis.yahooweather4a.WeatherInfo.ForecastInfo;
import zh.wang.android.apis.yahooweather4a.YahooWeather;
import zh.wang.android.apis.yahooweather4a.YahooWeatherInfoListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements YahooWeatherInfoListener, 
	LocationResult {
	
	private ImageView mIvWeather0;
	private TextView mTvWeather0;
	private TextView mTvErrorMessage;
	private TextView mTvTitle;
	private EditText mEtAreaOfCity;
	private Button mBtSearch;
	private Button mBtGPS;
	private LinearLayout mWeatherInfosLayout;
	private YahooWeather mYahooWeather = YahooWeather.getInstance();
    private String mLocation = "Shanghai China";
    
    private ProgressDialog mProgressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
        
    	mTvTitle = (TextView) findViewById(R.id.textview_title);
		mTvWeather0 = (TextView) findViewById(R.id.textview_weather_info_0);
		mTvErrorMessage = (TextView) findViewById(R.id.textview_error_message);
		mIvWeather0 = (ImageView) findViewById(R.id.imageview_weather_info_0);
        
		mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.queryYahooWeather(getApplicationContext(), mLocation, this);
        
        mEtAreaOfCity = (EditText) findViewById(R.id.edittext_area);
        mEtAreaOfCity.setText(mLocation);
        
        mBtSearch = (Button) findViewById(R.id.search_button);
        mBtSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String _location = mEtAreaOfCity.getText().toString();
				if (!TextUtils.isEmpty(_location)) {
					String _convertedLocation = AsciiUtils.convertNonAscii(_location);
					mYahooWeather.queryYahooWeather(getApplicationContext(), _convertedLocation, MainActivity.this);
					InputMethodManager imm = (InputMethodManager)getSystemService(
	              	      Context.INPUT_METHOD_SERVICE);
	              	imm.hideSoftInputFromWindow(mEtAreaOfCity.getWindowToken(), 0);

	              	if (mProgressDialog != null && mProgressDialog.isShowing()) {
	              		mProgressDialog.cancel();
	              	}
			        mProgressDialog = new ProgressDialog(MainActivity.this);
			        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			        mProgressDialog.show();
				}
			}
		});
        
        mBtGPS = (Button) findViewById(R.id.gps_button);
        mBtGPS.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

			}
		});

        mEtAreaOfCity.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.toString().length() == 0) {
					mBtSearch.setText("GPS");
				} else {
					mBtSearch.setText("Search");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
        
        mWeatherInfosLayout = (LinearLayout) findViewById(R.id.weather_infos);
     
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
		super.onDestroy();
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
        if (weatherInfo != null) {
        	setNormalLayout();
        	mWeatherInfosLayout.removeAllViews();
			mTvTitle.setText(weatherInfo.getTitle() + "\n"
					+ weatherInfo.getLocationCity() + ", "
					+ weatherInfo.getLocationCountry());
			mTvWeather0.setText("====== CURRENT ======" + "\n" +
					           "date: " + weatherInfo.getCurrentConditionDate() + "\n" +
							   "weather: " + weatherInfo.getCurrentText() + "\n" +
						       "temperature in ºC: " + weatherInfo.getCurrentTempC() + "\n" +
					           "temperature in ºF: " + weatherInfo.getCurrentTempF() + "\n" +
						       "wind chill in ºF: " + weatherInfo.getWindChill() + "\n" +
					           "wind direction: " + weatherInfo.getWindDirection() + "\n" +
						       "wind speed: " + weatherInfo.getWindSpeed() + "\n" +
					           "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
						       "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
					           "Visibility: " + weatherInfo.getAtmosphereVisibility()
					           );
			if (weatherInfo.getCurrentConditionIcon() != null) {
				mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
			}
			for (int i = 0; i < YahooWeather.FORECAST_INFO_MAX_SIZE; i++) {
				final LinearLayout forecastInfoLayout = (LinearLayout) 
						getLayoutInflater().inflate(R.layout.forecastinfo, null);
				final TextView tvWeather = (TextView) forecastInfoLayout.findViewById(R.id.textview_forecast_info);
				final ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);
				tvWeather.setText("====== FORECAST " + (i+1) + " ======" + "\n" +
				                   "date: " + forecastInfo.getForecastDate() + "\n" +
				                   "weather: " + forecastInfo.getForecastText() + "\n" +
						           "low  temperature in ºC: " + forecastInfo.getForecastTempLowC() + "\n" +
				                   "high temperature in ºC: " + forecastInfo.getForecastTempHighC() + "\n" +
						           "low  temperature in ºF: " + forecastInfo.getForecastTempLowF() + "\n" +
				                   "high temperature in ºF: " + forecastInfo.getForecastTempHighF() + "\n"
						           );
				final ImageView ivForecast = (ImageView) forecastInfoLayout.findViewById(R.id.imageview_forecast_info);
				if (forecastInfo.getForecastConditionIcon() != null) {
					ivForecast.setImageBitmap(forecastInfo.getForecastConditionIcon());
				}
				mWeatherInfosLayout.addView(forecastInfoLayout);
			}
        } else {
        	setNoResultLayout();
        }
	}
	
	@Override
	public void gotLocation(Location location) {
		final String lat = String.valueOf(location.getLatitude());
		final String lon = String.valueOf(location.getLongitude());
		mYahooWeather.queryYahooWeather(getApplicationContext(), lat, lon, this);
	}

	private void setNormalLayout() {
		mWeatherInfosLayout.setVisibility(View.VISIBLE);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvErrorMessage.setVisibility(View.INVISIBLE);
	}
	
	private void setNoResultLayout() {
		mTvTitle.setVisibility(View.INVISIBLE);
		mWeatherInfosLayout.setVisibility(View.INVISIBLE);
		mTvErrorMessage.setVisibility(View.VISIBLE);
		mTvErrorMessage.setText("Sorry, no result returned");
	}
	
}
