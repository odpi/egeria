/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.handlers;


import org.odpi.openmetadata.accessservices.assetconsumer.AssetConsumerTaggingInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.Tag;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * TaggingHandler provides the server-side support for the Asset Consumer OMAS's Tagging Interface.
 * This provides methods for managing informal tags (both public and private) and attaching/detaching
 * them to assets.
 */
public class TaggingHandler implements AssetConsumerTaggingInterface
{
    private static final String informalTagTypeName                  = "InformalTag";
    private static final String informalTagTypeGUID                  = "ba846a7b-2955-40bf-952b-2793ceca090a";
    private static final String privateTagTypeName                   = "PrivateTag";
    private static final String privateTagTypeGUID                   = "9b3f5443-2475-4522-bfda-8f1f17e9a6c3";
    private static final String tagNamePropertyName                  = "TagName";
    private static final String tagDescriptionPropertyName           = "TagDescription";
    private static final String attachedTagTypeGUID                  = "4b1641c4-3d1a-4213-86b2-d6968b6c65ab";
    private static final String assetTypeName                        = "Asset";


    private String               serviceName;

    private String               serverName       = null;
    private OMRSRepositoryHelper repositoryHelper = null;
    private ErrorHandler         errorHandler     = null;
    private RepositoryHandler    basicHandler     = null;


    /**
     * Construct the feedback handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    public TaggingHandler(String                  serviceName,
                          OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;

        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new ErrorHandler(repositoryConnector);
            basicHandler = new RepositoryHandler(serviceName, repositoryConnector);
        }
    }


    /**
     * Adds a new public tag to the asset's properties.
     *
     * @param userId          String - userId of user making request.
     * @param assetGUID       String - unique id for the asset.
     * @param tagName         String - name of the tag.
     * @param tagDescription  String - (optional) description of the tag.  Setting a description, particularly in
     *                       a public tag makes the tag more valuable to other users and can act as an embryonic
     *                       glossary term.
     *
     * @return String - GUID for new tag.
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws PropertyServerException    - There is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String addTagToAsset(String tagTypeGUID,
                                 String userId,
                                 String assetGUID,
                                 String tagName,
                                 String tagDescription,
                                 String methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String guidParameter = "assetGUID";
        final String nameParameter = "tagName";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGUID, guidParameter, methodName);
        errorHandler.validateName(tagName, nameParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        basicHandler.validateEntityGUID(userId, assetGUID, assetTypeName, metadataCollection, methodName);

        try
        {
            /*
             * Create the Tag Entity
             */
            InstanceProperties properties;

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      tagNamePropertyName,
                                                                      tagName,
                                                                      methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      tagDescriptionPropertyName,
                                                                      tagDescription,
                                                                      methodName);
            EntityDetail feedbackEntity = metadataCollection.addEntity(userId,
                                                                       tagTypeGUID,
                                                                       properties,
                                                                       null,
                                                                       InstanceStatus.ACTIVE);

            String  feedbackGUID = null;

            if (feedbackEntity != null)
            {
                feedbackGUID = feedbackEntity.getGUID();
            }

            return feedbackGUID;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        return null;
    }



    /**
     * Creates a new public informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createPublicTag(String userId,
                                  String tagName,
                                  String tagDescription) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName = "createPublicTag";



        return null;
    }


    /**
     * Creates a new private informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createPrivateTag(String userId,
                                   String tagName,
                                   String tagDescription) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "createPrivateTag";

        // todo

        return null;
    }


    /**
     * Updates the description of an existing tag (either private of public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateTagDescription(String userId,
                                       String tagGUID,
                                       String tagDescription) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName = "updateTagDescription";

        // todo
    }


    /**
     * Removes a tag from the repository.  All of the relationships to assets are lost.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String userId,
                            String tagGUID) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException
    {
        final String   methodName = "deleteTag";

        // todo
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Tag getTag(String userId,
                      String guid) throws InvalidParameterException,
                                          PropertyServerException,
                                          UserNotAuthorizedException
    {
        final String   methodName = "getTag";

        // todo

        return null;
    }


    /**
     * Return the list of tags matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<Tag> getTagsByName(String userId,
                                   String tag,
                                   int    startFrom,
                                   int    pageSize) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String   methodName = "getTagsByName";

        // todo

        return null;
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param userId           userId of user making request.
     * @param assetGUID        unique id for the asset.
     * @param tagGUID          unique id of the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addTagToAsset(String userId,
                                String assetGUID,
                                String tagGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String   methodName  = "addTagToAsset";

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        basicHandler.validateEntityGUID(userId, assetGUID, assetTypeName, metadataCollection, methodName);
        basicHandler.validateEntityGUID(userId, tagGUID, informalTagTypeName, metadataCollection, methodName);

        try
        {
            /*
             * Link the tag to the asset
             */
            metadataCollection.addRelationship(userId,
                                               attachedTagTypeGUID,
                                               null,
                                               assetGUID,
                                               tagGUID,
                                               InstanceStatus.ACTIVE);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeTagFromAsset(String userId,
                                     String assetGUID,
                                     String tagGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String methodName = "removeTagFromAsset";
        final String guidParameter = "tagGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(tagGUID, guidParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            metadataCollection.deleteEntity(userId, informalTagTypeGUID, informalTagTypeName, tagGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

    }
}
