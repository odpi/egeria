/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseColumnElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseForeignKeyProperties;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcForeignKey;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.List;
import java.util.function.Consumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT;

/**
 * Transfers metadata of a foreign key
 */
public class ForeignKeyTransfer implements Consumer<JdbcForeignKey> {

    private final Omas omas;
    private final AuditLog        auditLog;
    private final DatabaseElement database;

    public ForeignKeyTransfer(Omas omas, AuditLog auditLog, DatabaseElement database) {
        this.omas = omas;
        this.auditLog = auditLog;
        this.database = database;
    }

    /**
     * Triggers foreign key metadata transfer
     *
     * @param jdbcForeignKey foreign key
     */
    @Override
    public void accept(JdbcForeignKey jdbcForeignKey) {
        String databaseQualifiedName = database.getDatabaseProperties().getQualifiedName();
        String pkColumnQualifiedName = databaseQualifiedName
                + (jdbcForeignKey.getPkTableSchem() == null ? "" : "::" + jdbcForeignKey.getPkTableSchem() )
                + "::" + jdbcForeignKey.getPkTableName() + "::" + jdbcForeignKey.getPkColumnName();
        String fkColumnQualifiedName = databaseQualifiedName
                + ( jdbcForeignKey.getFkTableSchem() == null ? "" : "::" + jdbcForeignKey.getFkTableSchem())
                + "::" + jdbcForeignKey.getFkTableName() + "::" + jdbcForeignKey.getFkColumnName();

        DatabaseColumnElement pkColumn = determineColumn(omas.findDatabaseColumns(pkColumnQualifiedName));
        DatabaseColumnElement fkColumn = determineColumn(omas.findDatabaseColumns(fkColumnQualifiedName));

        if(pkColumn == null || fkColumn == null){
            return;
        }

        String pkColumnGuid = pkColumn.getElementHeader().getGUID();
        String fkColumnGuid = fkColumn.getElementHeader().getGUID();
        omas.setForeignKey(pkColumnGuid, fkColumnGuid, buildForeignKeyProperties(jdbcForeignKey));

        auditLog.logMessage("Foreign key set from column with guid " + pkColumnGuid + " to column with guid " + fkColumnGuid,
                TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("foreign key relationship from " + pkColumnGuid + " to " + fkColumnGuid));
    }

    /**
     * Extract item from list
     *
     * @param columns columns
     *
     * @return item at index 0 if size is 1 otherwise null
     */
    private DatabaseColumnElement determineColumn(List<DatabaseColumnElement> columns){
        if(columns.size() == 1){
            return columns.get(0);
        }
        return null;
    }

    /**
     * Build foreign key properties
     *
     * @param jdbcForeignKey foreign key
     *
     * @return properties
     */
    private DatabaseForeignKeyProperties buildForeignKeyProperties(JdbcForeignKey jdbcForeignKey){
        DatabaseForeignKeyProperties properties = new DatabaseForeignKeyProperties();
        properties.setName(jdbcForeignKey.getPkName() + " - " + jdbcForeignKey.getFkName());
        properties.setSource(database.getDatabaseProperties().getDatabaseImportedFrom());
        return properties;
    }
}
