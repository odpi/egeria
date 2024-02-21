/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;


import org.odpi.openmetadata.frameworks.connectors.properties.AssetFeedback;

/**
 * ConnectedFeedback provides the open metadata extension to the Open Connector Framework (OCF)'s AssetFeedback
 * class that returned the comments, tags, likes and ratings associated with an asset.
 */
public class ConnectedFeedback extends AssetFeedback
{
    /**
     * Typical constructor creates an AssetFeedback object primed with the iterators for the asset's comments,
     * tags, likes and ratings.
     *
     * @param serviceName calling service
     * @param serverName name of server to use on server calls.
     * @param userId user id to use on server calls.
     * @param platformURLRoot url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedFeedback(String                 serviceName,
                      String                 serverName,
                      String                 userId,
                      String                 platformURLRoot,
                      String                 assetGUID,
                      int                    maxCacheSize,
                      OCFRESTClient          restClient)
    {
        super();

        super.comments = new ConnectedComments(serviceName,
                                               serverName,
                                               userId,
                                               platformURLRoot,
                                               assetGUID,
                                               maxCacheSize,
                                               restClient);

        super.likes = new ConnectedLikes(serviceName,
                                         serverName,
                                         userId,
                                         platformURLRoot,
                                         assetGUID,
                                         maxCacheSize,
                                         restClient);

        super.ratings = new ConnectedAssetRatings(serviceName,
                                                  serverName,
                                                  userId,
                                                  platformURLRoot,
                                                  assetGUID,
                                                  maxCacheSize,
                                                  restClient);

        super.informalTags = new ConnectedInformalTags(serviceName,
                                                       serverName,
                                                       userId,
                                                       platformURLRoot,
                                                       assetGUID,
                                                       maxCacheSize,
                                                       restClient);
    }
}
