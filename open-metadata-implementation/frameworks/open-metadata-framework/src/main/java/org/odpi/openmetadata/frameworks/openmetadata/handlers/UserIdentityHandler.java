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
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * UserIdentityHandler describes how to maintain and query  user identities.
 */
public class UserIdentityHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public UserIdentityHandler(String             localServerName,
                               AuditLog           auditLog,
                               String             serviceName,
                               OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              serviceName,
              openMetadataClient,
              OpenMetadataType.USER_IDENTITY.typeName);
    }


    /**
     * Create a new user identity.
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
    public String createUserIdentity(String                                userId,
                                     NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     UserIdentityProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName = "createUserIdentity";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a user identity using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new user identity.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createUserIdentityFromTemplate(String                 userId,
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
     * Update the properties of a user identity.
     *
     * @param userId                 userId of user making request.
     * @param userIdentityGUID      unique identifier of the user identity (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateUserIdentity(String                 userId,
                                      String                 userIdentityGUID,
                                      UpdateOptions          updateOptions,
                                      UserIdentityProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "updateUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        return super.updateElement(userId,
                                   userIdentityGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach a profile to a user identity.
     *
     * @param userId                  userId of user making request
     * @param userIdentityGUID        unique identifier of the parent
     * @param profileGUID             unique identifier of the actor profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkIdentityToProfile(String                    userId,
                                      String                    userIdentityGUID,
                                      String                    profileGUID,
                                      MakeAnchorOptions         makeAnchorOptions,
                                      ProfileIdentityProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "linkIdentityToProfile";
        final String end1GUIDParameterName = "userIdentityGUID";
        final String end2GUIDParameterName = "profileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(userIdentityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(profileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                        profileGUID,
                                                        userIdentityGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an actor profile from a user identity.
     *
     * @param userId                 userId of user making request.
     * @param userIdentityGUID       unique identifier of the parent actor profile
     * @param profileGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProfileIdentity(String        userId,
                                      String        userIdentityGUID,
                                      String        profileGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachProfileIdentity";

        final String end1GUIDParameterName = "userIdentityGUID";
        final String end2GUIDParameterName = "profileGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(userIdentityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(profileGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                        profileGUID,
                                                        userIdentityGUID,
                                                        deleteOptions);
    }


    /**
     * Add the SecurityGroupMembership classification to the user identity.
     *
     * @param userId                 userId of user making request.
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings("DataFlowIssue")
    public void addSecurityGroupMembership(String                            userId,
                                           String                            userIdentityGUID,
                                           SecurityGroupMembershipProperties properties,
                                           MetadataSourceOptions             metadataSourceOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "addSecurityGroupMembership";
        final String guidParameterName = "userIdentityGUID";
        final String propertiesParameterName = "properties";
        final String groupsParameterName = "properties.groups";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(userIdentityGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateObject(properties.getGroups(), groupsParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          userIdentityGUID,
                                                          OpenMetadataType.SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the SecurityGroupMembership classification for the user identity.
     *
     * @param userId                 userId of user making request.
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties            properties for the classification
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSecurityGroupMembership(String                            userId,
                                              String                            userIdentityGUID,
                                              UpdateOptions                     updateOptions,
                                              SecurityGroupMembershipProperties properties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "updateSecurityGroupMembership";
        final String guidParameterName = "userIdentityGUID";
        final String propertiesParameterName = "properties";
        final String groupsParameterName = "properties.groups";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(userIdentityGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateObject(properties.getGroups(), groupsParameterName, methodName);

        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            userIdentityGUID,
                                                            OpenMetadataType.SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION.typeName,
                                                            updateOptions,
                                                            classificationBuilder.getElementProperties(properties));

        openMetadataClient.updateClassificationEffectivityInStore(userId,
                                                                  userIdentityGUID,
                                                                  OpenMetadataType.SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                  updateOptions,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo());
    }


    /**
     * Remove the SecurityGroupMembership classification from the user identity.
     *
     * @param userId                 userId of user making request.
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeAllSecurityGroupMembership(String                userId,
                                                 String                userIdentityGUID,
                                                 MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "removeAllSecurityGroupMembership";
        final String guidParameterName = "userIdentityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(userIdentityGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            userIdentityGUID,
                                                            OpenMetadataType.SECURITY_GROUP_MEMBERSHIP_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Delete a user identity.
     *
     * @param userId                 userId of user making request.
     * @param userIdentityGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteUserIdentity(String        userId,
                                   String        userIdentityGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName        = "deleteUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(userIdentityGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, userIdentityGUID, deleteOptions);
    }


    /**
     * Returns the list of user identities with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getUserIdentitiesByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getUserIdentitiesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.USER_ID.name,
                                                   OpenMetadataProperty.DISTINGUISHED_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Returns the named user identity.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param getOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getUserIdentityByUserId(String     userId,
                                                           String     name,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getUserIdentitiesByUserId";

        return super.getRootElementsByUniqueName(userId, name, OpenMetadataProperty.USER_ID.name, getOptions, methodName);
    }


    /**
     * Return the properties of a specific user identity.
     *
     * @param userId                 userId of user making request
     * @param userIdentityGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getUserIdentityByGUID(String     userId,
                                                         String     userIdentityGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getUserIdentityByGUID";

        return super.getRootElementByGUID(userId, userIdentityGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of user identities metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findUserIdentities(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "findUserIdentities";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
