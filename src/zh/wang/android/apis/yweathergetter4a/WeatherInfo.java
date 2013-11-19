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

package zh.wang.android.apis.yweathergetter4a;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

/**
 * A wrapper for all weather information provided by Yahoo weather apis.
 * @author Zhenghong Wang
 */
public class WeatherInfo {
	
	String mTitle;
	String mDescription;
	String mLanguage;
	String mLastBuildDate;
	String mLocationCity;
	String mLocationRegion; // region may be null
	String mLocationCountry;
	
	String mWindChill;
	String mWindDirection;
	String mWindSpeed;
	
	String mAtmosphereHumidity;
	String mAtmosphereVisibility;
	String mAtmospherePressure;
	String mAtmosphereRising;
	
	String mAstronomySunrise;
	String mAstronomySunset;
	
	String mConditionTitle;
	String mConditionLat;
	String mConditionLon;

	/*
	 * information in tag "yweather:condition"
	 */
	int mCurrentCode;
	String mCurrentText;
	int mCurrentTempC;
	int mCurrentTempF;
	String mCurrentConditionIconURL;
	Bitmap mCurrentConditionIcon;
	String mCurrentConditionDate;

	/*
	 * information in the first tag "yweather:forecast"
	 */

	/*
	 * information in the second tag "yweather:forecast"
	 */
	
	ForecastInfo mForecastInfo1 = new ForecastInfo();
	ForecastInfo mForecastInfo2 = new ForecastInfo();
	ForecastInfo mForecastInfo3 = new ForecastInfo();
	ForecastInfo mForecastInfo4 = new ForecastInfo();
	ForecastInfo mForecastInfo5 = new ForecastInfo();
	private List<ForecastInfo> mForecastInfoList;
	
	public WeatherInfo() {
		mForecastInfoList = new ArrayList<WeatherInfo.ForecastInfo>();
		mForecastInfoList.add(mForecastInfo1);
		mForecastInfoList.add(mForecastInfo2);
		mForecastInfoList.add(mForecastInfo3);
		mForecastInfoList.add(mForecastInfo4);
		mForecastInfoList.add(mForecastInfo5);
	}

	public List<ForecastInfo> getForecastInfoList() {
		return mForecastInfoList;
	}

	protected void setForecastInfoList(List<ForecastInfo> forecastInfoList) {
		mForecastInfoList = forecastInfoList;
	}

	public ForecastInfo getForecastInfo1() {
		return mForecastInfo1;
	}

	protected void setForecastInfo1(ForecastInfo forecastInfo1) {
		mForecastInfo1 = forecastInfo1;
	}

	public ForecastInfo getForecastInfo2() {
		return mForecastInfo2;
	}

	protected void setForecastInfo2(ForecastInfo forecastInfo2) {
		mForecastInfo2 = forecastInfo2;
	}

	public ForecastInfo getForecastInfo3() {
		return mForecastInfo3;
	}

	protected void setForecastInfo3(ForecastInfo forecastInfo3) {
		mForecastInfo3 = forecastInfo3;
	}

	public ForecastInfo getForecastInfo4() {
		return mForecastInfo4;
	}

	protected void setForecastInfo4(ForecastInfo forecastInfo4) {
		mForecastInfo4 = forecastInfo4;
	}

	public ForecastInfo getForecastInfo5() {
		return mForecastInfo5;
	}

	protected void setForecastInfo5(ForecastInfo forecastInfo5) {
		mForecastInfo5 = forecastInfo5;
	}

	public String getCurrentConditionDate() {
		return mCurrentConditionDate;
	}
	
	protected void setCurrentConditionDate(String currentConditionDate) {
		mCurrentConditionDate = currentConditionDate;
	}
	
	public int getCurrentCode() {
		return mCurrentCode;
	}

	protected void setCurrentCode(int currentCode) {
		mCurrentCode = currentCode;
		mCurrentConditionIconURL = "http://l.yimg.com/a/i/us/we/52/" + currentCode + ".gif";
	}

	public int getCurrentTempF() {
		return mCurrentTempF;
	}

	protected void setCurrentTempF(int currentTempF) {
		mCurrentTempF = currentTempF;
		mCurrentTempC = this.turnFtoC(currentTempF);
	}

	public int getCurrentTempC() {
		return mCurrentTempC;
	}

	public String getTitle() {
		return mTitle;
	}

	protected void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	protected void setDescription(String description) {
		mDescription = description;
	}

	public String getLanguage() {
		return mLanguage;
	}

	protected void setLanguage(String language) {
		mLanguage = language;
	}

	public String getLastBuildDate() {
		return mLastBuildDate;
	}

	protected void setLastBuildDate(String lastBuildDate) {
		mLastBuildDate = lastBuildDate;
	}

	public String getLocationCity() {
		return mLocationCity;
	}

	protected void setLocationCity(String locationCity) {
		mLocationCity = locationCity;
	}

	public String getLocationRegion() {
		return mLocationRegion;
	}

	protected void setLocationRegion(String locationRegion) {
		mLocationRegion = locationRegion;
	}

	public String getLocationCountry() {
		return mLocationCountry;
	}

	protected void setLocationCountry(String locationCountry) {
		mLocationCountry = locationCountry;
	}

	public String getWindChill() {
		return mWindChill;
	}

	protected void setWindChill(String windChill) {
		mWindChill = windChill;
	}

	public String getWindDirection() {
		return mWindDirection;
	}

	protected void setWindDirection(String windDirection) {
		mWindDirection = windDirection;
	}

	public String getWindSpeed() {
		return mWindSpeed;
	}

	protected void setWindSpeed(String windSpeed) {
		mWindSpeed = windSpeed;
	}

	public String getAtmosphereHumidity() {
		return mAtmosphereHumidity;
	}

	protected void setAtmosphereHumidity(String atmosphereHumidity) {
		mAtmosphereHumidity = atmosphereHumidity;
	}

	public String getAtmosphereVisibility() {
		return mAtmosphereVisibility;
	}

	protected void setAtmosphereVisibility(String atmosphereVisibility) {
		mAtmosphereVisibility = atmosphereVisibility;
	}

	public String getAtmospherePressure() {
		return mAtmospherePressure;
	}

	protected void setAtmospherePressure(String atmospherePressure) {
		mAtmospherePressure = atmospherePressure;
	}

	public String getAtmosphereRising() {
		return mAtmosphereRising;
	}

	protected void setAtmosphereRising(String atmosphereRising) {
		mAtmosphereRising = atmosphereRising;
	}

	public String getAstronomySunrise() {
		return mAstronomySunrise;
	}

	protected void setAstronomySunrise(String astronomySunrise) {
		mAstronomySunrise = astronomySunrise;
	}

	public String getAstronomySunset() {
		return mAstronomySunset;
	}

	protected void setAstronomySunset(String astronomySunset) {
		mAstronomySunset = astronomySunset;
	}

	public String getConditionTitle() {
		return mConditionTitle;
	}

	protected void setConditionTitle(String conditionTitle) {
		mConditionTitle = conditionTitle;
	}

	public String getConditionLat() {
		return mConditionLat;
	}

	protected void setConditionLat(String conditionLat) {
		mConditionLat = conditionLat;
	}

	public String getConditionLon() {
		return mConditionLon;
	}

	protected void setConditionLon(String conditionLon) {
		mConditionLon = conditionLon;
	}

	public String getCurrentText() {
		return mCurrentText;
	}

	protected void setCurrentText(String currentText) {
		mCurrentText = currentText;
	}

	protected void setCurrentTempC(int currentTempC) {
		mCurrentTempC = currentTempC;
	}

	public String getCurrentConditionIconURL() {
		return mCurrentConditionIconURL;
	}

	public Bitmap getCurrentConditionIcon() {
		return mCurrentConditionIcon;
	}

	protected void setCurrentConditionIcon(Bitmap mCurrentConditionIcon) {
		this.mCurrentConditionIcon = mCurrentConditionIcon;
	}

	private int turnFtoC(int tempF) {
		return (tempF - 32) * 5 / 9;
	}

	public class ForecastInfo {
		private String mForecastDay;
		private String mForecastDate;
		private int mForecastCode;
		private int mForecastTempHighC;
		private int mForecastTempLowC;
		private int mForecastTempHighF;
		private int mForecastTempLowF;
		private String mForecastConditionIconURL;
		private Bitmap mForecastConditionIcon;
		private String mForecastText;

		public Bitmap getForecastConditionIcon() {
			return mForecastConditionIcon;
		}

		protected void setForecastConditionIcon(Bitmap mForecastConditionIcon) {
			this.mForecastConditionIcon = mForecastConditionIcon;
		}

		public String getForecastDay() {
			return mForecastDay;
		}

		protected void setForecastDay(String forecastDay) {
			mForecastDay = forecastDay;
		}

		public String getForecastDate() {
			return mForecastDate;
		}

		protected void setForecastDate(String forecastDate) {
			mForecastDate = forecastDate;
		}

		public int getForecastCode() {
			return mForecastCode;
		}

		protected void setForecastCode(int forecastCode) {
			mForecastCode = forecastCode;
			mForecastConditionIconURL = "http://l.yimg.com/a/i/us/we/52/" + forecastCode + ".gif";
		}

		public int getForecastTempHighC() {
			return mForecastTempHighC;
		}

		protected void setForecastTempHighC(int forecastTempHighC) {
			mForecastTempHighC = forecastTempHighC;
		}

		public int getForecastTempLowC() {
			return mForecastTempLowC;
		}

		protected void setForecastTempLowC(int forecastTempLowC) {
			mForecastTempLowC = forecastTempLowC;
		}

		public int getForecastTempHighF() {
			return mForecastTempHighF;
		}

		protected void setForecastTempHighF(int forecastTempHighF) {
			mForecastTempHighF = forecastTempHighF;
			mForecastTempHighC = turnFtoC(forecastTempHighF);
		}

		public int getForecastTempLowF() {
			return mForecastTempLowF;
		}

		protected void setForecastTempLowF(int forecastTempLowF) {
			mForecastTempLowF = forecastTempLowF;
			mForecastTempLowC = turnFtoC(forecastTempLowF);
		}

		public String getForecastConditionIconURL() {
			return mForecastConditionIconURL;
		}

		public String getForecastText() {
			return mForecastText;
		}

		protected void setForecastText(String forecastText) {
			mForecastText = forecastText;
		}
		
	}
}
