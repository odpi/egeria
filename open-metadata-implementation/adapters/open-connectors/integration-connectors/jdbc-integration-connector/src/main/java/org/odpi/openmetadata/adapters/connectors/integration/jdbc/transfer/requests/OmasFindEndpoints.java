/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS;

/**
 * Manages the findEndpoints call to access service
 */
class OmasFindEndpoints implements Function<String, List<EndpointElement>> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasFindEndpoints(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Find endpoints
     *
     * @param searchBy criteria
     *
     * @return endpoints
     */
    @Override
    public List<EndpointElement> apply(String searchBy){
        String methodName = "OmasFindEndpoints";
        try{
            return Optional.ofNullable(
                    databaseIntegratorContext.findEndpoints(searchBy, 0, 0))
                    .orElseGet(ArrayList::new);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            auditLog.logException("Reading endpoints with name " + searchBy,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return new ArrayList<>();
    }

}
