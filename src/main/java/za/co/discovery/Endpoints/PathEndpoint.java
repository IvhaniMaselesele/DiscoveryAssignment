package za.co.discovery.Endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import za.co.discovery.Assignment.GetPathRequest;
import za.co.discovery.Assignment.GetPathResponse;
import za.co.discovery.repository.PathRepository;

@Endpoint
public class PathEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private PathRepository pathRepository;

    @Autowired
    public PathEndpoint(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPathRequest")
    @ResponsePayload
    public GetPathResponse getCountry(@RequestPayload GetPathRequest request) {
        GetPathResponse response = new GetPathResponse();
        response.setPath(pathRepository.findPath(request.getName()));

        return response;
    }

}
