package com.oskarjerzyk.newsapp.newsutils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spidersweb {

    private Connection connection = Jsoup.connect("https://www.spidersweb.pl/kategoria/nowe-technologie");
    private Document document;
    private List<String> imageURLList;
    private List<String> headersList;
    private List<String> links;

    public Spidersweb() throws IOException {
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

    public void downloadAllURLs() throws IOException {
        downloadHeaders();
        downloadImages();
        downloadLinks();
    }

    /**
     * This method downloads only images URLs, not images.
     *
     * @throws IOException - could be thrown by select method.
     */
    private void downloadImages() throws IOException {
        Elements allImageLinks = this.document.select("div > article > div > a > img");
        for (int i = 0; i < 15; i++) {
            Element element = allImageLinks.get(i);
            imageURLList.add(element.attr("data-src"));
        }
    }

    private void downloadHeaders() throws IOException {
        Elements allHeaders = document.select("div > article > div > a > img");
        for (int i = 0; i < 15; i++) {
            Element element = allHeaders.get(i);
            headersList.add(element.attr("alt"));
        }
    }

    private void downloadLinks() throws IOException {
        Elements allLinks = document.select("div > article > div > a");
        for (int i = 0; i < 15; i++) {
            Element element = allLinks.get(i);
            links.add(element.attr("href"));
        }
    }
}