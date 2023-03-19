/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;


import org.odpi.openmetadata.frameworks.connectors.properties.AssetFeedback;

/**
 * ConnectedFeedback provides the open metadata extension to the Open Connector Framework (OCF)'s AssetFeedback
 * class that returned the comments, tags, likes and ratings associated with an asset.
 */
public class ConnectedFeedback extends AssetFeedback
{
    private static final long    serialVersionUID = 1L;

    /**
     * Typical constructor creates an AssetFeedback object primed with the iterators for the asset's comments,
     * tags, likes and ratings.
     *
     * @param serviceName calling service
     * @param serverName name of server to use on server calls.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param commentCount the total number of comments to process.  A negative value is converted to 0.
     * @param likeCount the total number of likes to process.  A negative value is converted to 0.
     * @param ratingCount the total number of ratings to process.  A negative value is converted to 0.
     * @param tagCount the total number of comments to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedFeedback(String                 serviceName,
                      String                 serverName,
                      String                 userId,
                      String                 omasServerURL,
                      String                 assetGUID,
                      int                    commentCount,
                      int                    likeCount,
                      int                    ratingCount,
                      int                    tagCount,
                      int                    maxCacheSize,
                      OCFRESTClient          restClient)
    {
        super();

        if (commentCount > 0)
        {
            super.comments = new ConnectedComments(serviceName,
                                                   serverName,
                                                   userId,
                                                   omasServerURL,
                                                   assetGUID,
                                                   commentCount,
                                                   maxCacheSize,
                                                   restClient);
        }


        if (likeCount > 0)
        {
            super.likes = new ConnectedLikes(serviceName,
                                             serverName,
                                             userId,
                                             omasServerURL,
                                             assetGUID,
                                             likeCount,
                                             maxCacheSize,
                                             restClient);
        }

        if (ratingCount > 0)
        {
            super.ratings = new ConnectedAssetRatings(serviceName,
                                                      serverName,
                                                      userId,
                                                      omasServerURL,
                                                      assetGUID,
                                                      ratingCount,
                                                      maxCacheSize,
                                                      restClient);
        }

        if (tagCount > 0)
        {
            super.informalTags = new ConnectedInformalTags(serviceName,
                                                           serverName,
                                                           userId,
                                                           omasServerURL,
                                                           assetGUID,
                                                           tagCount,
                                                           maxCacheSize,
                                                           restClient);
        }
    }
}
