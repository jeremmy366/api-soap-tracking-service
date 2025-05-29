Proyecto creado por Johanna Nathaly Moncayo Valdiviezo y Jeremmy Daniel Varela Ronquillo
# SOAP Tracking Service

Servicio web SOAP para consulta de estado de paquetes, implementado en Java 21, Spring Boot 3.5.0, Spring Web Services y PostgreSQL.

## 📂 Estructura del proyecto

Directory structure:
└── jeremmy366-api-soap-tracking-service/
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── soap/
    │   │   │       ├── SoapApplication.java
    │   │   │       ├── WebServiceConfig.java
    │   │   │       ├── endpoint/
    │   │   │       │   └── TrackingEndpoint.java
    │   │   │       ├── entity/
    │   │   │       │   ├── PackageEntity.java
    │   │   │       │   └── TrackingEventEntity.java
    │   │   │       ├── repository/
    │   │   │       │   └── PackageRepository.java
    │   │   │       └── service/
    │   │   │           ├── InMemoryTrackingService.java
    │   │   │           ├── JpaTrackingService.java
    │   │   │           ├── TrackingNotFoundException.java
    │   │   │           └── TrackingService.java
    │   │   └── resources/
    │   │       ├── application.properties
    │   │       └── wsdl/
    │   │           └── tracking.xsd
    │   └── test/
    │       └── java/
    │           └── soap/
    │               └── SoapApplicationTests.java
    └── .mvn/
        └── wrapper/
            └── maven-wrapper.properties


## 🚀 Despliegue

1. **Clonar el repositorio**  
   ```bash
   git clone https://github.com/tu_usuario/soap-tracking-service.git
   cd soap-tracking-service

2. Configurar la base de datos

Crear la BD en PostgreSQL:
CREATE DATABASE trackdb;
Ajustar credenciales en src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/trackdb
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA

3. Compilar y generar código JAXB

.\mvnw.cmd clean compile

4. Ejecutar la aplicación

Sin base de datos: 
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=memory

Con base de datos

.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod

El servicio estará en http://localhost:8080/ws

El WSDL en http://localhost:8080/ws/tracking.wsdl

🧪 Pruebas
1. Consultar tracking válido
Request (SOAP 1.1)
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:tns="http://espe.edu.ec/soap">
  <soapenv:Header/>
  <soapenv:Body>
    <tns:getTrackingRequest>
      <tns:trackingId>PE1234567890</tns:trackingId>
    </tns:getTrackingRequest>
  </soapenv:Body>
</soapenv:Envelope>
Response
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:tns="http://espe.edu.ec/soap">
  <soapenv:Body>
    <tns:getTrackingResponse>
      <tns:status>Entregado</tns:status>
      <tns:currentLocation>Lima</tns:currentLocation>
      <tns:estimatedDeliveryDate>2025-04-15</tns:estimatedDeliveryDate>
      <tns:history>
        <tns:date>2025-04-05T00:00:00</tns:date>
        <tns:description>Paquete recibido en bodega central</tns:description>
        <tns:location>Lima</tns:location>
      </tns:history>
      <!-- ... -->
    </tns:getTrackingResponse>
  </soapenv:Body>
</soapenv:Envelope>

2. Consultar tracking inválido
Cambia trackingId por uno no existente.

Deberías recibir un SOAP Fault con faultcode CLIENT y mensaje descriptivo.

📖 Documentación adicional
El esquema XSD completo está en src/main/resources/wsdl/tracking.xsd.

Para generar las clases JAXB: revisa la configuración del plugin en pom.xml.

La configuración de Spring-WS en WebServiceConfig.java.
