package insta.instaf4fcheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {


    @GetMapping("/")
    public String index(){
        return "index";
    }
}
