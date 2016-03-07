package com.example.ant_test.service.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class EmPushMessage implements Parcelable{
	private static final String DIVIDE = "&&";
	private String uid;
	private String type;//1-股价提醒 2-公告提醒 3-研报提醒 4-数据提醒
	private String marketCode;//0：沪深A股，1：中小板...
	private String stockCode;//股票代码[600600]
	private String messageText;//消息内容
	private String gubaArticleId;//股吧唯一标示
	private String dataTime;//显示时间
	public EmPushMessage() {}
	public EmPushMessage(String str) {
		String[] data;
		String[] codes;
		try {
			data = str.split(DIVIDE);
		} catch (Exception e) {return;}
		if (data == null || data.length != 6) {
			return;
		}
		try {
			codes = data[2].split(",");
		} catch (Exception e) {return;}
		if (codes == null || codes.length != 2) {
			return;
		}
		this.uid = data[0];
		this.type = data[1];
		this.marketCode = codes[0];
		this.stockCode = codes[1];
		this.messageText = data[3];
		this.gubaArticleId = data[4];
		this.dataTime = data[5];
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMarketCode() {
		return marketCode;
	}
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getGubaArticleId() {
		return gubaArticleId;
	}
	public void setGubaArticleId(String gubaArticleId) {
		this.gubaArticleId = gubaArticleId;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	@Override
	public String toString() {
		return "EmPushMessage [uid=" + uid + ", type=" + type + ", marketCode="
				+ marketCode + ", stockCode=" + stockCode + ", messageText="
				+ messageText + ", gubaArticleId=" + gubaArticleId
				+ ", dataTime=" + dataTime + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(uid);
		dest.writeString(type);
		dest.writeString(marketCode);
		dest.writeString(stockCode);
		dest.writeString(messageText);
		dest.writeString(gubaArticleId);
		dest.writeString(dataTime);
	}
	
	public static final Parcelable.Creator<EmPushMessage> CREATOR = new Creator<EmPushMessage>() {
		
		@Override
		public EmPushMessage[] newArray(int size) {
			return new EmPushMessage[size];
		}
		
		@Override
		public EmPushMessage createFromParcel(Parcel source) {
			EmPushMessage message = new EmPushMessage();
			message.setUid(source.readString());
			message.setType(source.readString());
			message.setMarketCode(source.readString());
			message.setStockCode(source.readString());
			message.setMessageText(source.readString());
			message.setGubaArticleId(source.readString());
			message.setDataTime(source.readString());
			return message;
		}
	}; 
}
