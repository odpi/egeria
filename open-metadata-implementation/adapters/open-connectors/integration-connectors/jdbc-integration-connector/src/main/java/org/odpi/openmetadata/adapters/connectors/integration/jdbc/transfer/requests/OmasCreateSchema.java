/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabaseSchema call to access service
 */
class OmasCreateSchema implements BiFunction<String, DeployedDatabaseSchemaProperties, Optional<String>>
{
    private final AssetClient databaseSchemaClient;
    private final AuditLog    auditLog;

    OmasCreateSchema(AssetClient databaseSchemaClient, AuditLog auditLog)
    {
        this.databaseSchemaClient = databaseSchemaClient;
        this.auditLog             = auditLog;
    }


    /**
     * Create schema in database
     *
     * @param databaseGuid database guid
     * @param newSchemaProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String                           databaseGuid,
                                  DeployedDatabaseSchemaProperties newSchemaProperties)
    {
        final String methodName = "OmasCreateSchema";

        try
        {
            NewElementOptions newElementOptions = new NewElementOptions(databaseSchemaClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(databaseGuid);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(databaseGuid);
            newElementOptions.setParentAtEnd1(false);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

            return Optional.ofNullable(databaseSchemaClient.createAsset(newElementOptions, null, newSchemaProperties, null));
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException("Creating schema with qualified name " + newSchemaProperties.getQualifiedName()
                    + " in database with guid " + databaseGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return Optional.empty();
    }

}
