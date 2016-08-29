package com.avlasenko.test.indexer.core.index.producer_consumer;

import com.avlasenko.test.indexer.core.Page;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.avlasenko.test.indexer.core.index.IndexProps.*;

/**
 * Created by A. Vlasenko on 27.08.2016.
 */
//gradually uses tasks provided by PageIndexProducer and indexes results(loaded pages) of this tasks
public class PageIndexConsumer implements Runnable {

    private final BlockingQueue<Future<Page>> tasks;
    private final Path indexDirectory;

    //uses as a way to stop running PageIndexConsumer
    private volatile boolean end = false;

    public PageIndexConsumer(BlockingQueue<Future<Page>> tasks, Path indexDirectory) {
        this.tasks = tasks;
        this.indexDirectory = indexDirectory;
    }

    @Override
    public void run() {
        try (IndexWriter writer = getIndexWriter()) {
            while (!end) {
                Future<Page> future = tasks.poll();
                if (future != null) {
                    Page page = future.get();
                    if (page != null) {
                        indexPage(writer, page);
                        System.out.println("Page has been indexed: " + page.getUrl());
                    }
                }
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void indexPage(IndexWriter writer, Page page) throws IOException {
        Document document = new Document();
        String title = page.getTitle();
        String url = page.getUrl();
        String source = page.toPlainText();

        document.add(new TextField(CONTENTS, source, Field.Store.YES));
        document.add(new StringField(URL, url, Field.Store.YES));
        document.add(new StringField(TITLE, title, Field.Store.YES));
        document.add(new SortedDocValuesField(TITLE_SORT, new BytesRef(title)));

        writer.addDocument(document);
    }

    private IndexWriter getIndexWriter() throws IOException {
        FSDirectory directory = FSDirectory.open(indexDirectory);
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(directory, config);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void end() {
        //PageIndexConsumer can't be stopped while all tasks haven't been completed
        while (!tasks.isEmpty()) {
        }
        this.end = true;
    }
}
