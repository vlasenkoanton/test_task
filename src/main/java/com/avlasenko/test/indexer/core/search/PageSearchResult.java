package com.avlasenko.test.indexer.core.search;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public class PageSearchResult {
    private String link;
    private String title;
    private String fragment;

    PageSearchResult(String link, String title, String fragment) {
        this.link = link;
        this.title = title;
        this.fragment = fragment;
    }

    @Override
    public String toString() {
        return title+"\n"+link+"\n"+fragment+"\n";
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getFragment() {
        return fragment;
    }
}
