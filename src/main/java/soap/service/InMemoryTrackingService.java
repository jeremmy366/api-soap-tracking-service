package soap.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import soap.wsdl.GetTrackingResponse;
import soap.wsdl.TrackingEvent;

@Slf4j
@Service
@Profile("memory")   // Actívalo solo con el perfil 'memory'

public class InMemoryTrackingService implements TrackingService {
    /** Mapa de trackingId -> respuesta JAXB */
    private final Map<String, GetTrackingResponse> storage = new ConcurrentHashMap<>();

    public InMemoryTrackingService() {
        // Semilla de datos de ejemplo
        storage.put("PE1234567890", buildSample());
        storage.put("EC9876543210", buildInTransit());
        log.info("InMemoryTrackingService inicializado con {} paquetes de prueba", storage.size());
    }

    @Override
    public GetTrackingResponse fetchById(String trackingId) {
        GetTrackingResponse resp = storage.get(trackingId);
        if (resp == null) {
            throw new TrackingNotFoundException("No existe paquete con ID: " + trackingId);
        }
        return resp;
    }

    /* ---------- helpers ─ construir ejemplos ---------- */

    private GetTrackingResponse buildSample() {
        GetTrackingResponse r = new GetTrackingResponse();
        r.setStatus("Entregado");
        r.setCurrentLocation("Arequipa");
        r.setEstimatedDeliveryDate(toXmlDate(LocalDate.of(2025, 4, 15)));

        r.getHistory().add(buildEvent(
                LocalDateTime.of(2025, 4, 5, 10, 0),
                "Lima",
                "Paquete recibido en bodega central"));

        r.getHistory().add(buildEvent(
                LocalDateTime.of(2025, 4, 7, 8, 30),
                "Arequipa",
                "Salida hacia Lima"));

        return r;
    }

    private GetTrackingResponse buildInTransit() {
        GetTrackingResponse r = new GetTrackingResponse();
        r.setStatus("In Transit");
        r.setCurrentLocation("Guayaquil");
        r.setEstimatedDeliveryDate(toXmlDate(LocalDate.now().plusDays(4)));

        r.getHistory().add(buildEvent(
                LocalDateTime.now().minusDays(1),
                "Quito",
                "Recibido en centro de distribución"));

        r.getHistory().add(buildEvent(
                LocalDateTime.now().minusHours(3),
                "Guayaquil",
                "En ruta hacia destino"));

        return r;
    }

    private TrackingEvent buildEvent(LocalDateTime date,
                                     String location,
                                     String description) {
        TrackingEvent ev = new TrackingEvent();
        ev.setDate(toXmlDateTime(date));
        ev.setLocation(location);
        ev.setDescription(description);
        return ev;
    }

    /* ---------- conversores java.time -> XMLGregorianCalendar ---------- */

    private static XMLGregorianCalendar toXmlDate(LocalDate date) {
        try {
            var gc = GregorianCalendar.from(
                    date.atStartOfDay(ZoneId.systemDefault()));
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendarDate(
                            gc.get(java.util.Calendar.YEAR),
                            gc.get(java.util.Calendar.MONTH) + 1,
                            gc.get(java.util.Calendar.DAY_OF_MONTH),
                            gc.get(java.util.Calendar.ZONE_OFFSET) / 60000);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static XMLGregorianCalendar toXmlDateTime(LocalDateTime dt) {
        try {
            var gc = GregorianCalendar.from(dt.atZone(ZoneId.systemDefault()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
