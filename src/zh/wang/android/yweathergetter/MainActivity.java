package zh.wang.android.yweathergetter;

import zh.wang.android.utils.YahooWeather4a.WeatherInfo;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherUtils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils.getInstance();
        WeatherInfo weatherInfo = yahooWeatherUtils.queryYahooWeather(getApplicationContext(), "Tokyo");
        if(weatherInfo != null) {
        	TextView tv = (TextView) findViewById(R.id.textview_weather_info);
			tv.setText(weatherInfo.getTitle() + "\n"
					+ weatherInfo.getLocationCity() + ", "
					+ weatherInfo.getLocationCountry() + "\n"
					+ "current weather: " + weatherInfo.getCurrentText() + "\n"
					+ "forecast weather: " + weatherInfo.getForecast1Text() + "\n" 
					+ "forecast weather: " + weatherInfo.getForecast2Text());
        } 
        
    }

}
