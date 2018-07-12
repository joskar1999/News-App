package com.oskarjerzyk.newsapp;

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

    public void downloadHeaders() {
        Elements allHeaders = document.select(".itemTitle");
        int j = 0;
        for (Element element : allHeaders) {
            headersList.add(element.text());
            if (++j == 21){
                break;
            }
        }
        for (int i = 0; i < headersList.size(); i++) {
            System.out.println(headersList.get(i));
        }
    }

    public void downloadLinks() {
        Elements allLinks = document.select("div > a");
        int j = 0;
        for (Element element : allLinks) {
            links.add(element.attr("href"));
            if (++j == 21){
                break;
            }
        }
        for (int i = 0; i < links.size(); i++) {
            System.out.println(links.get(i));
        }
    }

    public void downloadImages() {
        Elements allImages = document.select(".itemImage");
        int j = 0;
        for (Element element : allImages) {
            imageURLList.add(element.attr("data-original"));
            if (++j == 21){
                break;
            }
        }
        for (int i = 0; i < imageURLList.size(); i++) {
            System.out.println(imageURLList.get(i));
        }
    }
}
