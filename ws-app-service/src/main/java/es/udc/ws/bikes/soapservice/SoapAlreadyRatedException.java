package es.udc.ws.bikes.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapAlreadyRatedException",
    targetNamespace="http://soap.ws.udc.es/"
)

public class SoapAlreadyRatedException extends Exception {

	private SoapAlreadyRatedExceptionInfo faultInfo;  
    
    protected SoapAlreadyRatedException(
            SoapAlreadyRatedExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapAlreadyRatedExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}
