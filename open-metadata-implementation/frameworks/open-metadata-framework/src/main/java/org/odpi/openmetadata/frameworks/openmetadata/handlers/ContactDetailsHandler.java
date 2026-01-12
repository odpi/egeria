/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ContactDetailsHandler provides methods to define contact methods (contact details).
 */
public class ContactDetailsHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ContactDetailsHandler(String             localServerName,
                                 AuditLog           auditLog,
                                 String             serviceName,
                                 OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              serviceName,
              openMetadataClient,
              OpenMetadataType.CONTACT_DETAILS.typeName);
    }


    /**
     * Create a new contact method.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createContactDetails(String                                userId,
                                       NewElementOptions                     newElementOptions,
                                       Map<String, ClassificationProperties> initialClassifications,
                                       ContactDetailsProperties              properties,
                                       RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createContactDetails";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a contact method using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new contact method.
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
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createContactDetailsFromTemplate(String                 userId,
                                                   TemplateOptions        templateOptions,
                                                   String                 templateGUID,
                                                   EntityProperties       replacementProperties,
                                                   Map<String, String>    placeholderProperties,
                                                   RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a contact method.
     *
     * @param userId                 userId of user making request.
     * @param contactDetailsGUID       unique identifier of the contact method (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateContactDetails(String                   userId,
                                        String                   contactDetailsGUID,
                                        UpdateOptions            updateOptions,
                                        ContactDetailsProperties properties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName        = "updateContactDetails";
        final String guidParameterName = "contactDetailsGUID";

        return super.updateElement(userId,
                                   contactDetailsGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach an element to its contact details.
     *
     * @param userId                 userId of user making request
     * @param elementGUID       unique identifier of the parent element
     * @param contactDetailsGUID           unique identifier of the contactDetails
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContactDetails(String                   userId,
                                   String                   elementGUID,
                                   String                   contactDetailsGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
                                   ContactThroughProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkContactDetails";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "contactDetailsGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(contactDetailsGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        contactDetailsGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an element from its contact details.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID            unique identifier of the parent element
     * @param contactDetailsGUID       unique identifier of the contact details element
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContactDetails(String        userId,
                                     String        elementGUID,
                                     String        contactDetailsGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachContactDetails";

        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "contactDetailsGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(contactDetailsGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        contactDetailsGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a contact method.
     *
     * @param userId                 userId of user making request.
     * @param contactDetailsGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContactDetails(String        userId,
                                     String        contactDetailsGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName        = "deleteContactDetails";
        final String guidParameterName = "contactDetailsGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contactDetailsGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, contactDetailsGUID, deleteOptions);
    }


    /**
     * Returns the list of contact methods with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getContactDetailsByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getContactDetailsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific contact method.
     *
     * @param userId                 userId of user making request
     * @param contactDetailsGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getContactDetailsByGUID(String     userId,
                                                           String     contactDetailsGUID,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getContactDetailsByGUID";

        return super.getRootElementByGUID(userId, contactDetailsGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of contact methods metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findContactDetails(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findContactDetails";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
