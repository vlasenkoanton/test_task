package com.avlasenko.test.indexer.core.index;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public abstract class IndexProps {
    //core storage directory
    public static final Path INDEX_DIR = Paths.get("/indexes");

    private IndexProps() {
    }
}
