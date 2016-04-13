package za.co.discovery.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import za.co.discovery.Assignment.Path;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Vertex;
import za.co.discovery.services.DijkstraAlgorithm;
import za.co.discovery.services.FileReadingService;
import za.co.discovery.services.GraphService;
import za.co.discovery.services.ShortestPathService;

import javax.annotation.PostConstruct;
import java.util.LinkedList;

@Component
public class PathRepository {

    private GraphService graphService;
    private FileReadingService fileReadingService;
    private ShortestPathService shortestPathService;
    private Graph graph;
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager platformTransactionManager;


    @Autowired
    public PathRepository(FileReadingService fileReadingService, ShortestPathService shortestPathService, GraphService graphService) {
        this.graphService = graphService;
        this.fileReadingService = fileReadingService;
        this.shortestPathService = shortestPathService;
    }

    @PostConstruct
    public void initData() {
        TransactionTemplate tpl = new TransactionTemplate(platformTransactionManager);
        tpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                graph = graphService.createNewGraph();
                fileReadingService.readPlanetSheet(graph);
                fileReadingService.readRouteAndTrafficSheets(graph);
            }
        });


    }

    public Path findPath(String vertexID) {
        Assert.notNull(vertexID);

        Vertex vertex = graph.getVertexById(vertexID);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(graph.getVertexes().get(0));
        LinkedList<Vertex> pathList = dijkstra.getPath(vertex);

        Path path = new Path();
        path.setName("Path from:");
        path.setDestination(vertex.getName());
        path.setOrigin("Earth");
        path.setPathList(shortestPathService.convertPathToString(pathList));
        return path;
    }
}
