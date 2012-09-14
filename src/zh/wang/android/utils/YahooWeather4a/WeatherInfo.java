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
	String mForecast1Day;
	String mForecast1Date;
	int mForecast1Code;
	String mForecast1Text;
	int mForecast1TempHighC;
	int mForecast1TempLowC;
	int mForecast1TempHighF;
	int mForecast1TempLowF;
	String mForecast1ConditionIconURL;

	/*
	 * information in the second tag "yweather:forecast"
	 */
	String mForecast2Day;
	String mForecast2Date;
	int mForecast2Code;
	String mForecast2Text;
	int mForecast2TempHighC;
	int mForecast2TempLowC;
	int mForecast2TempHighF;
	int mForecast2TempLowF;
	String mForecast2ConditionIconURL;
	
	public String getForecast1Date() {
		return mForecast1Date;
	}

	void setForecast1Date(String forecast1Date) {
		mForecast1Date = forecast1Date;
	}

	public String getForecast2Date() {
		return mForecast2Date;
	}

	void setForecast2Date(String forecast2Date) {
		mForecast2Date = forecast2Date;
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

	public String getForecast1Day() {
		return mForecast1Day;
	}

	void setForecast1Day(String forecast1Day) {
		mForecast1Day = forecast1Day;
	}

	public int getForecast1Code() {
		return mForecast1Code;
	}

	void setForecast1Code(int forecast1Code) {
		mForecast1Code = forecast1Code;
		mForecast1ConditionIconURL = "http://l.yimg.com/a/i/us/we/52/" + forecast1Code + ".gif";
	}

	public String getForecast1Text() {
		return mForecast1Text;
	}

	void setForecast1Text(String forecast1Text) {
		mForecast1Text = forecast1Text;
	}

	public int getForecast1TempHighF() {
		return mForecast1TempHighF;
	}

	void setForecast1TempHighF(int forecast1TempHighF) {
		mForecast1TempHighF = forecast1TempHighF;
		mForecast1TempHighC = this.turnFtoC(forecast1TempHighF);
	}

	public int getForecast1TempLowF() {
		return mForecast1TempLowF;
	}

	void setForecast1TempLowF(int forecast1TempLowF) {
		mForecast1TempLowF = forecast1TempLowF;
		mForecast1TempLowC = this.turnFtoC(forecast1TempLowF);
	}

	public String getForecast2Day() {
		return mForecast2Day;
	}

	void setForecast2Day(String forecast2Day) {
		mForecast2Day = forecast2Day;
	}

	public int getForecast2Code() {
		return mForecast2Code;
	}

	void setForecast2Code(int forecast2Code) {
		mForecast2Code = forecast2Code;
		mForecast2ConditionIconURL = "http://l.yimg.com/a/i/us/we/52/" + forecast2Code + ".gif";
	}

	public String getCurrentConditionIconURL() {
		return mCurrentConditionIconURL;
	}

	public String getForecast1ConditionIconURL() {
		return mForecast1ConditionIconURL;
	}

	public String getForecast2ConditionIconURL() {
		return mForecast2ConditionIconURL;
	}

	public String getForecast2Text() {
		return mForecast2Text;
	}

	void setForecast2Text(String forecast2Text) {
		mForecast2Text = forecast2Text;
	}

	public int getForecast2TempHighF() {
		return mForecast2TempHighF;
	}

	void setForecast2TempHighF(int forecast2TempHighF) {
		mForecast2TempHighF = forecast2TempHighF;
		mForecast2TempHighC = this.turnFtoC(forecast2TempHighF);
	}

	public int getForecast2TempLowF() {
		return mForecast2TempLowF;
	}

	void setForecast2TempLowF(int forecast2TempLowF) {
		mForecast2TempLowF = forecast2TempLowF;
		mForecast2TempLowC = this.turnFtoC(forecast2TempLowF);
	}

	public int getCurrentTempC() {
		return mCurrentTempC;
	}

	public int getForecast1TempHighC() {
		return mForecast1TempHighC;
	}

	public int getForecast1TempLowC() {
		return mForecast1TempLowC;
	}

	public int getForecast2TempHighC() {
		return mForecast2TempHighC;
	}

	public int getForecast2TempLowC() {
		return mForecast2TempLowC;
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


}
