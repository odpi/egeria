/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * UserIdentityHandler provides the exchange of metadata about glossaries between the repository and the OMAS.
 * Note user identities are governance metadata and are always defined with LOCAL-COHORT provenance.
 * They also do not have support for effectivity dates.
 *
 * @param <B> class that represents the user identity
 */
public class UserIdentityHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from
     * @param defaultZones list of zones that the access service should set in all new B instances
     * @param publishZones list of zones that the access service sets up in published B instances
     * @param auditLog destination for audit log events
     */
    public UserIdentityHandler(OpenMetadataAPIGenericConverter<B> converter,
                               Class<B>                           beanClass,
                               String                             serviceName,
                               String                             serverName,
                               InvalidParameterHandler            invalidParameterHandler,
                               RepositoryHandler                  repositoryHandler,
                               OMRSRepositoryHelper               repositoryHelper,
                               String                             localServerUserId,
                               OpenMetadataServerSecurityVerifier securityVerifier,
                               List<String>                       supportedZones,
                               List<String>                       defaultZones,
                               List<String>                       publishZones,
                               AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /**
     * Create the entity that represents a user identity.  If the profileGUID is supplied, the profile becomes the
     * anchor of the User Identity and they are linked together.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param profileGUID the unique identifier of the profile GUID that is the anchor of
     * @param profileGUIDParameterName parameter name supplying profileGUID
     * @param qualifiedName unique name for the user identity - used in other configuration
     * @param additionalProperties additional properties for a user identity
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance user identity subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new user identity object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createUserIdentity(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              profileGUID,
                                     String              profileGUIDParameterName,
                                     String              qualifiedName,
                                     Map<String, String> additionalProperties,
                                     String              suppliedTypeName,
                                     Map<String, Object> extendedProperties,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        UserIdentityBuilder builder = new UserIdentityBuilder(qualifiedName,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        if (profileGUID != null)
        {
            builder.setAnchors(userId, profileGUID, methodName);
        }

        String userIdentityGUID = this.createBeanInRepository(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              typeGUID,
                                                              typeName,
                                                              qualifiedName,
                                                              OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                              builder,
                                                              methodName);

        if ((userIdentityGUID != null) && (profileGUID != null))
        {
            final String userIdentityGUIDParameterName = "userIdentityGUID";

            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      profileGUID,
                                      profileGUIDParameterName,
                                      OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                      userIdentityGUID,
                                      userIdentityGUIDParameterName,
                                      OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                      false,
                                      false,
                                      supportedZones,
                                      OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME,
                                      null,
                                      methodName);
        }

        return userIdentityGUID;
    }


    /**
     * Update the entity that represents a user identity.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the user identity to update
     * @param userIdentityGUIDParameterName parameter passing the userIdentityGUID
     * @param qualifiedName unique name for the user identity - used in other configuration
     * @param additionalProperties additional properties for a governance user identity
     * @param typeName type of user identity
     * @param extendedProperties  properties for a governance user identity subtype
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateUserIdentity(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              userIdentityGUID,
                                   String              userIdentityGUIDParameterName,
                                   String              qualifiedName,
                                   Map<String, String> additionalProperties,
                                   String              typeName,
                                   Map<String, Object> extendedProperties,
                                   boolean             isMergeUpdate,
                                   String              methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(userIdentityGUID, userIdentityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        UserIdentityBuilder builder = new UserIdentityBuilder(qualifiedName,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    userIdentityGUID,
                                    userIdentityGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    false,
                                    false,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    new Date(),
                                    methodName);
    }





    /**
     * Remove the metadata element representing a user identity.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the metadata element to remove
     * @param userIdentityGUIDParameterName parameter supplying the user identityGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void deleteUserIdentity(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String userIdentityGUID,
                                   String userIdentityGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    userIdentityGUID,
                                    userIdentityGUIDParameterName,
                                    OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
                                    OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                    null,
                                    null,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param userIdentityGUID  unique identifier of the user identity
     * @param userIdentityGUIDParameterName parameter name supplying userIdentityGUID
     * @param profileGUID unique identifier of the profile
     * @param profileGUIDParameterName parameter name supplying profileGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addIdentityToProfile(String userId,
                                     String externalSourceGUID,
                                     String externalSourceName,
                                     String userIdentityGUID,
                                     String userIdentityGUIDParameterName,
                                     String profileGUID,
                                     String profileGUIDParameterName,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.relinkElementToNewElement(userId,
                                       externalSourceGUID,
                                       externalSourceName,
                                       userIdentityGUID,
                                       userIdentityGUIDParameterName,
                                       OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                       true,
                                       profileGUID,
                                       profileGUIDParameterName,
                                       OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                       false,
                                       false,
                                       supportedZones,
                                       OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_GUID,
                                       OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME,
                                       null,
                                       methodName);
    }


    /**
     * Unlink a user identity from a profile.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software server capability representing the caller
     * @param externalSourceName     unique name of software server capability representing the caller
     * @param userIdentityGUID  unique identifier of the user identity
     * @param userIdentityGUIDParameterName parameter name supplying userIdentityGUID
     * @param profileGUID unique identifier of the profile
     * @param profileGUIDParameterName parameter name supplying profileGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeIdentifyFromProfile(String userId,
                                          String externalSourceGUID,
                                          String externalSourceName,
                                          String userIdentityGUID,
                                          String userIdentityGUIDParameterName,
                                          String profileGUID,
                                          String profileGUIDParameterName,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      profileGUID,
                                      profileGUIDParameterName,
                                      OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME,
                                      userIdentityGUID,
                                      userIdentityGUIDParameterName,
                                      OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
                                      OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                      false,
                                      false,
                                      supportedZones,
                                      OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataAPIMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME,
                                      null,
                                      methodName);
    }


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getUserIdentitiesByName(String userId,
                                           String name,
                                           String nameParameterName,
                                           int    startFrom,
                                           int    pageSize,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return this.getBeansByQualifiedName(userId,
                                            OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
                                            OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                            name,
                                            nameParameterName,
                                            supportedZones,
                                            startFrom,
                                            pageSize,
                                            null,
                                            methodName);
    }


    /**
     * Retrieve the user identity metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getUserIdentityByGUID(String userId,
                                   String guid,
                                   String guidParameterName,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
                                          false,
                                          false,
                                          supportedZones,
                                          new Date(),
                                          methodName);

    }
}
