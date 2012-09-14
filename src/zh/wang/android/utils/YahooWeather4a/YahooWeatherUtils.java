package zh.wang.android.utils.YahooWeather4a;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.content.Context;
import android.widget.Toast;

public class YahooWeatherUtils {
	
	public static final String YAHOO_WEATHER_ERROR = "Yahoo! Weather - Error";
	
	private String woeidNumber;

	public static YahooWeatherUtils getInstance() {
		return new YahooWeatherUtils();
	}
	
	public WeatherInfo queryYahooWeather(Context context, String cityName) {
		WOEIDUtils woeidUtils = WOEIDUtils.getInstance();
		woeidNumber = woeidUtils.getWOEIDid(context, cityName);
		if(!woeidNumber.equals(WOEIDUtils.WOEID_NOT_FOUND)) {
			String weatherString = getWeatherString(context, woeidNumber);
			Document weatherDoc = convertStringToDocument(context, weatherString);
			WeatherInfo weatherInfo = parseWeatherInfo(context, weatherDoc);
			return weatherInfo;
		} else {
			return null;
		}
	}
	
	private String getWeatherString(Context context, String woeidNumber) {
		String qResult = "";
		String queryString = "http://weather.yahooapis.com/forecastrss?w=" + woeidNumber;

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(queryString);

		try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
			
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
				}

				qResult = stringBuilder.toString();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return qResult;
	}

	private Document convertStringToDocument(Context context, String src) {
		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			Toast.makeText(context, e1.toString(), Toast.LENGTH_LONG).show();
		} catch (SAXException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return dest;
	}
	
	private WeatherInfo parseWeatherInfo(Context context, Document doc) {
		WeatherInfo weatherInfo = new WeatherInfo();
		try {
			
			Node titleNode = doc.getElementsByTagName("title").item(0);
			
			if(titleNode.getTextContent().equals(YAHOO_WEATHER_ERROR)) {
				return null;
			}
			
			weatherInfo.setTitle(titleNode.getTextContent());
			weatherInfo.setDescription(doc.getElementsByTagName("description").item(0).getTextContent());
			weatherInfo.setLanguage(doc.getElementsByTagName("language").item(0).getTextContent());
			weatherInfo.setLastBuildDate(doc.getElementsByTagName("lastBuildDate").item(0).getTextContent());
			
			Node locationNode = doc.getElementsByTagName("yweather:location").item(0);
			weatherInfo.setLocationCity(locationNode.getAttributes().getNamedItem("city").getNodeValue());
			weatherInfo.setLocationRegion(locationNode.getAttributes().getNamedItem("region").getNodeValue());
			weatherInfo.setLocationCountry(locationNode.getAttributes().getNamedItem("country").getNodeValue());
			
			Node windNode = doc.getElementsByTagName("yweather:wind").item(0);
			weatherInfo.setWindChill(windNode.getAttributes().getNamedItem("chill").getNodeValue());
			weatherInfo.setWindDirection(windNode.getAttributes().getNamedItem("direction").getNodeValue());
			weatherInfo.setWindSpeed(windNode.getAttributes().getNamedItem("speed").getNodeValue());
			
			Node atmosphereNode = doc.getElementsByTagName("yweather:atmosphere").item(0);
			weatherInfo.setAtmosphereHumidity(atmosphereNode.getAttributes().getNamedItem("humidity").getNodeValue());
			weatherInfo.setAtmosphereVisibility(atmosphereNode.getAttributes().getNamedItem("visibility").getNodeValue());
			weatherInfo.setAtmospherePressure(atmosphereNode.getAttributes().getNamedItem("pressure").getNodeValue());
			weatherInfo.setAtmosphereRising(atmosphereNode.getAttributes().getNamedItem("rising").getNodeValue());
			
			Node astronomyNode = doc.getElementsByTagName("yweather:astronomy").item(0);
			weatherInfo.setAstronomySunrise(astronomyNode.getAttributes().getNamedItem("sunrise").getNodeValue());
			weatherInfo.setAstronomySunset(astronomyNode.getAttributes().getNamedItem("sunset").getNodeValue());
			
			weatherInfo.setConditionTitle(doc.getElementsByTagName("title").item(2).getTextContent());
			weatherInfo.setConditionLat(doc.getElementsByTagName("geo:lat").item(0).getTextContent());
			weatherInfo.setConditionLon(doc.getElementsByTagName("geo:long").item(0).getTextContent());
			
			
			
			
			Node currentConditionNode = doc.getElementsByTagName("yweather:condition").item(0);
			weatherInfo.setCurrentCode(
					Integer.parseInt(
							currentConditionNode.getAttributes().getNamedItem("code").getNodeValue()
							));
			weatherInfo.setCurrentText(
					currentConditionNode.getAttributes().getNamedItem("text").getNodeValue());
			weatherInfo.setCurrentTempF(
					Integer.parseInt(
							currentConditionNode.getAttributes().getNamedItem("temp").getNodeValue()
							));
			weatherInfo.setCurrentConditionDate(
					currentConditionNode.getAttributes().getNamedItem("date").getNodeValue());
			
			Node forecast1ConditionNode = doc.getElementsByTagName("yweather:forecast").item(0);
			weatherInfo.setForecast1Code(
					Integer.parseInt(
							forecast1ConditionNode.getAttributes().getNamedItem("code").getNodeValue()
							));
			weatherInfo.setForecast1Text(
					forecast1ConditionNode.getAttributes().getNamedItem("text").getNodeValue());
			weatherInfo.setForecast1Date(
					forecast1ConditionNode.getAttributes().getNamedItem("date").getNodeValue());
			weatherInfo.setForecast1Day(
					forecast1ConditionNode.getAttributes().getNamedItem("day").getNodeValue());
			weatherInfo.setForecast1TempHighF(
					Integer.parseInt(
							forecast1ConditionNode.getAttributes().getNamedItem("high").getNodeValue()
							));
			weatherInfo.setForecast1TempLowF(
					Integer.parseInt(
							forecast1ConditionNode.getAttributes().getNamedItem("low").getNodeValue()
							));
			
			
			
			Node forecast2ConditionNode = doc.getElementsByTagName("yweather:forecast").item(1);
			weatherInfo.setForecast2Code(
					Integer.parseInt(
							forecast2ConditionNode.getAttributes().getNamedItem("code").getNodeValue()
							));
			weatherInfo.setForecast2Text(
					forecast2ConditionNode.getAttributes().getNamedItem("text").getNodeValue());
			weatherInfo.setForecast2Date(
					forecast2ConditionNode.getAttributes().getNamedItem("date").getNodeValue());
			weatherInfo.setForecast2Day(
					forecast2ConditionNode.getAttributes().getNamedItem("day").getNodeValue());
			weatherInfo.setForecast2TempHighF(
					Integer.parseInt(
							forecast2ConditionNode.getAttributes().getNamedItem("high").getNodeValue()
							));
			weatherInfo.setForecast2TempLowF(
					Integer.parseInt(
							forecast2ConditionNode.getAttributes().getNamedItem("low").getNodeValue()
							));
			
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			Toast.makeText(context, "Parse XML failed - Unrecognized Tag", Toast.LENGTH_SHORT).show();
			weatherInfo = null;
		}
		
		return weatherInfo;
	}
}
