/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentDependency;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IncidentImpactedElement;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * StewardshipActionInterface defines methods that are used to request help for a situation that the caller can not handle.
 */
public interface StewardshipActionInterface
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
     * @param toDoQualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param assignTo qualified name of the PersonRole element for the recipient
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a to do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    String openToDo(String userId,
                    String toDoQualifiedName,
                    String title,
                    String instructions,
                    int    priority,
                    Date   dueDate,
                    String assignTo) throws InvalidParameterException,
                                            UserNotAuthorizedException,
                                            PropertyServerException;
}
