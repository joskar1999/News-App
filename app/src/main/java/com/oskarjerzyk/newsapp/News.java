package com.oskarjerzyk.newsapp;

public class News {

    private String header;
    private String url;
    private String image;

    public News() {
    }

    public News(String header, String newsURL, String image) {
        this.header = header;
        this.url = newsURL;
        this.image = image;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
