YWeatherGetter4a
================

An Yahoo Weather API wrapper for android

It contains an exmaple project works on android 2.2 or above.
Yahoo Weather API wrapper works on android 1.6 or above.

1. What is this?
This is a wrapper for getting weather information from Yahoo Weather API, for android.

2. How do I use it?
Add the following code to get weather information.

	YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils.getInstance();
	WeatherInfo weatherInfo = yahooWeatherUtils.queryYahooWeather(getApplicationContext(), "Name of City or Area");

Replace "Name of City or Area" to what you want.
Information retrieved from Yahoo Weather API is stored in weatherInfo.

3. What kind of weather information can I get?
You can get current condition of weather, humidity, wind, etc.
And some forecast information for next two days.

Check the XML returned by Yahoo Weather API here.
http://weather.yahooapis.com/forecastrss?w=2459115

(c) 2012 Zhenghong Wang
