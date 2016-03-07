package com.example.ant_test.banner;

/**
 * Created by chx7078 on 2015/4/27.
 */
public class HomeBanner {

    public int autoId;
    public  String uri;
    public  String imgUrl;
    public  String title;

    @Override
    public String toString() {
        return "HomeBanner{" +
                "autoId=" + autoId +
                ", uri='" + uri + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
