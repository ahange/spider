package com.timerchina.spider.db;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.timerchina.spider.bean.Constant;
import com.timerchina.spider.service.utils.PropertiesTool;

/**
 * @author jack.zhang 数据库连接池工厂
 */
public class MysqlDBPoolFactory {

    private final int DEFAULT_POOL_SIZE = 5;
    private final int MIN_POOL_SIZE = 1;
    private final int MAX_POOL_SIZE = 10;
    private final int ACQUIRE_INCREMENT = 1;

    private final int MAX_IDLE_TIME = 60 * 60;
    private final int ACQUIRE_RETRY_ATTEMPTS = 3;
    private final int ACQUIRE_RETRY_DELAY = 1000;
    private final int IDLE_CONNECTION_TEST_PERIOD = 60;

    private Map<String, ComboPooledDataSource> dbPoolMap = new HashMap<String, ComboPooledDataSource>();

    private Logger logger = Logger.getLogger(MysqlDBPoolFactory.class);

    private static MysqlDBPoolFactory poolFactory = new MysqlDBPoolFactory();

    private MysqlDBPoolFactory() {
    }

    public static MysqlDBPoolFactory getInstance() {
        return poolFactory;
    }

    public ComboPooledDataSource getComboPooledDataSource(String conId, String dbName) {
        synchronized (dbPoolMap) {
            ComboPooledDataSource ds = dbPoolMap.get(conId+" "+dbName);
            if (ds == null || !checkAlive(ds)) {
                ds = createComboPooledDataSourceByXML(conId, dbName);
                dbPoolMap.put(conId+" "+dbName, ds);
            }
            return ds;
        }
    }
    
    private boolean checkAlive(ComboPooledDataSource ds) {
        try {
            ds.getConnection().close();
            return true;
        } catch (SQLException e) {
            ds.close();
            return false;
        }
    }

    private ComboPooledDataSource createComboPooledDataSourceByXML(String conId, String dbName) {
    	
    	PropertiesTool props = PropertiesTool.getSpiderInstance();
    	
    	String user = null;
        String password = null;
        String jdbcUrl = null;
        
		user = props.getProperties(Constant.PROPS_SPIDER, "user"+conId);
        password = props.getProperties(Constant.PROPS_SPIDER, "password"+conId);
        jdbcUrl = props.getProperties(Constant.PROPS_SPIDER, "url"+conId);
    	
        System.out.println(user+"-"+password+"-"+jdbcUrl);
        
        
        try {
			return createComboPooledDataSource(jdbcUrl, user, password);
		} catch (PropertyVetoException e) {
			logger.info("获取数据连接出错");
		}
        return null;
    }

    private ComboPooledDataSource createComboPooledDataSource(String jdbcUrl, String user, String password) throws PropertyVetoException {
        ComboPooledDataSource ds = initDBPool();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUser(user);
        ds.setPassword(password);
        return ds;
    }

    private ComboPooledDataSource initDBPool() throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setInitialPoolSize(DEFAULT_POOL_SIZE);
        ds.setMaxPoolSize(MAX_POOL_SIZE);
        ds.setMinPoolSize(MIN_POOL_SIZE);
        ds.setAcquireIncrement(ACQUIRE_INCREMENT);
        ds.setTestConnectionOnCheckin(true);
//        ds.setTestConnectionOnCheckout(true);
        ds.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_PERIOD);
        ds.setMaxIdleTime(MAX_IDLE_TIME);
        ds.setAutoCommitOnClose(true);
        ds.setAcquireRetryAttempts(ACQUIRE_RETRY_ATTEMPTS);
        ds.setAcquireRetryDelay(ACQUIRE_RETRY_DELAY);
        return ds;
    }
}