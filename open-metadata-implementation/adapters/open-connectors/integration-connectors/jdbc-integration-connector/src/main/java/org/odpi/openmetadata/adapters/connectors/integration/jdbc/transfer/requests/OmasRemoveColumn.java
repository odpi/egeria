/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

import java.util.function.Consumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS;

/**
 * Manages the removeDatabaseColumn call to access service
 */
class OmasRemoveColumn implements Consumer<OpenMetadataRootElement>
{

    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasRemoveColumn(SchemaAttributeClient databaseColumnClient, AuditLog auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Remove column
     *
     * @param columnElement column
     */
    @Override
    public void accept(OpenMetadataRootElement columnElement)
    {
        String columnGuid = columnElement.getElementHeader().getGUID();
        String columnQualifiedName = databaseColumnClient.getQualifiedName(columnElement);

        try
        {
            databaseColumnClient.deleteSchemaAttribute(columnGuid, databaseColumnClient.getDeleteOptions(false));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage("Removing column with guid " + columnGuid
                    + " and qualified name " + columnQualifiedName,
                    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(columnGuid, columnQualifiedName));
        }
    }

}
