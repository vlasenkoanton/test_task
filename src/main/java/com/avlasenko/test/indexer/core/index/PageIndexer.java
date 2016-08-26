package com.avlasenko.test.indexer.core.index;

import com.avlasenko.test.indexer.core.Page;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by A. Vlasenko on 17.08.2016.
 */
public class PageIndexer implements Indexer {
    public static final String CONTENTS = "contents";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String TITLE_SORT = "titleSort";

    private Page startPage;
    private Path indexDirectory;

    public PageIndexer(Page startPage, Path indexDirectory) {
        this.startPage = startPage;
        this.indexDirectory = indexDirectory;
    }

    public void index() {
        try (IndexWriter writer = getIndexWriter()) {
            indexPage(writer, startPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void recursiveIndex(int depth) {
        if (depth == 0) {
            index();
        } else if (depth < 0) {
            throw new IllegalArgumentException("Depth must be >= 0");
        } else {
            try (IndexWriter writer = getIndexWriter()) {
                Set<Page> pages = new HashSet<>();
                ExecutorService executor = Executors.newFixedThreadPool(8);
                pages.add(startPage);
                indexPage(writer, startPage);

                for (int i = 0; i < depth; i++) {
                    List<Future<Page>> tasks = pageLoadTasks(pages, executor);
                    indexTasksResults(tasks, pages, writer);
                }
                executor.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //indexes all loaded pages
    private void indexTasksResults(Collection<Future<Page>> tasks, Set<Page> pages, IndexWriter writer) {
        for (Future<Page> f : tasks) {
            try {
                Page page = f.get();
                if (page != null && !pages.contains(page)) {
                    indexPage(writer, page);
                    pages.add(page);
                }
            } catch (ExecutionException | IOException | InterruptedException ignored) {
            }
        }
    }
    //Loads all pages need to be indexed in future
    private List<Future<Page>> pageLoadTasks(Set<Page> pages, ExecutorService executor) {
        Set<Page> temp = new HashSet<>(pages);
        List<Future<Page>> futures = new LinkedList<>();
        for (Page p : temp) {
            try {
                Set<String> links = p.getLinks();
                for (String link : links) {
                    Page p1 = new Page(link);
                    if (!pages.contains(p1)) {
                        //uses multithreading page loading as it is the most expensive operation
                        futures.add(executor.submit(new PageLoader(p1)));
                    }
                }
            } catch (IOException ignored) {
            }
        }
        return futures;
    }

    private void indexPage(IndexWriter writer, Page page) throws IOException {
        Document document = new Document();
        String title = page.getTitle();
        String url = page.getUrl();
        String source = page.toPlainText();
        System.out.println(title);
        System.out.println(url);

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
}
