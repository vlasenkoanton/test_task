package com.avlasenko.test.indexer.core.search;

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

import static com.avlasenko.test.indexer.core.index.PageIndexer.*;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public class PageSearcher implements Searcher<PageSearchResult> {
    //length of highlighted search fragment. Can be changed by "PageSearcher.setFragmentLength()"
    private static int FRAGMENT_LENGTH = 100;

    private Path indexDirectory;
    private int maxHits;

    public PageSearcher(Path indexDirectory, int maxHits) {
        this.indexDirectory = indexDirectory;
        this.maxHits = maxHits;
    }

    @Override
    public List<PageSearchResult> search(String queryString, Sort sort) {
        List<PageSearchResult> result = new ArrayList<>();

        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(indexDirectory))) {

            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            QueryParser parser = new QueryParser(CONTENTS, analyzer);
            Query query = parser.parse(queryString);

            TopDocs topDocs = searcher.search(query, maxHits, sort);
            ScoreDoc[] hits = topDocs.scoreDocs;

            for (ScoreDoc hit : hits) {
                Document d = searcher.doc(hit.doc);
                String urlText = d.get(URL);
                String titleText = d.get(TITLE);
                String fragmentText = null;

                try {
                    String contentText = d.get(CONTENTS);
                    fragmentText = getHighlightedFragment(query, analyzer, CONTENTS, contentText);
                } catch (InvalidTokenOffsetsException e) {
                    fragmentText = "";
                }

                result.add(new PageSearchResult(urlText, titleText, fragmentText));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void setFragmentLength(int fragmentLength) {
        FRAGMENT_LENGTH = fragmentLength;
    }

    private String getHighlightedFragment(Query query, Analyzer analyzer, String fieldName, String fieldValue)
            throws IOException, InvalidTokenOffsetsException {

        Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlited\">", "</span>");
        QueryScorer queryScorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, queryScorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, FRAGMENT_LENGTH));
        highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
        return highlighter.getBestFragment(analyzer, fieldName, fieldValue);
    }
}
