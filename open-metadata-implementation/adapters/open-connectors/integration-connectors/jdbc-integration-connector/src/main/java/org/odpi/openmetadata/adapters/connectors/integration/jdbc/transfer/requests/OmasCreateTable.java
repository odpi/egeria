/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabaseTable call to access service
 */
class OmasCreateTable implements BiFunction<String, RelationalTableProperties, Optional<String>>
{
    private final OpenMetadataRootElement          anchorAsset;
    private final AssetClient           dataAssetClient;
    private final SchemaTypeClient      databaseSchemaTypeClient;
    private final SchemaAttributeClient databaseTableClient;
    private final AuditLog              auditLog;

    OmasCreateTable(OpenMetadataRootElement          anchorAsset,
                    AssetClient           dataAssetClient,
                    SchemaTypeClient      databaseSchemaTypeClient,
                    SchemaAttributeClient databaseTableClient,
                    AuditLog              auditLog)
    {
        this.anchorAsset         = anchorAsset;
        this.dataAssetClient     = dataAssetClient;
        this.databaseSchemaTypeClient = databaseSchemaTypeClient;
        this.databaseTableClient = databaseTableClient;
        this.auditLog            = auditLog;
    }

    /**
     * Create table in schema
     *
     * @param parentGuid schema guid
     * @param newTableProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String                    parentGuid,
                                  RelationalTableProperties newTableProperties)
    {
        String methodName = "OmasCreateTable";

        try
        {
            OpenMetadataRootElement dataAsset = dataAssetClient.getAssetByGUID(parentGuid, dataAssetClient.getGetOptions());

            if (dataAsset.getRootSchemaType() == null)
            {
                // ToDo - check schemaType attached; set up anchor; set up parent
                NewElementOptions newElementOptions = new NewElementOptions(dataAssetClient.getMetadataSourceOptions());

                newElementOptions.setParentGUID(parentGuid);
            }

            return Optional.empty(); // todo Optional.ofNullable(databaseTableClient.createSchemaAttribute(null, newTableProperties));
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException("Creating table with qualified name " + newTableProperties.getQualifiedName()
                    + " in parent with guid " + parentGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return Optional.empty();
    }

}
