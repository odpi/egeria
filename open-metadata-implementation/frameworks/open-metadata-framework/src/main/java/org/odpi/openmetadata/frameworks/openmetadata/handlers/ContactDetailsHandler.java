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
 * ContactDetailsHandler provides methods to define contact methods
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
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createContactMethod(String                                userId,
                                      NewElementOptions                     newElementOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      ContactDetailsProperties properties,
                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "createContactMethod";

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
    public String createContactMethodFromTemplate(String                 userId,
                                                  TemplateOptions        templateOptions,
                                                  String                 templateGUID,
                                                  ElementProperties      replacementProperties,
                                                  Map<String, String>    placeholderProperties,
                                                  RelationshipBeanProperties parentRelationshipProperties) throws InvalidParameterException,
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
     * @param contactMethodGUID       unique identifier of the contact method (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateContactMethod(String                 userId,
                                    String                 contactMethodGUID,
                                    UpdateOptions          updateOptions,
                                    ContactDetailsProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName                 = "updateContactMethod";
        final String guidParameterName          = "contactMethodGUID";

        super.updateElement(userId,
                            contactMethodGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach a profile to a contactDetails.
     *
     * @param userId                 userId of user making request
     * @param contactMethodGUID       unique identifier of the contact method
     * @param contactDetailsGUID           unique identifier of the contactDetails
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContactDetailsToProfile(String                    userId,
                                            String                    contactMethodGUID,
                                            String                    contactDetailsGUID,
                                            MetadataSourceOptions     metadataSourceOptions,
                                            ContactThroughProperties relationshipProperties) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "linkContactDetailsToProfile";
        final String end1GUIDParameterName = "contactMethodGUID";
        final String end2GUIDParameterName = "contactDetailsGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contactMethodGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(contactDetailsGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                        contactMethodGUID,
                                                        contactDetailsGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a contact method from a contactDetails.
     *
     * @param userId                 userId of user making request.
     * @param contactMethodGUID       unique identifier of the contact method
     * @param contactDetailsGUID           unique identifier of the contactDetails
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContactDetailsFromProfile(String        userId,
                                                String        contactMethodGUID,
                                                String        contactDetailsGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "detachContactDetailsFromProfile";

        final String end1GUIDParameterName = "contactMethodGUID";
        final String end2GUIDParameterName = "contactDetailsGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contactMethodGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(contactDetailsGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                        contactDetailsGUID,
                                                        contactMethodGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a contact method.
     *
     * @param userId                 userId of user making request.
     * @param contactMethodGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContactMethod(String        userId,
                                    String        contactMethodGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName        = "deleteContactMethod";
        final String guidParameterName = "contactMethodGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contactMethodGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, contactMethodGUID, deleteOptions);
    }


    /**
     * Returns the list of contact methods with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getContactMethodsByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions,
                                                                 int          startFrom,
                                                                 int          pageSize) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName        = "getContactMethodsByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(startFrom, pageSize, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific contact method.
     *
     * @param userId                 userId of user making request
     * @param contactMethodGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getContactMethodByGUID(String     userId,
                                                          String     contactMethodGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getContactMethodByGUID";

        return super.getRootElementByGUID(userId, contactMethodGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of contact methods metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned contact methods include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findContactMethods(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findContactMethods";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
