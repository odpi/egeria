/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.InformalTagBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.InformalTagConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.InformalTagMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.InformalTag;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * InformalTagHandler manages InformalTag objects.  These are user controlled semantic tags. InformalTagHandler runs server-side in
 * the OMAG Server Platform and retrieves InformalTag entities through the OMRSRepositoryConnector.
 */
public class InformalTagHandler extends FeedbackHandlerBase
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public InformalTagHandler(String                  serviceName,
                              String                  serverName,
                              InvalidParameterHandler invalidParameterHandler,
                              RepositoryHandler       repositoryHandler,
                              OMRSRepositoryHelper    repositoryHelper,
                              LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);
    }


    /**
     * Count the number of informal tags attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countTags(String   userId,
                         String   anchorGUID,
                         String   methodName) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException
    {
        return this.countAttachments(userId,
                                     anchorGUID,
                                     ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                     InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                     InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                     methodName);
    }


    /**
     * Return the informal tags attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the identifier is attached to
     * @param anchorTypeName type name for anchor
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<InformalTag>  getAttachedTags(String   userId,
                                              String   anchorGUID,
                                              String   anchorTypeName,
                                              int      startingFrom,
                                              int      pageSize,
                                              String   methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        /*
         * Validates the parameters and retrieves the links to attached tags that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship>  relationships = super.getAttachmentLinks(userId,
                                                                     anchorGUID,
                                                                     anchorTypeName,
                                                                     InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                                                     InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                                                     startingFrom,
                                                                     pageSize,
                                                                     methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<InformalTag>  results = new ArrayList<>();

        for (Relationship  relationship : relationships)
        {
            if (relationship != null)
            {
                InformalTag bean = this.getTag(userId, relationship, methodName);
                if (bean != null)
                {
                    results.add(bean);
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the requested informal tag object - assumption is that the relationship
     * is visible to the end user.
     *
     * @param userId       calling user
     * @param relationship relationship between referenceable and tag
     * @param methodName   calling method
     * @return new bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private InformalTag getTag(String       userId,
                               Relationship relationship,
                               String       methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String guidParameterName = "relationship.end2.guid";

        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityTwoProxy();

            if (entityProxy != null)
            {
                EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityProxy.getGUID(),
                                                                        guidParameterName,
                                                                        InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                                                        methodName);

                InformalTagConverter converter = new InformalTagConverter(entity,
                                                                          relationship,
                                                                          repositoryHelper,
                                                                          serviceName);
                return converter.getBean();
            }
        }

        return null;
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param methodName calling method
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createTag(String  userId,
                            String  tagName,
                            String  tagDescription,
                            boolean isPublic,
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
                                                            userId,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        return repositoryHandler.createEntity(userId,
                                              InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                              InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                              builder.getInstanceProperties(methodName),
                                              methodName);
    }


    /**
     * Updates the description of an existing tag (either private of public).  Private tags can only be updated by their creator.
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     * @param methodName      calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateTagDescription(String  userId,
                                       String  tagGUID,
                                       String  tagDescription,
                                       String  methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        InformalTag  tag = this.getTag(userId, tagGUID, methodName);

        InformalTagBuilder builder = new InformalTagBuilder(tag.getName(),
                                                            tagDescription,
                                                            tag.isPublic(),
                                                            tag.getUser(),
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        repositoryHandler.updateEntity(userId,
                                       tagGUID,
                                       InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                       InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                       builder.getInstanceProperties(methodName),
                                       methodName);
    }


    /**
     * Removes a tag from the repository.
     * A private tag can be deleted by its creator and all of the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String userId,
                            String tagGUID,
                            String methodName) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException
    {
        InformalTag  tag = this.getTag(userId, tagGUID, methodName);

        if (tag != null)
        {
            if (tag.isPrivateTag())
            {
                repositoryHandler.removeEntity(userId,
                                               tagGUID,
                                               InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                               InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                               null,
                                               null,
                                               methodName);
            }
            else
            {
                if (repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                       tagGUID,
                                                                       InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                                                       InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                                                       InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                                                       methodName) == 0)
                {
                    repositoryHandler.removeEntity(userId,
                                                   tagGUID,
                                                   InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                                   InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                                   null,
                                                   null,
                                                   methodName);
                }
                else
                {
                    invalidParameterHandler.throwCannotDeleteElementInUse(tagGUID,
                                                                          InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                                                          serviceName,
                                                                          methodName);
                }
            }
        }
    }


    /**
     * Return the tag for the supplied unique identifier (guid).  The tag is only returned if
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     * @param methodName calling method
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public InformalTag getTag(String userId,
                              String guid,
                              String methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        final String tagGUIDParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, tagGUIDParameterName, methodName);

        EntityDetail tagEntity = repositoryHandler.getEntityByGUID(userId,
                                                                   guid,
                                                                   tagGUIDParameterName,
                                                                   InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                                                   methodName);

        if (tagEntity != null)
        {
            InformalTagConverter converter = new InformalTagConverter(tagEntity,
                                                                      repositoryHelper,
                                                                      serviceName);
            InformalTag tag = converter.getBean();

            /*
             * Only return a private tag to its creator.
             */
            if (tag.isPrivateTag())
            {
                if (userId.equals(tag.getUser()))
                {
                    return tag;
                }
            }
            else
            {
                return tag;
            }
        }

        return null;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param name name of tag.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTag> getTagsByName(String userId,
                                           String name,
                                           int    startFrom,
                                           int    pageSize,
                                           String methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InformalTagBuilder builder = new InformalTagBuilder(name,
                                                            userId,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        List<InformalTag>  results = new ArrayList<>();

        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        /*
         * The loop is necessary because some of the tags returned may not be visible to the calling user.
         * Once they are filtered out, more tags need to be retrieved to fill the gaps.
         */
        while (moreResultsAvailable && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                       builder.getNameInstanceProperties(methodName),
                                                                                       InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                                                                       startNextQueryFrom,
                                                                                       queryPageSize,
                                                                                       methodName);

            if (retrievedEntities != null)
            {
                results = this.getTagList(userId,
                                          retrievedEntities,
                                          results,
                                          queryPageSize);

                moreResultsAvailable = (retrievedEntities.size() == queryPageSize);
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
            else
            {
                moreResultsAvailable = false;
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param name name of tag.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTag> getMyTagsByName(String userId,
                                             String name,
                                             int    startFrom,
                                             int    pageSize,
                                             String methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InformalTagBuilder builder = new InformalTagBuilder(name,
                                                            userId,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);

        List<InformalTag>  results = new ArrayList<>();

        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        /*
         * The loop is necessary because some of the tags returned may not be visible to the calling user.
         * Once they are filtered out, more tags need to be retrieved to fill the gaps.
         */
        while (moreResultsAvailable && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByAllProperties(userId,
                                                                                                builder.getUserNameInstanceProperties(methodName),
                                                                                                InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                                                                                startNextQueryFrom,
                                                                                                queryPageSize,
                                                                                                methodName);


            if (retrievedEntities != null)
            {
                results = this.getMyTagList(retrievedEntities, results, queryPageSize);

                moreResultsAvailable = (retrievedEntities.size() == queryPageSize);
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
            else
            {
                moreResultsAvailable = false;
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the list of tags matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of tag.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTag> findTags(String userId,
                                      String searchString,
                                      int    startFrom,
                                      int    pageSize,
                                      String methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String nameParameterName = "searchString";

        invalidParameterHandler.validateName(searchString, nameParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InformalTagBuilder builder = new InformalTagBuilder(searchString,
                                                            searchString,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);



        List<InformalTag>  results = new ArrayList<>();

        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        /*
         * The loop is necessary because some of the tags returned may not be visible to the calling user.
         * Once they are filtered out, more tags need to be retrieved to fill the gaps.
         */
        while (moreResultsAvailable && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                       builder.getSearchInstanceProperties(methodName),
                                                                                       InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                                                                       startNextQueryFrom,
                                                                                       queryPageSize,
                                                                                       methodName);

            if (retrievedEntities != null)
            {
                results = this.getTagList(userId,
                                          retrievedEntities,
                                          results,
                                          queryPageSize);

                moreResultsAvailable = (retrievedEntities.size() == queryPageSize);
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
            else
            {
                moreResultsAvailable = false;
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the list of private tags for the user matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of tag.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTag> findMyTags(String userId,
                                        String searchString,
                                        int    startFrom,
                                        int    pageSize,
                                        String methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String nameParameterName = "searchString";

        invalidParameterHandler.validateName(searchString, nameParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InformalTagBuilder builder = new InformalTagBuilder(searchString,
                                                            searchString,
                                                            repositoryHelper,
                                                            serviceName,
                                                            serverName);



        List<InformalTag>  results = new ArrayList<>();

        boolean      moreResultsAvailable = true;
        int          startNextQueryFrom = startFrom;

        /*
         * The loop is necessary because some of the tags returned may not be visible to the calling user.
         * Once they are filtered out, more tags need to be retrieved to fill the gaps.
         */
        while (moreResultsAvailable && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByAllProperties(userId,
                                                                                                builder.getUserSearchInstanceProperties(methodName),
                                                                                                InformalTagMapper.INFORMAL_TAG_TYPE_GUID,
                                                                                                startNextQueryFrom,
                                                                                                queryPageSize,
                                                                                                methodName);

            if (retrievedEntities != null)
            {
                results = this.getTagList(userId,
                                          retrievedEntities,
                                          results,
                                          queryPageSize);

                moreResultsAvailable = (retrievedEntities.size() == queryPageSize);
                startNextQueryFrom = startNextQueryFrom + queryPageSize;
            }
            else
            {
                moreResultsAvailable = false;
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the list of tags that can be retrieved from the entities that came from the repository.
     *
     * @param userId calling user
     * @param retrievedEntities entities from repository
     * @param currentTagList currently extracted assets
     * @param maxPageSize maximum number of assets that can be returned in total.
     * @return accumulated list of assets
     */
    private List<InformalTag>  getTagList(String             userId,
                                          List<EntityDetail> retrievedEntities,
                                          List<InformalTag>  currentTagList,
                                          int                maxPageSize)
    {
        List<InformalTag>  results = currentTagList;

        if (results == null)
        {
            results = new ArrayList<>();
        }

        if (retrievedEntities != null)
        {
            for (EntityDetail entity : retrievedEntities)
            {
                if ((entity != null) && (results.size() < maxPageSize))
                {
                    InformalTagConverter  converter = new InformalTagConverter(entity, null, repositoryHelper, serviceName);
                    InformalTag           informalTag = converter.getBean();

                    if (informalTag != null)
                    {
                        if ((! informalTag.isPrivateTag() || (userId.equals(informalTag.getUser()))))
                        {
                            results.add(informalTag);
                        }
                    }
                }
            }
        }

        return results;
    }


    /**
     * Return the list of private for this user tags that can be retrieved from the entities that came from the repository.
     *
     * @param retrievedEntities entities from repository
     * @param currentTagList currently extracted assets
     * @param maxPageSize maximum number of assets that can be returned in total.
     * @return accumulated list of assets
     */
    private List<InformalTag>  getMyTagList(List<EntityDetail> retrievedEntities,
                                            List<InformalTag>  currentTagList,
                                            int                maxPageSize)
    {
        List<InformalTag>  results = currentTagList;

        if (results == null)
        {
            results = new ArrayList<>();
        }

        if (retrievedEntities != null)
        {
            for (EntityDetail entity : retrievedEntities)
            {
                if ((entity != null) && (results.size() < maxPageSize))
                {
                    InformalTagConverter  converter = new InformalTagConverter(entity, null, repositoryHelper, serviceName);
                    InformalTag           informalTag = converter.getBean();

                    if (informalTag != null)
                    {
                        if (informalTag.isPrivateTag())
                        {
                            results.add(informalTag);
                        }
                    }
                }
            }
        }

        return results;
    }


    /**
     * Adds a tag (either private of public) to an anchor.
     *
     * @param userId           userId of user making request.
     * @param anchorGUID       unique id for the anchor.
     * @param anchorType       type of the anchor.
     * @param tagGUID          unique id of the tag.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param methodName       calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addTagToAnchor(String  userId,
                               String  anchorGUID,
                               String  anchorType,
                               String  tagGUID,
                               boolean isPublic,
                               String  methodName) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final String tagGUIDParameterName = "tagGUID";
        final String anchorGUIDParameterName = "anchorGUID";
        final String lastAttachmentDescription = "Added informal tag to ";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);

        repositoryHandler.validateEntityGUID(userId, anchorGUID, anchorType, methodName, anchorGUIDParameterName);
        repositoryHandler.validateEntityGUID(userId, tagGUID, InformalTagMapper.INFORMAL_TAG_TYPE_NAME, methodName, tagGUIDParameterName);

        InstanceProperties properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      InformalTagMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                                      isPublic,
                                                                                      methodName);

        repositoryHandler.createRelationship(userId,
                                             InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                             anchorGUID,
                                             tagGUID,
                                             properties,
                                             methodName);
        if (isPublic)
        {
            lastAttachmentHandler.updateLastAttachment(anchorGUID,
                                                       anchorType,
                                                       tagGUID,
                                                       InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                                       userId,
                                                       lastAttachmentDescription + tagGUID,
                                                       methodName);
        }
    }


    /**
     * Removes a tag from the anchor that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param anchorGUID unique id for the asset.
     * @param anchorType type of the anchor.
     * @param tagGUID   unique id for the tag.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeTagFromAnchor(String userId,
                                    String anchorGUID,
                                    String anchorType,
                                    String tagGUID,
                                    String methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String tagGUIDParameterName = "tagGUID";
        final String assetGUIDParameterName = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, assetGUIDParameterName, methodName);

        repositoryHandler.validateEntityGUID(userId, anchorGUID, anchorType, methodName, assetGUIDParameterName);
        InformalTag tag = this.getTag(userId, tagGUID,  methodName);

        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                                            InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                                            anchorGUID,
                                                            AssetMapper.ASSET_TYPE_NAME,
                                                            tagGUID,
                                                            methodName);

        if (! tag.isPrivateTag())
        {
            lastAttachmentHandler.updateLastAttachment(anchorGUID,
                                                       anchorType,
                                                       tagGUID,
                                                       InformalTagMapper.INFORMAL_TAG_TYPE_NAME,
                                                       userId,
                                                       "Remove tag " + tagGUID,
                                                       methodName);
        }
    }
}
