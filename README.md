# Vaccination Demo App - Open Liberty and MicroProfile 4.0

Demo project using MicroProfile 4.0 and Jakarta EE 8 specifications with Open Liberty and dev mode for containers. 

### Demo Scenario
Start from the `start-mp` branch.

1. Notice Open Liberty beta configured through the Liberty Maven Plugin in the `pom.xml`. Notice that the MP 4.0 APIs (along with the Jakarta EE 8 APIs) have been added to the `pom.xml` as well. 

2. Notice the DockerFile. It is using the latest Open Liberty beta image. 

3. Start application with Open Liberty via dev mode for containers: `mvn liberty:devc. The application should be available at http://localhost:9080/. 

4. Send a curl request to add a vaccine to the inventory service: `curl -X POST http://localhost:9080/Pfizer/5/6000`. Notice that the inventory service has been updated at http://localhost:9080/inventory.

5. Navigate to the `server.xml`. Add the `microprofile-4.0` feature via the `featureManager` block. Notice that dev mode for containers installs all the features included with the `microprofile-4.0` umbrella feature.
```xml
<featureManager>
    <!-- Specify Open Liberty features --> 
    <feature>jakartaee-8.0</feature>
    <feature>microprofile-4.0</feature>
</featureManager>
```

**MicroProfile Health 3.0: Readiness and Liveness Check**

<details>
    <summary>6. Create the `InventoryReadinessCheck.java` class to implement a MicroProfile Health Readiness Check. If you have the Tools for MicroProfile by Red Hat extension installed in VS Code, you can use the `mpreadiness` snippet to create this class. </summary>

```java
package io.openliberty.sample.javax;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class InventoryReadinessCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named(InventoryReadinessCheck.class.getSimpleName()).withData("ready", true).up()
                .build();
    }
}
```
</details>

<details>
    <summary>7. Create the `InventoryLivenessCheck.java` class to implement a MicroProfile Health Liveness Check. If you have the Tools for MicroProfile by Red Hat extension installed in VS Code, you can use the `mpliveness` snippet to create this class. </summary>

```java
package io.openliberty.sample.javax;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class InventoryLivenessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        long memUsed = memBean.getHeapMemoryUsage().getUsed();
        long memMax = memBean.getHeapMemoryUsage().getMax();

        return HealthCheckResponse.named(InventoryResource.class.getSimpleName() + " Liveness Check")
                .withData("memory used", memUsed).withData("memory max", memMax).status(memUsed < memMax * 0.9).build();
    }
}
```
</details>

**MicroProfile OpenAPI 2.0: Documenting RESTful APIs**
8. View the OpenAPI endpoint at http://localhost:9080/openapi

9. Navigate to the `InventoryResource` class. Add OpenAPI parameters to the `listInventory` method. See the corresponding endpoint updated: http://localhost:9080/openapi.
```java
	@GET
	@Path("/inventory")
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(
        responseCode = "200",
        description = "Vaccine details stored in the inventory.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                type = SchemaType.OBJECT,
                implementation = VaccineInventoryBean.class)))
	@Operation(
		summary = "List inventory contents",
		description = "Returns the currently stored vaccines stored in the inventory"
	)
	public List<Vaccine> listInventory() {
		return vaccineInventory.listAll();
	}
```
10. Add OpenAPI parameters to the `addVaccine` method.
```java
	@POST
	@Path("/{vaccineType}/{numDoses}/{costPerThousandUnits}")
	@Operation(summary = "Add vaccine to inventory", description = "Adds a new vaccine type to the inventory")
	public void addVaccine(
			@Parameter(description = "The type of vaccine to be added", required = true, example = "Pfizer", schema = @Schema(type = SchemaType.STRING)) @PathParam("vaccineType") String vaccineType,
			@Parameter(description = "Number of doses", required = true, example = "5", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("numDoses") int numDoses,
			@Parameter(description = "The cost of the vaccine per thousand units", required = true, example = "5000", schema = @Schema(type = SchemaType.INTEGER)) @PathParam("costPerThousandUnits") int costPerThousandUnits) {
		System.out.println("ADDING VACCINE - VaccineType: " + vaccineType + "; numDoses: " + numDoses
				+ "; costPerThousandUnits: " + costPerThousandUnits);

		vaccineInventory.addProduct(vaccineType, numDoses, costPerThousandUnits);
	}
```

