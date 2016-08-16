package com.avlasenko.test.indexer.service;

import com.avlasenko.test.indexer.index.SearchResult;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public interface IndexerService {
    public List<SearchResult> search(String query);

    public void index(String url, int depth);
}
