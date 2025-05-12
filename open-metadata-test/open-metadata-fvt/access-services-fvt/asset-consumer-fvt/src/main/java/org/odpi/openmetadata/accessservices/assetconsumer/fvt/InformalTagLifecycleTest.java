/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.fvt;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetconsumer.client.rest.AssetConsumerRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformalTagElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.InformalTagProperties;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;


/**
 * InformalTagLifecycleTest calls the AssetConsumerClient to create a tag and attach it to assets and schemas
 * and then retrieve the results.
 */
public class InformalTagLifecycleTest
{
    private final static String testCaseName       = "InformalTagLifecycleTest";

    private final static int    maxPageSize        = 100;

    /*
     * The tag name is constant - the guid is created as part of the test.
     */
    private final static String publicTag1Name         = "TestPublicTag1";
    private final static String publicTag1Description1 = "PublicTag1 description1";
    private final static String publicTag1Description2 = "PublicTag1 description2";
    private final static String publicTag2Name         = "TestPublicTag2";
    private final static String publicTag2Description1 = "PublicTag2 description1";
    private final static String publicTag2Description2 = "PublicTag2 description2";
    private final static String privateTagName         = "TestPrivateTag";
    private final static String privateTagDescription1 = "PrivateTag description1";
    private final static String privateTagDescription2 = "PrivateTag description2";

    private final static String differentUser = "newUserId";

    private final static String searchStringGetAll     = "Tag";
    private final static String searchStringGetNone    = "Blah";
    private final static String searchStringGetPrivate = "Private";




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
            InformalTagLifecycleTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        InformalTagLifecycleTest thisTest = new InformalTagLifecycleTest();

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

        /* Create Tag */
        String publicTag1GUID = thisTest.getTagTest(client, userId, publicTag1Name, publicTag1Description1, false, "getPublicTag", "PublicTag1");
        System.out.println("PublicTag1GUID: " + publicTag1GUID);
        String publicTag2GUID = thisTest.getTagTest(client, userId, publicTag2Name, publicTag2Description1, false, "getPublicTag", "PublicTag2");
        System.out.println("PublicTag2GUID: " + publicTag2GUID);
        String privateTagGUID = thisTest.getTagTest(client, userId, privateTagName, privateTagDescription1, true, "getPrivateTag", "PrivateTag");
        System.out.println("PrivateTagGUID: " + privateTagGUID);

        /* Update Tag */
        thisTest.updateTagTest(client, userId, publicTag1GUID, publicTag1Name, publicTag1Description2, false, "updatePublicTag1", "PublicTag1");
        thisTest.updateTagTest(client, userId, publicTag2GUID, publicTag2Name, publicTag2Description2, false, "updatePublicTag2", "PublicTag2");
        thisTest.updateTagTest(client, userId, privateTagGUID, privateTagName, privateTagDescription2, true, "updatePrivateTag", "PrivateTag");
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
     * Create a tag, retrieve it by different methods and return its GUID.  Also test the retrieve methods
     *
     * @param client interface to Asset Consumer OMAS
     * @param userId calling user
     * @param tagName name of tag to store
     * @param tagDescription description of tag to store
     * @param isPrivate should this be a private tag or not?
     * @param testCaseName name of the test case
     * @param tagTypeName name of tag in reporting output
     * @return GUID of privateTag
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getTagTest(AssetConsumer client,
                              String        userId,
                              String        tagName,
                              String        tagDescription,
                              boolean       isPrivate,
                              String        testCaseName,
                              String        tagTypeName) throws FVTUnexpectedCondition
    {
        try
        {
            String activityName = testCaseName + "::create" + tagTypeName;
            System.out.println();
            String tagGUID;

            if (isPrivate)
            {
                tagGUID = client.createPrivateTag(userId, tagName, tagDescription);
            }
            else
            {
                tagGUID = client.createPublicTag(userId, tagName, tagDescription);
            }
            if (tagGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for create of" +  tagTypeName + ")");
            }

            activityName = testCaseName + "::getByGUIDAfterCreate" + tagTypeName;
            InformalTagElement retrievedElement = client.getTag(userId, tagGUID);

            this.validateTag(retrievedElement, userId, tagName, tagDescription, isPrivate, activityName, tagTypeName);

            activityName = testCaseName + "::getByNameAfterCreate" + tagTypeName;
            List<InformalTagElement> tagList = client.getTagsByName(userId, tagName, 0, maxPageSize);

            if (tagList == null)
            {
                Thread.sleep(600);
                tagList = client.getTagsByName(userId, tagName, 0, maxPageSize);
            }
            if (tagList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no tag for RetrieveByName of " + tagTypeName + ")");
            }
            else if (tagList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty list for RetrieveByName of " + tagTypeName + ")");
            }
            else if (tagList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(" + tagTypeName + " list for RetrieveByName contains" + tagList.size() +
                                                         " elements)");
            }

            retrievedElement = tagList.get(0);

            this.validateTag(retrievedElement, userId, tagName, tagDescription, isPrivate, activityName, tagTypeName);

            if (isPrivate)
            {
                activityName = testCaseName + "::getHiddenPrivateTag";
                try
                {
                    retrievedElement = client.getTag(differentUser, tagGUID);
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
            }

            return tagGUID;
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


    private void updateTagTest(AssetConsumer client,
                               String        userId,
                               String        tagGUID,
                               String        tagName,
                               String        newTagDescription,
                               boolean       isPrivate,
                               String        testCaseName,
                               String        tagTypeName) throws FVTUnexpectedCondition
    {
        try
        {
            String activityName = testCaseName + "::update" + tagTypeName;
            System.out.println();

            client.updateTagDescription(userId, tagGUID, newTagDescription);

            InformalTagElement  retrievedElement = client.getTag(userId, tagGUID);

            this.validateTag(retrievedElement, userId, tagName, newTagDescription, isPrivate, activityName, tagTypeName);
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


    /**
     * Create a tag, retrieve it by different methods and return its GUID.  Also test the retrieve methods
     *
     * @param retrievedElement element to test
     * @param userId calling user
     * @param tagName name of tag to store
     * @param tagDescription description of tag to store
     * @param isPrivate should this be a private tag or not?
     * @param activityName name of the test case
     * @param tagTypeName name of tag in reporting output
     * @return GUID of privateTag
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void validateTag(InformalTagElement retrievedElement,
                             String             userId,
                             String             tagName,
                             String             tagDescription,
                             boolean            isPrivate,
                             String             activityName,
                             String             tagTypeName) throws FVTUnexpectedCondition
    {
        InformalTagProperties retrievedTag = retrievedElement.getProperties();

        if (retrievedTag == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(no tag from Retrieve of " + tagTypeName + " by GUID)");
        }

        if (! tagName.equals(retrievedTag.getName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad tag name <" + retrievedTag.getName() + "> from Retrieve of " + tagTypeName + " by GUID).  Full Tag is " + retrievedElement.toString());
        }
        if (! tagDescription.equals(retrievedTag.getDescription()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description <" + retrievedTag.getDescription() + "> from Retrieve of " + tagTypeName + " by GUID)");
        }
        if (isPrivate)
        {
            if (! retrievedTag.getIsPrivateTag())
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Returned as public tag by Retrieve of " + tagTypeName + " by GUID)");
            }
        }
        else
        {
            if (retrievedTag.getIsPrivateTag())
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Returned as private tag by Retrieve of " + tagTypeName + " by GUID)");
            }
        }

        if (! userId.equals(retrievedTag.getUser()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad user from Retrieve of " + tagTypeName + " by GUID)");
        }
    }
}
