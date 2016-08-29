package com.avlasenko.test.indexer.core.index.producer_consumer;

import com.avlasenko.test.indexer.core.Page;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;

/**
 * Created by A. Vlasenko on 26.08.2016.
 */
//Jsoup page(url document) loading is the most expensive part of application
// thus it is performing in separate threads
class PageLoaderTask implements Callable<Page> {

    private Page page;
    private final Set<String> found;
    private final BlockingDeque<Page> pages;
    private int depth;

    PageLoaderTask(Page page, Set<String> found, BlockingDeque<Page> pages, int depth) {
        this.page = page;
        this.found = found;
        this.pages = pages;
        this.depth = depth;
    }

    @Override
    public Page call() throws Exception {
        try {
            page.loadDocument();

            //page generation helps us control adding new pages to "pages"
            if (page.getGeneration() < depth) {
                try {
                    Set<Page> internalPages = page.getInternalPages();
                    for (Page p : internalPages) {
                        if (!found.contains(p.getUrl())) {
                            found.add(p.getUrl());
                            pages.putLast(p);
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println("ignored" + e.getMessage());
                }
            }
            return page;
        } catch (IOException e) {
            return null;
        }
    }
}
