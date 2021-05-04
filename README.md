# Vaccination Demo App - Open Liberty and MicroProfile 4.0

Demo project using MicroProfile 4.0 and Jakarta EE 8 specifications with Open Liberty and dev mode for containers. 

To try out the application:
1. Start dev mode in a container: mvn liberty:devc
2. Make a curl request to add a new vaccine to the inventory `curl -X POST http://localhost:9080/Pfizer/5/6000`
3. Notice the added entry by visiting the `/inventory` endpoint at: http://localhost:9080/inventory
4. Notice the Vaccine Alert endpoints provided by the CDI producer methods at: http://localhost:9080/firstDose or http://localhost:9080/secondDose


MicroProfile Health 3.0
1. Visit the MicroProfile health enpoint at http://localhost:9080/health to see the corresponding liveness and readiness checks

MicroProfile Metrics 3.0
1. Visit the MicroProfile application metrics at https://localhost:9443/metrics/application