package es.udc.ws.app.model.bike;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlBikeDaoFactory {
	private final static String CLASS_NAME_PARAMETER = "SqlBikeDaoFactory.className";
	
	private static SqlBikeDao dao = null;
	
	private SqlBikeDaoFactory() {
	}
	
	@SuppressWarnings("rawtypes")
	private static SqlBikeDao getInstance() {
		try {
			String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class daoClass = Class.forName(daoClassName);
			return (SqlBikeDao) daoClass.newInstance();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public synchronized static SqlBikeDao getDao() {
		if(dao == null) {
			dao = getInstance();
		}
		return dao;
	}

}
