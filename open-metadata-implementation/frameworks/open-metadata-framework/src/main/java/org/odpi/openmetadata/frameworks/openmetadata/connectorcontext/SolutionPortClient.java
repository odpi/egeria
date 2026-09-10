/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SolutionPortHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortDelegationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;
import java.util.Map;

/**
 * SolutionPortClient provides methods to define solution ports and attach them to the solution components that
 * expose them, and to the ports of subcomponents that they delegate to.
 */
public class SolutionPortClient extends ConnectorContextClientBase
{
    private final SolutionPortHandler solutionPortHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext      connector's context
     * @param localServerName    local server where this client is running - called the local server
     * @param localServiceName   name of this service
     * @param connectorUserId    userId to use when issuing open metadata requests
     * @param connectorGUID      unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog           logging destination
     * @param maxPageSize        max number of elements that can be returned on a query
     */
    public SolutionPortClient(ConnectorContextBase parentContext,
                              String               localServerName,
                              String               localServiceName,
                              String               connectorUserId,
                              String               connectorGUID,
                              String               externalSourceGUID,
                              String               externalSourceName,
                              OpenMetadataClient   openMetadataClient,
                              AuditLog             auditLog,
                              int                  maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.solutionPortHandler = new SolutionPortHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new solution port.
     *
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSolutionPort(NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     SolutionPortProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        String elementGUID = solutionPortHandler.createSolutionPort(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a solution port using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solution port.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties that override the template
     * @param replacementClassifications   classifications that override the template
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSolutionPortFromTemplate(TemplateOptions                       templateOptions,
                                                 String                                templateGUID,
                                                 EntityProperties                      replacementProperties,
                                                 Map<String, ClassificationProperties> replacementClassifications,
                                                 Map<String, String>                   placeholderProperties,
                                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        String elementGUID = solutionPortHandler.createSolutionPortFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a solution port.
     *
     * @param solutionPortGUID unique identifier of the solution port (returned from create)
     * @param updateOptions    provides a structure for the additional options when updating an element.
     * @param properties       properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSolutionPort(String                 solutionPortGUID,
                                      UpdateOptions          updateOptions,
                                      SolutionPortProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        boolean updateOccurred = solutionPortHandler.updateSolutionPort(connectorUserId, solutionPortGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(solutionPortGUID);
        }

        return updateOccurred;
    }


    /**
     * Delete a solution port.
     *
     * @param solutionPortGUID unique identifier of the element
     * @param deleteOptions    options for a delete request
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSolutionPort(String        solutionPortGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        solutionPortHandler.deleteSolutionPort(connectorUserId, solutionPortGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(solutionPortGUID);
        }
    }


    /**
     * Returns the list of solution ports with a particular name.
     *
     * @param name         name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSolutionPortsByName(String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        return solutionPortHandler.getSolutionPortsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific solution port.
     *
     * @param solutionPortGUID unique identifier of the required element
     * @param getOptions       multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSolutionPortByGUID(String     solutionPortGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return solutionPortHandler.getSolutionPortByGUID(connectorUserId, solutionPortGUID, getOptions);
    }


    /**
     * Retrieve the list of solution ports that contain the search string.
     *
     * @param searchString  string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSolutionPorts(String        searchString,
                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return solutionPortHandler.findSolutionPorts(connectorUserId, searchString, searchOptions);
    }


    /**
     * Attach a solution port to the solution component that exposes it.
     *
     * @param solutionComponentGUID  unique identifier of the solution component
     * @param solutionPortGUID       unique identifier of the solution port
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionComponentPort(String                          solutionComponentGUID,
                                          String                          solutionPortGUID,
                                          MakeAnchorOptions               makeAnchorOptions,
                                          SolutionComponentPortProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        solutionPortHandler.linkSolutionComponentPort(connectorUserId, solutionComponentGUID, solutionPortGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a solution port from the solution component that exposed it.
     *
     * @param solutionComponentGUID unique identifier of the solution component
     * @param solutionPortGUID      unique identifier of the solution port
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionComponentPort(String        solutionComponentGUID,
                                            String        solutionPortGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        solutionPortHandler.detachSolutionComponentPort(connectorUserId, solutionComponentGUID, solutionPortGUID, deleteOptions);
    }


    /**
     * Attach a solution port to the solution port that it delegates to.
     *
     * @param alignsToPortGUID       unique identifier of the solution port that is aligned to
     * @param delegationPortGUID     unique identifier of the solution port that delegates
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionPortDelegation(String                           alignsToPortGUID,
                                           String                           delegationPortGUID,
                                           MakeAnchorOptions                makeAnchorOptions,
                                           SolutionPortDelegationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        solutionPortHandler.linkSolutionPortDelegation(connectorUserId, alignsToPortGUID, delegationPortGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a solution port from the solution port that it delegated to.
     *
     * @param alignsToPortGUID   unique identifier of the solution port that is aligned to
     * @param delegationPortGUID unique identifier of the solution port that delegates
     * @param deleteOptions      options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionPortDelegation(String        alignsToPortGUID,
                                             String        delegationPortGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        solutionPortHandler.detachSolutionPortDelegation(connectorUserId, alignsToPortGUID, delegationPortGUID, deleteOptions);
    }
}
