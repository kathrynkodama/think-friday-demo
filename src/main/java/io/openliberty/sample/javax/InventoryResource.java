package io.openliberty.sample.javax;


import java.util.*;
import javax.enterprise.context.*;
import javax.enterprise.inject.Produces;
import javax.inject.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.Timed;

import io.openliberty.sample.javax.model.Alert;
import io.openliberty.sample.javax.model.Vaccine;

@Path("")
@ApplicationScoped
public class InventoryResource {

	@Inject
	private VaccineInventoryBean vaccineInventory;
	
	@Inject @Named("firstDose")
	private Alert firstDose;
	
	@Inject @Named("secondDose")
	private Alert secondDose;
	
	@GET
	@Path("/inventory")
	@Timed(name = "inventoryProcessingTime", tags = {
			"method=get" }, absolute = true, description = "Time needed to process the inventory")
	@javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
	public List<Vaccine> listInventory() {
		return vaccineInventory.listAll();
	}

	@POST
	@Path("/{vaccineType}/{numDoses}/{costPerThousandUnits}")
	public void addVaccine(@PathParam("vaccineType") String vaccineType, @PathParam("numDoses") int numDoses,
			@PathParam("costPerThousandUnits") int costPerThousandUnits) {
		System.out.println("ADDING VACCINE - VaccineType: " + vaccineType 
				+ "; numDoses: " + numDoses 
				+ "; costPerThousandUnits: " + costPerThousandUnits);

		vaccineInventory.addProduct(vaccineType, numDoses, costPerThousandUnits);
	}
	
	// Producer methods 
	
	@Produces @Named("firstDose")
	public static Alert firstDose() {
		return new Alert("Time for your first dose!");
	}
	
	@Produces @Named ("secondDose")
	public static Alert secondDose() {
		return new Alert("Time for your second dose!");
	}
	
	@GET
	@Path("/firstDose")
	public String getFirstDoseMsg() {
		return firstDose.sendAlert();
	}

	@GET
	@Path("/secondDose")
	public String getSecondDoseMsg() {
		return secondDose.sendAlert();
	}


}