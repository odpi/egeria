/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.UserIdentityHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ProfileIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with user identity elements.
 */
public class UserIdentityClient extends ConnectorContextClientBase
{
    private final UserIdentityHandler userIdentityHandler;


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
    public UserIdentityClient(ConnectorContextBase     parentContext,
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

        this.userIdentityHandler = new UserIdentityHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new user identity.
     *
     * @param glossaryGUID unique identifier of the glossary
     * @param properties                   properties for the new element.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createUserIdentity(String                 glossaryGUID,
                                     UserIdentityProperties properties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        NewElementOptions newElementOptions = new NewElementOptions(this.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setAnchorScopeGUID(glossaryGUID);
        newElementOptions.setParentGUID(glossaryGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

        return this.createUserIdentity(newElementOptions,
                                       null,
                                       properties,
                                       null);

    }

    /**
     * Create a new user identity.
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
    public String createUserIdentity(NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     UserIdentityProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        String elementGUID = userIdentityHandler.createUserIdentity(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a user identity using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new user identity.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing user identity to copy (this will copy all the attachments such as nested content, schema
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
    public String createUserIdentityFromTemplate(TemplateOptions        templateOptions,
                                                 String                 templateGUID,
                                                 EntityProperties       replacementProperties,
                                                 Map<String, String>    placeholderProperties,
                                                 RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        String elementGUID = userIdentityHandler.createUserIdentityFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a user identity.
     *
     * @param userIdentityGUID       unique identifier of the user identity (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateUserIdentity(String                 userIdentityGUID,
                                      UpdateOptions          updateOptions,
                                      UserIdentityProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        boolean updateOccurred = userIdentityHandler.updateUserIdentity(connectorUserId, userIdentityGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(userIdentityGUID);
        }

        return updateOccurred;
    }
    

    /**
     * Attach a profile to a user identity.
     *
     * @param userIdentityGUID        unique identifier of the parent
     * @param profileGUID             unique identifier of the actor profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkIdentityToProfile(String                    userIdentityGUID,
                                      String                    profileGUID,
                                      MakeAnchorOptions         makeAnchorOptions,
                                      ProfileIdentityProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        userIdentityHandler.linkIdentityToProfile(connectorUserId, userIdentityGUID, profileGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an actor profile from a user identity.
     *
     * @param userIdentityGUID       unique identifier of the parent actor profile
     * @param profileGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProfileIdentity(String        userIdentityGUID,
                                      String        profileGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        userIdentityHandler.detachProfileIdentity(connectorUserId, userIdentityGUID, profileGUID, deleteOptions);
    }


    /**
     * Add the SecurityGroupMembership classification to the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addSecurityGroupMembership(String                            userIdentityGUID,
                                           SecurityGroupMembershipProperties properties,
                                           MetadataSourceOptions             metadataSourceOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        userIdentityHandler.addSecurityGroupMembership(connectorUserId, userIdentityGUID, properties, metadataSourceOptions);
    }


    /**
     * Update the SecurityGroupMembership classification for the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties            properties for the classification
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSecurityGroupMembership(String                            userIdentityGUID,
                                              UpdateOptions                     updateOptions,
                                              SecurityGroupMembershipProperties properties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        userIdentityHandler.updateSecurityGroupMembership(connectorUserId, userIdentityGUID, updateOptions, properties);
    }


    /**
     * Remove the SecurityGroupMembership classification from the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeAllSecurityGroupMembership(String                userIdentityGUID,
                                                 MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        userIdentityHandler.removeAllSecurityGroupMembership(connectorUserId, userIdentityGUID, metadataSourceOptions);
    }



    /**
     * Delete a user identity.
     *
     * @param userIdentityGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteUserIdentity(String        userIdentityGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        userIdentityHandler.deleteUserIdentity(connectorUserId, userIdentityGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(userIdentityGUID);
        }
    }


    /**
     * Returns the list of user identities with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getUserIdentitiesByName(String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        return userIdentityHandler.getUserIdentitiesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific user identity.
     *
     * @param userIdentityGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getUserIdentityByGUID(String     userIdentityGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return userIdentityHandler.getUserIdentityByGUID(connectorUserId, userIdentityGUID, getOptions);
    }


    /**
     * Retrieve the list of user identities metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned user identities include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param queryOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findUserIdentities(String              searchString,
                                                           SearchOptions       queryOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return userIdentityHandler.findUserIdentities(connectorUserId, searchString, queryOptions);
    }
}
