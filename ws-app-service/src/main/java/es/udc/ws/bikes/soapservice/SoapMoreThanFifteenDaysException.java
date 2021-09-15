package es.udc.ws.bikes.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapMoreThanFifteenDaysException",
    targetNamespace="http://soap.ws.udc.es/"
)

public class SoapMoreThanFifteenDaysException extends Exception {
	
	 private SoapMoreThanFifteenDaysExceptionInfo faultInfo;  
	    
	    protected SoapMoreThanFifteenDaysException(
	            SoapMoreThanFifteenDaysExceptionInfo faultInfo) {
	        this.faultInfo = faultInfo;
	    }

	    public SoapMoreThanFifteenDaysExceptionInfo getFaultInfo() {
	        return faultInfo;
	    }

}
