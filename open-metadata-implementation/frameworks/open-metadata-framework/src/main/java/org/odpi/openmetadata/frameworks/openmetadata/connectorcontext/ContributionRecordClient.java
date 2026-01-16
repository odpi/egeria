/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ContributionRecordHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with contribution record elements.
 */
public class ContributionRecordClient extends ConnectorContextClientBase
{
    private final ContributionRecordHandler contributionRecordHandler;


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
    public ContributionRecordClient(ConnectorContextBase     parentContext,
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

        this.contributionRecordHandler = new ContributionRecordHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Adds a contribution record to the element.
     *
     * @param elementGUID     unique identifier for the element
     * @param metadataSourceOptions options for the request
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the contribution record
     * @param relationshipProperties properties for the Contribution relationship
     *
     * @return guid of new contribution record.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addContributionRecordToElement(String                                elementGUID,
                                                 MetadataSourceOptions                 metadataSourceOptions,
                                                 Map<String, ClassificationProperties> initialClassifications,
                                                 ContributionRecordProperties          properties,
                                                 ContributionProperties                relationshipProperties) throws InvalidParameterException,
                                                                                                                      PropertyServerException,
                                                                                                                      UserNotAuthorizedException
    {
        String contributionRecordGUID = contributionRecordHandler.addContributionRecordToElement(connectorUserId, elementGUID, metadataSourceOptions, initialClassifications, properties, relationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(contributionRecordGUID);
        }

        return contributionRecordGUID;
    }


    /**
     * Create a new metadata element to represent a contribution record using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new contribution record.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createContributionRecordFromTemplate(String                     userId,
                                                       TemplateOptions            templateOptions,
                                                       String                     templateGUID,
                                                       EntityProperties           replacementProperties,
                                                       Map<String, String>        placeholderProperties,
                                                       RelationshipBeanProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        String contributionRecordGUID = contributionRecordHandler.createContributionRecordFromTemplate(userId,
                                                                                                       templateOptions,
                                                                                                       templateGUID,
                                                                                                       replacementProperties,
                                                                                                       placeholderProperties,
                                                                                                       parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(contributionRecordGUID);
        }

        return contributionRecordGUID;
    }



    /**
     * Update an existing contribution record.
     *
     * @param contributionRecordGUID   unique identifier for the contribution record to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the comment
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean   updateContributionRecord(String                       contributionRecordGUID,
                                              UpdateOptions                updateOptions,
                                              ContributionRecordProperties properties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        boolean updateOccurred = contributionRecordHandler.updateContributionRecord(connectorUserId, contributionRecordGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(contributionRecordGUID);
        }

        return updateOccurred;
    }


    /**
     * Removes a contribution record added to the element.
     *
     * @param contributionRecordGUID  unique identifier for the contribution record object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void deleteContributionRecord(String        contributionRecordGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        contributionRecordHandler.deleteContributionRecord(connectorUserId, contributionRecordGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(contributionRecordGUID);
        }
    }


    /**
     * Return the requested contribution record.
     *
     * @param contributionRecordGUID  unique identifier for the contribution record object.
     * @param getOptions multiple options to control the query
     * @return contribution record properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getContributionRecordByGUID(String     contributionRecordGUID,
                                                               GetOptions getOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return contributionRecordHandler.getContributionRecordByGUID(connectorUserId, contributionRecordGUID, getOptions);
    }


    /**
     * Retrieve the list of contribution record metadata elements that contain the requested keyword.
     *
     * @param name name to find
     * @param queryOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getContributionRecordsByName(String        name,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return contributionRecordHandler.getContributionRecordsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Retrieve the list of contribution record metadata elements that contain the search string.
     *
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findContributionRecords(String        searchString,
                                                                 SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return contributionRecordHandler.findContributionRecords(connectorUserId, searchString, searchOptions);
    }
}
