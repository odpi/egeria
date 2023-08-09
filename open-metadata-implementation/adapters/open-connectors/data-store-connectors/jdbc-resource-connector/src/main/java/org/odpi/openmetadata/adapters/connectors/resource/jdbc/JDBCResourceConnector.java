/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * JDBCResourceConnector provides basic implementation of {@link DataSource} interface in order to get a {@link Connection} to
 * target database. This is done via a static inner class, since {@link DataSource#getConnection()} clashes with
 * {@link ConnectorBase#getConnection()}
 */
public class JDBCResourceConnector extends ConnectorBase  {

    /**
     * Get as {@link DataSource}
     *
     * @return implementation
     */
    public DataSource asDataSource(){
        return new JdbcConnectorAsDataSource();
    }

    private class JdbcConnectorAsDataSource implements DataSource {

        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(connectionBean.getEndpoint().getAddress(), connectionBean.getUserId(),
                    connectionBean.getClearPassword());
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return DriverManager.getConnection(connectionBean.getEndpoint().getAddress(), username, password);
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }
    }
}
