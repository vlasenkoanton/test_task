package com.avlasenko.test.indexer;

import com.avlasenko.test.indexer.index.SearchResult;
import com.avlasenko.test.indexer.index.UrlDocumentIndexer;
import com.avlasenko.test.indexer.index.UrlSearcher;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by A. Vlasenko on 15.08.2016.
 */
public class Main {
    private static String testHTML = "https://www.javacodegeeks.com/2010/05/introduction-to-apache-lucene-for-full.html";
    private static String testDirectory = "C:\\Users\\losemind\\Desktop\\zzzzzz\\index\\1";

    public static void main(String[] args) throws IOException, ParseException {
        /*UrlDocumentIndexer indexer = new UrlDocumentIndexer(Paths.get(testDirectory));
        indexer.recursiveIndex(testHTML, 1);*/



        UrlSearcher searcher = new UrlSearcher(Paths.get(testDirectory), 10);
        List<SearchResult> results = searcher.search("maven");
        System.out.println("-------------------------------------");
        System.out.println("RESULTS:");
        results.forEach(System.out::println);
    }

}
