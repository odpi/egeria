/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.adapters.connectors.database.gaian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adapters.connectors.database.OCFDatabaseConnector;
import org.odpi.openmetadata.adapters.connectors.database.ffdc.OCFDatabaseConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A GaianOCFConnector is a connector to Gaian. It will connect to Gaian, execute the query.
 */

public class GaianOCFConnector extends OCFDatabaseConnector {

    private final String connectorName=GaianOCFConnector.class.getName();
    private static final Logger log = LoggerFactory.getLogger(GaianOCFConnector.class);


    /**
     * default constructor
     */
    public GaianOCFConnector(){
        super();
    }

    /**
     * all information about database url is offered for the constructor
     * @param dburlPrefix prefix in database url
     * @param dburlServeraddress server address in database url
     * @param dburlServerport database name in database url
     * @param dburlDatabase database name in database url
     * @param proxyUser proxy user in database url, it is for impersonation
     * @param proxyPwd proxy user in database url, it is for impersonation
     * @param create create true or not when connect to Gaian
     */
    public GaianOCFConnector(String dburlPrefix, String  dburlServeraddress, String dburlServerport, String  dburlDatabase, String proxyUser, String  proxyPwd, boolean create){
        super(dburlPrefix,dburlServeraddress,dburlServerport,dburlDatabase,proxyUser,proxyPwd,create);
    }






    /**
     * set up the database url
     * @param userId end user id
     * @throws ConnectionCheckedException properties is not set up correctly
     */
    public void setDBUrl(String userId ) throws ConnectionCheckedException{
        final String methodName="setDBUrl";
        String dbURL = dburlPrefix + dburlServeraddress + ":" + dburlServerport + "/" + dburlDatabase
                + ";create="+String.valueOf(create) + ";user=" + userId + ";password="+proxyPwd+";proxy-user="
                +proxyUser+";proxy-pwd="+proxyPwd;
        log.debug(dbURL);
        this.setUrl(dbURL);
    }


    /**
     * get connection to the database
     * @param userId the userId needed to connect to the database
     * @return the connection to the database
     * @throws ConnectionCheckedException if it is not able to connect to the database
     */
    @Override
    public Connection connect(String userId) throws ConnectionCheckedException {
        final String methodName="createConnection";
        /**
         * set up the dbURL
         */
        try{
            setDBUrl(userId);
        }catch(ConnectionCheckedException e){
            log.error("Exception in set up database url.",e);
        }

        try {
            if(this.getSqlConnection()==null){
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();

                if (log.isDebugEnabled())
                {
                    log.debug("new instance is successful created");
                }
                this.setSqlConnection(DriverManager.getConnection(this.getUrl()));
            }
        } catch (SQLException e) {
            /*
             * Wrap exception in the DatabaseConnectCheckedException with a suitable message
             * when the user is not able to connect to the database.
             */
            OCFDatabaseConnectorErrorCode errorCode = OCFDatabaseConnectorErrorCode.CONNECT_FAIL;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName, connectorName);

            throw new ConnectionCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } catch (IllegalAccessException e) {
            /*
             * Wrap exception in the DatabaseConnectCheckedException with a suitable message
             * when the user is not able to connect to the database.
             */
            OCFDatabaseConnectorErrorCode errorCode = OCFDatabaseConnectorErrorCode.ILLEGAL_ACCESS;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName, connectorName);

            throw new ConnectionCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } catch (InstantiationException e) {
           /*
             * Wrap exception in the DatabaseConnectCheckedException with a suitable message
             * when the user is not able to connect to the database.
             */
            OCFDatabaseConnectorErrorCode errorCode = OCFDatabaseConnectorErrorCode.ILLEGAL_INSTANCE_CREATION;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName, connectorName);

            throw new ConnectionCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } catch (ClassNotFoundException e) {
            /*
             * Wrap exception in the DatabaseConnectCheckedException with a suitable message
             * when the jdbc driver name is wrong
             */
            OCFDatabaseConnectorErrorCode errorCode = OCFDatabaseConnectorErrorCode.UNKNOWN_JDBC_DRIVER;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName, connectorName);

            throw new ConnectionCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
        if (log.isDebugEnabled())
        {
            log.debug("connect to database successfully");
        }
        return this.getSqlConnection();
    }

    /**
     * execute the query to get data
     * @param query the query
     * @throws ConnectorCheckedException if there are exceptions when execute the query
     */
    @Override
    public ResultSet executeQuery(String query) throws ConnectorCheckedException {
        final String methodName="executeQuery";
        if(query==null || query.equals("")){
            /*
             *  Wrap exception in the ConnectorCheckedException with a suitable message
             *  when the execution failed
            */
            OCFDatabaseConnectorErrorCode errorCode = OCFDatabaseConnectorErrorCode.NOT_VALID_QUERY;
            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName, connectorName);

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }else{
            try {
                if(this.getSqlStatement()==null){
                    this.setSqlStatement(this.getSqlConnection().createStatement());
                }
                this.setSqlResults(this.getSqlStatement().executeQuery(query) );
            } catch (SQLException e) {
                /*
                *  Wrap exception in the ExecutionCheckedException with a suitable message
                *  when the execution failed
                */
                OCFDatabaseConnectorErrorCode errorCode = OCFDatabaseConnectorErrorCode.QUERY_EXECUTION_FAIL;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName, connectorName);

                throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            }
            return this.getSqlResults();
        }
    }

    /**
     * disconnect all the connections to the database
     * @throws ConnectorCheckedException if there are exceptions when execute the query
     */
    @Override
    public void disconnect() throws ConnectorCheckedException {
        final String methodName="disconnect";
        try
        {
            if(this.getSqlStatement()!= null){
                this.getSqlStatement().close();
            }
            if(this.getSqlResults()!=null){
                this.getSqlResults().close();
            }
            if (this.getSqlConnection() != null)
            {
                this.getSqlConnection().close();
            }
            if (log.isDebugEnabled())
            {
                log.debug("disconnect successfully");
            }
        }
        catch (SQLException sqlExcept)
        {
            /*
             * Wrap exception in the ConnectorCheckedException with a suitable message
             * when the disconnect is failed
             */
            OCFDatabaseConnectorErrorCode errorCode = OCFDatabaseConnectorErrorCode.DISCONNECT_FAIL;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName, connectorName);

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    sqlExcept);
        }
    }



}
