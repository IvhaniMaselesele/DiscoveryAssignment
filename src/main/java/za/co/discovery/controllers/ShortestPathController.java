package za.co.discovery.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShortestPathController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
