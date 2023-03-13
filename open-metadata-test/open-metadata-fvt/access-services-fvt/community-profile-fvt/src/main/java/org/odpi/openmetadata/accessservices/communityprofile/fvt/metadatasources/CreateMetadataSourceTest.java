/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.metadatasources;


import org.odpi.openmetadata.accessservices.communityprofile.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.MetadataSourceElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.MetadataSourceProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;

/**
 * CreateMetadataSourceTest calls the MetadataSourceClient to check that metadata sources can be created for
 * third party sources of user information.
 */
public class CreateMetadataSourceTest
{
    private final static String testCaseName          = "CreateMetadataSourceTest";

    private final static int    maxPageSize           = 100;

    private final static String profileManagerName            = "TestProfileManager";
    private final static String profileManagerDisplayName     = "ProfileManager displayName";
    private final static String profileManagerDescription     = "ProfileManager description";
    private final static String profileManagerTypeDescription = "ProfileManager type";
    private final static String profileManagerVersion         = "ProfileManager version";

    private MetadataSourceProperties metadataSourceProperties;

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
            CreateMetadataSourceTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

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
        CreateMetadataSourceTest thisTest = new CreateMetadataSourceTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceWiki());

        MetadataSourceClient client = thisTest.getMetadataSourceClient(serverName, serverPlatformRootURL, auditLog);

        String metadataSourceGUID = thisTest.getMetadataSource(userId, client);

        thisTest.addClassifications(userId, client, metadataSourceGUID);
    }


    /**
     * Create the rest properties
     */
    private CreateMetadataSourceTest()
    {
        metadataSourceProperties = new MetadataSourceProperties();

        metadataSourceProperties.setQualifiedName(profileManagerName);
        metadataSourceProperties.setDisplayName(profileManagerDisplayName);
        metadataSourceProperties.setDescription(profileManagerDescription);
        metadataSourceProperties.setTypeDescription(profileManagerTypeDescription);
        metadataSourceProperties.setVersion(profileManagerVersion);
    }


    /**
     * Create and return a client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private MetadataSourceClient getMetadataSourceClient(String   serverName,
                                                         String   serverPlatformRootURL,
                                                         AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getMetadataSourceClient";

        try
        {
            CommunityProfileRESTClient restClient = new CommunityProfileRESTClient(serverName, serverPlatformRootURL, auditLog);

            return new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
    

    /**
     * Create a metadata source software server capability and return its GUID.
     *
     * @param userId calling user
     * @param client client to return
     * @return unique identifier of the metadata source
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getMetadataSource(String               userId,
                                     MetadataSourceClient client) throws FVTUnexpectedCondition
    {
        final String activityName = "getMetadataSource";

        try
        {
            String metadataSourceGUID = client.createMetadataSource(userId, metadataSourceProperties);

            if (metadataSourceGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            String retrievedProfileManagerGUID = client.getMetadataSourceGUID(userId, profileManagerName);

            if (retrievedProfileManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Retrieve)");
            }

            if (! retrievedProfileManagerGUID.equals(metadataSourceGUID))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Inconsistent GUIDs)");
            }

            return metadataSourceGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



    /**
     * Add the classifications to the metadata source.
     *
     * @param userId calling user
     * @param client client to return
     * @param metadataSourceGUID unique identifier of the metadata source
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void addClassifications(String               userId,
                                    MetadataSourceClient client,
                                    String               metadataSourceGUID) throws FVTUnexpectedCondition
    {
        final String activityName = "saveClassifications";

        try
        {
            MetadataSourceElement metadataSource = client.getMetadataSource(userId, metadataSourceGUID);

            if (metadataSource == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no metadata source retrieved)");
            }

            ElementHeader header = metadataSource.getElementHeader();

            if (header == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no header retrieved)");
            }


            validateClassifications("Plain metadata source",
                                    false,
                                    false,
                                    false,
                                    header.getClassifications());

            client.addUserProfileManagerClassification(userId, metadataSourceGUID);

            metadataSource = client.getMetadataSource(userId, metadataSourceGUID);

            if (metadataSource == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no metadata source retrieved 2)");
            }

            header = metadataSource.getElementHeader();

            if (header == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no header retrieved 2)");
            }

            validateClassifications("User Profile Manager Added",
                                    true,
                                    false,
                                    false,
                                    header.getClassifications());

            client.addUserAccessDirectoryClassification(userId, metadataSourceGUID);

            metadataSource = client.getMetadataSource(userId, metadataSourceGUID);

            if (metadataSource == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no metadata source retrieved 3)");
            }

            header = metadataSource.getElementHeader();

            if (header == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no header retrieved 3)");
            }

            validateClassifications("User Access Directory Added",
                                    true,
                                    true,
                                    false,
                                    header.getClassifications());

            client.addMasterDataManagerClassification(userId, metadataSourceGUID);

            metadataSource = client.getMetadataSource(userId, metadataSourceGUID);

            if (metadataSource == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no metadata source retrieved 4)");
            }

            header = metadataSource.getElementHeader();

            if (header == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no header retrieved 4)");
            }

            validateClassifications("Master Data Manager Added",
                                    true,
                                    true,
                                    true,
                                    header.getClassifications());

        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Verify that the metadata source's classifications are set up correctly.
     *
     * @param phaseName phase of the test
     * @param userProfileManagerExpected is the UserProfileManager classification expected?
     * @param userAccessDirectoryExpected is the UserProfileManager classification expected?
     * @param masterDataManagerExpected is the UserProfileManager classification expected?
     * @param classifications list of classifications to test
     * @throws FVTUnexpectedCondition this are not as expected
     */
    private void validateClassifications(String                      phaseName,
                                         boolean                     userProfileManagerExpected,
                                         boolean                     userAccessDirectoryExpected,
                                         boolean                     masterDataManagerExpected,
                                         List<ElementClassification> classifications) throws FVTUnexpectedCondition
    {
        final String activityName = "validateClassifications";

        boolean userProfileManagerSet = false;
        boolean userAccessDirectorySet = false;
        boolean masterDataManagerSet = false;

        if (classifications != null)
        {
            for (ElementClassification classification : classifications)
            {
                if (classification != null)
                {
                    if ("UserProfileManager".equals(classification.getClassificationName()))
                    {
                        if (!userProfileManagerExpected)
                        {
                            throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(unexpected UserProfileManager) " + classifications.toString());
                        }

                        userProfileManagerSet = true;
                    }

                    if ("UserAccessDirectory".equals(classification.getClassificationName()))
                    {
                        if (!userAccessDirectoryExpected)
                        {
                            throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(unexpected UserAccessDirectory) " + classifications.toString());
                        }

                        userAccessDirectorySet = true;
                    }

                    if ("MasterDataManager".equals(classification.getClassificationName()))
                    {
                        if (!userAccessDirectoryExpected)
                        {
                            throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(unexpected MasterDataManager) " + classifications.toString());
                        }

                        masterDataManagerSet = true;
                    }
                }
            }

            if (userProfileManagerExpected && ! userProfileManagerSet)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(missing UserProfileManager) " + classifications.toString());
            }

            if (userAccessDirectoryExpected && ! userAccessDirectorySet)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(missing UserAccessDirectory) " + classifications.toString());
            }

            if (masterDataManagerExpected && ! masterDataManagerSet)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(missing MasterDataManager) " + classifications.toString());
            }
        }
        else
        {
            if (userProfileManagerExpected)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(missing UserProfileManager)");
            }

            if (userAccessDirectoryExpected)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(missing UserAccessDirectory)");
            }

            if (masterDataManagerExpected)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + ":" + phaseName + "(missing MasterDataManager)");
            }
        }
    }
}
