YWeatherGetter4a
================

An Yahoo Weather API wrapper for android

It contains an exmaple project works on android 2.2 or above.
Yahoo Weather API wrapper works on android 1.6 or above.

1. What is this?
This is a wrapper for getting weather information from Yahoo Weather API, for android.

2. How do I use it? (Changed from 2013/03/23)

i.   Implement YahooWeatherInfoListener in your Activity. Overwrite the callback function "gotWeatherInfo", which will be called after querying from Yahoo weather API.

ii.  Use the following code to get weather information.

	YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils.getInstance();
	yahooWeatherUtils.queryYahooWeather(getApplicationContext(), "Name of City or Area", this);

Replace "Name of City or Area" to what you want.

3. What kind of weather information can I get?
You can get current condition of weather, humidity, wind, etc.
And some forecast information for next two days.

Check the XML returned by Yahoo Weather API here.
http://weather.yahooapis.com/forecastrss?w=2459115

(c) 2012 Zhenghong Wang
