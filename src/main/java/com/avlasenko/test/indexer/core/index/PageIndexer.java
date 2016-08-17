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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by A. Vlasenko on 17.08.2016.
 */
public class PageIndexer implements Indexer {
    public static final String CONTENTS = "contents";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String TITLE_SORT = "titleSort";

    private Page page;
    private Path indexDirectory;
    private Set<String> indexedLinks = new HashSet<>();

    public PageIndexer(Page page, Path indexDirectory) {
        this.page = page;
        this.indexDirectory = indexDirectory;
    }

    @Override
    public void recursiveIndex(int depth) {
        if (depth == 0) {
            index();
        } else if (depth < 0) {
            throw new IllegalArgumentException("\"depth\" should be >= 0");
        }

        try (IndexWriter writer = getIndexWriter()) {
            Set<String> links = new CopyOnWriteArraySet<>();
            links.add(page.getUrl());

            for (int i = 0; i <= depth; i++) {
                for (String link : links) {
                    try {
                        Page p = new Page(link);
                        if (i != depth) {
                            links.addAll(p.getLinks());
                        }
                        if (!indexedLinks.contains(link)) {
                            indexPage(writer, p);
                            indexedLinks.add(link);
                        }
                    } catch (IOException ignored) {
                        //skip all inaccessible and incompatible pages(e.g. pdf files)
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void index() {
        try (IndexWriter indexWriter = getIndexWriter()) {
            indexPage(indexWriter, page);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getIndexedLinks() {
        return indexedLinks;
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
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(FSDirectory.open(indexDirectory), config);
    }
}
