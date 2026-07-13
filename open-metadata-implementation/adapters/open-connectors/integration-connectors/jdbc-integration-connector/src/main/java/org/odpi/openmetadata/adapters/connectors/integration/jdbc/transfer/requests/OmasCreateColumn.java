/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.NestedSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabaseColumn call to access service
 */
class OmasCreateColumn implements BiFunction<String, RelationalColumnProperties, Optional<String>>
{
    private final OpenMetadataRootElement anchorAsset;
    private final SchemaAttributeClient   databaseColumnClient;
    private final AuditLog                auditLog;

    OmasCreateColumn(OpenMetadataRootElement anchorAsset,
                     SchemaAttributeClient   databaseColumnClient,
                     AuditLog                auditLog)
    {
        this.anchorAsset               = anchorAsset;
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Create column in table
     *
     * @param tableGuid table guid
     * @param newColumnProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String tableGuid, RelationalColumnProperties newColumnProperties)
    {
        final String methodName = "OmasCreateColumn";

        try
        {
            NewElementOptions newElementOptions = new NewElementOptions(databaseColumnClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(anchorAsset.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(tableGuid);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);

            NestedSchemaAttributeProperties nestedSchemaAttributeProperties = new NestedSchemaAttributeProperties();
            nestedSchemaAttributeProperties.setMaxCardinality(1);
            nestedSchemaAttributeProperties.setMinCardinality(1);

            return Optional.ofNullable(databaseColumnClient.createSchemaAttribute(newElementOptions,
                                                                                null,
                                                                                newColumnProperties,
                                                                                nestedSchemaAttributeProperties));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Creating column with qualified name " + newColumnProperties.getQualifiedName()
                    + " in table with guid " + tableGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(e.getClass().getName(), methodName, e.getMessage()), e);
        }

        return Optional.empty();
    }



}
