/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS;

/**
 * Manages the getConnectorTypesByName call to access service
 */
class OmasGetConnectorTypesByName implements Function<String, List<ConnectorTypeElement>> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasGetConnectorTypesByName(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Get connector types by name
     *
     * @param connectorTypeQualifiedName qualified name
     *
     * @return connector types
     */
    @Override
    public List<ConnectorTypeElement> apply(String connectorTypeQualifiedName){
        String methodName = "OmasGetConnectorTypesByName";
        try{
            return Optional.ofNullable(
                    databaseIntegratorContext.getConnectorTypesByName(connectorTypeQualifiedName, 0, 0))
                    .orElseGet(ArrayList::new);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            auditLog.logMessage("Reading connector type with qualified name " + connectorTypeQualifiedName,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()));
        }
        return new ArrayList<>();
    }

}
