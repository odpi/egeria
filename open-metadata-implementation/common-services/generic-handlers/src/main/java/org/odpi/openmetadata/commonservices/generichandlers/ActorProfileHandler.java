/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ActorProfileHandler provides the exchange of metadata about actor profiles between the repository and the OMAS.
 *
 * @param <B> class that represents the profile
 */
public class ActorProfileHandler<B> extends ReferenceableHandler<B>
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
    public ActorProfileHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Retrieve the list of profile metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param typeGUID unique identifier of the type of the profile to retrieve
     * @param typeName unique name of the type of the profile to retrieve
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findActorProfiles(String  userId,
                                     String  searchString,
                                     String  searchStringParameterName,
                                     String  typeGUID,
                                     String  typeName,
                                     int     startFrom,
                                     int     pageSize,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        List<EntityDetail> entities = this.findEntities(userId,
                                                        searchString,
                                                        searchStringParameterName,
                                                        typeGUID,
                                                        typeName,
                                                        null,
                                                        null,
                                                        startFrom,
                                                        pageSize,
                                                        null,
                                                        null,
                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                        null,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

        if (entities != null)
        {
            List<B> beans = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = this.getFullProfileBean(userId,
                                                     entity,
                                                     typeName,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

                    if (bean != null)
                    {
                        beans.add(bean);
                    }
                }
            }

            if (! beans.isEmpty())
            {
                return beans;
            }
        }

        return null;
    }


    /**
     * Retrieve the list of profile metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param typeGUID unique identifier of the type of the profile to retrieve
     * @param typeName unique name of the type of the profile to retrieve
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getActorProfilesByName(String  userId,
                                          String  name,
                                          String  nameParameterName,
                                          String  typeGUID,
                                          String  typeName,
                                          int     startFrom,
                                          int     pageSize,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.NAME.name);

        List<EntityDetail> entities = this.getEntitiesByValue(userId,
                                                              name,
                                                              nameParameterName,
                                                              typeGUID,
                                                              typeName,
                                                              specificMatchPropertyNames,
                                                              true,
                                                              false,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                              null,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              supportedZones,
                                                              startFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              methodName);

        return getFullBeans(userId, entities, typeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Return the actors attached to a supplied project via the project team relationship.
     *
     * @param userId     calling user
     * @param projectGUID identifier for the entity that the actor profiles are attached to
     * @param projectGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getActorsForProject(String              userId,
                                        String              projectGUID,
                                        String              projectGUIDParameterName,
                                        int                 startingFrom,
                                        int                 pageSize,
                                        boolean             forLineage,
                                        boolean             forDuplicateProcessing,
                                        Date                effectiveTime,
                                        String              methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        projectGUID,
                                        projectGUIDParameterName,
                                        OpenMetadataType.PROJECT.typeName,
                                        OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.PROJECT_TEAM_RELATIONSHIP.typeName,
                                        OpenMetadataType.ACTOR_PROFILE.typeName,
                                        (String)null,
                                        null,
                                        2,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the profile metadata element with the supplied unique identifier.  This method only returns
     * the core profile information from the ActorProfile entity.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param typeName unique name of the type of the profile to retrieve
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getActorProfileByGUID(String  userId,
                                   String  guid,
                                   String  guidParameterName,
                                   String  typeName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           typeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           supportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            return getFullProfileBean(userId,
                                      entity,
                                      typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }

        return null;
    }


    /**
     * Retrieve the profile metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param profileUserId unique name of the linked user id
     * @param profileUserIdParameterName parameter name of profileUserId
     * @param typeName unique name of the type of the profile to retrieve
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EntityDetail getActorProfileEntityForUser(String  userId,
                                                     String  profileUserId,
                                                     String  profileUserIdParameterName,
                                                     String  typeName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String userGUIDParameterName = "userIdentity.getGUID";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.USER_ID.name);

        EntityDetail userIdentity = this.getEntityByValue(userId,
                                                          profileUserId,
                                                          profileUserIdParameterName,
                                                          OpenMetadataType.USER_IDENTITY.typeGUID,
                                                          OpenMetadataType.USER_IDENTITY.typeName,
                                                          specificMatchPropertyNames,
                                                          null,
                                                          null,
                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                          null,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        if (userIdentity != null)
        {
            return this.getAttachedEntity(userId,
                                          userIdentity.getGUID(),
                                          userGUIDParameterName,
                                          OpenMetadataType.USER_IDENTITY.typeName,
                                          OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeGUID,
                                          OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                          typeName,
                                          1,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);
        }

        return null;
    }


    /**
     * Retrieve the profile metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param profileUserId unique name of the linked user id
     * @param profileUserIdParameterName parameter name of profileUserId
     * @param typeName unique name of the type of the profile to retrieve
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getActorProfileForUser(String  userId,
                                    String  profileUserId,
                                    String  profileUserIdParameterName,
                                    String  typeName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        EntityDetail entity = this.getActorProfileEntityForUser(userId,
                                                                profileUserId,
                                                                profileUserIdParameterName,
                                                                typeName,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);

        if (entity != null)
        {
            return getFullProfileBean(userId,
                                      entity,
                                      typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }

        return null;
    }


    /**
     * Retrieve the full profile metadata element for each entity in the list.
     * This includes contact details, user identity, contribution record and pointers to related elements such as
     * roles and peers.
     *
     * @param userId calling user
     * @param entities root entities for the profiles
     * @param typeName type name of the primary entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private List<B> getFullBeans(String             userId,
                                 List<EntityDetail> entities,
                                 String             typeName,
                                 boolean            forLineage,
                                 boolean            forDuplicateProcessing,
                                 Date               effectiveTime,
                                 String             methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        if (entities != null)
        {
            List<B> beans = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = this.getFullProfileBean(userId,
                                                     entity,
                                                     typeName,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

                    if (bean != null)
                    {
                        beans.add(bean);
                    }
                }
            }

            if (! beans.isEmpty())
            {
                return beans;
            }
        }

        return null;
    }


    /**
     * Retrieve the profile metadata element with the supplied unique identifier.
     * This includes contact details, user identity, contribution record and pointers to related elements such as
     * roles and peers.
     *
     * @param userId calling user
     * @param primaryEntity root entity for a profile
     * @param typeName type name of the primary entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private B getFullProfileBean(String       userId,
                                 EntityDetail primaryEntity,
                                 String       typeName,
                                 boolean      forLineage,
                                 boolean      forDuplicateProcessing,
                                 Date         effectiveTime,
                                 String       methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String profileGUIDParameterName = "profileGUID";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String contributionRecordGUIDParameterName = "contributionRecordGUID";
        final String contactDetailsGUIDParameterName = "contactDetailsGUID";

        List<EntityDetail> supplementaryEntities = new ArrayList<>();
        List<Relationship> relationships = this.getAllAttachmentLinks(userId,
                                                                      primaryEntity.getGUID(),
                                                                      profileGUIDParameterName,
                                                                      typeName,
                                                                      null,
                                                                      null,
                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if ((relationship != null) && (relationship.getType() != null))
                {
                    String relationshipTypeName = relationship.getType().getTypeDefName();

                    if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName))
                    {
                        EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                        EntityDetail entity = getEntityFromRepository(userId,
                                                                      entityProxy.getGUID(),
                                                                      userIdentityGUIDParameterName,
                                                                      OpenMetadataType.USER_IDENTITY.typeName,
                                                                      null,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      supportedZones,
                                                                      effectiveTime,
                                                                      methodName);

                        if (entity != null)
                        {
                            supplementaryEntities.add(entity);
                        }
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName))
                    {
                        EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                        EntityDetail entity = getEntityFromRepository(userId,
                                                                      entityProxy.getGUID(),
                                                                      contactDetailsGUIDParameterName,
                                                                      OpenMetadataType.CONTACT_DETAILS.typeName,
                                                                      null,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      supportedZones,
                                                                      effectiveTime,
                                                                      methodName);

                        if (entity != null)
                        {
                            supplementaryEntities.add(entity);
                        }
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName))
                    {
                        EntityProxy entityProxy = repositoryHelper.getOtherEnd(serviceName, primaryEntity.getGUID(), relationship);

                        EntityDetail entity = getEntityFromRepository(userId,
                                                                      entityProxy.getGUID(),
                                                                      contributionRecordGUIDParameterName,
                                                                      OpenMetadataType.CONTRIBUTION_RECORD.typeName,
                                                                      null,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      supportedZones,
                                                                      effectiveTime,
                                                                      methodName);

                        if (entity != null)
                        {
                            supplementaryEntities.add(entity);
                        }
                    }
                }
            }
        }

        return converter.getNewComplexGraphBean(beanClass,
                                                primaryEntity,
                                                supplementaryEntities,
                                                relationships,
                                                methodName);
    }
}
