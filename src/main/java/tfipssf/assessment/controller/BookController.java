package tfipssf.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tfipssf.assessment.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired BookService serviceRest;
    
    @GetMapping("/{works_id}")
    public String accessByWorksId(){
        return "";
    }
}
