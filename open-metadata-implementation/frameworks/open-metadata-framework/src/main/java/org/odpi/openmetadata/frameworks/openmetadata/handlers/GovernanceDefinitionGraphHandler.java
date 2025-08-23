/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;


/**
 * GovernanceDefinitionGraphHandler is the handler for managing governance definitions in context.
 */
public class GovernanceDefinitionGraphHandler extends GovernanceDefinitionHandler
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public GovernanceDefinitionGraphHandler(String             localServerName,
                                            AuditLog           auditLog,
                                            String             serviceName,
                                            OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient);
    }


    /**
     * Retrieve the definition metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the requested metadata element
     * @param queryOptions           multiple options to control the query
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElement getGovernanceDefinitionInContext(String       userId,
                                                                    String       governanceDefinitionGUID,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getGovernanceDefinitionInContext";

        return super.getRootElementByGUID(userId, governanceDefinitionGUID, queryOptions, methodName);
    }


    /*
     * Converter functions
     */

}
