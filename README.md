YWeatherGetter4a
================

An Yahoo Weather API wrapper for android

An iOS version can be found here.
https://github.com/zh-wang/YWeatherGetter4i

It contains an exmaple project works on android 2.2 or above.   
Yahoo Weather API wrapper works on android 1.6 or above.	
Sample app can be found on Google Play. 	
https://play.google.com/store/apps/details?id=zh.wang.android.yweathergetter

+  What is this?	 

  This is a wrapper for getting weather information from Yahoo Weather API, for android.

+ How do I use it? 
	
   * First, implement YahooWeatherInfoListener in your Activity. Overwrite the callback function "gotWeatherInfo", which will be called after querying from Yahoo weather API. And, you need `INTERNET` and `ACCESS_NETWORK_STATE` to use `YWeatherGetter4a`.
	
	```java
	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		if(weatherInfo != null) {
			// Add your code here
            // weatherInfo object contains all information returned by Yahoo Weather apis
		}
	}
	```  

  * Second, use following code to assign the location where you want to get weather information at. Or you can input the location in app's top search bar.
    `YahooWeather` class provides 3 methods to query Yahoo's Weather apis.
    - ` public void queryYahooWeatherByPlaceName(final Context context, final String cityAreaOrLocation, final YahooWeatherInfoListener result) ` - query by place's name, you can replace `cityAreaOrLocation` to what you want. For example, "Tokyo Japan", "Acaraù Brazil", "Shanghai China", etc.
    - ` public void queryYahooWeatherByLatLon(final Context context, final String lat, final String lon, final YahooWeatherInfoListener result) ` - query by latitude and longitude
    - ` public void queryYahooWeatherByGPS(final Context context, final YahooWeatherInfoListener result) ` - use device's gps, to detect current location, then do the query. Remeber, you need ` ACCESS_FINE_LOCATION ` or ` ACCESS_COARSE_LOCATION ` to use this.



+ What kind of weather information can I get?	
  You can get 
  * current condition of weather, humidity, wind, etc.
  * forecast information for next four days.

  Check the XML structure returned by Yahoo Weather API here.
  http://weather.yahooapis.com/forecastrss?w=2459115
  

Developed By
================
Zhenghong Wang - <viennakanon@gmail.com>

License
================
    Copyright 2012 Zhenghong Wang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
