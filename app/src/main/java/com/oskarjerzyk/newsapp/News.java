package com.oskarjerzyk.newsapp;

public class News {

    private String header;
    private String newsURL;
    private String image;

    public News() {
    }

    public News(String header, String newsURL, String image) {
        this.header = header;
        this.newsURL = newsURL;
        this.image = image;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNewsURL() {
        return newsURL;
    }

    public void setNewsURL(String newsURL) {
        this.newsURL = newsURL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
