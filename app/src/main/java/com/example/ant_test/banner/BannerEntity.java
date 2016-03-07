package com.example.ant_test.banner;

/**
 * Created by chx7078 on 2015/6/8.
 */
public class BannerEntity implements IBannerEntity{

    public int id;
    public String title;
    public String destinationUrl;
    public String imageUrl;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDestinationUrl() {
        return destinationUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}
