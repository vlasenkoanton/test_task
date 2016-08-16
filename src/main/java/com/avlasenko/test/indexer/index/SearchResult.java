package com.avlasenko.test.indexer.index;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public class SearchResult {
    private String link;
    private String title;

    SearchResult(String link, String title) {
        this.link = link;
        this.title = title;
    }

    @Override
    public String toString() {
        return title+"\n"+link+"\n";
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }
}
