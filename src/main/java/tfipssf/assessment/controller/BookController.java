package tfipssf.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tfipssf.assessment.service.BookService;

//@RestController
@Controller

public class BookController {
    @Autowired BookService serviceRest;
    
     @RequestMapping(path="/book/{works_id}")
    public String accessByWorksId(@PathVariable String works_id, Model ModelObj){

       
        ModelObj.addAttribute("title", serviceRest.searchbyID(works_id) );
        ModelObj.addAttribute("description", serviceRest.getDescription(works_id));
        return "BookDetail" ;       
    }  
}
