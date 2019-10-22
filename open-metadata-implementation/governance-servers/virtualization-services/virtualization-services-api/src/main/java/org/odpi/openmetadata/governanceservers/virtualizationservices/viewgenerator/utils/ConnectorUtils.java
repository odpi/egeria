/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.utils;

import org.odpi.openmetadata.accessservices.informationview.events.TableColumn;
import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.model.LogicTable;
import org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.model.MappedColumn;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectorUtils {
    public static final String TECHNICAL_PREFIX = "LTT";
    public static final String BUSINESS_PREFIX = "LTB";
    public static final String GENERAL = "General";

    /**
     * get Logical Table name bas
     * @param event IV event
     * @param type business or technical
     * @return Logical Table's name
     */
    public static String getLogicTableName(String type, TableContextEvent event, String gaianNodeName) {

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
     * provide the list of the mapped columns
     * @param tableContextEvent the event containing full context for a table
     * @return the list of columns with assigned business terms
     */
    public static List<MappedColumn> getMappedColumns(TableContextEvent tableContextEvent) {

        List<MappedColumn> mappedColumns = new ArrayList<>();
        List<TableColumn> databaseColumnList = tableContextEvent.getTableColumns();
        for (TableColumn databaseColumn : databaseColumnList) {
            if (databaseColumn.getBusinessTerms() != null && !databaseColumn.getBusinessTerms().isEmpty()) {
                MappedColumn mappedColumn = new MappedColumn();
                mappedColumn.setBusinessName(databaseColumn.getBusinessTerms().get(0).getName().replace(" ", "_"));//TODO logic for having only one business term
                mappedColumn.setType(databaseColumn.getType());
                mappedColumn.setTechnicalName(databaseColumn.getName());
                mappedColumns.add(mappedColumn);
            }
        }
        return mappedColumns;
    }


    /**
     * set the real column data type to columns
     *
     * @param mappedColumns
     * @param backEndTable
     */
    public static void updateColumnDataType(List<MappedColumn> mappedColumns, LogicTable backEndTable) {
        for (MappedColumn mappedColumn : mappedColumns) {
            for (Map.Entry<String, String> column : backEndTable.getLogicalTableDefinition().entrySet()) {
                if (mappedColumn.getTechnicalName().equals(column.getKey())) {
                    mappedColumn.setType(column.getValue());
                    break;
                }
            }
        }
    }
}
