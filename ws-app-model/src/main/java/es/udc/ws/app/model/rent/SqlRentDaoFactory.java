package es.udc.ws.app.model.rent;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRentDaoFactory {
	private final static String CLASS_NAME_PARAMETER = "SqlRentDaoFactory.className";
    private static SqlRentDao dao = null;

    private SqlRentDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlRentDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRentDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlRentDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
