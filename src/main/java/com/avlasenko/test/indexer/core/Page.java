package com.avlasenko.test.indexer.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by A. Vlasenko on 15.08.2016.
 */
public class Page {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36";

    private final String url;
    private Document document;

    public Page(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("Constructor parameter \"url\" shouldn't be null or empty.");
        }
        this.url = url;
    }

    public String toPlainText() throws IOException {
        loadDocument();
        return document.text();
    }

    public String getTitle() throws IOException {
        loadDocument();
        String title = document.title();
        if (!title.isEmpty()) {
            return title;
        }
        return "No title";
    }

    public Set<String> getLinks() throws IOException {
        loadDocument();
        Set<String> links = new HashSet<>();

        Elements elements = document.getElementsByTag("a");
        if (!elements.isEmpty()) {
            for (Element element : elements) {
                boolean nofollow = element.attr("rel").equalsIgnoreCase("nofollow");
                String href = element.attr("href");
                if (!nofollow && href.startsWith("http") && !links.contains(href)) {
                    links.add(href);
                }
            }
        }
        return links;
    }

    public Document getDocument() throws IOException {
        loadDocument();
        return document;
    }

    public String getUrl() {
        return url;
    }

    public void loadDocument() throws IOException {
        if (document == null) {
            this.document = Jsoup.connect(url).userAgent(USER_AGENT).timeout(3000).get();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return getUrl().equals(page.getUrl());

    }

    @Override
    public int hashCode() {
        return getUrl().hashCode();
    }
}
