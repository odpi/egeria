/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.fvt;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetconsumer.client.rest.AssetConsumerRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetFeedback;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.Comments;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CommentType;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;



/**
 * CommentLifecycleTest calls the AssetConsumerClient to create a comment and attach it to assets and schemas
 * and then retrieve the results.
 */
public class CommentLifecycleTest
{
    private final static String testCaseName       = "CommentLifecycleTest";

    private final static int    maxPageSize        = 100;

    /*
     * The comment name is constant - the guid is created as part of the test.
     */
    private final static String publicComment1Name         = "TestPublicComment1";
    private final static String publicComment1Description1 = "PublicComment1 description1";
    private final static String publicComment1Description2 = "PublicComment1 description2";
    private final static String publicComment2Name         = "TestPublicComment2";
    private final static String publicComment2Description1 = "PublicComment2 description1";
    private final static String publicComment2Description2 = "PublicComment2 description2";
    private final static String privateCommentName         = "TestPrivateComment";
    private final static String privateCommentDescription1 = "PrivateComment description1";
    private final static String privateCommentDescription2 = "PrivateComment description2";

    private final static String differentUser = "newUserId";

    private final static String searchStringGetAll     = "Comment";
    private final static String searchStringGetNone    = "Blah";
    private final static String searchStringGetPrivate = "Private";




    /**
     * Run all the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return  results of running test
     */
    public static FVTResults performFVT(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId)
    {
        FVTResults results = new FVTResults(testCaseName);

        results.incrementNumberOfTests();
        try
        {
            CommentLifecycleTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        System.out.println(results);
        return results;
    }


    /**
     * Run all the tests in this class.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLogDestination logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private static void runIt(String                 serverPlatformRootURL,
                              String                 serverName,
                              String                 userId,
                              FVTAuditLogDestination auditLogDestination) throws FVTUnexpectedCondition
    {
        CommentLifecycleTest thisTest = new CommentLifecycleTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceWiki());

        AssetConsumer client = thisTest.getAssetConsumerClient(serverName, serverPlatformRootURL, auditLog);
        AssetOwnerFactory factory = new AssetOwnerFactory(testCaseName, serverName, serverPlatformRootURL, auditLog);

        String assetGUID = factory.getAsset(userId);
        System.out.println("AssetGUID: " + assetGUID);

        String schemaTypeGUID = factory.getSchemaType(userId, assetGUID);
        System.out.println("SchemaTypeGUID: " + schemaTypeGUID);

        String asset2GUID = factory.getAssetFromTemplate(userId, assetGUID);
        System.out.println("Asset2GUID: " + asset2GUID);

        /* Create Comment */
        String publicComment1GUID = thisTest.getCommentTest(client, userId, assetGUID, CommentType.QUESTION, publicComment1Description1, true, "getPublicComment", publicComment1Name);
        System.out.println("PublicComment1GUID: " + publicComment1GUID);
        String publicComment2GUID = thisTest.getCommentTest(client, userId, asset2GUID, CommentType.USAGE_EXPERIENCE, publicComment2Description1, true, "getPublicComment", publicComment2Name);
        System.out.println("PublicComment2GUID: " + publicComment2GUID);
        String privateCommentGUID = thisTest.getCommentTest(client, userId, assetGUID, CommentType.SUGGESTION, privateCommentDescription1, false, "getPrivateComment", privateCommentName);
        System.out.println("PrivateCommentGUID: " + privateCommentGUID);


        /* Update Comment */
        thisTest.updateCommentTest(client, userId, assetGUID, publicComment1GUID, CommentType.STANDARD_COMMENT, publicComment1Description2, false, "updatePublicComment1", publicComment1Name);
        thisTest.updateCommentTest(client, userId, asset2GUID, publicComment2GUID, CommentType.STANDARD_COMMENT, publicComment2Description2, false, "updatePublicComment2", publicComment2Name);
        thisTest.updateCommentTest(client, userId, assetGUID, privateCommentGUID, CommentType.STANDARD_COMMENT, privateCommentDescription2, true, "updatePrivateComment", privateCommentName);


    }



    /**
     * Create and return an asset consumer client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private AssetConsumer getAssetConsumerClient(String   serverName,
                                                 String   serverPlatformRootURL,
                                                 AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getAssetConsumerClient";

        try
        {
            AssetConsumerRESTClient restClient = new AssetConsumerRESTClient(serverName, serverPlatformRootURL);

            return new AssetConsumer(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a comment and return its GUID.  Also test the retrieve methods
     *
     * @param client interface to Asset Consumer OMAS
     * @param userId calling user
     * @param assetGUID name of comment to store
     * @param commentText description of comment to store
     * @param isPublic should this be a private comment or not?
     * @param testCaseName name of the test case
     * @param commentTypeName name of comment in reporting output
     * @return GUID of privateComment
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getCommentTest(AssetConsumer client,
                                  String        userId,
                                  String        assetGUID,
                                  CommentType   commentType,
                                  String        commentText,
                                  boolean       isPublic,
                                  String        testCaseName,
                                  String        commentTypeName) throws FVTUnexpectedCondition
    {
        try
        {
            String activityName = testCaseName + "::create" + commentTypeName;
            System.out.println();
            String commentGUID;


            commentGUID = client.addCommentToAsset(userId, assetGUID, commentType, commentText, isPublic);

            if (commentGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for create of " +  commentTypeName + ")");
            }

            AssetUniverse assetProperties = client.getAssetProperties(userId, assetGUID);

            AssetFeedback feedback = assetProperties.getFeedback();

            Comments comments = feedback.getComments();

            while (comments.hasNext())
            {
                Comment nextComment = comments.next();

                if (nextComment.getGUID().equals(commentGUID))
                {
                    if (commentType.equals(nextComment.getCommentType()))
                    {
                        validateComment(nextComment, userId, commentType, commentText, isPublic, activityName, commentTypeName);
                    }

                    return commentGUID;
                }
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Comment " +  commentTypeName + " not attached to asset: " + assetGUID + ")");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, testCaseName, unexpectedError);
        }
    }


    private void updateCommentTest(AssetConsumer client,
                                   String        userId,
                                   String        assetGUID,
                                   String        commentGUID,
                                   CommentType   commentType,
                                   String        newCommentDescription,
                                   boolean       isPublic,
                                   String        testCaseName,
                                   String        commentTypeName) throws FVTUnexpectedCondition
    {
        try
        {
            String activityName = testCaseName + "::update" + commentTypeName;
            System.out.println();

            client.updateComment(userId, assetGUID, commentGUID, commentType, newCommentDescription,isPublic);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, testCaseName, unexpectedError);
        }
    }


    /**
     * Create a comment, retrieve it by different methods and return its GUID.  Also test the retrieve methods
     *
     * @param retrievedComment element to test
     * @param userId calling user
     * @param commentType type of comment to verify
     * @param commentDescription description of comment to verify
     * @param isPublic should this be a private comment or not?
     * @param activityName name of the test case
     * @param commentTypeName name of comment in reporting output
     * @return GUID of privateComment
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void validateComment(Comment retrievedComment,
                                 String         userId,
                                 CommentType    commentType,
                                 String         commentDescription,
                                 boolean        isPublic,
                                 String         activityName,
                                 String         commentTypeName) throws FVTUnexpectedCondition
    {
        if (! commentDescription.equals(retrievedComment.getCommentText()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description <" + retrievedComment.getCommentText() + "> from Retrieve of " + commentTypeName + ")");
        }

        if (! commentType.equals(retrievedComment.getCommentType()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad commentType <" + retrievedComment.getCommentText() + "> from Retrieve of " + commentTypeName + ")");
        }

        if (isPublic)
        {
            if (! retrievedComment.getIsPublic())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Returned as private comment by Retrieve of " + commentTypeName + ")");
            }
        }
        else
        {
            if (retrievedComment.getIsPublic())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Returned as public comment by Retrieve of " + commentTypeName + ")");
            }
        }

        if (! userId.equals(retrievedComment.getUser()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad user from Retrieve of " + commentTypeName + ")");
        }
    }
}
