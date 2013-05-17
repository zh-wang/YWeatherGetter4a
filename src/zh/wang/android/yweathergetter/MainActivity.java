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

import zh.wang.android.tools.ImageUtils;
import zh.wang.android.utils.YahooWeather4a.WeatherInfo;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherInfoListener;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements YahooWeatherInfoListener {

	private ImageView ivWeather0;
	private ImageView ivWeather1;
	private ImageView ivWeather2;
	private TextView tvWeather0;
	private TextView tvWeather1;
	private TextView tvWeather2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.d("YWeatherGetter4a", "onCreate");
        
        YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils.getInstance();
        yahooWeatherUtils.queryYahooWeather(getApplicationContext(), "tokyo", this);
    }

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
        if(weatherInfo != null) {
        	TextView tv = (TextView) findViewById(R.id.textview_title);
			tv.setText(weatherInfo.getTitle() + "\n"
					+ weatherInfo.getLocationCity() + ", "
					+ weatherInfo.getLocationCountry());
			tvWeather0 = (TextView) findViewById(R.id.textview_weather_info_0);
			tvWeather0.setText("====== CURRENT ======" + "\n" +
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
			tvWeather1 = (TextView) findViewById(R.id.textview_weather_info_1);
			tvWeather1.setText("====== FORECAST 1 ======" + "\n" +
			                   "date: " + weatherInfo.getForecast1Date() + "\n" +
			                   "weather: " + weatherInfo.getForecast1Text() + "\n" +
					           "low  temperature in ºC: " + weatherInfo.getForecast1TempLowC() + "\n" +
			                   "high temperature in ºC: " + weatherInfo.getForecast1TempHighC() + "\n" +
					           "low  temperature in ºF: " + weatherInfo.getForecast1TempLowF() + "\n" +
			                   "high temperature in ºF: " + weatherInfo.getForecast1TempHighF() + "\n"
					           );
			tvWeather2 = (TextView) findViewById(R.id.textview_weather_info_2);
			tvWeather2.setText("====== FORECAST 2 ======" + "\n" +
					   "date: " + weatherInfo.getForecast1Date() + "\n" +
	                   "weather: " + weatherInfo.getForecast2Text() + "\n" +
			           "low  temperature in ºC: " + weatherInfo.getForecast2TempLowC() + "\n" +
	                   "high temperature in ºC: " + weatherInfo.getForecast2TempHighC() + "\n" +
			           "low  temperature in ºF: " + weatherInfo.getForecast2TempLowF() + "\n" +
	                   "high temperature in ºF: " + weatherInfo.getForecast2TempHighF() + "\n"
			           );
			
			ivWeather0 = (ImageView) findViewById(R.id.imageview_weather_info_0);
			ivWeather1 = (ImageView) findViewById(R.id.imageview_weather_info_1);
			ivWeather2 = (ImageView) findViewById(R.id.imageview_weather_info_2);
			
			LoadWebImagesTask task = new LoadWebImagesTask();
			task.execute(
					weatherInfo.getCurrentConditionIconURL(), 
					weatherInfo.getForecast1ConditionIconURL(),
					weatherInfo.getForecast2ConditionIconURL()
					);
        } else {
        	Toast.makeText(getApplicationContext(), "Sorry, no result returned", Toast.LENGTH_SHORT).show();
        }
	}
	
	class LoadWebImagesTask extends AsyncTask<String, Void, Bitmap[]> {

		@Override
		protected Bitmap[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap[] res = new Bitmap[3];
			res[0] = ImageUtils.getBitmapFromWeb(params[0]);
			res[1] = ImageUtils.getBitmapFromWeb(params[1]);
			res[2] = ImageUtils.getBitmapFromWeb(params[2]);
			return res;
		}

		@Override
		protected void onPostExecute(Bitmap[] results) {
			// TODO Auto-generated method stub
			super.onPostExecute(results);
			ivWeather0.setImageBitmap(results[0]);
			ivWeather1.setImageBitmap(results[1]);
			ivWeather2.setImageBitmap(results[2]);
		}
		
	}

}
