package com.avlasenko.test.indexer.web;

import com.avlasenko.test.indexer.service.IndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by A. Vlasenko on 16.08.2016.
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexerService service;

    @RequestMapping(method = RequestMethod.GET)
    public String core() {
        return "linkIndex";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processIndexing(@RequestParam String q, @RequestParam Integer d) {
        service.index(q, d);
        return "redirect:/";
    }
}
