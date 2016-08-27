package com.avlasenko.test.indexer.core.index.producer_consumer;

import com.avlasenko.test.indexer.core.Page;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by A. Vlasenko on 26.08.2016.
 */
//Jsoup page(url document) loading is the most expensive part of application
// thus it is performing in separate threads
class PageLoaderTask implements Callable<Page> {

    private Page page;

    PageLoaderTask(Page page) {
        this.page = page;
    }

    @Override
    public Page call() throws Exception {
        try {
            page.loadDocument();
            return page;
        } catch (IOException e) {
            return null;
        }
    }
}
