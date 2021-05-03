package io.openliberty.sample.javax.finish;

import javax.enterprise.context.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import io.openliberty.sample.javax.finish.model.Vaccine;

import javax.enterprise.context.*;

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
