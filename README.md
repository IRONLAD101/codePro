International Space Station Location Service

Overview
The International Space Station Location Service is a REST API that provides the current latitude and longitude of the International Space Station (ISS). The service retrieves the location data from a public API and handles scenarios where the API is down or unreachable.

Technology Stack
The service is built using the following technologies:
•	Java 1.8
•	Spring Boot 3.0.4
•	Maven 3.0.1
•	JUnit 4.13.2
•	Mockito 4.8.1

Prerequisites
To run this project, you need to have the following software installed:
•	Java 8 or later
•	Apache Maven 3.6 or later

Building and Running the Service
To build and run the service, follow these steps:
1.	Clone the repository from GitHub:
git clone https://github.com/IRONLAD101/codePro.git
2.	Build the project using Maven:
cd ISS_Location
mvn clean package
3.	Run the service using the Spring Boot Maven plugin:
 	mvn spring-boot:run
4.	Access the service at http://localhost:8080/iss/location

API Documentation
The service provides a single API endpoint:
Request:
[GET] /location/currentISSLocation
Request Parameters:
The API does not accept any request parameters.
Response:
The API returns a JSON object with the current latitude and longitude of the ISS. The response object has the following format:
{
    "latitude": 12.087589740271,
    "longitude": 150.21953552525
}
The latitude and longitude fields are floating-point numbers representing the latitude and longitude of the ISS.
If the API is down or unreachable, the response object will have the following values:
{
  "latitude": 0.0,
  "longitude": 0.0
}
If the service encounters an unexpected error, it will return a 500 Internal Server Error response.

Error Handling
The service handles the following error scenarios:
If the public API is down or unreachable, the service will retry the API call once. If the retry succeeds, the service will return the current ISS location. If the retry fails, the service will check if it has a last known location for the ISS. If it does, it will return the last known location. If it doesn't, it will return (0.0, 0.0).
If the service encounters an unexpected error, it will return a (0.0,0.0).

Logging
The service uses the log4j2 logging API to log messages. The log level can be configured using the logging.level property in the log4j2.xml file.

Testing
The project includes unit tests for the controller and service classes, with a minimum test coverage of 75%. The tests use Mockito to mock the external API and test the different scenarios. To run the tests, use the following command:
mvn test
