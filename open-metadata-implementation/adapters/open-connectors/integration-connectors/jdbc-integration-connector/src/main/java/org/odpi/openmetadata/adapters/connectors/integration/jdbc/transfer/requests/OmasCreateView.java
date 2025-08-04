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
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.CalculatedValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabaseView call to access service
 */
class OmasCreateView implements BiFunction<String, RelationalTableProperties, Optional<String>>
{
    private final OpenMetadataRootElement              anchorAsset;
    private final CalculatedValueProperties calculatedValueProperties;
    private final AssetClient               dataAssetClient;
    private final SchemaTypeClient          databaseSchemaTypeClient;
    private final SchemaAttributeClient     databaseViewClient;
    private final AuditLog                  auditLog;

    OmasCreateView(OpenMetadataRootElement              anchorAsset,
                   CalculatedValueProperties calculatedValueProperties,
                   AssetClient               dataAssetClient,
                   SchemaTypeClient          databaseSchemaTypeClient,
                   SchemaAttributeClient     databaseViewClient,
                   AuditLog                  auditLog)
    {
        this.anchorAsset               = anchorAsset;
        this.calculatedValueProperties = calculatedValueProperties;
        this.dataAssetClient           = dataAssetClient;
        this.databaseSchemaTypeClient  = databaseSchemaTypeClient;
        this.databaseViewClient        = databaseViewClient;
        this.auditLog                  = auditLog;
    }


    /**
     * Create table in schema
     *
     * @param parentGuid schema guid
     * @param newViewProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String                    parentGuid,
                                  RelationalTableProperties newViewProperties)
    {
        final String methodName = "OmasCreateView";

        try
        {
            // ToDo - check schemaType attached; set up anchor; set up parent; set up initial classifications

            NewElementOptions newElementOptions = new NewElementOptions(databaseViewClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(anchorAsset.getElementHeader().getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(parentGuid);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName);

            return Optional.ofNullable(databaseViewClient.createSchemaAttribute(newElementOptions,
                                                                                null,
                                                                                newViewProperties,
                                                                                null));
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException("Creating view with qualified name " + newViewProperties.getQualifiedName()
                    + " in parent with guid " + parentGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return Optional.empty();
    }

}
