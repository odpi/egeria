/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.adapters.connectors.database;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AdditionalProperties;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * The OCFDatabaseConnector provides common services for OCF Database Connector implementations.
 */
public abstract class OCFDatabaseConnector extends ConnectorBase {
    protected AdditionalProperties securedProperties = null;
    /*
     * this url is the database url which is used to create the connection to the database
     */
    protected String url="";
    /*
     * this is returned from the database
     */
    protected ResultSet sqlResults =null;
    /*
     * sqlStatement is used to execute the sql
     */
    protected Statement sqlStatement =null;
    /*
     * connection is used to connect the database and create Statement instance
     */
    protected java.sql.Connection sqlConnection =null;
    //prefix in database url
    protected String dburlPrefix;
    //server address in database url
    protected String dburlServeraddress;
    //server port in database url
    protected String dburlServerport;
    //database name in database url
    protected String dburlDatabase;
    //proxy user in database url, it is for impersonation
    protected String  proxyUser;
    //proxy password in database url, it is for impersonation
    protected String proxyPwd;
    //create true or not when connect to Gaian
    protected boolean create;

    /**
     * default constructor
     */
    public OCFDatabaseConnector(){

    }

    /**
     * constructor with database url settings
     * @param dburlPrefix prefix in database url
     * @param dburlServeraddress server address in database url
     * @param dburlServerport database name in database url
     * @param dburlDatabase database name in database url
     * @param proxyUser proxy user in database url, it is for impersonation
     * @param proxyPwd proxy user in database url, it is for impersonation
     * @param create create true or not when connect to Gaian
     */
    public OCFDatabaseConnector(String dburlPrefix,String  dburlServeraddress,String dburlServerport,String  dburlDatabase,String proxyUser,String  proxyPwd,boolean create){
        this.dburlPrefix=dburlPrefix;
        this.dburlServeraddress=dburlServeraddress;
        this.dburlServerport=dburlServerport;
        this.dburlDatabase=dburlDatabase;
        this.proxyUser=proxyUser;
        this.proxyPwd=proxyPwd;
        this.create=create;
    }
    public String getDburlPrefix() {
        return dburlPrefix;
    }

    public void setDburlPrefix(String dburlPrefix) {
        this.dburlPrefix = dburlPrefix;
    }

    public String getDburlServeraddress() {
        return dburlServeraddress;
    }

    public void setDburlServeraddress(String dburlServeraddress) {
        this.dburlServeraddress = dburlServeraddress;
    }

    public String getDburlServerport() {
        return dburlServerport;
    }

    public void setDburlServerport(String dburlServerport) {
        this.dburlServerport = dburlServerport;
    }

    public String getDburlDatabase() {
        return dburlDatabase;
    }

    public void setDburlDatabase(String dburlDatabase) {
        this.dburlDatabase = dburlDatabase;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPwd() {
        return proxyPwd;
    }

    public void setProxyPwd(String proxyPwd) {
        this.proxyPwd = proxyPwd;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    /**
     * create the connection to the database
     * @param userId the userId needed to connect to the database
     * @return the Connection to the database
     * @throws ConnectionCheckedException if it is not able to connect the database
     */
    public abstract java.sql.Connection connect(String userId) throws ConnectionCheckedException;

    /**
     * execute the query to get data
     * @param query the query
     *  @return  return the data
     * @throws ConnectorCheckedException if there are exceptions when execute the query
     */
    public abstract ResultSet executeQuery(String query) throws ConnectorCheckedException;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public java.sql.Connection getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(java.sql.Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public ResultSet getSqlResults() {
        return sqlResults;
    }

    public void setSqlResults(ResultSet sqlResults) {
        this.sqlResults = sqlResults;
    }

    public Statement getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(Statement sqlStatement) {
        this.sqlStatement = sqlStatement;
    }
}
