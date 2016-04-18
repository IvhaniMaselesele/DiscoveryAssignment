package za.co.discovery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.discovery.Models.Edge;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Planet;
import za.co.discovery.Models.Vertex;
import za.co.discovery.services.*;

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
    private PlanetService planetService;

    @Autowired
    public ShortestPathController(FileReadingService fileReadingService, ShortestPathService shortestPathService, GraphService graphService, PlanetService planetService) {
        roundNumber = 0;
        this.planetService = planetService;
        this.graphService = graphService;
        this.fileReadingService = fileReadingService;
        this.shortestPathService = shortestPathService;
        graph = graphService.createNewGraph();
        this.fileReadingService.readPlanetSheet(graph);
        this.fileReadingService.readRouteAndTrafficSheets(graph);

    }

    @RequestMapping("/")
    public String index() {

        return "index";
    }


    @RequestMapping("/planetsList")
    public ModelAndView getPlanetsList() {
        ModelAndView mav = new ModelAndView("planetsList");
        mav.addObject("planets", graph.getVertexes());
        return mav;
    }

    @RequestMapping("/editPlanetPage/{vertexToEditId}")
    public String editPlanetGet(@PathVariable String vertexToEditId, Model model) {
        Vertex vertexById = graph.getVertexById(vertexToEditId);
        model.addAttribute("vertexToEdit", vertexById);
        return "editPlanet";
    }

    @RequestMapping(value = "editPlanetPageSubmit", method = RequestMethod.POST)
    public String editPlanetPageSubmit(@ModelAttribute(value = "vertexToEdit") Vertex vertex, Model model) {
        //TODO : only use planets when interacting with planetDAO or planetService otherwise use vertex
        Planet updatedPlanet = planetService.createNewPlanet(vertex.getId(), vertex.getName());
        planetService.persistPlanet(updatedPlanet);
        planetService.updateVertex(graph, updatedPlanet);

        model.addAttribute("planets", graph.getVertexes());
        //TODO SHow old name against new name : flash attributes
        return "planetsList";

    }

    @RequestMapping("/routesList")
    public ModelAndView getRouteList() {
        ModelAndView mav = new ModelAndView("routesList");
        mav.addObject("routes", graph.getEdges());
        return mav;
    }

    @RequestMapping(value = "/editRoutePage/{route}",
            method = RequestMethod.GET)
    public String editRoutePage(@PathVariable String route, Model model) {
        System.out.println("\n\n\n\n" + route + "\n\n\n\n\n");
        Edge edge = graph.getEdgeById(route);
        model.addAttribute("edgeToEdit", edge);
        model.addAttribute("planets", graph.getVertexes());
        return "editRoute";
    }



    @RequestMapping(value = "/getPlanets")
    private ModelAndView getPlanets() {
        ModelAndView mav = new ModelAndView("planets");
        //graph = new Graph();
        //graph = graphService.createNewGraph();
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
