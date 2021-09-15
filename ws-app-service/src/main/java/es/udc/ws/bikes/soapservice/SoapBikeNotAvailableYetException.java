package es.udc.ws.bikes.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapBikeNotAvailableYetException",
    targetNamespace="http://soap.ws.udc.es/"
)

public class SoapBikeNotAvailableYetException extends Exception {
	

    private SoapBikeNotAvailableYetExceptionInfo faultInfo;  
    
    protected SoapBikeNotAvailableYetException(
            SoapBikeNotAvailableYetExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapBikeNotAvailableYetExceptionInfo getFaultInfo() {
        return faultInfo;
    }

}
