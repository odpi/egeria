/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcColumn;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcPrimaryKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT;

/**
 * Transfers metadata of a column
 */
public class ColumnTransfer implements Function<JdbcColumn, OpenMetadataRootElement>
{

    private final Omas omas;
    private final OpenMetadataRootElement anchorAsset;
    private final AuditLog auditLog;
    private final List<OpenMetadataRootElement> omasColumns;
    private final List<JdbcPrimaryKey> jdbcPrimaryKeys;
    private final OpenMetadataRootElement omasTable;

    public ColumnTransfer(Omas omas,
                          OpenMetadataRootElement anchorAsset,
                          AuditLog auditLog,
                          List<OpenMetadataRootElement> omasColumns,
                          List<JdbcPrimaryKey> jdbcPrimaryKeys,
                          OpenMetadataRootElement omasTable)
    {
        this.omas = omas;
        this.anchorAsset = anchorAsset;
        this.auditLog = auditLog;
        this.omasColumns = omasColumns;
        this.jdbcPrimaryKeys = jdbcPrimaryKeys;
        this.omasTable = omasTable;
    }

    /**
     * Triggers column metadata transfer
     *
     * @param jdbcColumn column
     *
     * @return column
     */
    @Override
    public OpenMetadataRootElement apply(JdbcColumn jdbcColumn)
    {
        RelationalColumnProperties columnProperties = buildColumnProperties(jdbcColumn, omasTable);

        Optional<OpenMetadataRootElement> omasColumn = omasColumns.stream()
                .filter(dce -> omas.getQualifiedName(dce).equals(columnProperties.getQualifiedName()))
                .findFirst();

        if (omasColumn.isPresent())
        {
            removeForeignKey(omasColumn.get());
            omas.updateColumn(omasColumn.get().getElementHeader().getGUID(), columnProperties);
            auditLog.logMessage("Updated column with qualified name " + columnProperties.getQualifiedName(),
                    TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("column " + columnProperties.getQualifiedName()));

            // todo covert properties
            //  this.updateOrRemovePrimaryKey(jdbcPrimaryKeys, jdbcColumn, omasColumn.get().getElementHeader().getGUID(), omasColumn.get().getElementHeader().getPrimaryKey().getClassificationProperties());

            return omasColumn.get();
        }

        Optional<String> columnGuid = omas.createColumn(omasTable.getElementHeader().getGUID(), columnProperties);
        auditLog.logMessage("Created column with qualified name " + columnProperties.getQualifiedName(),
                TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("column " + columnProperties.getQualifiedName()));

        columnGuid.ifPresent(s -> this.updateOrRemovePrimaryKey(jdbcPrimaryKeys, jdbcColumn, s, null));

        return null;
    }

    /**
     * Build column properties
     *
     * @param jdbcColumn column
     * @param omasTable table
     *
     * @return properties
     */
    private RelationalColumnProperties buildColumnProperties(JdbcColumn              jdbcColumn,
                                                             OpenMetadataRootElement omasTable)
    {
        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(Jdbc.JDBC_CATALOG_KEY, jdbcColumn.getTableCat());
        additionalProperties.put(Jdbc.JDBC_SCHEMA_KEY, jdbcColumn.getTableSchem());
        additionalProperties.put(Jdbc.JDBC_TABLE_KEY, jdbcColumn.getTableName());
        additionalProperties.put(Jdbc.JDBC_COLUMN_KEY, jdbcColumn.getColumnName());

        RelationalColumnProperties properties = new RelationalColumnProperties();

        properties.setDisplayName(jdbcColumn.getColumnName());
        properties.setQualifiedName(omas.getQualifiedName(omasTable) + "::" + jdbcColumn.getColumnName());
        // todo add to typeEmbeddedAttribute
        //  properties.setDataType(extractDataType(jdbcColumn.getDataType()));
        properties.setAdditionalProperties(additionalProperties);

        return properties;
    }

    /**
     * Determines data type. See {@link JDBCType}
     *
     * @param jdbcDataType data type
     *
     * @return data type or "<unknown>"
     */
    private String extractDataType(int jdbcDataType)
    {
        String dataType = "<unknown>";
        try
        {
            dataType = JDBCType.valueOf(jdbcDataType).getName();
        }
        catch(IllegalArgumentException iae)
        {
            // do nothing
        }
        return dataType;
    }

    /**
     * Set primary key
     *
     * @param jdbcPrimaryKeys jdbc primary keys
     * @param jdbcColumn column
     * @param columnGuid guid
     * @param primaryKeyProperties primary key properties
     */
    private void updateOrRemovePrimaryKey(List<JdbcPrimaryKey> jdbcPrimaryKeys, JdbcColumn jdbcColumn, String columnGuid,
                                          PrimaryKeyProperties primaryKeyProperties)
    {
        Optional<JdbcPrimaryKey> jdbcPrimaryKey = jdbcPrimaryKeys.stream().filter(
                key -> (key.getTableCat() == null ? jdbcColumn.getTableCat() == null : key.getTableCat().equals(jdbcColumn.getTableCat()))
                        && (key.getTableSchem() == null ? jdbcColumn.getTableSchem() == null : key.getTableSchem().equals(jdbcColumn.getTableSchem()))
                        && key.getTableName().equals(jdbcColumn.getTableName())
                        && key.getColumnName().equals(jdbcColumn.getColumnName())
        ).findFirst();

        if (jdbcPrimaryKey.isEmpty())
        {
            if (primaryKeyProperties != null)
            {
                omas.removePrimaryKey(columnGuid);
                auditLog.logMessage("Primary key removed from column with guid " + columnGuid, null);
            }

            return;
        }

        primaryKeyProperties = buildPrimaryKeyProperties(jdbcPrimaryKey.get());
        omas.setPrimaryKey(columnGuid, primaryKeyProperties);
        auditLog.logMessage("Primary key set on column with guid " + columnGuid, null);
    }


    /**
     * Remove foreign key relationship
     *
     * @param databaseColumnElement column element
     */
    private void removeForeignKey(OpenMetadataRootElement databaseColumnElement)
    {
        String foreignKeyGuid = null; // todo databaseColumnElement.getReferencedColumnGUID();

        if (foreignKeyGuid == null)
        {
            return;
        }

        String primaryKeyGuid = databaseColumnElement.getElementHeader().getGUID();
        omas.removeForeignKey(primaryKeyGuid, foreignKeyGuid);
    }

    /**
     * Build primary key properties
     *
     * @param jdbcPrimaryKey primary key
     *
     * @return properties
     */
    private PrimaryKeyProperties buildPrimaryKeyProperties(JdbcPrimaryKey jdbcPrimaryKey)
    {
        PrimaryKeyProperties properties = new PrimaryKeyProperties();
        properties.setDisplayName(jdbcPrimaryKey.getPkName());

        return properties;
    }

}
