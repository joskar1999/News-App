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

    private Connection connection = Jsoup.connect("https://www.spidersweb.pl");
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
        for (Element element : allImageLinks) {
            imageURLList.add(element.attr("data-src"));
        }
        imageURLList.remove(0);
        imageURLList.remove(0);
    }

    private void downloadHeaders() throws IOException {
        Elements allHeaders = document.select("article > div > h1 > a > span > span");
        for (Element element : allHeaders) {
            headersList.add(element.text());
        }
        headersList.remove(0);
        headersList.remove(0);
        headersList.remove(0);
    }

    private void downloadLinks() throws IOException {
        Elements allLinks = document.select("div > article > div > a");
        for (Element element : allLinks) {
            links.add(element.attr("href"));
        }
        links.remove(0);
        links.remove(0);
    }
}
