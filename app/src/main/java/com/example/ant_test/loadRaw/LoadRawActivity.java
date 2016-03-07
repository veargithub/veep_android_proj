package com.example.ant_test.loadRaw;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.example.ant_test.R;
import android.app.Activity;
import android.os.Bundle;

public class LoadRawActivity extends Activity{
	private XmlPullParser parser;
	private HashMap<String, EmMarketInfo> hm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hm = new HashMap<String, EmMarketInfo>();
		parseMarketInfo(hm);
	}
	
	private void parseMarketInfo(HashMap<String, EmMarketInfo> hm) {
		InputStream inputStream = getResources().openRawResource(R.raw.market_info_v2);
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, "utf-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String elementName = parser.getName();
					if (("market").equals(elementName)) {
						parseMarket(parser, hm);
					}
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseMarket(XmlPullParser parser, HashMap<String, EmMarketInfo> hm) {
		String name = null;
		EmMarketInfo market = new EmMarketInfo();
		int eventType;
		while (true) {
			try {
				eventType = parser.next();
				if (eventType == XmlPullParser.START_TAG) {
					String elementName = parser.getName();
					if ("name".equals(elementName)) {
						name = parser.nextText();
						market.setMarketName(name);
					} else if ("id".equals(elementName)) {
						String id = parser.nextText();
						market.setMarketId(id);
					} else if ("item".equals(elementName)) {
						parseItem(parser, market);
					}
	            }
				if (eventType == XmlPullParser.END_TAG && parser.getName().equals("market")) {
					break;
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
            
		}
		if (market.getMarketName() != null) {
			hm.put(name, market);
			System.out.println(market);
		}
	}
	
	private void parseItem(XmlPullParser parser, EmMarketInfo market) {
		EmMarketItemInfo item = new EmMarketItemInfo();
		int eventType;
		while (true) {
			try {
				eventType = parser.next();
				if (eventType == XmlPullParser.START_TAG) {
					String elementName = parser.getName();
					if (elementName.equals("name")) {
						item.itemName = parser.nextText();
					} else if (elementName.equals("id")) {
						item.itemId = parser.nextText();
					} else if (elementName.equals("float_base")) {
						item.floatBase = parser.nextText();
					} else if (elementName.equals("share_board_lot")) {
						item.shareBoardLot = parser.nextText();
					} else if (elementName.equals("decimal")) {
						item.decimal = parser.nextText();
					}
				} else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("item")) {
					break;
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		if (item.itemName != null) {
			market.addItem(item.itemName, item);
		}
	}
}
