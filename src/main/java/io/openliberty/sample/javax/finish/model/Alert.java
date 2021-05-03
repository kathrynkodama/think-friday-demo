package io.openliberty.sample.javax.finish.model;

public class Alert {
	
	private String msg; 
	
	public Alert(String msg) {
		this.msg = msg;
	}
	
	public String sendAlert() {
		return this.msg;
	}

}
