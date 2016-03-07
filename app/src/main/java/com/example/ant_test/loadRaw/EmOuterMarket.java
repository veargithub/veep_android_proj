package com.example.ant_test.loadRaw;

import java.util.HashMap;

public class EmOuterMarket {
	private HashMap<String, EmMarketInfo> hm;
	
	public EmOuterMarket() {
		hm = new HashMap<String, EmMarketInfo>();
	}
	
	public void setMarkets(HashMap<String, EmMarketInfo> hm) {
		this.hm = hm;
	}
}
