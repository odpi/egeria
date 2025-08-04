/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcTable;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.CalculatedValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT;

/**
 * Transfers metadata of a view. Its parent can be a schema or directly a database, even though it is not explicitly enforced
 */
public class ViewTransfer implements Function<JdbcTable, OpenMetadataRootElement>
{
    private final Omas omas;
    private final OpenMetadataRootElement anchorAsset;
    private final AuditLog auditLog;
    private final List<OpenMetadataRootElement> omasViews;
    private final String parentQualifiedName;
    private final String parentGuid;

    public ViewTransfer(Omas                         omas,
                        OpenMetadataRootElement                 anchorAsset,
                        AuditLog                     auditLog,
                        List<OpenMetadataRootElement> omasViews,
                        String                       parentQualifiedName,
                        String                       parentGuid)
    {
        this.omas = omas;
        this.anchorAsset = anchorAsset;
        this.auditLog = auditLog;
        this.omasViews = omasViews;
        this.parentQualifiedName = parentQualifiedName;
        this.parentGuid = parentGuid;
    }

    /**
     * Triggers view metadata transfer
     *
     * @param jdbcTable view
     *
     * @return view element
     */
    @Override
    public OpenMetadataRootElement apply(JdbcTable jdbcTable)
    {
        // todo extend JdbcTable to include the SQL query that distinguishes a table from a view.
        // todo This needs to be packed into a CalculatedValueProperties object that is passed to both
        RelationalTableProperties viewProperties = this.buildViewProperties(jdbcTable);
        CalculatedValueProperties calculatedValueProperties = null; // todo
        Optional<OpenMetadataRootElement> omasView = omasViews.stream()
                .filter(dve -> omas.getQualifiedName(dve).equals(viewProperties.getQualifiedName()))
                .findFirst();

        if (omasView.isPresent())
        {
            omas.updateView(omasView.get().getElementHeader().getGUID(), viewProperties, calculatedValueProperties);
            auditLog.logMessage("Updated view with qualified name " + viewProperties.getQualifiedName(),
                    TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("view " + viewProperties.getQualifiedName()));
            return omasView.get();
        }

        omas.createView(anchorAsset, parentGuid, viewProperties, calculatedValueProperties);
        auditLog.logMessage("Created view with qualified name " + viewProperties.getQualifiedName(),
                TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("view " + viewProperties.getQualifiedName()));
        return null;
    }

    /**
     * Build view properties
     *
     * @param jdbcTable view
     *
     * @return properties
     */
    private RelationalTableProperties buildViewProperties(JdbcTable jdbcTable)
    {
        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put(Jdbc.JDBC_CATALOG_KEY, jdbcTable.getTableCat());
        additionalProperties.put(Jdbc.JDBC_SCHEMA_KEY, jdbcTable.getTableSchem());
        additionalProperties.put(Jdbc.JDBC_TABLE_KEY, jdbcTable.getTableName());
        additionalProperties.put(Jdbc.JDBC_TABLE_TYPE_KEY, jdbcTable.getTableType());

        RelationalTableProperties jdbcViewProperties = new RelationalTableProperties();
        jdbcViewProperties.setDisplayName(jdbcTable.getTableName());
        jdbcViewProperties.setQualifiedName(parentQualifiedName + "::" + jdbcTable.getTableName());
        jdbcViewProperties.setAdditionalProperties(additionalProperties);
        return jdbcViewProperties;
    }

}
