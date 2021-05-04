package io.openliberty.sample.jakarta.model;

public class Alert {
	
	private String msg; 
	
	public Alert(String msg) {
		this.msg = msg;
	}
	
	public String sendAlert() {
		return this.msg;
	}

}
