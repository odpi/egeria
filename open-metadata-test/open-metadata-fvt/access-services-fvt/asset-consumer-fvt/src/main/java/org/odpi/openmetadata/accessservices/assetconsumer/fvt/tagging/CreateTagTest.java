/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.fvt.tagging;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetconsumer.client.rest.AssetConsumerRESTClient;
import org.odpi.openmetadata.accessservices.assetconsumer.elements.InformalTagElement;
import org.odpi.openmetadata.accessservices.assetconsumer.fvt.setup.AssetOwnerFactory;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.InformalTagProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;


/**
 * CreateTagTest calls the AssetConsumerClient to create a tag and attach it to assets and schemas
 * and then retrieve the results.
 */
public class CreateTagTest
{
    private final static String testCaseName       = "CreateTagTest";

    private final static int    maxPageSize        = 100;

    /*
     * The tag name is constant - the guid is created as part of the test.
     */
    private final static String publicTag1Name         = "TestPublicTag1";
    private final static String publicTag1Description  = "PublicTag1 description";
    private final static String publicTag2Name         = "TestPublicTag2";
    private final static String publicTag2Description  = "PublicTag2 description";
    private final static String privateTagName         = "TestPrivateTag";
    private final static String privateTagDescription1 = "PrivateTag description1";
    private final static String privateTagDescription2 = "PrivateTag description2";

    private final static String differentUser = "newUserId";

    private final static String searchStringGetAll     = "PrivateTag description";
    private final static String searchStringGetNone    = "PrivateTag description";
    private final static String searchStringGetPrivate = "PrivateTag description";




    /**
     * Run all of the defined tests and capture the results.
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
            CreateTagTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        System.out.println(results.toString());
        return results;
    }


    /**
     * Run all of the tests in this class.
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
        CreateTagTest thisTest = new CreateTagTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_CONSUMER_OMAS.getAccessServiceCode(),
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
        String publicTagGUID = thisTest.getPublicTag(client, userId);
        System.out.println("PublicTagGUID: " + publicTagGUID);
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
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a public tag and return its GUID.  Also test the
     *
     * @param client interface to Asset Consumer OMAS
     * @param userId calling user
     * @return GUID of privateTag
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getPublicTag(AssetConsumer client,
                                String        userId) throws FVTUnexpectedCondition
    {
        final String methodName = "getPublicTag";


        try
        {
            String activityName = methodName + "::createPrivateTag";
            System.out.println();
            String privateTagGUID = client.createPrivateTag(userId, privateTagName, privateTagDescription1);
            if (privateTagGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for create of private tag)");
            }

            activityName = methodName + "::getAfterCreatePrivateTag";
            InformalTagElement    retrievedElement = client.getTag(userId, privateTagGUID);
            InformalTagProperties retrievedTag     = retrievedElement.getInformalTagProperties();

            if (retrievedTag == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no PrivateTag from Retrieve of private tag)");
            }

            if (! privateTagName.equals(retrievedTag.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad tag name from Retrieve of private tag)");
            }
            if (! privateTagDescription1.equals(retrievedTag.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve of private tag)");
            }
            if (! retrievedTag.getIsPrivateTag())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Returned as public tag by Retrieve of private tag)");
            }
            if (! userId.equals(retrievedTag.getUser()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad user from Retrieve of private tag)");
            }

            activityName = methodName + "::getByNameAfterCreatePrivateTag";
            List<InformalTagElement> privateTagList = client.getTagsByName(userId, privateTagName, 0, maxPageSize);

            if (privateTagList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no PrivateTag for RetrieveByName of private tag)");
            }
            else if (privateTagList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty list for RetrieveByName of private tag)");
            }
            else if (privateTagList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(PrivateTag list for RetrieveByName contains" + privateTagList.size() +
                                                 " elements)");
            }

            retrievedElement = privateTagList.get(0);
            retrievedTag = retrievedElement.getInformalTagProperties();

            if (! privateTagName.equals(retrievedTag.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad tag name from RetrieveByName of private tag)");
            }
            if (! privateTagDescription1.equals(retrievedTag.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName of private tag)");
            }
            if (! retrievedTag.getIsPrivateTag())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Returned as public tag by RetrieveByName of private tag)");
            }
            if (! userId.equals(retrievedTag.getUser()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad user from RetrieveByName of private tag)");
            }

            activityName = methodName + "::getHiddenPrivateTag";
            try
            {
                retrievedElement = client.getTag(differentUser, privateTagGUID);
                if (retrievedElement == null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null Private tag returned to wrong user");
                }
                else
                {
                    throw new FVTUnexpectedCondition(testCaseName,
                                                     activityName + "(Private tag returned to wrong user: " + retrievedElement.toString() + ")");
                }
            }
            catch (InvalidParameterException notFound)
            {
                // expected because this is a private tag
            }

            activityName = methodName + "::createPublicTag1";
            String publicTag1GUID = client.createPublicTag(userId, publicTag1Name, publicTag1Description);
            if (publicTag1GUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for create of public tag 1)");
            }

            activityName = methodName + "::getTagAfterCreatePublicTag1";
            retrievedElement = client.getTag(userId, publicTag1GUID);
            retrievedTag     = retrievedElement.getInformalTagProperties();

            if (retrievedTag == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no PublicTag from Retrieve of public tag 1)");
            }

            if (! publicTag1Name.equals(retrievedTag.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad tag name from Retrieve of public tag 1)");
            }
            if (! publicTag1Description.equals(retrievedTag.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve of public tag 1)");
            }
            if (! userId.equals(retrievedTag.getUser()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad user from Retrieve of public tag 1)");
            }

            activityName = methodName + "::getTagByNameAfterCreatePublicTag1";
            List<InformalTagElement> retrievedTagList = client.getTagsByName(userId, publicTag1Name, 0, maxPageSize);

            if (retrievedTagList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null result for RetrieveByName of public tag 1)");
            }
            else if (retrievedTagList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty list for RetrieveByName of public tag 1)");
            }
            else if (retrievedTagList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Public tag 1 list for RetrieveByName contains" + retrievedTagList.size() +
                                                         " elements)");
            }

            retrievedElement = retrievedTagList.get(0);
            retrievedTag = retrievedElement.getInformalTagProperties();

            if (! publicTag1Name.equals(retrievedTag.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad tag name from RetrieveByName of public tag 1)");
            }
            if (! publicTag1Description.equals(retrievedTag.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName of public tag 1)");
            }
            if (! userId.equals(retrievedTag.getUser()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad user from RetrieveByName of public tag 1)");
            }

            activityName = methodName + "::getTagAfterCreatePublicTag2";
            String publicTag2GUID = client.createPublicTag(userId, publicTag2Name, publicTag2Description);
            if (publicTag2GUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for create of public tag 2)");
            }

            return publicTag1GUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, methodName, unexpectedError);
        }
    }
}
