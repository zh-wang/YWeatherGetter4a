package zh.wang.android.yweathergetter;

import zh.wang.android.utils.YahooWeather4a.WeatherInfo;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherInfoListener;
import zh.wang.android.utils.YahooWeather4a.YahooWeatherUtils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements YahooWeatherInfoListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        YahooWeatherUtils yahooWeatherUtils = YahooWeatherUtils.getInstance();
        yahooWeatherUtils.queryYahooWeather(getApplicationContext(), "Tokyo", this);
    }

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
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
