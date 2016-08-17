package com.avlasenko.test.indexer.core.search;
import org.apache.lucene.search.Sort;

import java.util.Collection;

/**
 * Created by A. Vlasenko on 17.08.2016.
 */
public interface Searcher<R> {
    Collection<R> search(String queryString, Sort sort);
}
