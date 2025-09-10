/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ContributionRecordHandler provides methods to define contribution records
 */
public class ContributionRecordHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ContributionRecordHandler(String             localServerName,
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
     * Create a new contribution record.
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
    public String createContributionRecord(String                                userId,
                                           NewElementOptions                     newElementOptions,
                                           Map<String, ClassificationProperties> initialClassifications,
                                           ContributionRecordProperties properties,
                                           RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                      PropertyServerException,
                                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "createContributionRecord";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
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
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createContributionRecordFromTemplate(String                 userId,
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
     * Update the properties of a contribution record.
     *
     * @param userId                 userId of user making request.
     * @param contributionRecordGUID       unique identifier of the contribution record (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateContributionRecord(String                       userId,
                                         String                       contributionRecordGUID,
                                         UpdateOptions                updateOptions,
                                         ContributionRecordProperties properties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName                 = "updateContributionRecord";
        final String guidParameterName          = "contributionRecordGUID";

        super.updateElement(userId,
                            contributionRecordGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach a profile to a contribution record.
     *
     * @param userId                 userId of user making request
     * @param contributionRecordGUID       unique identifier of the contribution record
     * @param profileGUID           unique identifier of the profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContributionRecordToProfile(String                 userId,
                                                String                 profileGUID,
                                                String                 contributionRecordGUID,
                                                MetadataSourceOptions  metadataSourceOptions,
                                                ContributionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName            = "linkContributionRecordToProfile";
        final String end1GUIDParameterName = "profileGUID";
        final String end2GUIDParameterName = "contributionRecordGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(profileGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(contributionRecordGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                        profileGUID,
                                                        contributionRecordGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a contribution record from a profile.
     *
     * @param userId                 userId of user making request.
     * @param contributionRecordGUID       unique identifier of the contribution record
     * @param profileGUID           unique identifier of the profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContributionRecordFromProfile(String        userId,
                                                    String        profileGUID,
                                                    String        contributionRecordGUID,
                                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachContributionRecordFromProfile";

        final String end1GUIDParameterName = "contributionRecordGUID";
        final String end2GUIDParameterName = "profileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contributionRecordGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(profileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                        profileGUID,
                                                        contributionRecordGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a contribution record.
     *
     * @param userId                 userId of user making request.
     * @param contributionRecordGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContributionRecord(String        userId,
                                         String        contributionRecordGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName        = "deleteContributionRecord";
        final String guidParameterName = "contributionRecordGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contributionRecordGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, contributionRecordGUID, deleteOptions);
    }


    /**
     * Returns the list of contribution records with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getContributionRecordsByName(String       userId,
                                                                      String       name,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName  = "getContributionRecordsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific contribution record.
     *
     * @param userId                 userId of user making request
     * @param contributionRecordGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getContributionRecordByGUID(String     userId,
                                                               String     contributionRecordGUID,
                                                               GetOptions getOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getContributionRecordByGUID";

        return super.getRootElementByGUID(userId, contributionRecordGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of contribution records metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findContributionRecords(String        userId,
                                                                 String        searchString,
                                                                 SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "findContributionRecords";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
