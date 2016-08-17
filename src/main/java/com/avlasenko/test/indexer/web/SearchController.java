package com.avlasenko.test.indexer.web;

import com.avlasenko.test.indexer.index.SortType;
import com.avlasenko.test.indexer.index.SearchResult;
import com.avlasenko.test.indexer.service.IndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by A. Vlasenko on 15.08.2016.
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private IndexerService service;

    @RequestMapping
    public String sort(@RequestParam String q, @RequestParam(required = false) String sort, Model model) {
        List<SearchResult> results;
        if (sort == null) {
            results = service.search(q, SortType.NORMAL);
        } else {
            switch (sort) {
                case "relevance": results = service.search(q, SortType.RELEVANCE);
                    break;
                case "alphabetic": results = service.search(q, SortType.ALPHABETICAL);
                    break;
                default: results = service.search(q, SortType.NORMAL);
            }
        }
        model.addAttribute("results", results);
        return "results";
    }
}
