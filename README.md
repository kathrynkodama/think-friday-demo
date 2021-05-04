# Vaccination Demo App - Open Liberty and Jakarta EE 9

Demo project using Jakarta EE 9 specifications with Open Liberty.

### Demo Scenario
Start from the `start-jakartaee` branch.

1. Notice Open Liberty beta configured through the Liberty Maven Plugin in the `pom.xml`. Notice that the Jakarta EE 9.0 APIs have been added to the `pom.xml` as well.

2. Start application with Open Liberty via dev mode: `mvn liberty:dev`. The application should be available at http://localhost:9080/,

3. Navigate to the `server.xml`. Add the `jakartaee-9.0` feature via the `featureManager` block. Notice that dev mode installs all the features included with the `jakartaee-9.0` umbrella feature.
```xml
<featureManager>
    <feature>jakartaee-9.0</feature>
</featureManager>
```

**Jakarta EE RESTful Web Services: Making a GET request**

4. Notice the `hello` method in the `InventoryResource` class. Visit the endpoint here at http://localhost:9080/hello. Try changing the "Hello" string returned. Notice that dev mode recompiles your source code and your changes are reflected at the `/hello` endpoint. 

5. Notice the `Vaccine` class and that it takes 3 parameters: vaccine type, number of doses, and cost per thousand units.

**Jakarta EE CDI: Creating and injecting the Vaccine Inventory bean**

<details>
    <summary>6. Create the `VaccineInventoryBean.java` class to create a [ManagedBean](https://jakarta.ee/specifications/managedbeans/2.0/jakarta-managed-beans-spec-2.0#what-are-managed-beans). </summary>

```java
package io.openliberty.sample.jakarta;

import jakarta.enterprise.context.*;
import java.util.*;
import io.openliberty.sample.jakarta.model.Vaccine;
import jakarta.inject.Named;

@ApplicationScoped
@Named("vaccine")
public class VaccineInventoryBean {
	private List<Vaccine> inventory = Collections.synchronizedList(new ArrayList<>());

	public void addProduct(String name, int numDoses, int costPerThousandUnits) {
		Optional<Vaccine> target = inventory.stream().filter(v -> v.getVaccineType().equals(name)).findAny();

		if (target.isPresent()) {
			// modify existing vaccine
			Vaccine curVaccine = target.get();
			curVaccine.setCostPerThousandUnits(costPerThousandUnits);
			curVaccine.setNumDoses(curVaccine.getNumDoses() + numDoses);
		} else {
			// create new vaccine
			Vaccine vaccine = new Vaccine();
			vaccine.setVaccineType(name);
			vaccine.setNumDoses(numDoses);
			vaccine.setCostPerThousandUnits(costPerThousandUnits);
			inventory.add(vaccine);
		}
	}

	@Override
	public String toString() {
		return inventory.toString();
	}
	
	public List<Vaccine> listAll() {
		return inventory;
	}
}
```
</details>

7. Inject `VaccineInventoryBean` into `InventoryResource` class
```java
@Inject
private VaccineInventoryBean vaccineInventory;
```

8. Create a `listInventory` method
```java
@GET
@Path("/inventory")
@jakarta.ws.rs.Produces(MediaType.APPLICATION_JSON)
public List<Vaccine> listInventory() {
	return vaccineInventory.listAll();
}
```

9. Create an `addVaccine` method. Send a curl request to add a new vaccine to our inventory: `curl -X POST http://localhost:9080/Pfizer/5/6000`
```java
@POST
@Path("/{vaccineType}/{numDoses}/{costPerThousandUnits}")
public void addVaccine(@PathParam("vaccineType") String vaccineType, @PathParam("numDoses") int numDoses,@PathParam("costPerThousandUnits") int costPerThousandUnits) {
	System.out.println("ADDING VACCINE - VaccineType: " + vaccineType 
				+ "; numDoses: " + numDoses 
				+ "; costPerThousandUnits: " + costPerThousandUnits);

	vaccineInventory.addProduct(vaccineType, numDoses, costPerThousandUnits);
}
```
**Jakarta EE Bean Validation: Enforcing bean validation constraints**

10. Notice that as the application stands right now, you can send a POST request with a negative cost value: `curl -X POST http://localhost:9080/Moderna/5/-5000` and the application does not raise any errors. 

11.  Navigate to the `VaccineInventoryResource` class. Add the `jakarta.validation.constraints.Positive` annotation to the `costPerThousandUnits` field in the `addProduct` method. Optionally, enforce a `NotNull` on the name field as well.
```java
public void addProduct(@NotNull String name, int numDoses, @Positive int costPerThousandUnits) {
    ...
}
```
12. Notice now that when an invalid POST request is made, ie. `curl -X POST http://localhost:9080/Moderna/5/-5000`, a warning appears in the console running dev mode:
```
[WARNING ] addProduct.numDoses: must be greater than 0
```
The invalid entry will not be added to our inventory list. 