package io.openliberty.sample.jakarta.finish;

public class Vaccine {
	private int numDoses;
	private int costPerThousandUnits;
	private VaccineType type;
	
	private enum VaccineType {Pfizer, Moderna};

	public Vaccine(String type, int numDoses, int costPerThousandUnits) {
		this.type = VaccineType.valueOf(type);
		this.numDoses = numDoses;
		this.costPerThousandUnits = costPerThousandUnits;
	}
	
	public Vaccine() {}

	public int getNumDoses() {
		return numDoses;
	}
	
	public int getCostPerThousandUnits() {
		return costPerThousandUnits;
	}

	public String getVaccineType() {
		return type.toString();
	}
	
	public void setCostPerThousandUnits(int costPerThousandUnits) {
		this.costPerThousandUnits = costPerThousandUnits;
	}

	public void setNumDoses(int numDoses) {
		this.numDoses = numDoses;
	}

	public void setVaccineType(String vaccineType) {
		this.type = VaccineType.valueOf(vaccineType);
	}
	
	@Override
	public String toString() {
		return "vaccine type: " + this.type.toString() + " numdoses: " + numDoses + " cost per thousand units: " + costPerThousandUnits;
	}

}