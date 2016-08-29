package com.avlasenko.test.indexer.core.index.producer_consumer;

import com.avlasenko.test.indexer.core.Page;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by A. Vlasenko on 27.08.2016.
 */
public class PageIndexProducer {
    //means of controlling visited links
    private final Set<String> found = Collections.synchronizedSet(new HashSet<>());

    private final BlockingQueue<Future<Page>> indexTasks;

    public PageIndexProducer(BlockingQueue<Future<Page>> indexTasks) {
        this.indexTasks = indexTasks;
    }

    //Searches all internal links(pages) till specified depth starting from "startPage"
    public void producePages(Page startPage, ThreadPoolExecutor executor, int depth) {
        BlockingDeque<Page> pages = new LinkedBlockingDeque<>();

        try {
            pages.put(startPage);
            found.add(startPage.getUrl());
            while (true) {
                //Takes(polls) first page, executes page loading in separate threads
                // and provide PageIndexConsumer with results(tasks)
                // at the same time gets rid of heavyweight loaded page(with loaded Jsoup document inside)
                Page page = pages.pollFirst(500, TimeUnit.MILLISECONDS);
                if (page != null) {
                    indexTasks.put(executor.submit(new PageLoaderTask(page, found, pages, depth)));
                } else {
                    //getActiveCount() > 1 because one thread from pool we use for indexing
                    // (in PageIndexer we start PageIndexConsumer in separate thread).
                    //Using pollFirst(timeout) and loop check here instead of while condition is a way
                    // of decreasing unnecessary blocks of executor's main lock.
                    if (executor.getActiveCount() <= 1 && pages.isEmpty()) {
                        break;
                    }
                }
            }
        } catch (InterruptedException ignored) {
        }
    }
}
