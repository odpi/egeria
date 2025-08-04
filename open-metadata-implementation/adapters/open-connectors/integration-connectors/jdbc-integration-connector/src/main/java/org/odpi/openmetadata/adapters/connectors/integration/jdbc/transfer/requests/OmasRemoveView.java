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
 * Manages the removeDatabaseTable call to access service
 */
class OmasRemoveView implements Consumer<OpenMetadataRootElement>
{

    private final SchemaAttributeClient databaseViewClient;
    private final AuditLog              auditLog;

    OmasRemoveView(SchemaAttributeClient databaseViewClient,
                   AuditLog              auditLog)
    {
        this.databaseViewClient = databaseViewClient;
        this.auditLog           = auditLog;
    }

    /**
     * Remove table
     *
     * @param viewElement view
     */
    @Override
    public void accept(OpenMetadataRootElement viewElement)
    {
        String viewGuid = viewElement.getElementHeader().getGUID();

        try
        {
            databaseViewClient.deleteSchemaAttribute(viewGuid, databaseViewClient.getDeleteOptions(true));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage("Removing view with guid " + viewGuid,
                    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(viewGuid, "null"));
        }
    }

}
