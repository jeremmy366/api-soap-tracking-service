package soap.service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import soap.entity.PackageEntity;
import soap.repository.PackageRepository;
import soap.wsdl.GetTrackingResponse;
import soap.wsdl.TrackingEvent;

@Service
@Profile("prod")          // sólo se registra con el perfil prod
@RequiredArgsConstructor
public class JpaTrackingService implements TrackingService {
    private final PackageRepository repo;

    @Override
    @Transactional(readOnly = true)   //mantiene la sesión abierta
    public GetTrackingResponse fetchById(String trackingId) {

        PackageEntity pkg = repo.findById(trackingId)
                .orElseThrow(() ->
                        new TrackingNotFoundException("No existe paquete con ID: " + trackingId));

        GetTrackingResponse resp = new GetTrackingResponse();
        resp.setStatus(pkg.getStatus());
        resp.setCurrentLocation(pkg.getCurrentLocation());
        resp.setEstimatedDeliveryDate(toXmlDate(pkg.getEstimatedDeliveryDate()));

        pkg.getHistory().forEach(ev -> {
            TrackingEvent t = new TrackingEvent();
            t.setDate(toXmlDateTime(ev.getDate()));
            t.setDescription(ev.getDescription());
            t.setLocation(ev.getLocation());
            resp.getHistory().add(t);
        });
        return resp;
    }

    /* ---------- helpers de conversión ---------- */

    private static XMLGregorianCalendar toXmlDate(LocalDate date) {
        if (date == null) return null;
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
        if (dt == null) return null;
        try {
            var gc = GregorianCalendar.from(dt.atZone(ZoneId.systemDefault()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
