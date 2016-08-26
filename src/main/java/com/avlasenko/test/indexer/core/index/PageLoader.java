package com.avlasenko.test.indexer.core.index;

import com.avlasenko.test.indexer.core.Page;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by A. Vlasenko on 26.08.2016.
 */
//task for page loading(Json document loading)
class PageLoader implements Callable<Page> {

    private Page page;

    PageLoader(Page page) {
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
