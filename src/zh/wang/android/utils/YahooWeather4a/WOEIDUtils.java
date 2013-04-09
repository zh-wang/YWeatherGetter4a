/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2012 Zhenghong Wang
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class WOEIDUtils {
	
	public static final String WOEID_NOT_FOUND = "WOEID_NOT_FOUND"; 
	private final String yahooapisBase = "http://query.yahooapis.com/v1/public/yql?q=select*from%20geo.places%20where%20text=";
	private final String yahooapisFormat = "&format=xml";
	private String yahooAPIsQuery;
	
	public static WOEIDUtils getInstance() {
		return new WOEIDUtils();
	}
	
	public String getWOEIDid(Context context, String cityName) {
		return queryWOEIDfromYahooAPIs(context, cityName);
	}

	private String queryWOEIDfromYahooAPIs(Context context, String uriPlace) {
		Log.d("tag", "QueryYahooApis");

		yahooAPIsQuery = yahooapisBase + "%22" + uriPlace + "%22"
				+ yahooapisFormat;
		
		yahooAPIsQuery = yahooAPIsQuery.replace(" ", "%20");
		
		Log.d("tag", "yahooAPIsQuery: " + yahooAPIsQuery);

		String woeidString = queryYahooWeather(context, yahooAPIsQuery);
		Document woeidDoc = convertStringToDocument(context, woeidString);
		return getFirstMatchingWOEID(woeidDoc);

	}
	
	private String queryYahooWeather(Context context, String queryString) {
		Log.d("tag", "QueryYahooWeather");
		String qResult = "";
//		queryString = Uri.encode(queryString);

		HttpClient httpClient = new DefaultHttpClient();

		// return Uri.encode(queryString);

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
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
					.show();
		}

		return qResult;

	}
	
	private Document convertStringToDocument(Context context, String src) {
		Log.d("tag", "convertStringToDocument");
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
		Log.d("tag", "parserWOEID");

		try {
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
}
