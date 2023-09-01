/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseSchemaElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseSchemaProperties;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcSchema;
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
 * Transfers metadata of a schema
 */
public class SchemaTransfer implements Function<JdbcSchema, DatabaseSchemaElement> {

    private final Omas omas;
    private final AuditLog auditLog;
    private final List<DatabaseSchemaElement> omasSchemas;
    private final String databaseQualifiedName;
    private final String databaseGuid;

    public SchemaTransfer(Omas omas, AuditLog auditLog, List<DatabaseSchemaElement> omasSchemas, String databaseQualifiedName, String databaseGuid) {
        this.omas = omas;
        this.auditLog = auditLog;
        this.omasSchemas = omasSchemas;
        this.databaseQualifiedName = databaseQualifiedName;
        this.databaseGuid = databaseGuid;
    }

    /**
     * Triggers schema metadata transfer
     *
     * @param jdbcSchema schema
     *
     * @return schema element
     */
    @Override
    public DatabaseSchemaElement apply(JdbcSchema jdbcSchema) {
        DatabaseSchemaProperties schemaProperties = buildSchemaProperties(jdbcSchema);

        Optional<DatabaseSchemaElement> omasSchema = omasSchemas.stream()
                .filter(dse -> dse.getDatabaseSchemaProperties()
                        .getQualifiedName().equals(schemaProperties.getQualifiedName()))
                .findFirst();

        if (omasSchema.isPresent()) {
            omas.updateSchema(omasSchema.get().getElementHeader().getGUID(), schemaProperties);
            auditLog.logMessage("Updated schema with qualified name " + schemaProperties.getQualifiedName(),
                    TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("schema " + schemaProperties.getQualifiedName()));
            return omasSchema.get();
        }

        omas.createSchema(databaseGuid, schemaProperties);
        auditLog.logMessage("Created schema with qualified name " + schemaProperties.getQualifiedName(),
                TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("schema " + schemaProperties.getQualifiedName()));
        return null;
    }

    /**
     * Build schema properties
     *
     * @param jdbcSchema schema
     *
     * @return properties
     */
    private DatabaseSchemaProperties buildSchemaProperties(JdbcSchema jdbcSchema) {
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put(Jdbc.JDBC_CATALOG_KEY, jdbcSchema.getTableCatalog());
        additionalProperties.put(Jdbc.JDBC_SCHEMA_KEY, jdbcSchema.getTableSchem());

        DatabaseSchemaProperties jdbcSchemaProperties = new DatabaseSchemaProperties();
        jdbcSchemaProperties.setName(jdbcSchema.getTableSchem());
        jdbcSchemaProperties.setQualifiedName(databaseQualifiedName + "::" + jdbcSchema.getTableSchem());
        jdbcSchemaProperties.setAdditionalProperties(additionalProperties);
        return jdbcSchemaProperties;
    }

}
