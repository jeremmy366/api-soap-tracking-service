***📦 SOAP Tracking Service***

Proyecto creado por Johanna Nathaly Moncayo Valdiviezo y Jeremmy Daniel Varela Ronquillo

Servicio web SOAP para consulta del estado de paquetes, implementado en Java 21, Spring Boot 3.5.0, Spring Web Services y PostgreSQL.

***📂 Estructura del Proyecto***
```bash
api-soap-tracking-service/
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
```
**🚀 Despliegue**
1. Clonar el repositorio
```bash
git clone https://github.com/tu_usuario/soap-tracking-service.git
cd soap-tracking-service
```
2. Configurar la base de datos

2.1. Crear la base de datos en PostgreSQL:
```bash
CREATE DATABASE trackdb;
```
2.2. Editar src/main/resources/application.properties con tus credenciales:
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/trackdb
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```
3. Compilar y generar código JAXB
```bash
./mvnw clean compile
```
4. Ejecutar la aplicación

Con datos en memoria (sin PostgreSQL):
```bash
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=memory"
```
Con base de datos (modo producción):
```bash
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=prod"
```
El servicio estará disponible en:
http://localhost:8080/ws

El archivo WSDL estará en:
http://localhost:8080/ws/tracking.wsdl

🧪 Pruebas
1. Consultar tracking válido

Request (SOAP 1.1):
```bash
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:tns="http://espe.edu.ec/soap">
  <soapenv:Header/>
  <soapenv:Body>
    <tns:getTrackingRequest>
      <tns:trackingId>PE1234567890</tns:trackingId>
    </tns:getTrackingRequest>
  </soapenv:Body>
</soapenv:Envelope>
```
Response:
```bash
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:getTrackingResponse xmlns:ns2="http://espe.edu.ec/soap">
            <ns2:status>Entregado</ns2:status>
            <ns2:currentLocation>Arequipa</ns2:currentLocation>
            <ns2:estimatedDeliveryDate>2025-04-15-05:00</ns2:estimatedDeliveryDate>
            <ns2:history>
                <ns2:date>2025-04-05T10:00:00.000-05:00</ns2:date>
                <ns2:description>Paquete recibido en bodega central</ns2:description>
                <ns2:location>Lima</ns2:location>
            </ns2:history>
            <ns2:history>
                <ns2:date>2025-04-07T08:30:00.000-05:00</ns2:date>
                <ns2:description>Salida hacia Lima</ns2:description>
                <ns2:location>Arequipa</ns2:location>
            </ns2:history>
        </ns2:getTrackingResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
2. Consultar tracking inválido

Utiliza un trackingId inexistente.
Deberías recibir una respuesta SOAP Fault con:

faultcode: CLIENT

Mensaje descriptivo del error.

📖 Documentación Adicional

El esquema XSD se encuentra en:
src/main/resources/wsdl/tracking.xsd

La generación de clases JAXB está configurada en el pom.xml.

La configuración principal del servicio SOAP está en WebServiceConfig.java

