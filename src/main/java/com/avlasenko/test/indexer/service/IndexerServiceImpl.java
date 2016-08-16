package com.avlasenko.test.indexer.service;

import com.avlasenko.test.indexer.index.SearchResult;
import com.avlasenko.test.indexer.index.UrlDocumentIndexer;
import com.avlasenko.test.indexer.index.UrlSearcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.avlasenko.test.indexer.index.IndexSearchProperties.*;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
@Service
public class IndexerServiceImpl implements IndexerService {

    public List<SearchResult> search(String query) {
        UrlSearcher searcher = new UrlSearcher(INDEX_DIR, MAX_HITS);
        try {
            return searcher.search(query);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void index(String url, int depth) {
        UrlDocumentIndexer indexer = new UrlDocumentIndexer(INDEX_DIR);
        try {
            indexer.recursiveIndex(url, depth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
