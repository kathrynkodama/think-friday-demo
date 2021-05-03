package io.openliberty.sample.javax.finish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.Timed;

import io.openliberty.sample.javax.finish.model.Alert;
import io.openliberty.sample.javax.finish.model.Vaccine;

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