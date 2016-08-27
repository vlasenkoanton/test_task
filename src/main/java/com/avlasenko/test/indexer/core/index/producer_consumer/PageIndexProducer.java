package com.avlasenko.test.indexer.core.index.producer_consumer;

import com.avlasenko.test.indexer.core.Page;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by A. Vlasenko on 27.08.2016.
 */
public class PageIndexProducer {
    private Set<String> found = new HashSet<>();

    private final BlockingQueue<Future<Page>> indexTasks;

    public PageIndexProducer(BlockingQueue<Future<Page>> indexTasks) {
        this.indexTasks = indexTasks;
    }

    //Searches all internal links(pages) till specified depth starting from "startPage"
    public void producePages(Page startPage, ExecutorService executor, int depth) {
        BlockingDeque<Page> pages = new LinkedBlockingDeque<>();

        try {
            pages.put(startPage);
            found.add(startPage.getUrl());
            while (!pages.isEmpty()) {
                //Takes first page, executes page loading in separate threads
                // and provide PageIndexConsumer with results(tasks)
                // at the same time gets rid of heavyweight loaded page(with loaded Jsoup document inside)
                Page page = pages.takeFirst();
                indexTasks.put(executor.submit(new PageLoaderTask(page)));

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
                    } catch (IOException | InterruptedException ignored) {
                    }
                }
            }
        } catch (InterruptedException ignored) {
        }
    }
}
