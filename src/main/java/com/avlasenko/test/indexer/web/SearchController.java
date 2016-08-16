package com.avlasenko.test.indexer.web;

import com.avlasenko.test.indexer.index.SearchResult;
import com.avlasenko.test.indexer.service.IndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by A. Vlasenko on 15.08.2016.
 */
@Controller
@RequestMapping
public class SearchController {

    @Autowired
    private IndexerService service;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam String q, Model model) {
        List<SearchResult> results = service.search(q);
        model.addAttribute("results", results);
        return "results";
    }
}
