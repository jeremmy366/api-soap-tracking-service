package soap.service;

import soap.wsdl.GetTrackingResponse;

public interface TrackingService {
    /**
     * Busca el seguimiento y devuelve la respuesta completa.
     * @param trackingId número de tracking
     * @return GetTrackingResponse con datos
     * @throws TrackingNotFoundException si no existe el ID
     */
    GetTrackingResponse fetchById(String trackingId);
}
