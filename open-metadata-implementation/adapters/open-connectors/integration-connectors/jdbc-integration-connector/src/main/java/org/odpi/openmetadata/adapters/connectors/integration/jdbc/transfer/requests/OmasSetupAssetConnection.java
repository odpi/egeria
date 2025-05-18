/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the setupAssetConnection call to access service
 */
class OmasSetupAssetConnection implements TriConsumer<String, String, String> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasSetupAssetConnection(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Setup asset connection
     *
     * @param assetGuid guid
     * @param assetSummary summary
     * @param connectionGuid guid
     */
    @Override
    public void accept(String assetGuid, String assetSummary, String connectionGuid){
        String methodName = "OmasSetupAssetConnection";
        try {
            databaseIntegratorContext.setupAssetConnection(assetGuid, connectionGuid);
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            auditLog.logException("Setting up asset connection for asset with guid " + assetGuid
                            + ", with summary  " + assetSummary
                            + ", and connection with guid " + connectionGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
