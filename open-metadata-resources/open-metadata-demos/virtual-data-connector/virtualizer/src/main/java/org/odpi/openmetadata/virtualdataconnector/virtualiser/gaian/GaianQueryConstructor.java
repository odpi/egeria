/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian;

import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserCheckedException;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.ffdc.VirtualiserErrorCode;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.util.ExecuteQueryUtil;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * @param columnContextEvent json file from Information View OMAS
     * @return whether create or remove tables correctly
     * @throws VirtualiserCheckedException when there is no Logical Table created for the table in the Gaian node
     */
    public Map<String, String> notifyGaian(ColumnContextEvent columnContextEvent) {
        if (columnContextEvent == null) {
            log.debug("Object ColumnContextEvent is null");
            return Collections.emptyMap();
        }

        try {
            executeQueryUtil.getConnection();

            String gaianNodeName = columnContextEvent.getConnectionDetails().getNetworkAddress().replace(".", "").toLowerCase();
            String technicalTableName = getLogicTableName(TECHNICAL_PREFIX, columnContextEvent, gaianNodeName);
            String businessTableName = getLogicTableName(BUSINESS_PREFIX, columnContextEvent, gaianNodeName);
            String logicalTableName = getLogicTableName(GENERAL, columnContextEvent, gaianNodeName);
            List<MappedColumn> mappedColumns = getMappedColumns(columnContextEvent);

            if (mappedColumns == null || mappedColumns.isEmpty()) {
                log.info("There are no business term associations to columns in the received event, removing existing definitions");
                if (getMatchingTableDefinition(gaianFrontEndName, Arrays.asList(businessTableName, technicalTableName)) != null) {
                    removeTechnicalAndBussinessTable(businessTableName, technicalTableName);
                }
            } else {
                return createTableDefinitions(columnContextEvent, gaianNodeName, technicalTableName, businessTableName, logicalTableName, mappedColumns);
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

    private Map<String, String> createTableDefinitions(ColumnContextEvent columnContextEvent, String gaianNodeName, String technicalTableName, String businessTableName, String logicalTableName, List<MappedColumn> mappedColumns) throws VirtualiserCheckedException {
        String methodName = "createTableDefinitions";
        Map<String, String> createdTables = new HashMap();
        LogicTable backendTable = getMatchingTableDefinition(gaianNodeName, Collections.singletonList(logicalTableName));
        if (backendTable != null) {
            createMirroringLogicalTable(logicalTableName, gaianNodeName);
            updateColumnDataType(mappedColumns, backendTable);

            String updatedTable = createTableDefinition(businessTableName, (c -> c.getBusinessName()), mappedColumns, gaianNodeName, logicalTableName);
            if (updatedTable != null) {
                createdTables.put(BUSINESS_PREFIX, updatedTable);
            }

            updatedTable = createTableDefinition(technicalTableName, (c -> c.getTechnicalName()), mappedColumns, gaianNodeName,logicalTableName );
            if (updatedTable != null) {
                createdTables.put(TECHNICAL_PREFIX, updatedTable);
            }

            log.info("Remove mirrored logical table");
            executeQueryUtil.executeUpdate(buildRemoveTableStatement(logicalTableName));

            return createdTables;
        } else {
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.NO_lOGICTABLE;

            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(columnContextEvent.getTableContext().getTableName(), gaianNodeName);


            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    null);
        }
    }

    private String createTableDefinition(String tableName, Function<MappedColumn, String> function, List<MappedColumn> mappedColumns, String gaianNodeName, String logicalTableName) {
        String businessTableCreateStatement = buildTableCreateStatement(tableName, mappedColumns, function);
        String setBusinessTableDataSource = buildCreateTableDataSourceStatement(tableName, gaianNodeName, mappedColumns, logicalTableName);

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
     * @param columnContextEvent the event containing full context for a table
     * @return the list of columns with assigned business terms
     */
    private List<MappedColumn> getMappedColumns(ColumnContextEvent columnContextEvent) {

        List<MappedColumn> mappedColumns = new ArrayList<>();
        List<ColumnDetails> columnDetailsList = columnContextEvent.getTableColumns();
        for (ColumnDetails columnDetails : columnDetailsList) {
            if (columnDetails.getBusinessTerm() != null) {
                MappedColumn mappedColumn = new MappedColumn();
                mappedColumn.setBusinessName(columnDetails.getBusinessTerm().getName().replace(" ", "_"));
                mappedColumn.setType(columnDetails.getType());
                mappedColumn.setTechnicalName(columnDetails.getAttributeName());
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
    private String getLogicTableName(String type, ColumnContextEvent event, String gaianNodeName) {

        String connectorProviderType = event.getConnectionDetails().getConnectorProviderName().toLowerCase();


        String name;
        if (type.equals(GENERAL)) {
            //form Logical Table's name for the back-end Gaian
            name = connectorProviderType + "_" + event.getTableContext().getDatabaseName() + "_" + event.getTableContext().getSchemaName() + "_" + event.getTableContext().getTableName();
        } else {
            //form Logical Table's name for the front-end Gaian
            name = type + "_" + gaianNodeName + "_" + connectorProviderType + "_" + event.getTableContext().getDatabaseName() + "_" + event.getTableContext().getSchemaName() + "_" + event.getTableContext().getTableName();
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
     * @param tableName     name of the Logical table
     * @param gaianNodeName
     * @param mappedColumns
     * @param logicalTableName
     * @return the call to Gaian
     */
    private String buildCreateTableDataSourceStatement(String tableName, String gaianNodeName, List<MappedColumn> mappedColumns, String logicalTableName) {
        String statementForCreatingDataSource = "call setdsrdbtable('" +
                tableName +
                "', '', '" +
                gaianNodeName.toUpperCase() +
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
