/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.EndpointClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS;

/**
 * Manages the findEndpoints call to access service
 */
class OmasFindEndpoints implements Function<String, List<OpenMetadataRootElement>>
{
    private final EndpointClient endpointClient;
    private final AuditLog       auditLog;

    OmasFindEndpoints(EndpointClient endpointClient, AuditLog auditLog)
    {
        this.endpointClient = endpointClient;
        this.auditLog       = auditLog;
    }

    /**
     * Find endpoints
     *
     * @param searchBy criteria
     *
     * @return endpoints
     */
    @Override
    public List<OpenMetadataRootElement> apply(String searchBy)
    {
        final String methodName = "OmasFindEndpoints";

        try
        {
            return Optional.ofNullable(
                            endpointClient.findEndpoints(searchBy, endpointClient.getSearchOptions()))
                    .orElseGet(ArrayList::new);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Reading endpoints with name " + searchBy,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }

        return new ArrayList<>();
    }
}
