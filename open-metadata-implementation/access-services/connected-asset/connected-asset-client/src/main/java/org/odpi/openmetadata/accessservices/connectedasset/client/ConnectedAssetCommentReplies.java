/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;


import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.rest.CommentResponse;
import org.odpi.openmetadata.accessservices.connectedasset.rest.CommentsResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedAssetComments provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) AssetComments abstract class.
 * Its role is to query the property servers (metadata repository cohort) to extract comments
 * related to the connected asset.
 */
public class ConnectedAssetCommentReplies extends AssetCommentReplies
{
    private String         serverName;
    private String         userId;
    private String         omasServerURL;
    private String         rootCommentGUID;
    private ConnectedAsset connectedAsset;
    private int            maxCacheSize;


    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param rootCommentGUID unique identifier of the comment that the replies are attached to.
     * @param parentAsset descriptor of parent asset.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    ConnectedAssetCommentReplies(String              serverName,
                                 String              userId,
                                 String              omasServerURL,
                                 String              rootCommentGUID,
                                 ConnectedAsset      parentAsset,
                                 int                 totalElementCount,
                                 int                 maxCacheSize)
    {
        super(parentAsset, totalElementCount, maxCacheSize);

        this.serverName      = serverName;
        this.userId          = userId;
        this.omasServerURL   = omasServerURL;
        this.rootCommentGUID = rootCommentGUID;
        this.connectedAsset  = parentAsset;
        this.maxCacheSize    = maxCacheSize;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedAssetCommentReplies(ConnectedAsset   parentAsset, ConnectedAssetCommentReplies template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.serverName      = template.serverName;
            this.userId          = template.userId;
            this.omasServerURL   = template.omasServerURL;
            this.rootCommentGUID = template.rootCommentGUID;
            this.connectedAsset  = parentAsset;
            this.maxCacheSize    = template.maxCacheSize;
        }
    }


    /**
     * Clones this iterator.
     *
     * @param parentAsset descriptor of parent asset
     * @return new cloned object.
     */
    protected  AssetCommentReplies cloneIterator(AssetDescriptor parentAsset)
    {
        return new ConnectedAssetCommentReplies(connectedAsset, this);
    }



    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    protected  AssetPropertyBase cloneElement(AssetDescriptor  parentAsset, AssetPropertyBase template)
    {
        return new AssetComment(parentAsset, (AssetComment)template);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    protected  List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                     int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "AssetCommentReplies.getCachedList";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/connected-asset/users/{1}/assets/{2}/comments?elementStart={3}&maxElements={4}";

        connectedAsset.validateOMASServerURL(methodName);

        try
        {
            CommentsResponse restResult = (CommentsResponse)connectedAsset.callGetRESTCall(methodName,
                                                                                           CommentsResponse.class,
                                                                                           omasServerURL + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           rootCommentGUID,
                                                                                           cacheStartPointer,
                                                                                           maximumSize);

            connectedAsset.detectAndThrowInvalidParameterException(methodName, restResult);
            connectedAsset.detectAndThrowUnrecognizedAssetGUIDException(methodName, restResult);
            connectedAsset.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            connectedAsset.detectAndThrowPropertyServerException(methodName, restResult);

            List<CommentResponse> Responses = restResult.getList();
            if ((Responses == null) || (Responses.isEmpty()))
            {
                return null;
            }
            else
            {
                List<AssetPropertyBase>   resultList = new ArrayList<>();

                for (CommentResponse  commentResponse : Responses)
                {
                    if (commentResponse != null)
                    {
                        Comment                      bean = commentResponse.getComment();
                        ConnectedAssetCommentReplies commentReplies = null;

                        if (commentResponse.getReplyCount() > 0)
                        {
                            commentReplies = new ConnectedAssetCommentReplies(serverName,
                                                                              userId,
                                                                              omasServerURL,
                                                                              bean.getGUID(),
                                                                              connectedAsset,
                                                                              commentResponse.getReplyCount(),
                                                                              maxCacheSize);
                        }

                        /*
                         * Note replies are ignored - but can be extracted through the Asset Consumer OMAS
                         */
                        resultList.add(new AssetComment(connectedAsset, bean, commentReplies));
                    }
                }

                return resultList;
            }
        }
        catch (Throwable  error)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.EXCEPTION_RESPONSE_FROM_API;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     omasServerURL,
                                                                                                     error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }
}
