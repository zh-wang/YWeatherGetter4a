YWeatherGetter4a [![Build Status](https://travis-ci.org/zh-wang/YWeatherGetter4a.svg?branch=master)](https://travis-ci.org/zh-wang/YWeatherGetter4a)
================

An Yahoo Weather API wrapper for android

An iOS version can be found here.
https://github.com/zh-wang/YWeatherGetter4i

It is a [library project](http://developer.android.com/tools/projects/projects-eclipse.html).  
This repo also contains a sample project in /library_project.  
Yahoo Weather API wrapper works on android 1.6 or above.  
Sample app can be found on Google Play.  
[https://play.google.com/store/apps/details?id=zh.wang.android.yweathergetter](https://play.google.com/store/apps/details?id=zh.wang.android.yweathergetter)

Request and response elements are defined here.  
[https://developer.yahoo.com/weather/documentation.html#response](https://developer.yahoo.com/weather/documentation.html#response)

Check the XML structure returned by Yahoo Weather API here.  
[Sample Weather API Response - Shanghai](https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Shanghai%22))

https://developer.yahoo.com/weather/documentation.html#response

+  What is this?

  This is a wrapper for getting weather information from Yahoo Weather RSS Feed.  
  You can find its guideline here.  [Yahoo Weather RSS Feed](https://developer.yahoo.com/weather/).  
  Note that, if you want to use weather API for commercial purpose, you should contact Yahoo first.  

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
      1. Query by place's name. You can replace `cityAreaOrLocation` to what you want. For example, "Tokyo Japan", "Acaraù Brazil", "Shanghai China", etc.  
      ```java
      public void queryYahooWeatherByPlaceName(final Context context, final String cityAreaOrLocation, final YahooWeatherInfoListener result) 
      ```

      2. Query by latitude and longitude
      ```java
      public void queryYahooWeatherByLatLon(final Context context, final String lat, final String lon, final YahooWeatherInfoListener result) 
      ```

      3. Use device's gps to detect current location. Then do the query. Remeber, you need ` ACCESS_FINE_LOCATION ` or ` ACCESS_COARSE_LOCATION ` to use this.  
      ```java
      public void queryYahooWeatherByGPS(final Context context, final YahooWeatherInfoListener result) 
      ```

      4. You can set connect timeout and socket timeout by `setConnectTimeout` and `setSocketTimeout`. Or get the instance by `getInstance(int connectTimeout, int socketTimeout)`.

      5. Handle exception by using `setExceptionListener`.

  * If you use GPS position for query, the result `WeatherInfo` will contains an `Address` object, which is the detail address object return from `Geocoder`. It contains a lot of information but this lib only use `locality`, `adminArea`, and `countryName` by default. See `YahooWeather.addressToPlaceName` and [Geocoder](http://developer.android.com/intl/ja/reference/android/location/Address.html) for details.

+ What kind of weather information can I get?
  You can get
  * current condition of weather, humidity, wind, etc.
  * forecast information for next four days.

Developed By
================
Zhenghong Wang - <viennakanon@gmail.com>

License
================
    Copyright 2016 Zhenghong Wang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
