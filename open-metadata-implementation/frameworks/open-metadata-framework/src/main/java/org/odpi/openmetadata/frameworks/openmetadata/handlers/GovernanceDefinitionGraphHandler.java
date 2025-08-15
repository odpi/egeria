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

    /**
     * Add a standard mermaid graph to the root element.  This method may be overridden by the subclasses if
     * they have a more fancy graph to display.
     *
     * @param userId calling user
     * @param rootElement new root element
     * @param queryOptions options from the caller
     * @return root element with graph
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected OpenMetadataRootElement addMermaidToRootElement(String                  userId,
                                                              OpenMetadataRootElement rootElement,
                                                              QueryOptions            queryOptions) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        if (rootElement != null)
        {
            List<RelatedMetadataElementSummary> parentElements1 = super.getElementHierarchies(userId,
                                                                                       rootElement.getElementHeader().getGUID(),
                                                                                       rootElement.getSupportingGovernanceDefinitions(),
                                                                                       2,
                                                                                       OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                                                                                       queryOptions,
                                                                                       1);

            List<RelatedMetadataElementSummary> parentElements2 = super.getElementHierarchies(userId,
                                                                                              rootElement.getElementHeader().getGUID(),
                                                                                              rootElement.getSupportingGovernanceDefinitions(),
                                                                                              2,
                                                                                              OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName,
                                                                                              queryOptions,
                                                                                              1);

            if (parentElements1 != null)
            {
                if (parentElements2 != null)
                {
                    parentElements1.addAll(parentElements2);
                }

                rootElement.setSupportingGovernanceDefinitions(parentElements1);
            }
            else if (parentElements2 != null)
            {
                rootElement.setSupportingGovernanceDefinitions(parentElements2);
            }

            List<RelatedMetadataElementSummary> peerElements1 = super.getElementHierarchies(userId,
                                                                                              rootElement.getElementHeader().getGUID(),
                                                                                              rootElement.getPeerGovernanceDefinitions(),
                                                                                              0,
                                                                                              OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName,
                                                                                              queryOptions,
                                                                                              1);

            List<RelatedMetadataElementSummary> peerElements2 = super.getElementHierarchies(userId,
                                                                                              rootElement.getElementHeader().getGUID(),
                                                                                              rootElement.getPeerGovernanceDefinitions(),
                                                                                              0,
                                                                                              OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName,
                                                                                              queryOptions,
                                                                                              1);

            List<RelatedMetadataElementSummary> peerElements3 = super.getElementHierarchies(userId,
                                                                                            rootElement.getElementHeader().getGUID(),
                                                                                            rootElement.getPeerGovernanceDefinitions(),
                                                                                            0,
                                                                                            OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName,
                                                                                            queryOptions,
                                                                                            1);

            if (peerElements1 != null)
            {
                if (peerElements2 != null)
                {
                    if (peerElements3 != null)
                    {
                        peerElements2.addAll(peerElements3);
                    }

                    peerElements1.addAll(peerElements2);
                }

                rootElement.setPeerGovernanceDefinitions(peerElements1);
            }
            else if (parentElements2 != null)
            {
                if (peerElements3 != null)
                {
                    peerElements2.addAll(peerElements3);
                }

                rootElement.setPeerGovernanceDefinitions(peerElements2);
            }
            else if (peerElements3 != null)
            {
                rootElement.setPeerGovernanceDefinitions(peerElements3);
            }

            super.addMermaidToRootElement(userId, rootElement, queryOptions);
        }

        return rootElement;
    }
}
