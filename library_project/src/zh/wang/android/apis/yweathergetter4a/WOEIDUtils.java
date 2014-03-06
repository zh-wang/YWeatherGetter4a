/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2014 Zhenghong Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zh.wang.android.apis.yweathergetter4a;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.widget.Toast;

class WOEIDUtils {
	
	public static final String WOEID_NOT_FOUND = "WOEID_NOT_FOUND"; 

	private static final String WOEID_QUERY_PREFIX_FIND_BY_PLACE = "http://query.yahooapis.com/v1/public/yql?q=select*from%20geo.places%20where%20text=";
	private static final String WOEID_QUERY_SUFFIX_FORMAT = "&format=xml";
	
	private static final String WOEID_QUERY_PREFIX_FIND_BY_GPS = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22";
	private static final String WOEID_QUERY_CONSECTION_FIND_BY_GPS = "%2C";
	private static final String WOEID_QUERY_SUFFIX_FIND_BY_GPS = "%22%20and%20gflags%3D%22R%22";

	private String yahooAPIsQuery;
	private Map<String, String> mParsedResult;
	
	private static WOEIDUtils mInstance = new WOEIDUtils();
	
	public static WOEIDUtils getInstance() {
		return mInstance;
	}
	
	public String getWOEIDlocation() {
		String res = "";
		Iterator<String> it = mParsedResult.keySet().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            res += (o + " = " + mParsedResult.get(o)) + "\n";
        }
        return res;
	}
	
	public String getWOEID(Context context, String cityName) {
		return queryWOEIDfromYahooAPIs(context, cityName);
	}
	
	public String getWOEID(Context context, String lat, String lon) {
		return queryWOEIDfromYahooAPIs(context, lat, lon);
	}

	private String queryWOEIDfromYahooAPIs(Context context, String uriPlace) {
		YahooWeatherLog.d("Query WOEID by name of place");

		yahooAPIsQuery = WOEID_QUERY_PREFIX_FIND_BY_PLACE + "%22" + uriPlace + "%22"
				+ WOEID_QUERY_SUFFIX_FORMAT;
		
		yahooAPIsQuery = yahooAPIsQuery.replace(" ", "%20");
		
		YahooWeatherLog.d("Query WOEID: " + yahooAPIsQuery);

		String woeidString = fetchWOEIDxmlString(context, yahooAPIsQuery);
		Document woeidDoc = convertStringToDocument(context, woeidString);
		return getFirstMatchingWOEID(woeidDoc);
	}
	
	private String queryWOEIDfromYahooAPIs(Context context, String lat, String lon) {
		YahooWeatherLog.d("Query WOEID by latlon");
		
		yahooAPIsQuery = WOEID_QUERY_PREFIX_FIND_BY_GPS + lat +
						 WOEID_QUERY_CONSECTION_FIND_BY_GPS + lon +
						 WOEID_QUERY_SUFFIX_FIND_BY_GPS;
		
		yahooAPIsQuery = yahooAPIsQuery.replace(" ", "%20");
		
		YahooWeatherLog.d("Query WOEID: " + yahooAPIsQuery);
		String woeidString = fetchWOEIDxmlString(context, yahooAPIsQuery);
		Document woeidDoc = convertStringToDocument(context, woeidString);
		return getFirstMatchingWOEID(woeidDoc);
	}
	
	private String fetchWOEIDxmlString(Context context, String queryString) {
		YahooWeatherLog.d("fetch WOEID xml string");
		String qResult = "";

		HttpClient httpClient = NetworkUtils.createHttpClient();

		HttpGet httpGet = new HttpGet(queryString);

		try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String readLine = null;

				while ((readLine = bufferedreader.readLine()) != null) {
					YahooWeatherLog.d(readLine);
					stringBuilder.append(readLine + "\n");
				}

				qResult = stringBuilder.toString();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return qResult;
	}
	
	private Document convertStringToDocument(Context context, String src) {
		YahooWeatherLog.d("convert string to document");
		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			Toast.makeText(context, e1.toString(), Toast.LENGTH_LONG)
					.show();
		} catch (SAXException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
					.show();
		}

		return dest;

	}
	
	private String getFirstMatchingWOEID(Document srcDoc) {
		YahooWeatherLog.d("get first matching WOEID");

		try {

			mParsedResult = new HashMap<String, String>();
			for (int i = 1; i <= 4; i++) {
				String name = "line" + i;
				parseLocationLines(srcDoc, name);
			}

			NodeList nodeListDescription = srcDoc.getElementsByTagName("woeid");
			if (nodeListDescription.getLength() > 0) {
				Node node = nodeListDescription.item(0);
				return node.getTextContent();
			} else {
				return WOEID_NOT_FOUND;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			return WOEID_NOT_FOUND;
		}
		
	}
	
	private void parseLocationLines(Document srcDoc, String name) {
		NodeList nodeList = srcDoc.getElementsByTagName(name);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			mParsedResult.put(name, node.getTextContent());
		} 
	}
}
