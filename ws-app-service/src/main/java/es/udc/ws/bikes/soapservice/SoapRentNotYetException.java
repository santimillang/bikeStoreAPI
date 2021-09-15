package es.udc.ws.bikes.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapRentNotYetException",
    targetNamespace="http://soap.ws.udc.es/"
)

public class SoapRentNotYetException extends Exception {
	

    private SoapRentNotYetExceptionInfo faultInfo;  
    
    protected SoapRentNotYetException(
            SoapRentNotYetExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapRentNotYetExceptionInfo getFaultInfo() {
        return faultInfo;
    }

}
