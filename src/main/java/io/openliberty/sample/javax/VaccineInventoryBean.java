package io.openliberty.sample.javax;

import javax.enterprise.context.*;
import java.util.*;
import javax.inject.Named;
import io.openliberty.sample.javax.model.Vaccine;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@ApplicationScoped
@Named("vaccine")
public class VaccineInventoryBean {
	private List<Vaccine> inventory = Collections.synchronizedList(new ArrayList<>());

	public void addProduct(@NotNull String name, int numDoses, @Positive int costPerThousandUnits) {
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
