package com.avlasenko.test.indexer.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.avlasenko.test.indexer.index.IndexSearchProperties.*;

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

    public List<SearchResult> search(String queryString, Sort sort) throws IOException, ParseException {
        List<SearchResult> result = new ArrayList<>();

        IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDirectory));

        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();

        QueryParser parser = new QueryParser(UrlDocumentIndexer.CONTENTS, analyzer);
        Query query = parser.parse(queryString);

        TopDocs topDocs = searcher.search(query, maxHits, sort);
        ScoreDoc[] hits = topDocs.scoreDocs;

        for (ScoreDoc hit : hits) {
            Document d = searcher.doc(hit.doc);
            String urlText = d.get(UrlDocumentIndexer.URL);
            String titleText = d.get(UrlDocumentIndexer.TITLE);
            String contentText = d.get(UrlDocumentIndexer.CONTENTS);

            String fragmentText = null;
            try {
                fragmentText = getHighlightedFragment(query, analyzer, UrlDocumentIndexer.CONTENTS, contentText);
            } catch (InvalidTokenOffsetsException e) {
                fragmentText = "";
            }

            result.add(new SearchResult(urlText, titleText, fragmentText));
        }

        return result;
    }

    private String getHighlightedFragment(Query query, Analyzer analyzer, String fieldName, String fieldValue) throws IOException, InvalidTokenOffsetsException {
        Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlited\">", "</span>");
        QueryScorer queryScorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, queryScorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, FRAGMENT_LENGTH));
        highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
        return highlighter.getBestFragment(analyzer, fieldName, fieldValue);
    }
}
