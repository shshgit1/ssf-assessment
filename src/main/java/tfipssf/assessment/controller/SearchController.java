package tfipssf.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tfipssf.assessment.service.BookService;

@Controller
@RequestMapping(path ="/book")
public class SearchController {
    private final Logger logger = Logger.getLogger("from the searchcontroller:");
    @Autowired BookService service;

    @GetMapping
    public String searchByTitle(@RequestParam String bookName, Model modelobj){
    modelobj.addAttribute("book1", bookName);

    //List searchResults=service.search(bookName);
    HashMap<String,String> searchResults=service.search(bookName);
    modelobj.addAttribute("results", searchResults);
        logger.log(Level.INFO,""+searchResults);
    return "SearchResult";
    }
    
  
}
