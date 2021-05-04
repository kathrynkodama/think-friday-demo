# Vaccination Demo App - Open Liberty and Jakarta EE 9

Demo project using Jakarta EE 9 specifications with Open Liberty.

To try out the application:
1. Start dev mode: `mvn liberty:dev`
2. Make a curl request to add a new vaccine to the inventory `curl -X POST http://localhost:9080/Pfizer/5/6000`
3. Notice the added entry by visiting the `/inventory` endpoint at: http://localhost:9080/inventory
4. Notice the Vaccine Alert endpoints provided by the CDI producer methods at: http://localhost:9080/firstDose or http://localhost:9080/secondDose