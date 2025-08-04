/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcTable;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT;

/**
 * Transfers metadata of a table. Its parent can be a schema or directly a database, even though it is not explicitly enforced
 */
public class TableTransfer implements Function<JdbcTable, OpenMetadataRootElement>
{

    private final Omas omas;
    private final OpenMetadataRootElement anchorAsset;
    private final AuditLog auditLog;
    private final List<OpenMetadataRootElement> omasTables;
    private final String parentQualifiedName;
    private final String parentGuid;

    public TableTransfer(Omas                         omas,
                         OpenMetadataRootElement                 anchorAsset,
                         AuditLog                     auditLog,
                         List<OpenMetadataRootElement> omasTables,
                         String                       parentQualifiedName,
                         String                       parentGuid)
    {
        this.omas = omas;
        this.anchorAsset = anchorAsset;
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
    public OpenMetadataRootElement apply(JdbcTable jdbcTable)
    {
        RelationalTableProperties tableProperties = this.buildTableProperties(jdbcTable);

        Optional<OpenMetadataRootElement> omasTable = omasTables.stream()
                .filter(dte -> omas.getQualifiedName(dte).equals(tableProperties.getQualifiedName()))
                .findFirst();

        if (omasTable.isPresent())
        {
            omas.updateTable(omasTable.get().getElementHeader().getGUID(), tableProperties);
            auditLog.logMessage("Updated table with qualified name " + tableProperties.getQualifiedName(),
                    TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("table " + tableProperties.getQualifiedName()));
            return omasTable.get();
        }

        omas.createTable(anchorAsset, parentGuid, tableProperties);
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
    private RelationalTableProperties buildTableProperties(JdbcTable jdbcTable)
    {
        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(Jdbc.JDBC_CATALOG_KEY, jdbcTable.getTableCat());
        additionalProperties.put(Jdbc.JDBC_SCHEMA_KEY, jdbcTable.getTableSchem());
        additionalProperties.put(Jdbc.JDBC_TABLE_KEY, jdbcTable.getTableName());
        additionalProperties.put(Jdbc.JDBC_TABLE_TYPE_KEY, jdbcTable.getTableType());

        RelationalTableProperties jdbcTableProperties = new RelationalTableProperties();
        jdbcTableProperties.setDisplayName(jdbcTable.getTableName());
        jdbcTableProperties.setQualifiedName(parentQualifiedName + "::" + jdbcTable.getTableName());
        jdbcTableProperties.setAdditionalProperties(additionalProperties);
        return jdbcTableProperties;
    }

}
