package com.avlasenko.test.indexer.core.search;

import com.avlasenko.test.indexer.core.index.IndexProps;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

/**
 * Created by A. Vlasenko on 17.08.2016.
 */
public abstract class SearchProps {
    //how many search results to show
    public static final int MAX_HITS = 10;
    //fragment with highlighted search results length
    static final int RESULT_FRAGMENT_LENGTH = 100;

    //Sorting types
    public static class Sorting {
        public static final Sort RELEVANCE = Sort.RELEVANCE;
        public static final Sort ALPHABETICAL = new Sort(new SortField(IndexProps.TITLE_SORT, SortField.Type.STRING));
        public static final Sort NORMAL = Sort.INDEXORDER;
    }

    private SearchProps() {
    }
}
