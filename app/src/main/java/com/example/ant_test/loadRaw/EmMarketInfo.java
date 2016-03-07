package com.example.ant_test.loadRaw;

import java.util.HashMap;


public class EmMarketInfo {
	private String marketName;
	private String marketId;
	private HashMap<String, EmMarketItemInfo> items;
	
	public EmMarketInfo() {
		this.items = new HashMap<String, EmMarketItemInfo>();
	}
	
	public void addItem(String name, EmMarketItemInfo item) {
		items.put(name, item);
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String name) {
		this.marketName = name;
	}

	public String getMarketId() {
		return marketId;
	}

	public void setMarketId(String id) {
		this.marketId = id;
	}

	@Override
	public String toString() {
		return "EmMarketInfo [marketName=" + marketName + ", marketId=" + marketId + ", items=" + items
				+ "]";
	}
	
}
