/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.util;


import org.apache.atlas.virtualiser.ffdc.VirtualiserCheckedException;
import org.apache.atlas.virtualiser.ffdc.VirtualiserErrorCode;
import org.apache.atlas.virtualiser.gaian.LogicTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExecuteQueryUtil offers basic operations in Gaian. GaianQueryConstructor uses this to
 * query and update Gaian
 */
public class ExecuteQueryUtil {
    /**
     * this url is the database url which is used to create the connection to the database
     */
    private String url="";
    /**
     * this is returned from the database
     */
    private ResultSet sqlResults =null;
    /**
     * sqlStatement is used to execute the sql
     */
    private Statement sqlStatement =null;
    /**
     * connection is used to connect the database and create Statement instance
     */
    private Connection sqlConnection =null;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Connection getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }
    private static final Logger log = LoggerFactory.getLogger(ExecuteQueryUtil.class);


    /**
     *  connect to Gaian based on the configurations
     * @return connection to Gaian
     * @throws VirtualiserCheckedException when Virtualiser is not able to connect Gaian
     */
    public Connection getConnection() throws VirtualiserCheckedException {
        final String methodName="getConnection";
        if (this.getSqlConnection() == null) {
            try {
                /*
                 * set up the dbURL
                 */
                setDBUrl(PropertiesHelper.properties.getProperty("gaian_proxy_username"));
                Class.forName(PropertiesHelper.properties.getProperty("derby_driver")).newInstance();
                this.setSqlConnection(DriverManager.getConnection(this.getUrl()));
            } catch (InstantiationException e) {
               /*
             * Wrap exception in the DatabaseConnectCheckedException with a suitable message
             * when Virtualiser is not able to connect to the database.
             */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.ILLEGAL_INSTANCE_CREATION;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);

                throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
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
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.ILLEGAL_ACCESS;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);

                throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            } catch (ClassNotFoundException e) {
                /*
                 * Wrap exception in the VirtualiserCheckedException with a suitable message
                 * when the jdbc driver name is wrong
                */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.UNKNOWN_JDBC_DRIVER;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);

                throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            } catch (SQLException e) {
                /*
             * Wrap exception in the VirtualiserCheckedException with a suitable message
             * when the user is not able to connect to the database.
             */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.CONNECT_FAIL;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);

                throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            }
        }
        return this.getSqlConnection();
    }

    /**
     * set up the database url
     * @param userId end user id
     * @throws VirtualiserCheckedException properties is not set up correctly
     */
    public void setDBUrl(String userId ) throws VirtualiserCheckedException{
        final String methodName="setDBUrl";
        if(PropertiesHelper.properties.size()==0){
            /*
             * Wrap exception in the DatabaseConnectCheckedException with a suitable message
             * when the properties is not set up correctly
             */
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.EMPTY_GAIAN_PROPERTIES;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }
        String dbURL = PropertiesHelper.properties.getProperty("gaian_url_prefix") + PropertiesHelper.properties.getProperty("gaian_server") + ":" + PropertiesHelper.properties.getProperty("gaian_port") + "/" + PropertiesHelper.properties.getProperty("gaian_db") + ";create="+PropertiesHelper.properties.getProperty("create") + ";user=" + userId + ";password="+PropertiesHelper.properties.getProperty("gaian_proxy_password")+";proxy-user="+PropertiesHelper.properties.getProperty("gaian_proxy_username")+";proxy-pwd="+PropertiesHelper.properties.getProperty("gaian_proxy_password");
        this.setUrl(dbURL);
    }

    /**
     * get all the Logical Tables in the network
     * @param query call listlts()
     * @return a list of Logical Tables in the network
     * @throws VirtualiserCheckedException when the query is executed successfully in Gaian
     */
    public List<LogicTable> getLogicTables(String query) throws VirtualiserCheckedException {
        final String methodName="getLogicTables";
        try {
            executeQuery(query);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute query in Gaian.",e);
            e.printStackTrace();
        }
        List<LogicTable> logicTables=new ArrayList<>();
        try {
            while (this.getSqlResults().next()) {
                LogicTable logicTable = new LogicTable();
                Map<String, String> defLists=new HashMap<>();
                //set gaian node
                logicTable.setGDBNode(this.getSqlResults().getString(PropertiesHelper.properties.getProperty("gdb_node")));
                //set Logical Table name
                logicTable.setLTName(this.getSqlResults().getString(PropertiesHelper.properties.getProperty("lt_name")));
                //set Logical Table definition
                String def=this.getSqlResults().getString(PropertiesHelper.properties.getProperty("lt_def"));
                String[] defs=def.split(", ");//here we need to split with ,+space, it is showed in Gaian LTDEF
                for(String column:defs){
                    String[] temp=column.split(" ");
                    defLists.put(temp[0],temp[1]);
                }
                logicTable.setLTDef(defLists);

                logicTables.add(logicTable);
            }
        } catch (SQLException e) {
            /*
             *  Wrap exception in the VirtualiserCheckedException with a suitable message
             *  when the execution failed
            */
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.GET_DATA_FAIL;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
        if(logicTables.size()==0){
            logicTables=null;
        }
        return logicTables;
    }
    /**
     * disconnect Gaian
     * @throws VirtualiserCheckedException when Virtualiser is not able to close everything related to Gaian
     */
    public void disconnect() throws VirtualiserCheckedException {
        final String methodName = "disconnect";
        try {
			if (this.getSqlStatement() != null) {
				this.getSqlStatement().close();
			}
            if (this.getSqlResults() != null) {
                this.getSqlResults().close();
            }
            if (this.getSqlConnection() != null) {              
                this.getSqlConnection().close();
            }
        } catch (SQLException e) {
			/*
            *  Wrap exception in the VirtualiserCheckedException with a suitable message
            *  when Virtualiser is not able to close everything related to Gaian
            */
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.DISCONNECT_FAIL;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }

    }

    /**
     * execute query in Gaian
     * @param query the query
     * @throws VirtualiserCheckedException if there are exceptions when execute the query
     */
    public void executeQuery(String query)throws VirtualiserCheckedException {
        final String methodName="executeQuery";
        if(query==null || query.equals("")){
            /*
             *  Wrap exception in the VirtualiserCheckedException with a suitable message
             *  when the execution failed
            */
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.NOT_VALID_QUERY;
            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }else{
            if(this.getSqlStatement()==null){
                try {
                    this.setSqlStatement(this.getSqlConnection().createStatement());
                    this.getSqlStatement().setQueryTimeout(Integer.parseInt(PropertiesHelper.properties.getProperty("gaian_query_time_out")));
                } catch (SQLException e) {
                   /*
                    *  Wrap exception in the VirtualiserCheckedException with a suitable message
                    *  when the execution failed
                   */
                    VirtualiserErrorCode errorCode = VirtualiserErrorCode.STATEMENT_CREATION_FAIL;

                    String        errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(methodName);

                    throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction(),
                            e);
                }
            }
            try {
                this.setSqlResults(this.getSqlStatement().executeQuery(query) );
            } catch (SQLException e) {
            /*
             *  Wrap exception in the VirtualiserCheckedException with a suitable message
             *  when the execution failed
             */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.QUERY_EXECUTION_FAIL;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);

                throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            }

        }

    }

    /**
     * execute update to Gaian
     * @param query the query
     * @throws VirtualiserCheckedException when Virtualiser is not able to create statement or update query properly
     */
    public void executeUpdate(String query)throws VirtualiserCheckedException {
        final String methodName="executeUpdate";
        if(query==null || query.equals("")){
            /*
             * Build and throw exception.
             */
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.NOT_VALID_QUERY;
            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }else{
            if(this.getSqlStatement()==null){
                try {
                    this.setSqlStatement(this.getSqlConnection().createStatement());
                    this.getSqlStatement().setQueryTimeout(Integer.parseInt(PropertiesHelper.properties.getProperty("gaian_query_time_out")));
                } catch (SQLException e) {
                    /*
                     *  Wrap exception in the VirtualiserCheckedException with a suitable message
                     *  when the execution failed
                     */
                    VirtualiserErrorCode errorCode = VirtualiserErrorCode.STATEMENT_CREATION_FAIL;

                    String        errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(methodName);

                    throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction(),
                            e);
                }
            }
            try {
                this.getSqlStatement().executeUpdate(query);
            } catch (SQLException e) {
             /*
             *  Wrap exception in the VirtualiserCheckedException with a suitable message
             *  when the execution failed
             */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.UPDATE_EXECUTION_FAIL;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);

                throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            }
        }

    }


}
