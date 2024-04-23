/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CommunityHandler provides the exchange of metadata about communities between the repository and the OMAS.
 *
 * @param <B> class that represents the community
 */
public class CommunityHandler<B> extends ReferenceableHandler<B>
{
    private static final String qualifiedNameParameterName = "qualifiedName";

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
    public CommunityHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create the community object.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this community
     * @param externalSourceName unique name of the software capability that owns this community
     * @param qualifiedName unique name for the community - used in other configuration
     * @param displayName short display name for the community
     * @param description description of the governance community
     * @param mission purpose of the community
     * @param additionalProperties additional properties for a community
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance community subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new community object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createCommunity(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              mission,
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
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.COMMUNITY_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.COMMUNITY_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        CommunityBuilder builder = new CommunityBuilder(qualifiedName,
                                                        displayName,
                                                        description,
                                                        mission,
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
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new community.
     *
     * All categories and terms are linked to a single community.  They are owned by this community and if the
     * community is deleted, any linked terms and categories are deleted as well.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this community
     * @param externalSourceName unique name of the software capability that owns this community
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the community - used in other configuration
     * @param displayName short display name for the community
     * @param description description of the governance community
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createCommunityFromTemplate(String userId,
                                              String externalSourceGUID,
                                              String externalSourceName,
                                              String templateGUID,
                                              String qualifiedName,
                                              String displayName,
                                              String description,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        CommunityBuilder builder = new CommunityBuilder(qualifiedName,
                                                        displayName,
                                                        description,
                                                        repositoryHelper,
                                                        serviceName,
                                                        serverName);

        builder.setAnchors(userId, null, OpenMetadataType.COMMUNITY_TYPE_NAME, methodName);


        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataType.COMMUNITY_TYPE_GUID,
                                           OpenMetadataType.COMMUNITY_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                           builder,
                                           supportedZones,
                                           true,
                                           false,
                                           null,
                                           methodName);
    }


    /**
     * Update the anchor object that all elements in a community (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this community
     * @param externalSourceName unique name of the software capability that owns this community
     * @param communityGUID unique identifier of the community to update
     * @param communityGUIDParameterName parameter passing the communityGUID
     * @param qualifiedName unique name for the community - used in other configuration
     * @param displayName short display name for the community
     * @param description description of the governance community
     * @param mission purpose of the community
     * @param additionalProperties additional properties for a governance community
     * @param suppliedTypeName type of community
     * @param extendedProperties  properties for a governance community subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateCommunity(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              communityGUID,
                                  String              communityGUIDParameterName,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              mission,
                                  Map<String, String> additionalProperties,
                                  String              suppliedTypeName,
                                  Map<String, Object> extendedProperties,
                                  Date                effectiveFrom,
                                  Date                effectiveTo,
                                  boolean             isMergeUpdate,
                                  boolean             forLineage,
                                  boolean             forDuplicateProcessing,
                                  Date                effectiveTime,
                                  String              methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(communityGUID, communityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.COMMUNITY_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.COMMUNITY_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        CommunityBuilder builder = new CommunityBuilder(qualifiedName,
                                                        displayName,
                                                        description,
                                                        mission,
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
                                    communityGUID,
                                    communityGUIDParameterName,
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
     * Add a member (PersonRole) to community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this community
     * @param externalSourceName unique name of the software capability that owns this community
     * @param communityGUID unique identifier of the community
     * @param communityGUIDParameterName parameter supplying the communityGUID
     * @param memberGUID unique identifier of the element that is being added to the community
     * @param memberGUIDParameterName parameter supplying the memberGUID
     * @param membershipType why is the element a member? (optional)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addRoleToCommunity(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  communityGUID,
                                   String  communityGUIDParameterName,
                                   String  memberGUID,
                                   String  memberGUIDParameterName,
                                   int     membershipType,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        InstanceProperties properties;

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    null,
                                                                    OpenMetadataType.MEMBERSHIP_TYPE_PROPERTY_NAME,
                                                                    OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_ENUM_TYPE_NAME,
                                                                    membershipType,
                                                                    methodName);
        }
        catch (TypeErrorException classificationNotSupported)
        {
            throw new InvalidParameterException(classificationNotSupported, OpenMetadataType.MEMBERSHIP_TYPE_PROPERTY_NAME);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  communityGUID,
                                  communityGUIDParameterName,
                                  OpenMetadataType.COMMUNITY_TYPE_NAME,
                                  memberGUID,
                                  memberGUIDParameterName,
                                  OpenMetadataType.PERSON_ROLE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_GUID,
                                  OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a member (PersonRole) from community.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this community
     * @param externalSourceName unique name of the software capability that owns this community
     * @param communityGUID unique identifier of the community
     * @param communityGUIDParameterName parameter supplying the communityGUID
     * @param memberGUID unique identifier of the element that is being added to the community
     * @param memberGUIDParameterName parameter supplying the memberGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeRoleFromCommunity(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  communityGUID,
                                        String  communityGUIDParameterName,
                                        String  memberGUID,
                                        String  memberGUIDParameterName,
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
                                      communityGUID,
                                      communityGUIDParameterName,
                                      OpenMetadataType.COMMUNITY_TYPE_NAME,
                                      memberGUID,
                                      memberGUIDParameterName,
                                      OpenMetadataType.PERSON_ROLE_TYPE_GUID,
                                      OpenMetadataType.PERSON_ROLE_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_GUID,
                                      OpenMetadataType.COMMUNITY_MEMBERSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a community.  This will delete the community and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this community
     * @param externalSourceName unique name of the software capability that owns this community
     * @param communityGUID unique identifier of the metadata element to remove
     * @param communityGUIDParameterName parameter supplying the communityGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeCommunity(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  communityGUID,
                                String  communityGUIDParameterName,
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
                                    communityGUID,
                                    communityGUIDParameterName,
                                    OpenMetadataType.COMMUNITY_TYPE_GUID,
                                    OpenMetadataType.COMMUNITY_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of community metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findCommunities(String  userId,
                                   String  searchString,
                                   String  searchStringParameterName,
                                   int     startFrom,
                                   int     pageSize,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataType.COMMUNITY_TYPE_GUID,
                              OpenMetadataType.COMMUNITY_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of community metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCommunitiesByName(String  userId,
                                          String  name,
                                          String  nameParameterName,
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

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.COMMUNITY_TYPE_GUID,
                                    OpenMetadataType.COMMUNITY_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of community metadata elements.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCommunities(String  userId,
                                    int     startFrom,
                                    int     pageSize,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataType.COMMUNITY_TYPE_GUID,
                                   OpenMetadataType.COMMUNITY_TYPE_NAME,
                                   null,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   startFrom,
                                   pageSize,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Retrieve the community metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getCommunityByGUID(String  userId,
                                String  guid,
                                String  guidParameterName,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataType.COMMUNITY_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

    }
}
