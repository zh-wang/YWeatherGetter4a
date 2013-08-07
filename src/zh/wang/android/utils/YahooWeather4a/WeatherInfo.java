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

package zh.wang.android.utils.YahooWeather4a;

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
	String mCurrentConditionDate;

	/*
	 * information in the first tag "yweather:forecast"
	 */

	/*
	 * information in the second tag "yweather:forecast"
	 */
	
	ForecastInfo mForecastInfo1 = new ForecastInfo();
	ForecastInfo mForecastInfo2 = new ForecastInfo();
	
	public ForecastInfo getForecastInfo1() {
		return mForecastInfo1;
	}

	public void setForecastInfo1(ForecastInfo forecastInfo1) {
		mForecastInfo1 = forecastInfo1;
	}

	public ForecastInfo getForecastInfo2() {
		return mForecastInfo2;
	}

	public void setForecastInfo2(ForecastInfo forecastInfo2) {
		mForecastInfo2 = forecastInfo2;
	}

	public String getCurrentConditionDate() {
		return mCurrentConditionDate;
	}
	
	void setCurrentConditionDate(String currentConditionDate) {
		mCurrentConditionDate = currentConditionDate;
	}
	
	private int turnFtoC(int tempF) {
		return (tempF - 32) * 5 / 9;
	}

	public int getCurrentCode() {
		return mCurrentCode;
	}

	void setCurrentCode(int currentCode) {
		mCurrentCode = currentCode;
		mCurrentConditionIconURL = "http://l.yimg.com/a/i/us/we/52/" + currentCode + ".gif";
	}

	public int getCurrentTempF() {
		return mCurrentTempF;
	}

	void setCurrentTempF(int currentTempF) {
		mCurrentTempF = currentTempF;
		mCurrentTempC = this.turnFtoC(currentTempF);
	}

	public String getCurrentConditionIconURL() {
		return mCurrentConditionIconURL;
	}

	public int getCurrentTempC() {
		return mCurrentTempC;
	}

	public String getTitle() {
		return mTitle;
	}

	void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	void setDescription(String description) {
		mDescription = description;
	}

	public String getLanguage() {
		return mLanguage;
	}

	void setLanguage(String language) {
		mLanguage = language;
	}

	public String getLastBuildDate() {
		return mLastBuildDate;
	}

	void setLastBuildDate(String lastBuildDate) {
		mLastBuildDate = lastBuildDate;
	}

	public String getLocationCity() {
		return mLocationCity;
	}

	void setLocationCity(String locationCity) {
		mLocationCity = locationCity;
	}

	public String getLocationRegion() {
		return mLocationRegion;
	}

	void setLocationRegion(String locationRegion) {
		mLocationRegion = locationRegion;
	}

	public String getLocationCountry() {
		return mLocationCountry;
	}

	void setLocationCountry(String locationCountry) {
		mLocationCountry = locationCountry;
	}

	public String getWindChill() {
		return mWindChill;
	}

	void setWindChill(String windChill) {
		mWindChill = windChill;
	}

	public String getWindDirection() {
		return mWindDirection;
	}

	void setWindDirection(String windDirection) {
		mWindDirection = windDirection;
	}

	public String getWindSpeed() {
		return mWindSpeed;
	}

	void setWindSpeed(String windSpeed) {
		mWindSpeed = windSpeed;
	}

	public String getAtmosphereHumidity() {
		return mAtmosphereHumidity;
	}

	void setAtmosphereHumidity(String atmosphereHumidity) {
		mAtmosphereHumidity = atmosphereHumidity;
	}

	public String getAtmosphereVisibility() {
		return mAtmosphereVisibility;
	}

	void setAtmosphereVisibility(String atmosphereVisibility) {
		mAtmosphereVisibility = atmosphereVisibility;
	}

	public String getAtmospherePressure() {
		return mAtmospherePressure;
	}

	void setAtmospherePressure(String atmospherePressure) {
		mAtmospherePressure = atmospherePressure;
	}

	public String getAtmosphereRising() {
		return mAtmosphereRising;
	}

	void setAtmosphereRising(String atmosphereRising) {
		mAtmosphereRising = atmosphereRising;
	}

	public String getAstronomySunrise() {
		return mAstronomySunrise;
	}

	void setAstronomySunrise(String astronomySunrise) {
		mAstronomySunrise = astronomySunrise;
	}

	public String getAstronomySunset() {
		return mAstronomySunset;
	}

	void setAstronomySunset(String astronomySunset) {
		mAstronomySunset = astronomySunset;
	}

	public String getConditionTitle() {
		return mConditionTitle;
	}

	void setConditionTitle(String conditionTitle) {
		mConditionTitle = conditionTitle;
	}

	public String getConditionLat() {
		return mConditionLat;
	}

	void setConditionLat(String conditionLat) {
		mConditionLat = conditionLat;
	}

	public String getConditionLon() {
		return mConditionLon;
	}

	void setConditionLon(String conditionLon) {
		mConditionLon = conditionLon;
	}

	public String getCurrentText() {
		return mCurrentText;
	}

	void setCurrentText(String currentText) {
		mCurrentText = currentText;
	}

	void setCurrentTempC(int currentTempC) {
		mCurrentTempC = currentTempC;
	}

	void setCurrentConditionIconURL(String currentConditionIconURL) {
		mCurrentConditionIconURL = currentConditionIconURL;
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
		private String mForecastText;

		public String getForecastDay() {
			return mForecastDay;
		}

		public void setForecastDay(String forecastDay) {
			mForecastDay = forecastDay;
		}

		public String getForecastDate() {
			return mForecastDate;
		}

		public void setForecastDate(String forecastDate) {
			mForecastDate = forecastDate;
		}

		public int getForecastCode() {
			return mForecastCode;
		}

		public void setForecastCode(int forecastCode) {
			mForecastCode = forecastCode;
			mForecastConditionIconURL = "http://l.yimg.com/a/i/us/we/52/" + forecastCode + ".gif";
		}

		public int getForecastTempHighC() {
			return mForecastTempHighC;
		}

		public void setForecastTempHighC(int forecastTempHighC) {
			mForecastTempHighC = forecastTempHighC;
		}

		public int getForecastTempLowC() {
			return mForecastTempLowC;
		}

		public void setForecastTempLowC(int forecastTempLowC) {
			mForecastTempLowC = forecastTempLowC;
		}

		public int getForecastTempHighF() {
			return mForecastTempHighF;
		}

		public void setForecastTempHighF(int forecastTempHighF) {
			mForecastTempHighF = forecastTempHighF;
			mForecastTempHighC = turnFtoC(forecastTempHighF);
		}

		public int getForecastTempLowF() {
			return mForecastTempLowF;
		}

		public void setForecastTempLowF(int forecastTempLowF) {
			mForecastTempLowF = forecastTempLowF;
			mForecastTempLowC = turnFtoC(forecastTempLowF);
		}

		public String getForecastConditionIconURL() {
			return mForecastConditionIconURL;
		}

		public void setForecastConditionIconURL(String forecastConditionIconURL) {
			mForecastConditionIconURL = forecastConditionIconURL;
		}

		public String getForecastText() {
			return mForecastText;
		}

		public void setForecastText(String forecastText) {
			mForecastText = forecastText;
		}
		
	}
}
