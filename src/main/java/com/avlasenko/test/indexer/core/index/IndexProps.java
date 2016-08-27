package com.avlasenko.test.indexer.core.index;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public abstract class IndexProps {
    //index fields names
    public static final String CONTENTS = "contents";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String TITLE_SORT = "titleSort";

    //core storage directory
    public static final Path INDEX_DIR = Paths.get("/indexes");
    //ThreadPoolExecutor size for multithreading recursive indexing
    public static final int POOL_SIZE = 8;

    private IndexProps() {
    }
}
