package com.avlasenko.test.indexer.index;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
public abstract class IndexSearchProperties {
    //length of text fragment with highlighted <span> tags
    public static final int FRAGMENT_LENGTH = 100;
    //index storage directory
    public static final Path INDEX_DIR = Paths.get("C:\\Users\\losemind\\Desktop\\zzzzzz\\index\\1");
    //how many search results to show
    public static final int MAX_HITS = 10;

    private IndexSearchProperties() {
    }
}
