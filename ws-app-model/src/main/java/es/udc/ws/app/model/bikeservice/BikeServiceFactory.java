package es.udc.ws.app.model.bikeservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class BikeServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "BikeServiceFactory.className";
    private static BikeService service = null;

    private BikeServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static BikeService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (BikeService) serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static BikeService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
