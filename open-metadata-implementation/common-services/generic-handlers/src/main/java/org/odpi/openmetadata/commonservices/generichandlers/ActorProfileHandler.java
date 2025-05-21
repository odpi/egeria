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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * Create the anchor object that all elements in a profile (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param qualifiedName unique name for the profile - used in other configuration
     * @param name short display name for the profile
     * @param description description of the governance profile
     * @param additionalProperties additional properties for a profile
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance profile subtype
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new profile object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createActorProfile(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              qualifiedName,
                                     String              name,
                                     String              description,
                                     Map<String, String> additionalProperties,
                                     String              suppliedTypeName,
                                     Map<String, Object> extendedProperties,
                                     Date                effectiveFrom,
                                     Date                effectiveTo,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.ACTOR_PROFILE.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ActorProfileBuilder builder = new ActorProfileBuilder(qualifiedName,
                                                              name,
                                                              description,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the anchor object that all elements in a profile (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param profileGUID unique identifier of the profile to update
     * @param profileGUIDParameterName parameter passing the profileGUID
     * @param qualifiedName unique name for the profile - used in other configuration
     * @param name short display name for the profile
     * @param description description of the governance profile
     * @param additionalProperties additional properties for a governance profile
     * @param typeName type of profile
     * @param extendedProperties  properties for a governance profile subtype
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateActorProfile(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              profileGUID,
                                     String              profileGUIDParameterName,
                                     String              qualifiedName,
                                     String              name,
                                     String              description,
                                     Map<String, String> additionalProperties,
                                     String              typeName,
                                     Map<String, Object> extendedProperties,
                                     boolean             isMergeUpdate,
                                     Date                effectiveFrom,
                                     Date                effectiveTo,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(profileGUID, profileGUIDParameterName, methodName);

        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ActorProfileBuilder builder = new ActorProfileBuilder(qualifiedName,
                                                              name,
                                                              description,
                                                              additionalProperties,
                                                              typeGUID,
                                                              typeName,
                                                              extendedProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    profileGUID,
                                    profileGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Link two team profiles as part of a hierarchy.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param superTeamGUID unique identifier of team profile
     * @param superTeamGUIDParameterName parameter name supplying superTeamGUID
     * @param subTeamGUID  unique identifier of the other team profile
     * @param subTeamGUIDParameterName parameter name supplying subTeamGUID
     * @param delegationEscalationAuthority can workflows delegate/escalate through this link?
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  linkTeamHierarchy(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  superTeamGUID,
                                   String  superTeamGUIDParameterName,
                                   String  subTeamGUID,
                                   String  subTeamGUIDParameterName,
                                   boolean delegationEscalationAuthority,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        ActorProfileBuilder builder = new ActorProfileBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getTeamStructureProperties(delegationEscalationAuthority, methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  superTeamGUID,
                                  superTeamGUIDParameterName,
                                  OpenMetadataType.TEAM.typeName,
                                  subTeamGUID,
                                  subTeamGUIDParameterName,
                                  OpenMetadataType.TEAM.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                  setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Unlink two team profiles from the hierarchy.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param superTeamGUID unique identifier of team profile
     * @param superTeamGUIDParameterName parameter name supplying superTeamGUID
     * @param subTeamGUID  unique identifier of the other team profile
     * @param subTeamGUIDParameterName parameter name supplying subTeamGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void unlinkTeamHierarchy(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  superTeamGUID,
                                    String  superTeamGUIDParameterName,
                                    String  subTeamGUID,
                                    String  subTeamGUIDParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      superTeamGUID,
                                      superTeamGUIDParameterName,
                                      OpenMetadataType.TEAM.typeName,
                                      subTeamGUID,
                                      subTeamGUIDParameterName,
                                      OpenMetadataType.TEAM.typeGUID,
                                      OpenMetadataType.TEAM.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a profile.  This will delete the profile and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param profileGUID unique identifier of the metadata element to remove
     * @param profileGUIDParameterName parameter supplying the profileGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeActorProfile(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  profileGUID,
                                   String  profileGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    profileGUID,
                                    profileGUIDParameterName,
                                    OpenMetadataType.ACTOR_PROFILE.typeGUID,
                                    OpenMetadataType.ACTOR_PROFILE.typeName,
                                    false,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
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
     * Return the locations attached to an actor profile.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the feedback is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getActorProfilesByLocation(String       userId,
                                              String       elementGUID,
                                              String       elementGUIDParameterName,
                                              String       elementTypeName,
                                              int          startingFrom,
                                              int          pageSize,
                                              boolean      forLineage,
                                              boolean      forDuplicateProcessing,
                                              Date         effectiveTime,
                                              String       methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        List<EntityDetail> entities =  this.getAttachedEntities(userId,
                                                                elementGUID,
                                                                elementGUIDParameterName,
                                                                elementTypeName,
                                                                OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeGUID,
                                                                OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                                OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                null,
                                                                null,
                                                                1,
                                                                null,
                                                                null,
                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                startingFrom,
                                                                pageSize,
                                                                effectiveTime,
                                                                methodName);

        return getFullBeans(userId, entities, elementTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Return all the actor profiles.
     *
     * @param userId     calling user
     * @param elementTypeGUID identifier of the type of object to retrieve
     * @param elementTypeName name of the type of object to retrieve
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B> getActorProfiles(String       userId,
                                    String       elementTypeGUID,
                                    String       elementTypeName,
                                    int          startingFrom,
                                    int          pageSize,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        List<EntityDetail> entities =  this.getEntitiesByType(userId,
                                                              elementTypeGUID,
                                                              elementTypeName,
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

        return getFullBeans(userId, entities, elementTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
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
