package com.oskarjerzyk.newsapp.newsutils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Forbes {

    private Connection connection = Jsoup.connect("https://www.forbes.pl/technologie");
    private Document document;
    private List<String> imageURLList;
    private List<String> headersList;
    private List<String> links;

    public Forbes() throws IOException {
        document = connection.get();
        imageURLList = new ArrayList<String>();
        headersList = new ArrayList<String>();
        links = new ArrayList<String>();
    }

    public List<String> getImageURLList() {
        return imageURLList;
    }

    public List<String> getHeadersList() {
        return headersList;
    }

    public List<String> getLinks() {
        return links;
    }

    public void downloadAllURLs(){
        downloadHeaders();
        downloadLinks();
        downloadImages();
    }

    private void downloadHeaders() {
        Elements allHeaders = document.select(".itemTitle");
        int j = 0;
        for (Element element : allHeaders) {
            headersList.add(element.text());
            if (++j == 21){
                break;
            }
        }
    }

    private void downloadLinks() {
        Elements allLinks = document.select("div > a");
        int j = 0;
        for (Element element : allLinks) {
            links.add(element.attr("href"));
            if (++j == 21){
                break;
            }
        }
    }

    private void downloadImages() {
        String https = "https:";
        Elements allImages = document.select(".itemImage");
        int j = 0;
        for (Element element : allImages) {
            if (element.attr("data-original").substring(0, 6).equals(https)) {
                imageURLList.add(element.attr("data-original"));
            } else {
                imageURLList.add(https + element.attr("data-original"));
            }
            if (++j == 21) {
                break;
            }
        }
    }
}
