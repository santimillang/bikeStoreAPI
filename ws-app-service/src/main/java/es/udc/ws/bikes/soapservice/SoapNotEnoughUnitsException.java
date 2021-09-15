package es.udc.ws.bikes.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapNotEnoughUnitsException",
    targetNamespace="http://soap.ws.udc.es/"
)

public class SoapNotEnoughUnitsException extends Exception {
	

    private SoapNotEnoughUnitsExceptionInfo faultInfo;  
    
    protected SoapNotEnoughUnitsException(
            SoapNotEnoughUnitsExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapNotEnoughUnitsExceptionInfo getFaultInfo() {
        return faultInfo;
    }

}
