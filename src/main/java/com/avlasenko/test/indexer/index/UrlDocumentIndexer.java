package com.avlasenko.test.indexer.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by A. Vlasenko on 15.08.2016.
 */
public class UrlDocumentIndexer {
    static final String CONTENTS = "contents";
    static final String URL = "url";
    static final String TITLE = "title";

    private final Path indexDirectory;

    public UrlDocumentIndexer(Path indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public void recursiveIndex(String url, int depth) throws IOException {
        Directory directory;
        if (indexDirectory == null) {
            directory = new RAMDirectory();
        } else {
            directory = FSDirectory.open(indexDirectory);
        }

        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter iw = new IndexWriter(directory, config);

        Set<String> links = recursiveLinksSearch(url, depth);

        for (String link : links) {
            UrlDocument ud = null;
            try {
                ud = new UrlDocument(link);
                indexFile(iw, ud.parseToString(), link, ud.getTitle());
            } catch (IOException ignored) {
            }
        }
        iw.close();
    }

    private Set<String> recursiveLinksSearch(String url, int depth) {
        Set<String> result = new HashSet<>();
        result.add(url);

        int i = 0;
        while (i < depth) {
            Set<String> temp = new HashSet<>();
            for (String link : result) {
                temp.addAll(getAllDocumentLinks(link));
            }
            result.addAll(temp);
            i++;
        }
        return result;
    }

    private Set<String> getAllDocumentLinks(String url) {
        Set<String> result = new HashSet<>();

        UrlDocument urlDocument = null;
        try {
            urlDocument = new UrlDocument(url);
        } catch (IOException e) {
            return result;
        }
        org.jsoup.nodes.Document document = urlDocument.getDocument();

        Elements links = document.getElementsByTag("a");

        for (Element link : links) {
            boolean nofollow = link.attr("rel").equalsIgnoreCase("nofollow");
            String href = link.attr("href");
            if (!nofollow && href.startsWith("http") && !result.contains(href)) {
                result.add(href);
            }
        }
        return result;
    }

    private void indexFile(IndexWriter indexWriter, String source, String link, String title) throws IOException {
        Document document = new Document();

        document.add(new TextField(CONTENTS, new StringReader(source)));
        document.add(new StringField(URL, link, Field.Store.YES));
        document.add(new StringField(TITLE, title, Field.Store.YES));

        indexWriter.addDocument(document);
    }

}
