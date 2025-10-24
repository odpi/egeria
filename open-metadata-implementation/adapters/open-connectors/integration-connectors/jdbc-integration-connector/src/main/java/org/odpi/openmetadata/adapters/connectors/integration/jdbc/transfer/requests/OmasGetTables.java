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
 * Manages the getTablesForDatabaseAsset call to access service
 */
class OmasGetTables implements Function<String, List<OpenMetadataRootElement>>
{

    private final AssetClient           dataAssetClient;
    private final SchemaAttributeClient databaseTableClient;
    private final AuditLog              auditLog;


    OmasGetTables(AssetClient           dataAssetClient,
                  SchemaAttributeClient databaseTableClient,
                  AuditLog              auditLog)
    {
        this.dataAssetClient     = dataAssetClient;
        this.databaseTableClient = databaseTableClient;
        this.auditLog            = auditLog;
    }


    /**
     * Get tables of schema
     *
     * @param assetGuid database or schema guid
     *
     * @return tables
     */
    @Override
    public List<OpenMetadataRootElement> apply(String assetGuid)
    {
        final String methodName = "OmasGetTables";

        try
        {
            OpenMetadataRootElement dataAsset = dataAssetClient.getAssetByGUID(assetGuid, dataAssetClient.getGetOptions());

            if (dataAsset.getSchemaType() == null)
            {
                return new ArrayList<>();
            }
            else
            {
                // todo limit search to only schema attributes without the CalculatedValue classification
                return Optional.ofNullable(
                        databaseTableClient.getAttributesForSchemaType(dataAsset.getSchemaType().getRelatedElement().getElementHeader().getGUID(),
                                                                       databaseTableClient.getQueryOptions())).orElseGet(ArrayList::new);
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
