package es.udc.ws.bikes.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapOutOfDateException",
    targetNamespace="http://soap.ws.udc.es/"
)

public class SoapOutOfDateException extends Exception {
	
	   public SoapOutOfDateException(String message) {
	        super(message);
	    }
	    
	    public String getFaultInfo() {
	        return getMessage();
	    }   

}
