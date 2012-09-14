YWeatherGetter4a
================

An Yahoo Weather API wrapper for android

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

<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<rss version="2.0" xmlns:yweather="http://xml.weather.yahoo.com/ns/rss/1.0" xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#">
 <channel>

<title>Yahoo! Weather - New York, NY</title>
<link>http://us.rd.yahoo.com/dailynews/rss/weather/New_York__NY/*http://weather.yahoo.com/forecast/USNY0996_f.html</link>
<description>Yahoo! Weather for New York, NY</description>
<language>en-us</language>
<lastBuildDate>Fri, 23 Mar 2012 8:49 pm EDT</lastBuildDate>
<ttl>60</ttl>
<yweather:location city="New York" region="NY"   country="United States"/>
<yweather:units temperature="F" distance="mi" pressure="in" speed="mph"/>
<yweather:wind chill="60"   direction="0"   speed="0" />
<yweather:atmosphere humidity="49"  visibility="10"  pressure="30.08"  rising="0" />
<yweather:astronomy sunrise="6:52 am"   sunset="7:10 pm"/>
<image>
<title>Yahoo! Weather</title>
<width>142</width>
<height>18</height>
<link>http://weather.yahoo.com</link>
<url>http://l.yimg.com/a/i/brand/purplelogo//uh/us/news-wea.gif</url>
</image>
<item>
<title>Conditions for New York, NY at 8:49 pm EDT</title>
<geo:lat>40.71</geo:lat>
<geo:long>-74.01</geo:long>
<link>http://us.rd.yahoo.com/dailynews/rss/weather/New_York__NY/*http://weather.yahoo.com/forecast/USNY0996_f.html</link>
<pubDate>Fri, 23 Mar 2012 8:49 pm EDT</pubDate>
<yweather:condition  text="Fair"  code="33"  temp="60"  date="Fri, 23 Mar 2012 8:49 pm EDT" />
<description><![CDATA[
<img src="http://l.yimg.com/a/i/us/we/52/33.gif"/><br />
<b>Current Conditions:</b><br />
Fair, 60 F<BR />
<BR /><b>Forecast:</b><BR />
Fri - Mostly Cloudy. High: 72 Low: 52<br />
Sat - PM Showers. High: 61 Low: 49<br />
<br />
<a href="http://us.rd.yahoo.com/dailynews/rss/weather/New_York__NY/*http://weather.yahoo.com/forecast/USNY0996_f.html">Full Forecast at Yahoo! Weather</a><BR/><BR/>
(provided by <a href="http://www.weather.com" >The Weather Channel</a>)<br/>
]]></description>
<yweather:forecast day="Fri" date="23 Mar 2012" low="52" high="72" text="Mostly Cloudy" code="27" />
<yweather:forecast day="Sat" date="24 Mar 2012" low="49" high="61" text="PM Showers" code="39" />
<guid isPermaLink="false">USNY0996_2012_03_24_7_00_EDT</guid>
</item>
</channel>
</rss>
<!-- api1.weather.sg1.yahoo.com compressed/chunked Fri Mar 23 18:50:03 PDT 2012 -->

