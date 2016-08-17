package com.avlasenko.test.indexer.index;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

/**
 * Created by A. Vlasenko on 17.08.2016.
 */
public abstract class SortType {
    public static final Sort RELEVANCE = Sort.RELEVANCE;
    public static final Sort ALPHABETICAL = new Sort(new SortField(UrlDocumentIndexer.TITLE_SORT, SortField.Type.STRING));
    public static final Sort NORMAL = Sort.INDEXORDER;

    private SortType() {
    }
}
