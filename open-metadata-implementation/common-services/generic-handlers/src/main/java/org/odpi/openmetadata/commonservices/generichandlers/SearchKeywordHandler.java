/* SPDX-License-Identifier: Apache-2.0 */
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
 * SearchKeywordHandler manages SearchKeyword objects.  These are user controlled semantic keywords. SearchKeywordHandler runs server-side in
 * the OMAG Server Platform and retrieves SearchKeyword entities through the OMRSRepositoryConnector.
 * This handler does not support effectivity dates because it does not make sense for the element type.
 */
public class SearchKeywordHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
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
    public SearchKeywordHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Creates a new keyword and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param keywordName          name of the keyword.
     * @param keywordDescription  (optional) description of the keyword.  Setting a description, particularly in a public keyword
     *                        makes the keyword more valuable to other users and can act as an embryonic glossary term.
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return GUID for new keyword.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the keyword properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createKeyword(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  keywordName,
                                String  keywordDescription,
                                Date    effectiveFrom,
                                Date    effectiveTo,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String nameParameter = "keywordName";

        invalidParameterHandler.validateName(keywordName, nameParameter, methodName);

        SearchKeywordBuilder builder = new SearchKeywordBuilder(keywordName,
                                                                keywordDescription,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
                                           OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Updates the description of an existing keyword.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param keywordGUID         unique identifier for the keyword
     * @param keywordGUIDParameterName parameter providing keywordGUID
     * @param keywordDescription  description of the keyword.  Setting a description, particularly in a public keyword
     *                        makes the keyword more valuable to other users and can act as an embryonic glossary term.
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName      calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the keyword properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateKeywordDescription(String       userId,
                                           String       externalSourceGUID,
                                           String       externalSourceName,
                                           String       keywordGUID,
                                           String       keywordGUIDParameterName,
                                           String       keywordDescription,
                                           Date         effectiveFrom,
                                           Date         effectiveTo,
                                           List<String> serviceSupportedZones,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.KEYWORD_DESCRIPTION_PROPERTY_NAME,
                                                                                     keywordDescription,
                                                                                     methodName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    keywordGUID,
                                    keywordGUIDParameterName,
                                    OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
                                    OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    this.setUpEffectiveDates(properties, effectiveFrom,  effectiveTo),
                                    true,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Removes a keyword from the repository.
     * A private keyword can be deleted by its creator and all the references are lost;
     * a public keyword can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param userId    userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param keywordGUID   unique id for the keyword.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the keyword properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteKeyword(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  keywordGUID,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String keywordGUIDParameterName = "keywordGUID";

        B keyword = this.getKeyword(userId, keywordGUID, keywordGUIDParameterName, supportedZones, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        if (keyword != null)
        {
            if (repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                   keywordGUID,
                                                                   OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME,
                                                                   2,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName) == 0)
            {
                repositoryHandler.removeEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               keywordGUID,
                                               keywordGUIDParameterName,
                                               OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
                                               OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                               null,
                                               null,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
            }
            else
            {
                invalidParameterHandler.throwCannotDeleteElementInUse(keywordGUID,
                                                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                                                      serviceName,
                                                                      methodName);
            }

        }
    }



    /**
     * Count the number of informal keywords attached to a supplied entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countKeywords(String  userId,
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
                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_GUID,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME,
                                     2,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Return the keywords attached to a supplied entity.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the keyword is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
    public List<B>  getAttachedKeywords(String       userId,
                                        String       startingGUID,
                                        String       startingGUIDParameterName,
                                        String       startingTypeName,
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
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the keywords attached to a supplied entity.
     *
     * @param userId     calling user
     * @param startingKeywordGUID identifier for the entity that the keyword is attached to
     * @param startingKeywordGUIDParameterName name of parameter supplying the GUID
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
    public List<B>  getRelatedKeywords(String       userId,
                                       String       startingKeywordGUID,
                                       String       startingKeywordGUIDParameterName,
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
                                        startingKeywordGUID,
                                        startingKeywordGUIDParameterName,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
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
     * Return the keyword for the supplied unique identifier (guid).  The keyword is only returned if
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the keyword.
     * @param guidParameterName name of the parameter supplying the GUID
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return keyword
     * @throws InvalidParameterException the userId or guid is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getKeyword(String       userId,
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
                                          OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Return the list of keywords exactly matching the supplied keyword.
     *
     * @param userId the keyword of the calling user
     * @param keyword keyword of keyword
     * @param keywordParameterName parameter providing keyword
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return keyword list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getKeywordsByName(String       userId,
                                     String       keyword,
                                     String       keywordParameterName,
                                     List<String> serviceSupportedZones,
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

        propertyNames.add(OpenMetadataAPIMapper.KEYWORD_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    keyword,
                                    keywordParameterName,
                                    OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
                                    OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                    propertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the list of keywords matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of keyword.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param serviceSupportedZones list of supported zones for this service.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return keyword list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findKeywords(String       userId,
                                String       searchString,
                                String       searchStringParameterName,
                                List<String> serviceSupportedZones,
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
                                    OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
                                    OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                    null,
                                    false,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Adds a keyword to the requested element.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID       unique id for the element.
     * @param elementGUIDParameterName name of the parameter supplying the elementGUID
     * @param elementType       type of the element.
     * @param keywordGUID          unique id of the keyword.
     * @param keywordGUIDParameterName name of the parameter supplying the keywordOneGUID
     * @param serviceSupportedZones list of zones supported by this service
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the keyword properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addKeywordToElement(String       userId,
                                    String       externalSourceGUID,
                                    String       externalSourceName,
                                    String       elementGUID,
                                    String       elementGUIDParameterName,
                                    String       elementType,
                                    String       keywordGUID,
                                    String       keywordGUIDParameterName,
                                    List<String> serviceSupportedZones,
                                    Date         effectiveFrom,
                                    Date         effectiveTo,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  elementGUID,
                                  elementGUIDParameterName,
                                  elementType,
                                  keywordGUID,
                                  keywordGUIDParameterName,
                                  OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_GUID,
                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Removes the link between a keyword and an element.
     *
     * @param userId    userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID unique id for the referenceable to connect to
     * @param elementGUIDParameterName name of the parameter supplying the elementGUID
     * @param elementType type of the element
     * @param keywordGUID   unique id for the keyword
     * @param keywordGUIDParameterName name of parameter supplying keywordGUID
     * @param serviceSupportedZones list of supported zones for this service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the keyword properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeKeywordFromElement(String       userId,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       elementGUID,
                                         String       elementGUIDParameterName,
                                         String       elementType,
                                         String       keywordGUID,
                                         String       keywordGUIDParameterName,
                                         List<String> serviceSupportedZones,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
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
                                      keywordGUID,
                                      keywordGUIDParameterName,
                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Create a related keyword relationship between search keywords.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param keywordOneGUID       unique id for the first keyword
     * @param keywordOneGUIDParameterName name of the parameter supplying the keywordOneGUID
     * @param keywordTwoGUID          unique id of the second keyword
     * @param keywordTwoGUIDParameterName name of the parameter supplying the keywordTwoGUID
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the keyword properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRelatedKeywords(String       userId,
                                    String       externalSourceGUID,
                                    String       externalSourceName,
                                    String       keywordOneGUID,
                                    String       keywordOneGUIDParameterName,
                                    String       keywordTwoGUID,
                                    String       keywordTwoGUIDParameterName,
                                    List<String> serviceSupportedZones,
                                    Date         effectiveFrom,
                                    Date         effectiveTo,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  keywordOneGUID,
                                  keywordOneGUIDParameterName,
                                  OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                  keywordTwoGUID,
                                  keywordTwoGUIDParameterName,
                                  OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID,
                                  OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Removes a relationship between keywords.
     *
     * @param userId    userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param keywordOneGUID unique id for the first keyword.
     * @param keywordOneGUIDParameterName name of the parameter supplying the keywordOneGUID
     * @param keywordTwoGUID   unique id for the second keyword.
     * @param keywordTwoGUIDParameterName name of parameter supplying keywordTwoGUID
     * @param serviceSupportedZones list of supported zones for this service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the keyword properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkRelatedKeywords(String       userId,
                                      String       externalSourceGUID,
                                      String       externalSourceName,
                                      String       keywordOneGUID,
                                      String       keywordOneGUIDParameterName,
                                      String       keywordTwoGUID,
                                      String       keywordTwoGUIDParameterName,
                                      List<String> serviceSupportedZones,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      keywordOneGUID,
                                      keywordOneGUIDParameterName,
                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                      keywordTwoGUID,
                                      keywordTwoGUIDParameterName,
                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID,
                                      OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }
}
