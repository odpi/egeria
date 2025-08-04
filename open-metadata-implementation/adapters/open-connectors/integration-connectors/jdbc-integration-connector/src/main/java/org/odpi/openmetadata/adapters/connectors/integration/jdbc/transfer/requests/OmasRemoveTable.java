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
class OmasRemoveTable implements Consumer<OpenMetadataRootElement>
{
    private final SchemaAttributeClient databaseTableClient;
    private final AuditLog              auditLog;

    OmasRemoveTable(SchemaAttributeClient databaseTableClient,
                    AuditLog              auditLog)
    {
        this.databaseTableClient = databaseTableClient;
        this.auditLog            = auditLog;
    }

    /**
     * Remove table
     *
     * @param tableElement table
     */
    @Override
    public void accept(OpenMetadataRootElement tableElement)
    {
        String tableGuid = tableElement.getElementHeader().getGUID();
        String tableQualifiedName = databaseTableClient.getQualifiedName(tableElement);
        try
        {
            databaseTableClient.deleteSchemaAttribute(tableGuid, databaseTableClient.getDeleteOptions(true));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage("Removing table with guid " + tableGuid
                    + " and qualified name " + tableQualifiedName,
                    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(tableGuid, tableQualifiedName));
        }
    }
}
