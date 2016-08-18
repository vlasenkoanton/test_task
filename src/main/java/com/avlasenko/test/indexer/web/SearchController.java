package com.avlasenko.test.indexer.web;

import com.avlasenko.test.indexer.core.search.PageSearchResult;
import com.avlasenko.test.indexer.service.IndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.avlasenko.test.indexer.core.search.SearchProps.*;

/**
 * Created by A. Vlasenko on 15.08.2016.
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private IndexerService service;

    @RequestMapping
    public String search(@RequestParam String q, @RequestParam(required = false) String sort, Model model) {
        List<PageSearchResult> results;
        if (sort == null) {
            results = service.search(q, Sorting.RELEVANCE);
        } else {
            switch (sort) {
                case "relevance": results = service.search(q, Sorting.RELEVANCE);
                    break;
                case "alphabetic": results = service.search(q, Sorting.ALPHABETICAL);
                    break;
                default: results = service.search(q, Sorting.NORMAL);
            }
        }
        model.addAttribute("results", results);
        return "results";
    }
}
