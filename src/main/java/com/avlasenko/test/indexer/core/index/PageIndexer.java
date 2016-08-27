package com.avlasenko.test.indexer.core.index;

import com.avlasenko.test.indexer.core.Page;
import com.avlasenko.test.indexer.core.index.producer_consumer.PageIndexConsumer;
import com.avlasenko.test.indexer.core.index.producer_consumer.PageIndexProducer;

import java.nio.file.Path;
import java.util.concurrent.*;

import static com.avlasenko.test.indexer.core.index.IndexProps.POOL_SIZE;

/**
 * Created by A. Vlasenko on 17.08.2016.
 */
public class PageIndexer implements Indexer {

    private String startUrl;
    private Path indexDirectory;

    public PageIndexer(String startUrl, Path indexDirectory) {
        this.startUrl = startUrl;
        this.indexDirectory = indexDirectory;
    }

    public void index() {
        recursiveIndex(0);
    }

    @Override
    public void recursiveIndex(int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Depth can't be < 0");
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
            BlockingQueue<Future<Page>> indexTasks = new ArrayBlockingQueue<>(16);
            //PageIndexConsumer should be started first because it will use results
            // provided by PageIndexProducer one by one as soon as they become available
            PageIndexConsumer pageIndexConsumer = new PageIndexConsumer(indexTasks, indexDirectory);
            executor.submit(pageIndexConsumer);

            PageIndexProducer producer = new PageIndexProducer(indexTasks);
            producer.producePages(new Page(startUrl), executor, depth);

            pageIndexConsumer.end();
            executor.shutdown();
        }
    }
}
