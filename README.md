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

**MicroProfile Metrics 3.0: Metrics on processing time**
8. Navigate to the `server.xml`. Turn off authentication for `mpMetrics`:
```xml
<mpMetrics authentication="false"/>
```

9. Access the http://localhost:9080/metrics endpoint to see the metrics already being collected by default. 

10. Navigate to the `InventoryResource` class. Add a `org.eclipse.microprofile.metrics.annotation.Timed` annotation to the `listInventory` method to collect metrics on the time needed to list the inventory.  Notice these metrics have been added at the http://localhost:9080/metrics/application endpoint. 
```java
@Timed(name = "inventoryProcessingTime", tags = {
    "method=get" }, absolute = true, description = "Time needed to process the inventory")
```
