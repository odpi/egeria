/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.util;


import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserCheckedException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserErrorCode;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian.LogicTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExecuteQueryUtil offers basic operations in Gaian. GaianQueryConstructor uses this to
 * query and update Gaian
 */
@Service
public class ExecuteQueryUtil {
    private static final Logger log = LoggerFactory.getLogger(ExecuteQueryUtil.class);
    @Value("${gaian_proxy_username}")
    private String gaianProxyUsername;
    @Value("${derby_driver}")
    private String derbyDriver;
    @Value("${gaian_url_prefix}")
    private String gaianUrlPrefix;
    @Value("${gaian_server}")
    private String gaianServer;
    @Value("${gaian_port}")
    private String gaianPort;
    @Value("${gaian_db}")
    private String gaianDb;
    @Value("${create}")
    private String createProperty;
    @Value("${gaian_proxy_password}")
    private String gaianProxyPassword;
    @Value("${gdb_node}")
    private String gdbNode;
    @Value("${lt_name}")
    private String ltName;
    @Value("${lt_def}")
    private String logicalTableDefinition;
    @Value("${gaian_query_time_out}")
    private Integer gaianQueryTimeout;
    private String gaianUrl;
    private Connection sqlConnection = null;


    @PostConstruct
    public void afterPropertiesSet() {
        gaianUrl = gaianUrlPrefix + gaianServer + ":" + gaianPort + "/" + gaianDb + ";create=" + createProperty
                + ";user=" + gaianProxyUsername + ";password=" + gaianProxyPassword + ";proxy-user=" + gaianProxyUsername + ";proxy-pwd=" + gaianProxyPassword;
    }


    /**
     * connect to Gaian based on the configurations
     *
     * @return connection to Gaian
     * @throws VirtualiserCheckedException when Virtualiser is not able to connect Gaian
     */
    public Connection getConnection() throws VirtualiserCheckedException {
        final String methodName = "getConnection";


        try {
            if (sqlConnection == null || sqlConnection.isClosed()) {
                Class.forName(derbyDriver).newInstance();
                sqlConnection = DriverManager.getConnection(gaianUrl);
                log.info("Connection was created");
            }
        } catch (InstantiationException e) {

            VirtualiserErrorCode errorCode = VirtualiserErrorCode.ILLEGAL_INSTANCE_CREATION;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } catch (IllegalAccessException e) {

            VirtualiserErrorCode errorCode = VirtualiserErrorCode.ILLEGAL_ACCESS;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } catch (ClassNotFoundException e) {

            VirtualiserErrorCode errorCode = VirtualiserErrorCode.UNKNOWN_JDBC_DRIVER;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } catch (SQLException e) {

            VirtualiserErrorCode errorCode = VirtualiserErrorCode.CONNECT_FAIL;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }

        return sqlConnection;
    }


    /**
     * disconnect Gaian
     *
     * @throws VirtualiserCheckedException when Virtualiser is not able to close everything related to Gaian
     */
    public void disconnect() throws VirtualiserCheckedException {
        final String methodName = "disconnect";
        try {

            if (sqlConnection != null) {
                sqlConnection.close();
            }
        } catch (SQLException e) {

            VirtualiserErrorCode errorCode = VirtualiserErrorCode.DISCONNECT_FAIL;

            String errorMessage = errorCode.getErrorMessageId()
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
     * load table definitions from Gaian
     *
     * @param query the query to see all table definitions
     * @return the list of logic tables existing in gaian
     * @throws VirtualiserCheckedException if there are exceptions when executing the query
     */
    public List<LogicTable> getLogicTableDefinitions(String query) throws VirtualiserCheckedException {
        final String methodName = "getLogicTableDefinitions";
        Statement sqlStatement = null;
        List list = new ArrayList();
        if (StringUtils.isEmpty(query)) {
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.NOT_VALID_QUERY;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        try {
            sqlStatement = this.sqlConnection.createStatement();
            sqlStatement.setQueryTimeout(gaianQueryTimeout);
            log.info("Executing query: {}", query);
            ResultSet resultSet = sqlStatement.executeQuery(query);

            while (resultSet.next()) {
                list.add(extractLogicTableDefinition(resultSet));
            }

        } catch (SQLException e) {

            VirtualiserErrorCode errorCode = VirtualiserErrorCode.QUERY_EXECUTION_FAIL;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } finally {
            if (sqlStatement != null) {
                try {
                    sqlStatement.close();
                } catch (SQLException e) {
                    log.error("Exception closing sql statement", e);
                }
            }
        }

        return list;
    }

    /**
     * execute update to Gaian
     *
     * @param query the query
     * @return true if update was successful
     * @throws VirtualiserCheckedException when Virtualiser is not able to create statement or update query properly
     */
    public boolean executeUpdate(String query) throws VirtualiserCheckedException {
        final String methodName = "executeUpdate";
        Statement sqlStatement = null;
        if (StringUtils.isEmpty(query)) {

            VirtualiserErrorCode errorCode = VirtualiserErrorCode.NOT_VALID_QUERY;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        try {
            log.info("Executing query: {}", query);
            sqlStatement = sqlConnection.createStatement();
            sqlStatement.setQueryTimeout(gaianQueryTimeout);

            sqlStatement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.UPDATE_EXECUTION_FAIL;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        } finally {
            if (sqlStatement != null) {
                try {
                    sqlStatement.close();
                } catch (SQLException e) {
                    log.error("Exception closing statement", e);
                }
            }
        }

    }

    private LogicTable extractLogicTableDefinition(ResultSet sqlResults) throws SQLException {
        LogicTable logicTable = new LogicTable();
        Map<String, String> defLists = new HashMap<>();
        logicTable.setGaianNode(sqlResults.getString(gdbNode));
        logicTable.setLogicalTableName(sqlResults.getString(ltName));
        String def = sqlResults.getString(logicalTableDefinition);
        String[] defs = def.split(", ");//here we need to split with ,+space, it is showed in Gaian LTDEF
        for (String column : defs) {
            String[] temp = column.split(" ");
            defLists.put(temp[0], temp[1]);
        }
        logicTable.setLogicalTableDefinition(defLists);
        return logicTable;
    }

}
