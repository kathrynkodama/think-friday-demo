package io.openliberty.sample.jakarta.finish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;

@Path("")
@ApplicationScoped
public class InventoryResource {

	@Inject
	private VaccineInventoryBean vaccineInventory;
	
	@GET
	@Path("/inventory")
	@jakarta.ws.rs.Produces(MediaType.APPLICATION_JSON)
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
	
	@Produces
	public Alert firstDose() {
		return new Alert("Time for your first dose!");
	}
	
	@Produces
	public Alert secondDose() {
		return new Alert("Time for your second dose!");
	}
	
	@GET
	@Path("/firstDose")
	public String getFirstDoseMsg() {
		return firstDose().sendAlert();
	}

	@GET
	@Path("/secondDose")
	public String getSecondDoseMsg() {
		return secondDose().sendAlert();
	}
}