YWeatherGetter4a
================

--------------

__Note that [YQL service is retired](https://developer.yahoo.com/yql/?guccounter=1), so this lib don't work anymore :(__

--------------

An Yahoo Weather API wrapper for android

An iOS version can be found here.
https://github.com/zh-wang/YWeatherGetter4i

Works on android 2.3 or above.  
Sample app can be found on Google Play.  
[https://play.google.com/store/apps/details?id=zh.wang.android.yweathergetter](https://play.google.com/store/apps/details?id=zh.wang.android.yweathergetter)

Request and response elements are defined here.  
[https://developer.yahoo.com/weather/documentation.html#response](https://developer.yahoo.com/weather/documentation.html#response)

Check the XML structure returned by Yahoo Weather API here.  
[Sample Weather API Response - Shanghai](https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Shanghai%22))

For more information, check Yahoo's document here.  
[Yahoo Weather](https://developer.yahoo.com/weather/).  

+ Install

  `compile 'zh.wang.android:yweathergetter4a:1.3.0'`

+ How to use ([Sample Activity](https://github.com/zh-wang/YWeatherGetter4a/blob/master/app/src/main/java/zh/wang/android/yweathergetter4a_demo/MainActivity.java))

   * Implement `YahooWeatherInfoListener` in your Activity. `INTERNET` and `ACCESS_NETWORK_STATE` permissions are required.

        ```java
        @Override
        public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
            if(weatherInfo != null) {
                // Add your code here
                // weatherInfo object contains all information returned by Yahoo Weather API
                // if `weatherInfo` is null, you can get the error from `errorType`
            }
        }
        ```

  * Get a instance of `YahooWeather` class.

        ```java
        YahooWeather mYahooWeather = YahooWeather.getInstance();
        ```

  * Query by place's name. You can replace `cityAreaOrLocation` to what you want. For example, "Tokyo Japan", "Acaraù Brazil", "Shanghai China", etc.

        ```java
        public void queryYahooWeatherByPlaceName(final Context context, final String cityAreaOrLocation, final YahooWeatherInfoListener result)
        ```

  * Query by latitude and longitude

        ```java
        public void queryYahooWeatherByLatLon(final Context context, final String lat, final String lon, final YahooWeatherInfoListener result)
        ```

  * Or use device's gps to detect current location. __`ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION` is required__.

        ```java
        public void queryYahooWeatherByGPS(final Context context, final YahooWeatherInfoListener result)
        ```

+ Other settings

  * You can set connect timeout and socket timeout by `setConnectTimeout` and `setSocketTimeout`. Or get the instance by `getInstance(int connectTimeout, int socketTimeout)`.

  * If you use GPS position for query, the result `WeatherInfo` will contains an `Address` object, which is the detail address object return from `Geocoder`. It contains a lot of information but this lib only use `locality`, `adminArea`, and `countryName` by default. See `YahooWeather.addressToPlaceName` and [Geocoder](http://developer.android.com/intl/ja/reference/android/location/Address.html) for details.

+ What kind of weather information can I get?
  You can get
  * current condition of weather, humidity, wind, etc.
  * forecast of next four days.
  * for more details, https://developer.yahoo.com/weather/documentation.html#response

Developed By
================
Zhenghong Wang - <viennakanon@gmail.com>

License
================
    Copyright 2017 Zhenghong Wang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
