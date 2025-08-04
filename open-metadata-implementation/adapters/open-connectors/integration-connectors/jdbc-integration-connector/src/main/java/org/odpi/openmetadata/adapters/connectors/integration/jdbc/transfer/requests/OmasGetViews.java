/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS;

/**
 * Manages the getViewsForDatabaseAsset call to access service
 */
class OmasGetViews implements Function<String, List<OpenMetadataRootElement>>
{
    private final AssetClient           dataAssetClient;
    private final SchemaAttributeClient databaseViewClient;
    private final AuditLog              auditLog;

    OmasGetViews(AssetClient           dataAssetClient,
                 SchemaAttributeClient databaseViewClient,
                 AuditLog              auditLog)
    {
        this.dataAssetClient    = dataAssetClient;
        this.databaseViewClient = databaseViewClient;
        this.auditLog           = auditLog;
    }


    /**
     * Get views of schema
     *
     * @param assetGuid database or schema guid
     *
     * @return tables
     */
    @Override
    public List<OpenMetadataRootElement> apply(String assetGuid)
    {
        final String methodName = "OmasGetViews";

        try
        {
            OpenMetadataRootElement dataAsset = dataAssetClient.getAssetByGUID(assetGuid, dataAssetClient.getGetOptions());

            if (dataAsset.getRootSchemaType() == null)
            {
                return new ArrayList<>();
            }
            else
            {
                // todo limit search to only schema attributes with the CalculatedValue classification
                return Optional.ofNullable(
                        databaseViewClient.getAttributesForSchemaType(dataAsset.getRootSchemaType().getRelatedElement().getElementHeader().getGUID(),
                                                                      databaseViewClient.getQueryOptions())).orElseGet(ArrayList::new);
            }
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Reading views for assetGuid: " + assetGuid,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }

        return new ArrayList<>();
    }

}
