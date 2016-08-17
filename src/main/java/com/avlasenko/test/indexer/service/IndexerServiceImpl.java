package com.avlasenko.test.indexer.service;

import com.avlasenko.test.indexer.core.Page;
import com.avlasenko.test.indexer.core.index.IndexProps;
import com.avlasenko.test.indexer.core.index.Indexer;
import com.avlasenko.test.indexer.core.index.PageIndexer;
import com.avlasenko.test.indexer.core.search.PageSearchResult;
import com.avlasenko.test.indexer.core.search.PageSearcher;
import com.avlasenko.test.indexer.core.search.SearchProps;
import org.apache.lucene.search.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
@Service
public class IndexerServiceImpl implements IndexerService {

    @Override
    public List<PageSearchResult> search(String query, Sort sort) {
        PageSearcher searcher = new PageSearcher(IndexProps.INDEX_DIR, SearchProps.MAX_HITS);
        return searcher.search(query, sort);
    }

    @Override
    public void index(String url, int depth) {
        Indexer indexer = new PageIndexer(new Page(url), IndexProps.INDEX_DIR);
        indexer.recursiveIndex(depth);
    }
}
