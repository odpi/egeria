/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.externalidentifiers;

import org.odpi.openmetadata.accessservices.assetmanager.client.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.common.AssetManagerTestBase;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataCorrelationHeader;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;

/**
 * ManageExternalIdsTest calls the AssetManagerClientBase to ensure it is possible to manage
 * many-to-many relationships between elements in open metadata and external metadata sources.
 */
public class ManageExternalIdsTest extends AssetManagerTestBase
{
    private final static String testCaseName       = "ManageExternalIdsTest";
    
    private final static String glossaryName        = "TestGlossary";
    private final static String glossaryDisplayName = "Glossary displayName";
    private final static String glossaryDescription = "Glossary description";
    private final static String glossaryUsage       = "Glossary usage";
    private final static String glossaryLanguage    = "Glossary Language";

    private final static String glossaryTwoName        = "TestGlossary Two";
    private final static String glossaryTwoDisplayName = "Glossary Two displayName";
    private final static String glossaryTwoDescription = "Glossary Two description";
    private final static String glossaryTwoUsage       = "Glossary Two usage";
    private final static String glossaryTwoLanguage    = "Glossary Two language";

    private final static String     externalGlossaryIdentifierOne           = "TestExternalIdentifierOne";
    private final static String     externalGlossaryIdentifierOneName       = "TestExternalIdentifierOneName";
    private final static String     externalGlossaryIdentifierOneUsage      = "TestExternalIdentifierOneUsage";
    private final static String     externalGlossaryIdentifierOneSource     = "TestExternalIdentifierOneSource";
    private final static KeyPattern externalGlossaryIdentifierOneKeyPattern = KeyPattern.AGGREGATE_KEY;

    private final static String     externalGlossaryIdentifierTwo           = "TestExternalIdentifierTwo";
    private final static String     externalGlossaryIdentifierTwoName       = "TestExternalIdentifierTwoName";
    private final static String     externalGlossaryIdentifierTwoUsage      = "TestExternalIdentifierTwoUsage";
    private final static String     externalGlossaryIdentifierTwoSource     = "TestExternalIdentifierTwoSource";
    private final static KeyPattern externalGlossaryIdentifierTwoKeyPattern = KeyPattern.MIRROR_KEY;

    private final static String assetManagerOneName = "Asset Manager One";
    private final static String assetManagerTwoName = "Asset Manager";


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
            ManageExternalIdsTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

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
        ManageExternalIdsTest thisTest = new ManageExternalIdsTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());


        thisTest.testOneToOne(serverPlatformRootURL, serverName, userId, auditLog);
        thisTest.testMultipleAssetManagers(serverPlatformRootURL, serverName, userId, auditLog);
        thisTest.testMultipleAssetManagersSameExtID(serverPlatformRootURL, serverName, userId, auditLog);
    }


    /**
     * Create a glossary with a one-to-one mapping to an external identifier.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testOneToOne(String   serverPlatformRootURL,
                              String   serverName,
                              String   userId,
                              AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testOneToOne("+ glossaryName +")";

        GlossaryExchangeClient client = this.getGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String assetManagerGUID = this.getAssetManager(assetManagerOneName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);

        try
        {
            String glossaryGUID = this.getGlossary(client,
                                                   testCaseName,
                                                   activityName,
                                                   userId,
                                                   assetManagerGUID,
                                                   assetManagerOneName,
                                                   glossaryName,
                                                   glossaryDisplayName,
                                                   glossaryDescription,
                                                   glossaryUsage,
                                                   glossaryLanguage,
                                                   externalGlossaryIdentifierOne,
                                                   externalGlossaryIdentifierOneName,
                                                   externalGlossaryIdentifierOneUsage,
                                                   externalGlossaryIdentifierOneSource,
                                                   externalGlossaryIdentifierOneKeyPattern,
                                                   null);


            if (glossaryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary)");
            }

            GlossaryElement retrievedElement = client.getGlossaryByGUID(userId, assetManagerGUID, assetManagerOneName, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            List<MetadataCorrelationHeader> metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve)");
            }

            MetadataCorrelationHeader correlationHeader = metadataCorrelationHeaders.get(0);

            if (correlationHeader == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties in pos 0 from Retrieve)");
            }

            if (! externalGlossaryIdentifierOne.equals(correlationHeader.getExternalIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier)");
            }

            if (! externalGlossaryIdentifierOneName.equals(correlationHeader.getExternalIdentifierName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier name)");
            }

            if (! externalGlossaryIdentifierOneUsage.equals(correlationHeader.getExternalIdentifierUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier usage)");
            }

            if (! externalGlossaryIdentifierOneSource.equals(correlationHeader.getExternalIdentifierSource()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier source)");
            }

            if (correlationHeader.getLastSynchronized() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null last sync date)");
            }

            if (! assetManagerGUID.equals(correlationHeader.getAssetManagerGUID()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad assetManagerGUID)");
            }

            if (! assetManagerOneName.equals(correlationHeader.getAssetManagerName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad assetManagerName)");
            }

            client.removeGlossary(userId, assetManagerGUID, assetManagerOneName, glossaryGUID, externalGlossaryIdentifierOne);

            try
            {
                client.getGlossaryByGUID(userId, assetManagerGUID, assetManagerOneName, glossaryGUID);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(glossary not deleted)");
            }
            catch (InvalidParameterException notFound)
            {
                // all well
            }
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
     * Create a glossary with a multiple asset managers mapping different external identifiers to it.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testMultipleAssetManagers(String   serverPlatformRootURL,
                                           String   serverName,
                                           String   userId,
                                           AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testMultipleAssetManagers("+ assetManagerOneName +", "+assetManagerTwoName+")";

        GlossaryExchangeClient     glossaryExchangeClient = this.getGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, testCaseName);
        ExternalAssetManagerClient assetManagerClient     = this.getAssetManagerClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String assetManagerOneGUID = this.getAssetManager(assetManagerOneName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);
        String assetManagerTwoGUID = this.getAssetManager(assetManagerTwoName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);

        if (assetManagerOneGUID.equals(assetManagerTwoGUID))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary)");
        }

        try
        {
            String glossaryGUID = this.getGlossary(glossaryExchangeClient,
                                                   testCaseName,
                                                   activityName,
                                                   userId,
                                                   assetManagerOneGUID,
                                                   assetManagerOneName,
                                                   glossaryName,
                                                   glossaryDisplayName,
                                                   glossaryDescription,
                                                   glossaryUsage,
                                                   glossaryLanguage,
                                                   externalGlossaryIdentifierOne,
                                                   externalGlossaryIdentifierOneName,
                                                   externalGlossaryIdentifierOneUsage,
                                                   externalGlossaryIdentifierOneSource,
                                                   externalGlossaryIdentifierOneKeyPattern,
                                                   null);

            if (glossaryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary)");
            }

            ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

            externalIdentifierProperties.setExternalIdentifier(externalGlossaryIdentifierTwo);
            externalIdentifierProperties.setExternalIdentifierName(externalGlossaryIdentifierTwoName);
            externalIdentifierProperties.setExternalIdentifierUsage(externalGlossaryIdentifierTwoUsage);
            externalIdentifierProperties.setExternalIdentifierSource(externalGlossaryIdentifierTwoSource);
            externalIdentifierProperties.setKeyPattern(externalGlossaryIdentifierTwoKeyPattern);
            externalIdentifierProperties.setSynchronizationDirection(SynchronizationDirection.BOTH_DIRECTIONS);

            assetManagerClient.addExternalIdentifier(userId,
                                                     assetManagerTwoGUID,
                                                     assetManagerTwoName,
                                                     glossaryGUID,
                                                     GLOSSARY_TYPE_NAME,
                                                     externalIdentifierProperties);

            GlossaryElement                 retrievedElement;
            List<MetadataCorrelationHeader> metadataCorrelationHeaders;
            MetadataCorrelationHeader       correlationHeader;

            /*
             * Retrieve element for just asset manager two
             */
            retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerTwoGUID, assetManagerTwoName, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve of Two)");
            }

            metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve of Two)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve of Two)");
            }

            if (metadataCorrelationHeaders.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve of Two)");
            }

            correlationHeader = metadataCorrelationHeaders.get(0);

            if (correlationHeader == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties in pos 0 from Retrieve of Two)");
            }

            if (! externalGlossaryIdentifierTwo.equals(correlationHeader.getExternalIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier) of Two");
            }

            if (! externalGlossaryIdentifierTwoName.equals(correlationHeader.getExternalIdentifierName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier name of Two)");
            }

            if (! externalGlossaryIdentifierTwoUsage.equals(correlationHeader.getExternalIdentifierUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier usage of Two)");
            }

            if (! externalGlossaryIdentifierTwoSource.equals(correlationHeader.getExternalIdentifierSource()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier source of Two)");
            }

            if (correlationHeader.getLastSynchronized() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null last sync date of Two)");
            }

            /*
             * Retrieve element for just asset manager one
             */
            retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve)");
            }

            correlationHeader = metadataCorrelationHeaders.get(0);

            if (correlationHeader == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties in pos 0 from Retrieve)");
            }

            if (! externalGlossaryIdentifierOne.equals(correlationHeader.getExternalIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier)");
            }

            if (! externalGlossaryIdentifierOneName.equals(correlationHeader.getExternalIdentifierName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier name)");
            }

            if (! externalGlossaryIdentifierOneUsage.equals(correlationHeader.getExternalIdentifierUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier usage)");
            }

            if (! externalGlossaryIdentifierOneSource.equals(correlationHeader.getExternalIdentifierSource()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier source)");
            }

            if (correlationHeader.getLastSynchronized() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null last sync date)");
            }

            /*
             * Retrieve correlation headers for all asset managers
             */
            retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, null, null, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve) " + metadataCorrelationHeaders);
            }

            glossaryExchangeClient.removeGlossary(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID, externalGlossaryIdentifierOne);

            try
            {
                glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(glossary not deleted)");
            }
            catch (InvalidParameterException notFound)
            {
                // all well
            }
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
     * Create a glossary with a multiple asset managers mapping the same external identifier to it.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testMultipleAssetManagersSameExtID(String   serverPlatformRootURL,
                                                    String   serverName,
                                                    String   userId,
                                                    AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testMultipleAssetManagersSameExtId("+ assetManagerOneName +", "+assetManagerTwoName+")";

        GlossaryExchangeClient     glossaryExchangeClient = this.getGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, testCaseName);
        ExternalAssetManagerClient assetManagerClient     = this.getAssetManagerClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String assetManagerOneGUID = this.getAssetManager(assetManagerOneName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);
        String assetManagerTwoGUID = this.getAssetManager(assetManagerTwoName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);

        if (assetManagerOneGUID.equals(assetManagerTwoGUID))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary)");
        }

        try
        {
            String glossaryGUID = this.getGlossary(glossaryExchangeClient,
                                                   testCaseName,
                                                   activityName,
                                                   userId,
                                                   assetManagerOneGUID,
                                                   assetManagerOneName,
                                                   glossaryName,
                                                   glossaryDisplayName,
                                                   glossaryDescription,
                                                   glossaryUsage,
                                                   glossaryLanguage,
                                                   externalGlossaryIdentifierOne,
                                                   externalGlossaryIdentifierOneName,
                                                   externalGlossaryIdentifierOneUsage,
                                                   externalGlossaryIdentifierOneSource,
                                                   externalGlossaryIdentifierOneKeyPattern,
                                                   null);


            if (glossaryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary)");
            }

            ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

            externalIdentifierProperties.setExternalIdentifier(externalGlossaryIdentifierOne);
            externalIdentifierProperties.setExternalIdentifierName(externalGlossaryIdentifierOneName);
            externalIdentifierProperties.setExternalIdentifierUsage(externalGlossaryIdentifierOneUsage);
            externalIdentifierProperties.setExternalIdentifierSource(externalGlossaryIdentifierOneSource);
            externalIdentifierProperties.setKeyPattern(externalGlossaryIdentifierOneKeyPattern);
            externalIdentifierProperties.setSynchronizationDirection(SynchronizationDirection.BOTH_DIRECTIONS);

            assetManagerClient.addExternalIdentifier(userId,
                                                     assetManagerTwoGUID,
                                                     assetManagerTwoName,
                                                     glossaryGUID,
                                                     GLOSSARY_TYPE_NAME,
                                                     externalIdentifierProperties);

            GlossaryElement                 retrievedElement;
            List<MetadataCorrelationHeader> metadataCorrelationHeaders;
            MetadataCorrelationHeader       correlationHeader;

            /*
             * Retrieve from asset manager two
             */
            retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerTwoGUID, assetManagerTwoName, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve of Two)");
            }

            metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve of Two)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve of Two)");
            }

            if (metadataCorrelationHeaders.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve of Two)");
            }

            correlationHeader = metadataCorrelationHeaders.get(0);

            if (correlationHeader == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties in pos 0 from Retrieve of Two)");
            }

            if (! externalGlossaryIdentifierOne.equals(correlationHeader.getExternalIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier) of Two");
            }

            if (! externalGlossaryIdentifierOneName.equals(correlationHeader.getExternalIdentifierName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier name of Two)");
            }

            if (! externalGlossaryIdentifierOneUsage.equals(correlationHeader.getExternalIdentifierUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier usage of Two)");
            }

            if (! externalGlossaryIdentifierOneSource.equals(correlationHeader.getExternalIdentifierSource()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier source of Two)");
            }

            if (correlationHeader.getLastSynchronized() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null last sync date of Two)");
            }


            /*
             * Retrieve from asset manager one
             */
            retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve)");
            }

            correlationHeader = metadataCorrelationHeaders.get(0);

            if (correlationHeader == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties in pos 0 from Retrieve)");
            }

            if (! externalGlossaryIdentifierOne.equals(correlationHeader.getExternalIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier)");
            }

            if (! externalGlossaryIdentifierOneName.equals(correlationHeader.getExternalIdentifierName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier name)");
            }

            if (! externalGlossaryIdentifierOneUsage.equals(correlationHeader.getExternalIdentifierUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier usage)");
            }

            if (! externalGlossaryIdentifierOneSource.equals(correlationHeader.getExternalIdentifierSource()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier source)");
            }

            if (correlationHeader.getLastSynchronized() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null last sync date)");
            }

            /*
             * Retrieve from all asset managers.
             */
            retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, null, null, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve)");
            }


            glossaryExchangeClient.removeGlossary(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID, externalGlossaryIdentifierOne);

            try
            {
                glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(glossary not deleted)");
            }
            catch (InvalidParameterException notFound)
            {
                // all well
            }
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
     * Create a glossary mapped to multiple external identifiers from the same asset manager.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testOneToMany(String   serverPlatformRootURL,
                               String   serverName,
                               String   userId,
                               AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testManyToOne("+ assetManagerOneName+")";

        GlossaryExchangeClient     glossaryExchangeClient = this.getGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, testCaseName);
        ExternalAssetManagerClient assetManagerClient     = this.getAssetManagerClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String assetManagerOneGUID = this.getAssetManager(assetManagerOneName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);

        try
        {
            String glossaryGUID = this.getGlossary(glossaryExchangeClient,
                                                   testCaseName,
                                                   activityName,
                                                   userId,
                                                   assetManagerOneGUID,
                                                   assetManagerOneName,
                                                   glossaryName,
                                                   glossaryDisplayName,
                                                   glossaryDescription,
                                                   glossaryUsage,
                                                   glossaryLanguage,
                                                   externalGlossaryIdentifierOne,
                                                   externalGlossaryIdentifierOneName,
                                                   externalGlossaryIdentifierOneUsage,
                                                   externalGlossaryIdentifierOneSource,
                                                   externalGlossaryIdentifierOneKeyPattern,
                                                   null);


            if (glossaryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary One)");
            }

            ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

            externalIdentifierProperties.setExternalIdentifier(externalGlossaryIdentifierTwo);
            externalIdentifierProperties.setExternalIdentifierName(externalGlossaryIdentifierTwoName);
            externalIdentifierProperties.setSynchronizationDirection(SynchronizationDirection.BOTH_DIRECTIONS);
            externalIdentifierProperties.setExternalIdentifierUsage(externalGlossaryIdentifierTwoUsage);
            externalIdentifierProperties.setExternalIdentifierSource(externalGlossaryIdentifierTwoSource);
            externalIdentifierProperties.setKeyPattern(externalGlossaryIdentifierTwoKeyPattern);

            assetManagerClient.addExternalIdentifier(userId,
                                                     assetManagerOneGUID,
                                                     assetManagerOneName,
                                                     glossaryGUID,
                                                     GLOSSARY_TYPE_NAME,
                                                     externalIdentifierProperties);

            GlossaryElement retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            List<MetadataCorrelationHeader> metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve)");
            }

            glossaryExchangeClient.removeGlossary(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID, externalGlossaryIdentifierOne);

            try
            {
                glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryGUID);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(glossary not deleted)");
            }
            catch (InvalidParameterException notFound)
            {
                // all well
            }
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
     * Create 2 glossaries mapped to the same external identifier.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLog logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testManyToOne(String   serverPlatformRootURL,
                               String   serverName,
                               String   userId,
                               AuditLog auditLog) throws FVTUnexpectedCondition
    {
        String activityName = "testManyToOne("+ assetManagerOneName+")";

        GlossaryExchangeClient glossaryExchangeClient = this.getGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String assetManagerOneGUID = this.getAssetManager(assetManagerOneName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);

        try
        {
            String glossaryOneGUID = this.getGlossary(glossaryExchangeClient,
                                                      testCaseName,
                                                      activityName,
                                                      userId,
                                                      assetManagerOneGUID,
                                                      assetManagerOneName,
                                                      glossaryName,
                                                      glossaryDisplayName,
                                                      glossaryDescription,
                                                      glossaryUsage,
                                                      glossaryLanguage,
                                                      externalGlossaryIdentifierOne,
                                                      externalGlossaryIdentifierOneName,
                                                      externalGlossaryIdentifierOneUsage,
                                                      externalGlossaryIdentifierOneSource,
                                                      externalGlossaryIdentifierOneKeyPattern,
                                                      null);

            String glossaryTwoGUID = this.getGlossary(glossaryExchangeClient,
                                                      testCaseName,
                                                      activityName,
                                                      userId,
                                                      assetManagerOneGUID,
                                                      assetManagerOneName,
                                                      glossaryTwoName,
                                                      glossaryTwoDisplayName,
                                                      glossaryTwoDescription,
                                                      glossaryTwoUsage,
                                                      glossaryTwoLanguage,
                                                      externalGlossaryIdentifierOne,
                                                      externalGlossaryIdentifierOneName,
                                                      externalGlossaryIdentifierOneUsage,
                                                      externalGlossaryIdentifierOneSource,
                                                      externalGlossaryIdentifierOneKeyPattern,
                                                      null);


            if (glossaryOneGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary One)");
            }

            if (glossaryTwoGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for getGlossary Two)");
            }

            GlossaryElement retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryOneGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            List<MetadataCorrelationHeader> metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve)");
            }

            MetadataCorrelationHeader correlationHeader = metadataCorrelationHeaders.get(0);

            if (correlationHeader == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties in pos 0 from Retrieve)");
            }

            if (! externalGlossaryIdentifierOne.equals(correlationHeader.getExternalIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier)");
            }

            if (! externalGlossaryIdentifierOneName.equals(correlationHeader.getExternalIdentifierName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier name)");
            }

            if (! externalGlossaryIdentifierOneUsage.equals(correlationHeader.getExternalIdentifierUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier usage)");
            }

            if (! externalGlossaryIdentifierOneSource.equals(correlationHeader.getExternalIdentifierSource()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier source)");
            }

            if (correlationHeader.getLastSynchronized() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null last sync date)");
            }


            retrievedElement = glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryTwoGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve of Two)");
            }

            metadataCorrelationHeaders = retrievedElement.getCorrelationHeaders();

            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve of Two)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve of Two)");
            }

            if (metadataCorrelationHeaders.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of Correlation properties from Retrieve of Two)");
            }

            correlationHeader = metadataCorrelationHeaders.get(0);

            if (correlationHeader == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties in pos 0 from Retrieve of Two)");
            }

            if (! externalGlossaryIdentifierOne.equals(correlationHeader.getExternalIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier)");
            }

            if (! externalGlossaryIdentifierOneName.equals(correlationHeader.getExternalIdentifierName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier name)");
            }

            if (! externalGlossaryIdentifierOneUsage.equals(correlationHeader.getExternalIdentifierUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier usage)");
            }

            if (! externalGlossaryIdentifierOneSource.equals(correlationHeader.getExternalIdentifierSource()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad external identifier source)");
            }


            glossaryExchangeClient.removeGlossary(userId, assetManagerOneGUID, assetManagerOneName, glossaryOneGUID, externalGlossaryIdentifierOne);

            try
            {
                glossaryExchangeClient.getGlossaryByGUID(userId, assetManagerOneGUID, assetManagerOneName, glossaryOneGUID);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(glossary not deleted)");
            }
            catch (InvalidParameterException notFound)
            {
                // all well
            }
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
}
