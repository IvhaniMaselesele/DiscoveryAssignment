package za.co.discovery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.discovery.services.FileReadingService;

import javax.servlet.http.HttpSession;

@Controller
public class ShortestPathController {
    FileReadingService fileReadingService;

    @Autowired
    public ShortestPathController(FileReadingService fileReadingService) {
        this.fileReadingService = fileReadingService;
    }

    @RequestMapping("/")
    public String index(HttpSession session) {
        return "index";
    }

    @RequestMapping("/round")
    public String shortestPath(HttpSession session) {
        fileReadingService.readPlanetSheet();
        fileReadingService.readRouteAndTrafficSheets();
        return "index";
    }
}
