/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.handlers;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * FeedbackHandler manages the creation of feedback (likes, ratings, comments and tags) in the
 * property org.odpi.openmetadata.accessservices.subjectarea.server.
 */
public class FeedbackHandler
{
    private static final String informalTagTypeName                  = "InformalTag";
    private static final String informalTagTypeGUID                  = "ba846a7b-2955-40bf-952b-2793ceca090a";
    private static final String privateTagTypeName                   = "PrivateTag";
    private static final String privateTagTypeGUID                   = "9b3f5443-2475-4522-bfda-8f1f17e9a6c3";
    private static final String tagNamePropertyName                  = "TagName";
    private static final String tagDescriptionPropertyName           = "TagDescription";
    private static final String attachedTagTypeGUID                  = "4b1641c4-3d1a-4213-86b2-d6968b6c65ab";

    private static final String likeTypeName                         = "Like";
    private static final String likeTypeGUID                         = "deaa5ca0-47a0-483d-b943-d91c76744e01";
    private static final String attachedLikeTypeGUID                 = "e2509715-a606-415d-a995-61d00503dad4";

    private static final String ratingTypeName                       = "Rating";
    private static final String ratingTypeGUID                       = "7299d721-d17f-4562-8286-bcd451814478";
    private static final String starsPropertyName                    = "stars";
    private static final String reviewPropertyName                   = "review";
    private static final String attachedRatingTypeGUID               = "0aaad9e9-9cc5-4ad8-bc2e-c1099bab6344";

    private static final String commentTypeName                      = "Comment";
    private static final String commentTypeGUID                      = "1a226073-9c84-40e4-a422-fbddb9b84278";
    private static final String qualifiedNamePropertyName            = "qualifiedName";
    private static final String commentPropertyName                  = "comment";
    private static final String commentTypePropertyName              = "commentType";
    private static final String attachedCommentTypeGUID              = "0d90501b-bf29-4621-a207-0c8c953bdac9";

    private String                  serviceName;

    private String                  serverName = null;
    private OMRSRepositoryHelper repositoryHelper = null;
    private ErrorHandler            errorHandler     = null;


    /**
     * Construct the feedback handler with a link to the property org.odpi.openmetadata.accessservices.subjectarea.server's connector and this access service's
     * official name.
     *
     * @param serviceName - name of this service
     * @param repositoryConnector - connector to the property org.odpi.openmetadata.accessservices.subjectarea.server.
     */
    public FeedbackHandler(String                  serviceName,
                           OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;

        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
        }
    }
}
