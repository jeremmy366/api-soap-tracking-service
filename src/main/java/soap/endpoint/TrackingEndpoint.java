package soap.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import soap.service.TrackingService;
import soap.wsdl.GetTrackingRequest;
import soap.wsdl.GetTrackingResponse;

@Endpoint
public class TrackingEndpoint {
    private static final String NAMESPACE_URI = "http://espe.edu.ec/soap";

    private final TrackingService trackingService;

    @Autowired
    public TrackingEndpoint(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getTrackingRequest")
    @ResponsePayload
    public GetTrackingResponse getTracking(@RequestPayload GetTrackingRequest request) {
        // Si no existe el ID, trackingService lanzará TrackingNotFoundException.
        return trackingService.fetchById(request.getTrackingId());
    }
}
