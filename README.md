Overview :
This Kotlin-based application is a Spring Boot microservice for managing delivery records. It includes REST endpoints and uses an H2 database for data storage. The application is containerized using Docker for easy deployment

Prerequisites :
Docker and Docker Compose installed.
 Git installed. 
Java 11 or higher installed locally (for local development).
 Maven for building the application.

Deployment Steps :
Extract the Files Unzip the provided archive to your desired directory. Navigate to the project folder.
Start Docker Containers		
Run the following command in the project directory:
docker-compose up --build
This will build and start the application and the H2 database containers.

Access the Application
REST API: Accessible at http://localhost:8080.
H2 Console: Access at http://localhost:8080/h2-console.


Using the H2 Database Console
Enter the following details to connect:
JDBC URL:
Username: sa
Password: password


Usage REST API Endpoints :


Create Delivery POST /deliveries curl -X POST http://localhost:8080/deliveries -H "Content-Type: application/json" -d '{ "createdAt": "2024-11-29T12:00:00", "finishedAt": null, "status": "IN_PROGRESS", "description": "Sample delivery description" }'

	Update Delivery
		PATCH /deliveries/{id}
curl -X PATCH http://localhost:8080/deliveries/b65bd0de-a222-4e8f-affc-98eb09f71dcc -H "Content-Type: application/json" -d '{ "finishedAt": "2024-11-29T14:00:00", "status": "DELIVERED" }'

	Update multiple Deliveries
		PATCH /deliveries/bulk-update

	View Business Summary
		GET /deliveries/business-summary

Stopping the Application To stop the application, run: docker-compose down