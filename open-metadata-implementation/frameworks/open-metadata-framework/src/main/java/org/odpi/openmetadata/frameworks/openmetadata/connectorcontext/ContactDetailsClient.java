/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ContactDetailsHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactDetailsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactThroughProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with contact details elements.
 */
public class ContactDetailsClient extends ConnectorContextClientBase
{
    private final ContactDetailsHandler contactDetailsHandler;


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
    public ContactDetailsClient(ConnectorContextBase     parentContext,
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

        this.contactDetailsHandler = new ContactDetailsHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new contact method.
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
    public String createContactDetails(NewElementOptions                     newElementOptions,
                                       Map<String, ClassificationProperties> initialClassifications,
                                       ContactDetailsProperties properties,
                                       RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        String contactDetailsGUID = contactDetailsHandler.createContactDetails(connectorUserId,
                                                                               newElementOptions,
                                                                               initialClassifications,
                                                                               properties,
                                                                               parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(contactDetailsGUID);
        }

        return contactDetailsGUID;
    }


    /**
     * Create a new metadata element to represent a contact method using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new contact method.
     *
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
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createContactDetailsFromTemplate(TemplateOptions            templateOptions,
                                                   String                     templateGUID,
                                                   ElementProperties          replacementProperties,
                                                   Map<String, String>        placeholderProperties,
                                                   RelationshipBeanProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        String contactDetailsGUID = contactDetailsHandler.createContactDetailsFromTemplate(connectorUserId,
                                                                                           templateOptions,
                                                                                           templateGUID,
                                                                                           replacementProperties,
                                                                                           placeholderProperties,
                                                                                           parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(contactDetailsGUID);
        }

        return contactDetailsGUID;
    }


    /**
     * Update the properties of a contact method.
     *
     * @param contactDetailsGUID       unique identifier of the contact method (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateContactDetails(String                   contactDetailsGUID,
                                        UpdateOptions            updateOptions,
                                        ContactDetailsProperties properties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        boolean updateOccurred = contactDetailsHandler.updateContactDetails(connectorUserId, contactDetailsGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(contactDetailsGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach an element to its contact details.
     *
     * @param elementGUID       unique identifier of the contact method
     * @param contactDetailsGUID           unique identifier of the contactDetails
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContactDetails(String                   elementGUID,
                                   String                   contactDetailsGUID,
                                   MetadataSourceOptions    metadataSourceOptions,
                                   ContactThroughProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        contactDetailsHandler.linkContactDetails(connectorUserId,
                                                 elementGUID,
                                                 contactDetailsGUID,
                                                 metadataSourceOptions,
                                                 relationshipProperties);
    }


    /**
     * Detach an element from its contact details.
     *
     * @param elementGUID            unique identifier of the contact method
     * @param contactDetailsGUID       unique identifier of the contact details element
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContactDetails(String        elementGUID,
                                     String        contactDetailsGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        contactDetailsHandler.detachContactDetails(connectorUserId, elementGUID, contactDetailsGUID, deleteOptions);
    }


    /**
     * Delete a contact method.
     *
     * @param contactDetailsGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContactDetails(String        contactDetailsGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        contactDetailsHandler.deleteContactDetails(connectorUserId, contactDetailsGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(contactDetailsGUID);
        }
    }


    /**
     * Returns the list of contact methods with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getContactDetailsByName(String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        return contactDetailsHandler.getContactDetailsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific contact method.
     *
     * @param contactDetailsGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getContactDetailsByGUID(String     contactDetailsGUID,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        return contactDetailsHandler.getContactDetailsByGUID(connectorUserId, contactDetailsGUID, getOptions);
    }


    /**
     * Retrieve the list of contact methods metadata elements that contain the search string.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findContactDetails(String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return contactDetailsHandler.findContactDetails(connectorUserId, searchString, searchOptions);
    }
}
