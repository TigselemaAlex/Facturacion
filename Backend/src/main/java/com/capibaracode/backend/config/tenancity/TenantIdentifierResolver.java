package com.capibaracode.backend.config.tenancity;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Component
public class TenantIdentifierResolver implements
         HibernatePropertiesCustomizer, CurrentTenantIdentifierResolver {
    private static final Logger logger = LoggerFactory.getLogger(TenantIdentifierResolver.class);
    private String currentTenant= "unknown";


    public void setCurrentTenant(String tenant) {
        currentTenant = tenant;
    }
    /*@Autowired
    private  DataSource dataSource;
    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection("public");
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        logger.info(connection.getSchema() + " info");
        connection.close();
    }

    @Override
    public Connection getConnection(String schema) throws SQLException {
        final Connection connection = dataSource.getConnection();
        System.out.println("getConnection "+ schema);
        this.currentTenant = schema;
        connection.setSchema(this.currentTenant);
        try {
            connection.createStatement().execute( "set schema '" + schema+"'" );
            logger.info(connection.getSchema());
        }
        catch ( SQLException e ) {
            throw new HibernateException(
                    "Could not alter JDBC connection to specified schema [" +
                            schema + "]" + e.getMessage(),
                    e
            );
        }
        return connection;
    }

    @Override
    public void releaseConnection(String schema, Connection connection) throws SQLException {
        System.out.println("releaseConnection ,"+ schema);
        connection.setSchema(schema);
        this.currentTenant = "public";
        try {

            connection.createStatement().execute( "set schema 'public'" );
            this.currentTenant= "public";
        }
        catch ( SQLException e ) {

            throw new HibernateException(
                    "Could not alter JDBC connection to specified schema [" +
                            schema + "]",
                    e
            );
        }
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }


    @Override
    public boolean isUnwrappableAs(Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }*/

    @Override
    public String resolveCurrentTenantIdentifier() {
        return this.currentTenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
