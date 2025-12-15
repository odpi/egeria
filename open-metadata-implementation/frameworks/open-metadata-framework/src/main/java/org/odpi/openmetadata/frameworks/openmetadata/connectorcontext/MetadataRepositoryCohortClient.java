/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.MetadataRepositoryCohortHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataCohortPeerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataRepositoryCohortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with metadata repository cohort elements.
 */
public class MetadataRepositoryCohortClient extends ConnectorContextClientBase
{
    private final MetadataRepositoryCohortHandler metadataRepositoryCohortHandler;


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
    public MetadataRepositoryCohortClient(ConnectorContextBase     parentContext,
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

        this.metadataRepositoryCohortHandler = new MetadataRepositoryCohortHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new open metadata repository cohort.
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
    public String createMetadataRepositoryCohort(NewElementOptions                     newElementOptions,
                                                 Map<String, ClassificationProperties> initialClassifications,
                                                 MetadataRepositoryCohortProperties    properties,
                                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                            PropertyServerException,
                                                                                                                            UserNotAuthorizedException
    {
        String elementGUID = metadataRepositoryCohortHandler.createMetadataRepositoryCohort(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent an open metadata repository cohort using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new community.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing community to copy (this will copy all the attachments such as nested content, schema
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
    public String createMetadataRepositoryCohortFromTemplate(TemplateOptions        templateOptions,
                                                             String                 templateGUID,
                                                             EntityProperties       replacementProperties,
                                                             Map<String, String>    placeholderProperties,
                                                             RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         PropertyServerException
    {
        String elementGUID = metadataRepositoryCohortHandler.createMetadataRepositoryCohortFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of an open metadata repository cohort.
     *
     * @param metadataRepositoryCohortGUID       unique identifier of the community (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateMetadataRepositoryCohort(String                             metadataRepositoryCohortGUID,
                                                  UpdateOptions                      updateOptions,
                                                  MetadataRepositoryCohortProperties properties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        boolean updateOccurred = metadataRepositoryCohortHandler.updateMetadataRepositoryCohort(connectorUserId, metadataRepositoryCohortGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataRepositoryCohortGUID);
        }

        return updateOccurred;
    }


    /**
     * Delete a community.
     *
     * @param metadataRepositoryCohortGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteMetadataRepositoryCohort(String        metadataRepositoryCohortGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        metadataRepositoryCohortHandler.deleteMetadataRepositoryCohort(connectorUserId, metadataRepositoryCohortGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(metadataRepositoryCohortGUID);
        }
    }



    /**
     * Attach an open metadata cohort to a cohort member.
     *
     * @param metadataRepositoryCohortGUID             unique identifier of the cohort
     * @param cohortMemberGUID            unique identifier of the member
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkCohortToMember(String                       metadataRepositoryCohortGUID,
                                   String                       cohortMemberGUID,
                                   MakeAnchorOptions            makeAnchorOptions,
                                   MetadataCohortPeerProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        metadataRepositoryCohortHandler.linkCohortToMember(connectorUserId, metadataRepositoryCohortGUID, cohortMemberGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an open metadata cohort from a cohort member.
     *
     * @param metadataRepositoryCohortGUID             unique identifier of the cohort
     * @param cohortMemberGUID            unique identifier of the member
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCohortFromMember(String        metadataRepositoryCohortGUID,
                                       String        cohortMemberGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        metadataRepositoryCohortHandler.detachCohortFromMember(connectorUserId, metadataRepositoryCohortGUID, cohortMemberGUID, deleteOptions);
    }


    /**
     * Returns the list of open metadata repository cohorts with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getMetadataRepositoryCohortsByName(String       name,
                                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        return metadataRepositoryCohortHandler.getMetadataRepositoryCohortsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific open metadata repository cohort.
     *
     * @param metadataRepositoryCohortGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getMetadataRepositoryCohortByGUID(String     metadataRepositoryCohortGUID,
                                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        return metadataRepositoryCohortHandler.getMetadataRepositoryCohortByGUID(connectorUserId, metadataRepositoryCohortGUID, getOptions);
    }



    /**
     * Retrieve the list of open metadata repository cohorts metadata elements that contain the search string.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findMetadataRepositoryCohorts(String        searchString,
                                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return metadataRepositoryCohortHandler.findMetadataRepositoryCohorts(connectorUserId, searchString, searchOptions);
    }
}
