/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.client.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenMetadataClient provides access to metadata elements stored in the metadata repositories.  It is implemented by a
 * metadata repository provider. In Egeria, this class is implemented in the GAF Metadata Management running in the
 * Metadata Access Server OMAG Server.
 */
public abstract class OpenGovernanceClient extends OpenMetadataClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     */
    public OpenGovernanceClient(String serviceURLMarker,
                                String serverName,
                                String serverPlatformURLRoot)
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);
    }

    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets map of action target names to GUIDs for the resulting governance service
     * @param startTime future start time or null for "as soon as possible"
     * @param requestParameters request properties to be passed to the first engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the first governance action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public abstract String initiateGovernanceActionProcess(String                userId,
                                                           String                processQualifiedName,
                                                           List<String>          requestSourceGUIDs,
                                                           List<NewActionTarget> actionTargets,
                                                           Date                  startTime,
                                                           Map<String, String>   requestParameters,
                                                           String                originatorServiceName,
                                                           String                originatorEngineName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException;


    /**
     * Create an incident report to capture the situation detected by this governance service.
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
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract String createIncidentReport(String                        userId,
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataClient{" +
                "serverName='" + serverName + '\'' +
                ", serviceURLMarker='" + serviceURLMarker + '\'' +
                ", serverPlatformURLRoot='" + serverPlatformURLRoot + '\'' +
                '}';
    }
}
