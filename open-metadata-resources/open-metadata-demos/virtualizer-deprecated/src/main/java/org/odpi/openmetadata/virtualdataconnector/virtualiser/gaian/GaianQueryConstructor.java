/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian;

import org.odpi.openmetadata.accessservices.informationview.events.TableColumn;
import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserCheckedException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserErrorCode;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.util.ExecuteQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;


/**
 * GaianQueryConstructor creates or removes Logical Tables.
 */
@Service
public class GaianQueryConstructor {

    private static final Logger log = LoggerFactory.getLogger(GaianQueryConstructor.class);
    public static String TECHNICAL_PREFIX = "LTT";
    public static String BUSINESS_PREFIX = "LTB";
    private static String GENERAL = "General";
    @Value("${gaian_front_end_name}")
    private String gaianFrontEndName;
    @Value("${get_logic_tables}")
    private String getLogicTables;
    @Autowired
    private ExecuteQueryUtil executeQueryUtil;


    /**
     * based on the json file, create Logical Tables and update Gaian
     *
     * @param tableContextEvent json file from Information View OMAS
     * @return whether create or remove tables correctly
     */
    public Map<String, String> notifyGaian(TableContextEvent tableContextEvent) {
        if (tableContextEvent == null) {
            log.debug("Object TableContextEvent is null");
            return Collections.emptyMap();
        }

        try {
            executeQueryUtil.getConnection();

            String gaianNodeName = tableContextEvent.getTableSource().getDatabaseSource().getEndpointSource().getNetworkAddress().replace(".", "").toLowerCase();
            String technicalTableName = getLogicTableName(TECHNICAL_PREFIX, tableContextEvent, gaianNodeName);
            String businessTableName = getLogicTableName(BUSINESS_PREFIX, tableContextEvent, gaianNodeName);
            String logicalTableName = getLogicTableName(GENERAL, tableContextEvent, gaianNodeName);
            List<MappedColumn> mappedColumns = getMappedColumns(tableContextEvent);

            if (mappedColumns == null || mappedColumns.isEmpty()) {
                log.info("There are no business term associations to columns in the received event, removing existing definitions");
                if (getMatchingTableDefinition(gaianFrontEndName, Arrays.asList(businessTableName, technicalTableName)) != null) {
                    removeTechnicalAndBussinessTable(businessTableName, technicalTableName);
                }
            } else {
                return createTableDefinitions(tableContextEvent, gaianNodeName, technicalTableName, businessTableName, logicalTableName, mappedColumns);
            }

        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to create views in Gaian.", e);
            return Collections.emptyMap();
        } finally {
            try {
                executeQueryUtil.disconnect();
                log.debug("Disconnect from Gaian");
            } catch (VirtualiserCheckedException e) {
                log.error("Exception: Not able to disconnect from Gaian.", e);
            }
        }
        return Collections.emptyMap();
    }

    private Map<String, String> createTableDefinitions(TableContextEvent tableContextEvent, String gaianNodeName, String technicalTableName, String businessTableName, String logicalTableName, List<MappedColumn> mappedColumns) throws VirtualiserCheckedException {
        String methodName = "createTableDefinitions";
        Map<String, String> createdTables = new HashMap();
        LogicTable backendTable = getMatchingTableDefinition(gaianNodeName, Collections.singletonList(logicalTableName));
        if (backendTable != null) {
            if(!backendTable.getGaianNode().equals(gaianFrontEndName)) {
                createMirroringLogicalTable(logicalTableName, gaianNodeName);
            }
            updateColumnDataType(mappedColumns, backendTable);

            String updatedTable = createTableDefinition(tableContextEvent.getTableSource().getDatabaseSource().getName(), businessTableName, (c -> c.getBusinessName()), mappedColumns, gaianNodeName, logicalTableName);
            if (updatedTable != null) {
                createdTables.put(BUSINESS_PREFIX, updatedTable);
            }

            updatedTable = createTableDefinition(tableContextEvent.getTableSource().getDatabaseSource().getName(), technicalTableName, (c -> c.getTechnicalName()), mappedColumns, gaianNodeName,logicalTableName );
            if (updatedTable != null) {
                createdTables.put(TECHNICAL_PREFIX, updatedTable);
            }

            if(!backendTable.getGaianNode().equals(gaianFrontEndName)) {
                log.info("Remove mirrored logical table: {}", logicalTableName);
                executeQueryUtil.executeUpdate(buildRemoveTableStatement(logicalTableName));
            }
            return createdTables;
        } else {
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.NO_lOGICTABLE;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(tableContextEvent.getTableSource().getName(), gaianNodeName);


            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    null);
        }
    }

    private String createTableDefinition(String databaseName, String tableName, Function<MappedColumn, String> function, List<MappedColumn> mappedColumns, String gaianNodeName, String logicalTableName) {
        String businessTableCreateStatement = buildTableCreateStatement(tableName, mappedColumns, function);
        String setBusinessTableDataSource = buildCreateTableDataSourceStatement(databaseName, tableName, gaianNodeName, mappedColumns, logicalTableName);

        if (updateGaian(businessTableCreateStatement, setBusinessTableDataSource)) {
            log.debug("Successfully created table {}", tableName);
            return tableName;
        } else {
            log.error("Failed to create table {}" ,tableName);
        }
        return null;
    }


    private LogicTable getMatchingTableDefinition(String gaianNodeName, List<String> tables) {
        log.debug("gaianNodeName: " + gaianNodeName);
        log.debug("tables to match in gaian: " + tables);
        List<LogicTable> logicTableList = new ArrayList<>();
        try {
            logicTableList = executeQueryUtil.getLogicTableDefinitions(getLogicTables);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute query in Gaian.", e);
        }
        if (logicTableList != null && !logicTableList.isEmpty()) {
            return logicTableList.stream().filter(e -> (e.getGaianNode().equals(gaianNodeName) && tables.contains(e.getLogicalTableName()))).findFirst().orElse(null);
        }
        return null;
    }


    /**
     *
     * @param tableContextEvent the event containing full context for a table
     * @return the list of columns with assigned business terms
     */
    private List<MappedColumn> getMappedColumns(TableContextEvent tableContextEvent) {

        List<MappedColumn> mappedColumns = new ArrayList<>();
        List<TableColumn> databaseColumnList = tableContextEvent.getTableColumns();
        for (TableColumn databaseColumn : databaseColumnList) {
            if (databaseColumn.getBusinessTerm() != null) {
                MappedColumn mappedColumn = new MappedColumn();
                mappedColumn.setBusinessName(databaseColumn.getBusinessTerm().getName().replace(" ", "_"));
                mappedColumn.setType(databaseColumn.getType());
                mappedColumn.setTechnicalName(databaseColumn.getName());
                mappedColumns.add(mappedColumn);
            }
        }
        return mappedColumns;
    }

    /**
     * get Logical Table name
     *
     * @param type          business or technical
     * @param gaianNodeName
     * @return Logical Table's name
     */
    private String getLogicTableName(String type, TableContextEvent event, String gaianNodeName) {

        String connectorProviderType = event.getTableSource().getDatabaseSource().getEndpointSource().getConnectorProviderName().toLowerCase();


        String name;
        if (type.equals(GENERAL)) {
            //form Logical Table's name for the back-end Gaian
            name = connectorProviderType + "_" + event.getTableSource().getDatabaseSource().getName() + "_" + event.getTableSource().getSchemaName() + "_" + event.getTableSource().getName();
        } else {
            //form Logical Table's name for the front-end Gaian
            name = type + "_" + gaianNodeName + "_" + connectorProviderType + "_" + event.getTableSource().getDatabaseSource().getName() + "_" + event.getTableSource().getSchemaName() + "_" + event.getTableSource().getName();
        }
        return name.toUpperCase();
    }

    /**
     *
     * @param tableName name of the table to be created
     * @param mappedColumns columns to be added to table definition
     * @param function to retrieve the value to be used as column name; it is either technical or business name
     * @return
     */
    private String buildTableCreateStatement(String tableName, List<MappedColumn> mappedColumns, Function<MappedColumn, String> function) {
        StringBuilder statement = new StringBuilder("call setlt('" +
                tableName +
                "','");
        for (MappedColumn mappedColumn : mappedColumns) {
            statement.append(function.apply(mappedColumn)).append(" ").append(mappedColumn.getType()).append(",");
        }
        statement = new StringBuilder(statement.substring(0, (statement.length() - 1)));
        statement.append("','')");
        return statement.toString();
    }

    /**
     * remove the business and technical Logical Tables created in the front-end Gaian
     *
     * @param businessTableName name of the table with business terms definitions
     * @param technicalTableName name of the table with technical name definitions
     */
    private void removeTechnicalAndBussinessTable(String businessTableName, String technicalTableName) {
        log.debug("Remove existing technical and business Logical Tables");
        String removeBusinessTable = "call removelt('" +
                businessTableName + "')";
        String removeTechnicalTable = "call removelt('" +
                technicalTableName + "')";
        try {
            executeQueryUtil.executeUpdate(removeBusinessTable);
            executeQueryUtil.executeUpdate(removeTechnicalTable);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute update in Gaian.", e);
        }
    }


    /**
     * build statement to remove a table from gaian
     *
     * @param tableName
     */
    private String buildRemoveTableStatement(String tableName) {
        return "call removelt('" + tableName + "')";
    }


    private boolean updateGaian(String createTableStatement, String createDataSource) {
        boolean status = false;
        try {
            status = executeQueryUtil.executeUpdate(createTableStatement);
            if (status) {
                executeQueryUtil.executeUpdate(createDataSource);
            }
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute update in Gaian.", e);
        }
        if (!status) {
            log.error("Unable to execute update in Gaian.");
        }
        return status;
    }


    /**
     * connect the data source to the Logical Table
     *
     *
     * @param databaseName
     * @param tableName     name of the Logical table
     * @param gaianNodeName
     * @param mappedColumns
     * @param logicalTableName
     * @return the call to Gaian
     */
    private String buildCreateTableDataSourceStatement(String databaseName, String tableName, String gaianNodeName, List<MappedColumn> mappedColumns, String logicalTableName) {
        String connectionName = gaianNodeName.toUpperCase();
        String statementForCreatingDataSource = "call setdsrdbtable('" +
                tableName +
                "', '', '" +
                connectionName +
                "', '" +
                logicalTableName +
                "','', '";

        for (MappedColumn mappedColumn : mappedColumns) {
            statementForCreatingDataSource += mappedColumn.getTechnicalName() + ",";
        }
        statementForCreatingDataSource = statementForCreatingDataSource.substring(0, (statementForCreatingDataSource.length() - 1));
        statementForCreatingDataSource += "')";
        return statementForCreatingDataSource;
    }

    /**
     * Set Logical Table mirroring the definition for the given Logical Table Name on another GaianDB node, so its data can be queried remotely.
     *
     * @param logicalTableName
     * @param gaianNodeName
     */
    private void createMirroringLogicalTable(String logicalTableName, String gaianNodeName) {
        log.debug("Set up Logical Table for Gaian node");
        String setLogicalTableForNode = "call setltfornode('" +
                logicalTableName + "','" +
                gaianNodeName + "')";
        try {
            executeQueryUtil.executeUpdate(setLogicalTableForNode);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute update in Gaian.", e.getMessage());
        }
    }


    /**
     * set the real column data type to columns
     *
     * @param mappedColumns
     * @param backEndTable
     */
    private void updateColumnDataType(List<MappedColumn> mappedColumns, LogicTable backEndTable) {
        for (MappedColumn mappedColumn : mappedColumns) {
            for (Entry<String, String> column : backEndTable.getLogicalTableDefinition().entrySet()) {
                if (mappedColumn.getTechnicalName().equals(column.getKey())) {
                    mappedColumn.setType(column.getValue());
                    break;
                }
            }
        }
    }


}
