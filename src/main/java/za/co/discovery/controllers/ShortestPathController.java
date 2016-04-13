package za.co.discovery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Vertex;
import za.co.discovery.services.DijkstraAlgorithm;
import za.co.discovery.services.FileReadingService;
import za.co.discovery.services.GraphService;
import za.co.discovery.services.ShortestPathService;

import java.util.LinkedList;

@Controller
public class ShortestPathController {
    private final GraphService graphService;
    FileReadingService fileReadingService;
    ShortestPathService shortestPathService;
    int roundNumber;
    LinkedList<Vertex> path = null;
    Graph graph = null;
    private String stringPath;

    @Autowired
    public ShortestPathController(FileReadingService fileReadingService, ShortestPathService shortestPathService, GraphService graphService) {
        roundNumber = 0;
        this.graphService = graphService;
        this.fileReadingService = fileReadingService;
        this.shortestPathService = shortestPathService;
    }

    @RequestMapping("/")
    public String index() {
        return "planets";
    }


    @RequestMapping(value = "/getPlanets")
    private ModelAndView getPlanets() {
        ModelAndView mav = new ModelAndView("planets");
        //graph = new Graph();
        graph = graphService.createNewGraph();
        fileReadingService.readPlanetSheet(graph);
        fileReadingService.readRouteAndTrafficSheets(graph);

        if (roundNumber == 0) {
            mav.addObject("roundNumber", "0");
        } else {
            mav.addObject("roundNumber", "!0");
            mav.addObject("pathList", path);
        }
        mav.addObject("mapList", graph.getVertexes());
        return mav;
    }

    @RequestMapping(
            value = "shortestPath/{planet}",
            method = RequestMethod.GET)
    @ResponseBody
    public void getShortestPath(@PathVariable String planet) {
        if (path != null) {
            path.clear();
        }
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(graph.getVertexes().get(0));
        path = dijkstra.getPath(graph.getVertexById(planet));
        stringPath = shortestPathService.convertPathToString(path);
        roundNumber++;
    }


}
