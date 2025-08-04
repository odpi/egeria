/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

import java.util.function.Consumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS;

/**
 * Manages the removeDatabaseSchema call to access service
 */
class OmasRemoveSchema implements Consumer<OpenMetadataRootElement>
{

    private final AssetClient databaseSchemaClient;
    private final AuditLog    auditLog;

    OmasRemoveSchema(AssetClient databaseSchemaClient,
                     AuditLog    auditLog)
    {
        this.databaseSchemaClient = databaseSchemaClient;
        this.auditLog             = auditLog;
    }

    /**
     * Remove schema
     *
     * @param schemaElement schema
     */
    @Override
    public void accept(OpenMetadataRootElement schemaElement)
    {
        String schemaGuid = schemaElement.getElementHeader().getGUID();
        String schemaQualifiedName = databaseSchemaClient.getQualifiedName(schemaElement);
        try
        {
            databaseSchemaClient.deleteAsset(schemaGuid, databaseSchemaClient.getDeleteOptions(true));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage("Removing schema with guid " + schemaGuid
                    + " and qualified name " + schemaQualifiedName,
                    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(schemaGuid, schemaQualifiedName));
        }
    }

}
