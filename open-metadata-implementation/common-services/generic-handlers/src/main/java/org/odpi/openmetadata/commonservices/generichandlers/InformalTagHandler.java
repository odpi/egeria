/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * InformalTagHandler manages InformalTag objects.  These are user-controlled semantic tags. InformalTagHandler runs server-side in
 * the OMAG Server Platform and retrieves InformalTag entities through the OMRSRepositoryConnector.
 * Since these are informal labels there is no support for effectivity dates because assets are not governed by them.
 */
public class InformalTagHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler for informal tags.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public InformalTagHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Count the number of informal tags attached to a supplied entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countTags(String  userId,
                         String  elementGUID,
                         boolean forLineage,
                         boolean forDuplicateProcessing,
                         Date    effectiveTime,
                         String  methodName) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException
    {
        return this.countAttachments(userId,
                                     elementGUID,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                     2,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Return the informal tags attached to a supplied entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the identifier is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getAttachedTags(String       userId,
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
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                        OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the informal tags attached to a supplied entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the identifier is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getAttachedTags(String       userId,
                                    String       elementGUID,
                                    String       elementGUIDParameterName,
                                    String       elementTypeName,
                                    List<String> serviceSupportedZones,
                                    int          startingFrom,
                                    int          pageSize,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                        OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createTag(String  userId,
                            String  externalSourceGUID,
                            String  externalSourceName,
                            String  tagName,
                            String  tagDescription,
                            boolean isPublic,
                            Date    effectiveFrom,
                            Date    effectiveTo,
                            Date    effectiveTime,
                            String  methodName) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String nameParameter = "tagName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tagName, nameParameter, methodName);

        InformalTagBuilder builder = new InformalTagBuilder(tagName,
                                                            tagDescription,
                                                            isPublic,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                           OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Updates the description of an existing tag (either private of public).  Private tags can only be updated by their creator.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param tagGUID         unique identifier for the tag.
     * @param tagGUIDParameterName name of parameter supplying the GUID
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     * @param effectiveFrom       starting time for this relationship (null for all time)
     * @param effectiveTo         ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName      calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateTagDescription(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  tagGUID,
                                     String  tagGUIDParameterName,
                                     String  tagDescription,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                                     tagDescription,
                                                                                     methodName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    tagGUID,
                                    tagGUIDParameterName,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                    true,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Removes a tag from the repository.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     * This method is sufficiently special that it does not use the generic handler
     *
     * @param userId    userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param tagGUID   unique id for the tag.
     * @param tagGUIDParameterName name of parameter supplying the GUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String  userId,
                            String  externalSourceGUID,
                            String  externalSourceName,
                            String  tagGUID,
                            String  tagGUIDParameterName,
                            boolean forLineage,
                            boolean forDuplicateProcessing,
                            Date    effectiveTime,
                            String  methodName) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    tagGUID,
                                    tagGUIDParameterName,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Removes a tag from the repository.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     * This method is sufficiently special that it does not use the generic handler
     *
     * @param userId    userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param tagGUID   unique id for the tag.
     * @param tagGUIDParameterName name of parameter supplying the GUID
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String       userId,
                            String       externalSourceGUID,
                            String       externalSourceName,
                            String       tagGUID,
                            String       tagGUIDParameterName,
                            List<String> serviceSupportedZones,
                            boolean      forLineage,
                            boolean      forDuplicateProcessing,
                            Date         effectiveTime,
                            String       methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    tagGUID,
                                    tagGUIDParameterName,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the tag for the supplied unique identifier (guid).  The tag is only returned if it is public
     * or if it is private and the
     *
     * @param userId userId of the user making the request
     * @param guid unique identifier of the tag
     * @param guidParameterName name of the parameter supplying the guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getTag(String       userId,
                    String       guid,
                    String       guidParameterName,
                    boolean      forLineage,
                    boolean      forDuplicateProcessing,
                    Date         effectiveTime,
                    String       methodName) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Return the tag for the supplied unique identifier (guid).  The tag is only returned if it is public
     * or if it is private and the
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     * @param guidParameterName name of the parameter supplying the guid
     * @param serviceSupportedZones list of zones that assets can be retrieved from
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getTag(String       userId,
                    String       guid,
                    String       guidParameterName,
                    List<String> serviceSupportedZones,
                    boolean      forLineage,
                    boolean      forDuplicateProcessing,
                    Date         effectiveTime,
                    String       methodName) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          effectiveTime,
                                          methodName);
    }




    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user
     * @param name name of tag
     * @param nameParameterName name of the parameter supplying the tag name
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getTagsByName(String       userId,
                                 String       name,
                                 String       nameParameterName,
                                 int          startFrom,
                                 int          pageSize,
                                 boolean      forLineage,
                                 boolean      forDuplicateProcessing,
                                 Date         effectiveTime,
                                 String       methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        List<String>  propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                    propertyNames,
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
     * Return the list of tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user
     * @param name name of tag
     * @param nameParameterName name of the parameter supplying the tag name
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getMyTagsByName(String       userId,
                                   String       name,
                                   String       nameParameterName,
                                   int          startFrom,
                                   int          pageSize,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        List<String>  propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME);

        return this.getBeansByCreator(userId,
                                      name,
                                      nameParameterName,
                                      OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                      OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                      propertyNames,
                                      true,
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
     * Return the list of tags matching the supplied searchString.
     *
     * @param userId the searchString of the calling user
     * @param searchString searchString of tag.  This may include wild card characters
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findTags(String       userId,
                            String       searchString,
                            String       searchStringParameterName,
                            int          startFrom,
                            int          pageSize,
                            boolean      forLineage,
                            boolean      forDuplicateProcessing,
                            Date         effectiveTime,
                            String       methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return this.getBeansByValue(userId,
                                    searchString,
                                    searchStringParameterName,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                    OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                    null,
                                    false,
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
     * Return the list of private tags for the user matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of tag.  This may include wild card characters.
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findMyTags(String       userId,
                              String       searchString,
                              String       searchStringParameterName,
                              int          startFrom,
                              int          pageSize,
                              boolean      forLineage,
                              boolean      forDuplicateProcessing,
                              Date         effectiveTime,
                              String       methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.getBeansByCreator(userId,
                                      searchString,
                                      searchStringParameterName,
                                      OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                      OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                      null,
                                      false,
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
     * Adds a tag (either private of public) to the requested element.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID       unique id for the element.
     * @param elementGUIDParameterName name of the parameter supplying the elementGUID
     * @param elementType       type of the element.
     * @param tagGUID          unique id of the tag.
     * @param tagGUIDParameterName name of the parameter supplying the tagGUID
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param effectiveFrom       starting time for this relationship (null for all time)
     * @param effectiveTo         ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the tag properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addTagToElement(String        userId,
                                String        externalSourceGUID,
                                String        externalSourceName,
                                String        elementGUID,
                                String        elementGUIDParameterName,
                                String        elementType,
                                String        tagGUID,
                                String        tagGUIDParameterName,
                                boolean       isPublic,
                                Date          effectiveFrom,
                                Date          effectiveTo,
                                boolean       forLineage,
                                boolean       forDuplicateProcessing,
                                Date          effectiveTime,
                                String        methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        this.addTagToElement(userId,
                             externalSourceGUID,
                             externalSourceName,
                             elementGUID,
                             elementGUIDParameterName,
                             elementType,
                             tagGUID,
                             tagGUIDParameterName,
                             supportedZones,
                             isPublic,
                             effectiveFrom,
                             effectiveTo,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Adds a tag (either private of public) to the requested element.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID       unique id for the element.
     * @param elementGUIDParameterName name of the parameter supplying the elementGUID
     * @param elementType       type of the element.
     * @param tagGUID          unique id of the tag.
     * @param tagGUIDParameterName name of the parameter supplying the tagGUID
     * @param suppliedSupportedZones    list of zones supported by this service
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName       calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the tag properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addTagToElement(String        userId,
                                String        externalSourceGUID,
                                String        externalSourceName,
                                String        elementGUID,
                                String        elementGUIDParameterName,
                                String        elementType,
                                String        tagGUID,
                                String        tagGUIDParameterName,
                                List<String>  suppliedSupportedZones,
                                boolean       isPublic,
                                Date          effectiveFrom,
                                Date          effectiveTo,
                                boolean       forLineage,
                                boolean       forDuplicateProcessing,
                                Date          effectiveTime,
                                String        methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        InstanceProperties properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                                      isPublic,
                                                                                      methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  elementGUID,
                                  elementGUIDParameterName,
                                  elementType,
                                  tagGUID,
                                  tagGUIDParameterName,
                                  OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  suppliedSupportedZones,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                  this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Removes a tag from the element.
     *
     * @param userId    userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID unique id for the element to unlink from tag
     * @param elementGUIDParameterName name of the parameter supplying the elementGUID
     * @param elementType type of the element.
     * @param tagGUID   unique id for the tag.
     * @param tagGUIDParameterName name of the parameter supplying the tagGUID
     * @param suppliedSupportedZones    list of zones supported by this service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the tag properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeTagFromElement(String        userId,
                                     String        externalSourceGUID,
                                     String        externalSourceName,
                                     String        elementGUID,
                                     String        elementGUIDParameterName,
                                     String        elementType,
                                     String        tagGUID,
                                     String        tagGUIDParameterName,
                                     List<String>  suppliedSupportedZones,
                                     boolean       forLineage,
                                     boolean       forDuplicateProcessing,
                                     Date          effectiveTime,
                                     String        methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      elementGUID,
                                      elementGUIDParameterName,
                                      elementType,
                                      tagGUID,
                                      tagGUIDParameterName,
                                      OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
                                      OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      suppliedSupportedZones,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }
}
