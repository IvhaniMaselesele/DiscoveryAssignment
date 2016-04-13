package za.co.discovery.services;

import org.springframework.stereotype.Service;
import za.co.discovery.Models.Graph;

@Service
public class GraphService {

    public Graph createNewGraph() {
        return new Graph();
    }
}
