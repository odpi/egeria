/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentDependency;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentImpactedElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * StewardshipRequestInterface defines methods that are used to request help for a situation that the caller can not handle.
 */
public interface StewardshipRequestInterface
{
    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createIncidentReport(String                        userId,
                                String                        qualifiedName,
                                int                           domainIdentifier,
                                String                        background,
                                List<IncidentImpactedElement> impactedResources,
                                List<IncidentDependency>      previousIncidents,
                                Map<String, Integer>          incidentClassifiers,
                                Map<String, String>           additionalProperties,
                                String                        originatorGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Create a "To Do" request for someone to work on.
     *
     * @param userId caller's userId
     * @param qualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param category a category of to dos (for example, "data error", "access request")
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param assignTo qualified name of the Actor element for the recipient
     * @param causeGUID unique identifier of the element that describes the rule, project that this is on behalf of
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new to do element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a "to do" entity
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    String openToDo(String                userId,
                    String                qualifiedName,
                    String                title,
                    String                instructions,
                    String                category,
                    int                   priority,
                    Date                  dueDate,
                    Map<String, String>   additionalProperties,
                    String                assignTo,
                    String                causeGUID,
                    List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;
}
