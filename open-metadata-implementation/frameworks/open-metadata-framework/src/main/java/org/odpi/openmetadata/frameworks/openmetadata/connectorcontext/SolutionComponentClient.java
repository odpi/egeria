/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SolutionComponentHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with solution component elements.
 */
public class SolutionComponentClient extends ConnectorContextClientBase
{
    private final SolutionComponentHandler solutionComponentHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public SolutionComponentClient(ConnectorContextBase     parentContext,
                                   String                   localServerName,
                                   String                   localServiceName,
                                   String                   connectorUserId,
                                   String                   connectorGUID,
                                   String                   externalSourceGUID,
                                   String                   externalSourceName,
                                   OpenMetadataClient       openMetadataClient,
                                   AuditLog                 auditLog,
                                   int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.solutionComponentHandler = new SolutionComponentHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new data class.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSolutionComponent(NewElementOptions                     newElementOptions,
                                          Map<String, ClassificationProperties> initialClassifications,
                                          SolutionComponentProperties           properties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        String elementGUID = solutionComponentHandler.createSolutionComponent(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a solutionComponent using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solutionComponent.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing solutionComponent to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSolutionComponentFromTemplate(TemplateOptions        templateOptions,
                                                      String                 templateGUID,
                                                      ElementProperties      replacementProperties,
                                                      Map<String, String>    placeholderProperties,
                                                      RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        String elementGUID = solutionComponentHandler.createSolutionComponentFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a data class.
     *
     * @param solutionComponentGUID       unique identifier of the solutionComponent (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSolutionComponent(String                      solutionComponentGUID,
                                           UpdateOptions               updateOptions,
                                           SolutionComponentProperties properties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        boolean updateOccurred = solutionComponentHandler.updateSolutionComponent(connectorUserId, solutionComponentGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(solutionComponentGUID);
        }

        return updateOccurred;
    }



    /**
     * Attach a solution component to a solution component.
     *
     * @param parentSolutionComponentGUID unique identifier of the parent
     * @param solutionComponentGUID     unique identifier of the solution component
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubcomponent(String                        parentSolutionComponentGUID,
                                 String                        solutionComponentGUID,
                                 MetadataSourceOptions         metadataSourceOptions,
                                 SolutionCompositionProperties relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        solutionComponentHandler.linkSubcomponent(connectorUserId, parentSolutionComponentGUID, solutionComponentGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a solution component from a solution component.
     *
     * @param parentSolutionComponentGUID    unique identifier of the parent solution component.
     * @param solutionComponentGUID    unique identifier of the solution component.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubcomponent(String        parentSolutionComponentGUID,
                                   String        solutionComponentGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        solutionComponentHandler.detachSubcomponent(connectorUserId, parentSolutionComponentGUID, solutionComponentGUID, deleteOptions);
    }


    /**
     * Attach an element communicating with a solution component.
     *
     * @param solutionComponentOneGUID unique identifier of the solution component at end 1
     * @param solutionComponentTwoGUID unique identifier of the solution component at end 2
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionLinkingWire(String                        solutionComponentOneGUID,
                                        String                        solutionComponentTwoGUID,
                                        MetadataSourceOptions         metadataSourceOptions,
                                        SolutionLinkingWireProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        solutionComponentHandler.linkSolutionLinkingWire(connectorUserId, solutionComponentOneGUID, solutionComponentTwoGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an element communicating with a solution component.
     *
     * @param solutionComponentOneGUID unique identifier of the solution component at end 1
     * @param solutionComponentTwoGUID unique identifier of the solution component at end 2
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionLinkingWire(String        solutionComponentOneGUID,
                                          String        solutionComponentTwoGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        solutionComponentHandler.detachSolutionLinkingWire(connectorUserId, solutionComponentOneGUID, solutionComponentTwoGUID, deleteOptions);
    }


    /**
     * Attach a solution component to an actor role.
     *
     * @param solutionRoleGUID unique identifier of the parent
     * @param solutionComponentGUID     unique identifier of the solution component
     * @param relationshipProperties  description of the relationship.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionComponentActor(String                           solutionRoleGUID,
                                           String                           solutionComponentGUID,
                                           MetadataSourceOptions            metadataSourceOptions,
                                           SolutionComponentActorProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        solutionComponentHandler.linkSolutionComponentActor(connectorUserId, solutionRoleGUID, solutionComponentGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a solution component from an actor role.
     *
     * @param solutionRoleGUID    unique identifier of the parent solution component.
     * @param solutionComponentGUID    unique identifier of the nested solution component.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionComponentActor(String        solutionRoleGUID,
                                             String        solutionComponentGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        solutionComponentHandler.detachSolutionComponentActor(connectorUserId, solutionRoleGUID, solutionComponentGUID, deleteOptions);
    }


    /**
     * Delete a solutionComponent.
     *
     * @param solutionComponentGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSolutionComponent(String        solutionComponentGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        solutionComponentHandler.deleteSolutionComponent(connectorUserId, solutionComponentGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(solutionComponentGUID);
        }
    }


    /**
     * Returns the list of solution components with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSolutionComponentsByName(String       name,
                                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        return solutionComponentHandler.getSolutionComponentsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific data class.
     *
     * @param solutionComponentGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSolutionComponentByGUID(String     solutionComponentGUID,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return solutionComponentHandler.getSolutionComponentByGUID(connectorUserId, solutionComponentGUID, getOptions);
    }


    /**
     * Retrieve the list of solutionComponents metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned solutionComponents include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSolutionComponents(String        searchString,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return solutionComponentHandler.findSolutionComponents(connectorUserId, searchString, searchOptions);
    }
}
