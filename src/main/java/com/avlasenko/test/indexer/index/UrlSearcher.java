package com.avlasenko.test.indexer.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public class UrlSearcher {
    private Path indexDirectory;
    private int maxHits;

    public UrlSearcher(Path indexDirectory, int maxHits) {
        this.indexDirectory = indexDirectory;
        this.maxHits = maxHits;
    }

    public List<SearchResult> search(String queryString) throws IOException, ParseException {
        List<SearchResult> result = new ArrayList<>();

        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDirectory));

        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        QueryParser parser = new QueryParser(UrlDocumentIndexer.CONTENTS, analyzer);
        Query query = parser.parse(queryString);

        TopDocs topDocs = searcher.search(query, maxHits, Sort.RELEVANCE);
        ScoreDoc[] hits = topDocs.scoreDocs;

        for (ScoreDoc hit : hits) {
            Document d = searcher.doc(hit.doc);
            result.add(new SearchResult(d.get(UrlDocumentIndexer.URL), d.get(UrlDocumentIndexer.TITLE)));
        }

        return result;
    }
}
