package com.avlasenko.test.indexer.index;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by A. Vlasenko on 15.08.2016.
 */
class UrlDocument {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/45.0.2454.85 Safari/537.36";

    private final Document document;
    private final String url;

    UrlDocument(String url) throws IOException {
        this.url = url;
        this.document = Jsoup.connect(url).userAgent(USER_AGENT).get();
    }

    String parseToString() {
        Elements elements = document.getAllElements();

        StringBuilder sb = new StringBuilder();
        for (Element element : elements) {
            sb.append(element.text());
        }
        return sb.toString();
    }

    String getTitle() throws IOException {
        Elements titles = document.getElementsByTag("title");
        if (titles != null && !titles.isEmpty()) {
            return titles.get(0).text().trim();
        }
        return "No title";
    }

    Document getDocument() {
        return document;
    }

    public String getUrl() {
        return url;
    }


}
