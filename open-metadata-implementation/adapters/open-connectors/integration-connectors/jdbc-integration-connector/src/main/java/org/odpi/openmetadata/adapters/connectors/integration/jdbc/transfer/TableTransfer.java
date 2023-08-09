/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseTableElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseTableProperties;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcTable;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT;

/**
 * Transfers metadata of a table. Its parent can be a schema or directly a database, even though it is not explicitly enforced
 */
public class TableTransfer implements Function<JdbcTable, DatabaseTableElement> {

    private final Omas omas;
    private final AuditLog auditLog;
    private final List<DatabaseTableElement> omasTables;
    private final String parentQualifiedName;
    private final String parentGuid;

    public TableTransfer(Omas omas, AuditLog auditLog, List<DatabaseTableElement> omasTables, String parentQualifiedName, String parentGuid) {
        this.omas = omas;
        this.auditLog = auditLog;
        this.omasTables = omasTables;
        this.parentQualifiedName = parentQualifiedName;
        this.parentGuid = parentGuid;
    }

    /**
     * Triggers table metadata transfer
     *
     * @param jdbcTable table
     *
     * @return table element
     */
    @Override
    public DatabaseTableElement apply(JdbcTable jdbcTable) {
        DatabaseTableProperties tableProperties = this.buildTableProperties(jdbcTable);

        Optional<DatabaseTableElement> omasTable = omasTables.stream()
                .filter(dte -> dte.getDatabaseTableProperties().getQualifiedName().equals(tableProperties.getQualifiedName()))
                .findFirst();

        if(omasTable.isPresent()){
            omas.updateTable(omasTable.get().getElementHeader().getGUID(), tableProperties);
            auditLog.logMessage("Updated table with qualified name " + tableProperties.getQualifiedName(),
                    TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("table " + tableProperties.getQualifiedName()));
            return omasTable.get();
        }

        omas.createTable(parentGuid, tableProperties);
        auditLog.logMessage("Created table with qualified name " + tableProperties.getQualifiedName(),
                TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("table " + tableProperties.getQualifiedName()));
        return null;
    }

    /**
     * Build table properties
     *
     * @param jdbcTable table
     *
     * @return properties
     */
    private DatabaseTableProperties buildTableProperties(JdbcTable jdbcTable){
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put(Jdbc.JDBC_CATALOG_KEY, jdbcTable.getTableCat());
        additionalProperties.put(Jdbc.JDBC_SCHEMA_KEY, jdbcTable.getTableSchem());
        additionalProperties.put(Jdbc.JDBC_TABLE_KEY, jdbcTable.getTableName());
        additionalProperties.put(Jdbc.JDBC_TABLE_TYPE_KEY, jdbcTable.getTableType());

        DatabaseTableProperties jdbcTableProperties = new DatabaseTableProperties();
        jdbcTableProperties.setDisplayName(jdbcTable.getTableName());
        jdbcTableProperties.setQualifiedName(parentQualifiedName + "::" + jdbcTable.getTableName());
        jdbcTableProperties.setAdditionalProperties(additionalProperties);
        return jdbcTableProperties;
    }

}
