package za.co.discovery.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Vertex;
import za.co.discovery.services.FileReadingService;
import za.co.discovery.services.GraphService;
import za.co.discovery.services.ShortestPathService;

import javax.annotation.PostConstruct;

@Component
public class PathRepository {

    private GraphService graphService;
    private FileReadingService fileReadingService;
    private ShortestPathService shortestPathService;
    private Graph graph;

    @Autowired
    public PathRepository(FileReadingService fileReadingService, ShortestPathService shortestPathService, GraphService graphService) {
        this.graphService = graphService;
        this.fileReadingService = fileReadingService;
        this.shortestPathService = shortestPathService;
    }

    @PostConstruct
    public void initData() {

        graph = graphService.createNewGraph();
        fileReadingService.readPlanetSheet(graph);
        fileReadingService.readRouteAndTrafficSheets(graph);

    }

    public Vertex findVertex(String vertexID) {
        Assert.notNull(vertexID);
        return graph.getVertexById(vertexID);
    }
}
