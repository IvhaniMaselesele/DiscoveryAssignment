package za.co.discovery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.discovery.Models.*;
import za.co.discovery.services.*;

import java.util.LinkedList;
import java.util.UUID;

@Controller
public class ShortestPathController {
    private final GraphService graphService;
    FileReadingService fileReadingService;
    ShortestPathService shortestPathService;
    int roundNumber;
    LinkedList<Vertex> path = null;
    Graph graph = null;
    private String stringPath;
    private boolean withTraffic = false;
    private PlanetService planetService;
    private RouteService routeService;

    @Autowired
    public ShortestPathController(FileReadingService fileReadingService, ShortestPathService shortestPathService, GraphService graphService, PlanetService planetService, RouteService routeService) {
        this.routeService = routeService;
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

    @RequestMapping(value = "/deletePlanet/{vertexToDeleteId}")
    public String deletePlanet(@PathVariable String vertexToDeleteId, Model model) {
        Vertex vertexById = graph.getVertexById(vertexToDeleteId);
        String planetNode = vertexById.getId();
        planetService.deletePlanet(planetNode);
        planetService.deleteVertex(graph, vertexById);
        model.addAttribute("planets", graph.getVertexes());
        return "planetsList";
    }

    @RequestMapping("/addPlanetPage")
    public String addPlanetGet(Model model) {
        model.addAttribute("newVertex", new Vertex());
        return "createPlanet";
    }

    @RequestMapping(value = "addPlanetPageSubmit", method = RequestMethod.POST)
    public String addPlanetPageSubmit(@ModelAttribute(value = "newVertex") Vertex vertex, Model model) {
        //TODO : only use planets when interacting with planetDAO or planetService otherwise use vertex
        String vertexId = UUID.randomUUID().toString();
        vertex.setId(vertexId);
        Planet newPlanet = planetService.createNewPlanet(vertexId, vertex.getName());
        planetService.persistPlanet(newPlanet);
        planetService.addPlanetToGraphAsVertex(graph, newPlanet);

        model.addAttribute("planets", graph.getVertexes());
        //TODO SHow old name against new name : flash attributes
        return "planetsList";
    }

    @RequestMapping("/routesList")
    public String getRouteList(Model model) {
        model.addAttribute("routes", graph.getEdges());
        return "routesList";
    }

    @RequestMapping("/editRoutePage/{edge}")
    public String editRoutePage(@PathVariable String edge, Model model) {
        Edge edgeById = graph.getEdgeById(edge);
        model.addAttribute("edge", edgeById);
        model.addAttribute("planets", graph.getVertexes());
        return "editRoute";
    }

    @RequestMapping(value = "editRoutePageSubmit", method = RequestMethod.POST)
    public String editRoutePageSubmit(@ModelAttribute(value = "edge") Edge edge, Model model) {
        Edge retrievedEdge = graph.getEdgeById(edge.getEdgeId());
        int index = graph.getEdges().indexOf(retrievedEdge);
        edge = routeService.setSourceAndDestinationById(graph, edge);
        graph.getEdges().set(index, edge);
        routeService.updateRouteFromEdge(edge);
        model.addAttribute("routes", graph.getEdges());
        model.addAttribute("planets", graph.getVertexes());
        return "routesList";
    }

    @RequestMapping("/addRoutePage")
    public String addRoutePage(Model model) {
        model.addAttribute("newEdge", new Edge());
        Edge edge = new Edge();
        edge.setDestinationId("5");
        model.addAttribute("edge", edge);

        model.addAttribute("planets", graph.getVertexes());
        return "createRoute";
    }

    @RequestMapping(value = "addRoutePageSubmit", method = RequestMethod.POST)
    public String addRoutePageSubmit(@ModelAttribute(value = "edge") Edge edge, Model model) {
        String edgeId = UUID.randomUUID().toString();
        edge.setEdgeId(edgeId);
        edge = routeService.setSourceAndDestinationById(graph, edge);
        graph.addEdge(edge);
        Route route = routeService.retrieveRouteFromEdge(edge);
        routeService.persistRoute(route);
        model.addAttribute("routes", graph.getEdges());
        model.addAttribute("planets", graph.getVertexes());
        return "routesList";
    }

    @RequestMapping("/deleteRoutePage/{edge}")
    public String deleteRoutePage(@PathVariable String edge, Model model) {
        Edge edgeById = graph.getEdgeById(edge);
        model.addAttribute("edge", edgeById);
        model.addAttribute("planets", graph.getVertexes());
        return "deleteRoute";
    }


    @RequestMapping(value = "/deleteRoute/{edgeToDeleteId}")
    public String deleteRoute(@PathVariable String edgeToDeleteId, Model model) {
        Edge edgeById = graph.getEdgeById(edgeToDeleteId);
        String routeId = edgeToDeleteId;
        routeService.deleteEdge(graph, edgeById);
        routeService.deleteRoute(routeId);

        model.addAttribute("routes", graph.getEdges());
        return "routesList";
    }

    @RequestMapping(value = "/getPlanets")
    private ModelAndView getPlanets() {
        ModelAndView mav = new ModelAndView("planets");

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
        System.out.println(withTraffic);
        System.out.println();
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.setWithTraffic(withTraffic);
        dijkstra.execute(graph.getVertexes().get(0));
        path = dijkstra.getPath(graph.getVertexById(planet));
        stringPath = shortestPathService.convertPathToString(path);
        roundNumber++;
    }

    @RequestMapping(
            value = "withTraffic",
            method = RequestMethod.GET)
    @ResponseBody
    public void withTraffic() {
        withTraffic = true;
        System.out.println();
    }

}
