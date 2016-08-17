package com.avlasenko.test.indexer.service;

import com.avlasenko.test.indexer.index.SearchResult;
import org.apache.lucene.search.Sort;

import java.util.List;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public interface IndexerService {
    List<SearchResult> search(String query, Sort sort);

    void index(String url, int depth);
}
