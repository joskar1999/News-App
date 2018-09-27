package com.oskarjerzyk.newsapp.newsutils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WP {

    private Connection connection = Jsoup.connect("https://gadzetomania.pl/");
    private Document document;
    private List<String> imageURLList;
    private List<String> headersList;
    private List<String> links;

    public WP() throws IOException {
        document = connection.get();
        imageURLList = new ArrayList<String>();
        headersList = new ArrayList<String>();
        links = new ArrayList<String>();
    }

    public void downloadHeaders() {
        Elements allHeaders = document.select("div > a > h2");
        for (Element element : allHeaders) {
            headersList.add(element.text());
        }
        for (int i = 0; i < headersList.size(); i++) {
            System.out.println(headersList.get(i));
        }
    }

    public void downloadImages() {
        Elements allImages = document.select("article > figure > a > picture > img");
        for (Element element : allImages) {
            if (element.attr("src").equals("data:image/gif;base64,R0lGODlhAQABAAAAACwAAAAAAQABAAA=")) {
                imageURLList.add(element.attr("data-src"));
            } else {
                imageURLList.add(element.attr("src"));
            }
        }
        for (int i = 0; i < imageURLList.size(); i++) {
            System.out.println(imageURLList.get(i));
        }
    }

    public void downloadLinks() {
        Elements allLinks = document.select("article > figure > a");
        for (Element element : allLinks) {
            links.add(element.attr("href"));
        }
        for (int i = 0; i < links.size(); i++) {
            System.out.println(links.get(i));
        }
    }
}
