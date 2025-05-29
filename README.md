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
    </tns:getTrackingResponse>
  </soapenv:Body>
</soapenv:Envelope>
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

