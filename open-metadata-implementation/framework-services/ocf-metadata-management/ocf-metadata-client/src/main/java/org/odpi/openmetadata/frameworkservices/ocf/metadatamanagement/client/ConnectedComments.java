/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.CommentResponse;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.CommentsResponse;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.CommentConversation;
import org.odpi.openmetadata.frameworks.connectors.properties.Comments;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedComments provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) Comments abstract class.
 * Its role is to query the property servers (metadata repository cohort) to extract comments
 * related to the connected asset.
 */
public class ConnectedComments extends Comments
{
    private String                 serviceName;
    private String                 serverName;
    private String                 userId;
    private String                 platformURLRoot;
    private String                 assetGUID;
    private int                    maxCacheSize;
    private OCFRESTClient          restClient;

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param platformURLRoot url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedComments(String                 serviceName,
                      String                 serverName,
                      String                 userId,
                      String                 platformURLRoot,
                      String                 assetGUID,
                      int                    maxCacheSize,
                      OCFRESTClient          restClient)
    {
        super(maxCacheSize);

        this.serviceName     = serviceName;
        this.serverName      = serverName;
        this.userId          = userId;
        this.platformURLRoot   = platformURLRoot;
        this.assetGUID       = assetGUID;
        this.maxCacheSize    = maxCacheSize;
        this.restClient      = restClient;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedComments(ConnectedComments template)
    {
        super(template);

        if (template != null)
        {
            this.serviceName     = template.serviceName;
            this.serverName      = template.serverName;
            this.userId          = template.userId;
            this.platformURLRoot = template.platformURLRoot;
            this.assetGUID       = template.assetGUID;
            this.maxCacheSize    = template.maxCacheSize;
            this.restClient      = template.restClient;
        }
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    @Override
    protected Comments cloneIterator()
    {
        return new ConnectedComments(this);
    }



    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param template object to clone
     * @return new cloned object.
     */
    @Override
    protected ElementBase cloneElement(ElementBase template)
    {
        if (template instanceof CommentConversation)
        {
            return new CommentConversation((CommentConversation)template);
        }
        else if (template instanceof Comment)
        {
            return new CommentConversation((Comment)template);
        }

        return null;
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    @Override
    protected  List<ElementBase> getCachedList(int  cacheStartPointer,
                                               int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "Comments.getCachedList";
        final String   urlTemplate = "/servers/{0}/open-metadata/framework-services/{1}/connected-asset/users/{2}/assets/{3}/comments?elementStart={4}&maxElements={5}";

        RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();

        try
        {
            CommentsResponse restResult = restClient.callOCFCommentsGetRESTCall(methodName,
                                                                                platformURLRoot + urlTemplate,
                                                                                serverName,
                                                                                serviceName,
                                                                                userId,
                                                                                assetGUID,
                                                                                cacheStartPointer,
                                                                                maximumSize);

            restExceptionHandler.detectAndThrowInvalidParameterException(restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(restResult);

            List<CommentResponse> Responses = restResult.getList();
            if ((Responses == null) || (Responses.isEmpty()))
            {
                return null;
            }
            else
            {
                List<ElementBase>   resultList = new ArrayList<>();

                for (CommentResponse  commentResponse : Responses)
                {
                    if (commentResponse != null)
                    {
                        Comment                 bean           = commentResponse.getComment();
                        ConnectedCommentReplies commentReplies = new ConnectedCommentReplies(serviceName,
                                                                                             serverName,
                                                                                             userId,
                                                                                             platformURLRoot,
                                                                                             bean.getGUID(),
                                                                                             maxCacheSize,
                                                                                             restClient);

                        resultList.add(new CommentConversation(bean, commentReplies));
                    }
                }

                return resultList;
            }
        }
        catch (Exception  error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, serverName, platformURLRoot);
        }

        return null;
    }
}
