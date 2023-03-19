/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.common;

import org.odpi.openmetadata.accessservices.assetmanager.client.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataCorrelationHeader;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;
import java.util.Map;


/**
 * AssetManagerTestBase provides common functions for test cases.
 */
public class AssetManagerTestBase
{
    protected final static int    maxPageSize        = 100;

    /*
     * The asset manager name is constant - the guid is created as part of the test.
     */
    private final static String assetManagerDisplayName     = " AssetManager displayName";
    private final static String assetManagerDescription     = " AssetManager description";
    private final static String assetManagerTypeDescription = " AssetManager type";
    private final static String assetManagerVersion         = " AssetManager version";

    protected final static String GLOSSARY_TYPE_NAME         = "Glossary";


    /**
     * Create and return an asset manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @param testCaseName name of calling test case
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    protected ExternalAssetManagerClient getAssetManagerClient(String   serverName,
                                                               String   serverPlatformRootURL,
                                                               AuditLog auditLog,
                                                               String   testCaseName) throws FVTUnexpectedCondition
    {
        final String activityName = "getAssetManagerClient";

        try
        {
            AssetManagerRESTClient restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);

            return new ExternalAssetManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a glossary exchange client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @param testCaseName name of calling test case
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    protected GlossaryExchangeClient getGlossaryExchangeClient(String   serverName,
                                                               String   serverPlatformRootURL,
                                                               AuditLog auditLog,
                                                               String   testCaseName) throws FVTUnexpectedCondition
    {
        final String activityName = "getGlossaryExchangeClient";

        try
        {
            AssetManagerRESTClient restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);

            return new GlossaryExchangeClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create an asset manager entity and return its GUID.
     *
     * @param assetManagerName name of asset manager to use
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @param testCaseName name of calling test case
     * @return unique identifier of the asset manager entity
     * @throws FVTUnexpectedCondition the test case failed
     */
    protected String getAssetManager(String   assetManagerName,
                                     String   serverName,
                                     String   serverPlatformRootURL,
                                     String   userId,
                                     AuditLog auditLog,
                                     String   testCaseName) throws FVTUnexpectedCondition
    {
        final String activityName = "getAssetManager";

        try
        {
            AssetManagerRESTClient     restClient = new AssetManagerRESTClient(serverName, serverPlatformRootURL);
            ExternalAssetManagerClient client     = new ExternalAssetManagerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);

            String assetManagerGUID = client.getExternalAssetManagerGUID(userId, assetManagerName);

            if (assetManagerGUID == null)
            {
                AssetManagerProperties properties = new AssetManagerProperties();

                properties.setQualifiedName(assetManagerName);
                properties.setDisplayName(assetManagerName + assetManagerDisplayName);
                properties.setDescription(assetManagerName + assetManagerDescription);
                properties.setTypeDescription(assetManagerName + assetManagerTypeDescription);
                properties.setVersion(assetManagerName + assetManagerVersion);

                assetManagerGUID = client.createExternalAssetManager(userId, properties);
            }

            if (assetManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            assetManagerGUID = client.getExternalAssetManagerGUID(userId, assetManagerName);

            if (assetManagerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Retrieve)");
            }

            return assetManagerGUID;
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


    protected void validateCorrelationHeader(List<MetadataCorrelationHeader> metadataCorrelationHeaders,
                                             String                          testCaseName,
                                             String                          activityName,
                                             String                          assetManagerGUID,
                                             String                          assetManagerName,
                                             String                          glossaryExternalIdentifier,
                                             String                          glossaryExternalIdentifierName,
                                             String                          glossaryExternalIdentifierUsage,
                                             String                          glossaryExternalIdentifierSource,
                                             KeyPattern                      glossaryExternalIdentifierKeyPattern,
                                             Map<String, String>             mappingProperties,
                                             int                             expectedNumberOfIdentifiers) throws FVTUnexpectedCondition
    {
        if (glossaryExternalIdentifier == null)
        {
            if (metadataCorrelationHeaders != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Unexpected correlation properties from Retrieve)");
            }
        }
        else
        {
            if (metadataCorrelationHeaders == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Correlation properties from Retrieve)");
            }

            if (metadataCorrelationHeaders.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(empty Correlation properties from Retrieve)");
            }

            int correlationHeaderCount = 0;


        }
    }


    protected void validateElementHeader(ElementHeader elementHeader,
                                         String        testCaseName,
                                         String        activityName,
                                         String        guid,
                                         String        typeName) throws FVTUnexpectedCondition
    {
        if (elementHeader == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ElementBase from Retrieve)");
        }
        if (! guid.equals(elementHeader.getGUID()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad guid from Retrieve)");
        }
        if (elementHeader.getType() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No type from Retrieve)");
        }
        if (! typeName.equals(elementHeader.getType().getTypeName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad typeName from Retrieve)");
        }
    }



    protected void validateGlossaryProperties(GlossaryProperties  glossaryProperties,
                                              String              testCaseName,
                                              String              activityName,
                                              String              glossaryName,
                                              String              glossaryDisplayName,
                                              String              glossaryDescription,
                                              String              glossaryUsage,
                                              String              glossaryLanguage) throws FVTUnexpectedCondition
    {
        if (glossaryProperties == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryProperties from Retrieve)");
        }

        if (! glossaryName.equals(glossaryProperties.getQualifiedName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
        }
        if (! glossaryDisplayName.equals(glossaryProperties.getDisplayName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
        }
        if (! glossaryDescription.equals(glossaryProperties.getDescription()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
        }
        if (! glossaryUsage.equals(glossaryProperties.getUsage()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad glossaryUsage from Retrieve)");
        }
        if (! glossaryLanguage.equals(glossaryProperties.getLanguage()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad glossaryLanguage from Retrieve)");
        }
    }


    /**
     * Create a new glossary object.  The identifier may be null.  The glossary is retrieved and the return values tested to
     * be sure the new element was created correctly.
     *
     * @param client glossary client
     * @param testCaseName calling test case
     * @param activityName calling activity within the test case
     * @param userId calling user
     * @param assetManagerGUID unique identifier of asset manager to associate the identifier with
     * @param assetManagerName unique name of asset manager to associate the identifier with
     * @param glossaryName qualified name of glossary
     * @param glossaryDisplayName display name for glossary
     * @param glossaryDescription description of glossary
     * @param glossaryUsage expected usage of the glossary
     * @param glossaryLanguage language used in the glossary
     * @param glossaryExternalIdentifier external identifier used in third party technology
     * @param glossaryExternalIdentifierName name of external identifier used in third party technology
     * @param glossaryExternalIdentifierUsage usage of external identifier used in third party technology
     * @param glossaryExternalIdentifierSource source of external identifier used in third party technology
     * @param glossaryExternalIdentifierKeyPattern key pattern for external identifier used in third party technology
     * @param mappingProperties additional mapping properties for external identifier used in third party technology
     * @return unique identifier of the new glossary
     * @throws FVTUnexpectedCondition error
     */
    protected String getGlossary(GlossaryExchangeClient client,
                                 String                 testCaseName,
                                 String                 activityName,
                                 String                 userId,
                                 String                 assetManagerGUID,
                                 String                 assetManagerName,
                                 String                 glossaryName,
                                 String                 glossaryDisplayName,
                                 String                 glossaryDescription,
                                 String                 glossaryUsage,
                                 String                 glossaryLanguage,
                                 String                 glossaryExternalIdentifier,
                                 String                 glossaryExternalIdentifierName,
                                 String                 glossaryExternalIdentifierUsage,
                                 String                 glossaryExternalIdentifierSource,
                                 KeyPattern             glossaryExternalIdentifierKeyPattern,
                                 Map<String, String>    mappingProperties) throws FVTUnexpectedCondition
    {
        try
        {
            GlossaryProperties properties = new GlossaryProperties();

            properties.setQualifiedName(glossaryName);
            properties.setDisplayName(glossaryDisplayName);
            properties.setDescription(glossaryDescription);
            properties.setUsage(glossaryUsage);
            properties.setLanguage(glossaryLanguage);

            ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

            externalIdentifierProperties.setExternalIdentifier(glossaryExternalIdentifier);
            externalIdentifierProperties.setExternalIdentifierName(glossaryExternalIdentifierName);
            externalIdentifierProperties.setExternalIdentifierUsage(glossaryExternalIdentifierUsage);
            externalIdentifierProperties.setExternalIdentifierSource(glossaryExternalIdentifierSource);
            externalIdentifierProperties.setKeyPattern(glossaryExternalIdentifierKeyPattern);

            String glossaryGUID = client.createGlossary(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        externalIdentifierProperties,
                                                        properties);

            if (glossaryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            GlossaryElement retrievedElement = client.getGlossaryByGUID(userId, assetManagerGUID, assetManagerName, glossaryGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryElement from Retrieve)");
            }

            validateElementHeader(retrievedElement.getElementHeader(),
                                  testCaseName,
                                  activityName,
                                  glossaryGUID,
                                  GLOSSARY_TYPE_NAME);

            validateGlossaryProperties(retrievedElement.getGlossaryProperties(),
                                       testCaseName,
                                       activityName,
                                       glossaryName,
                                       glossaryDisplayName,
                                       glossaryDescription,
                                       glossaryUsage,
                                       glossaryLanguage);

            validateCorrelationHeader(retrievedElement.getCorrelationHeaders(),
                                      testCaseName,
                                      activityName,
                                      assetManagerGUID,
                                      assetManagerName,
                                      glossaryExternalIdentifier,
                                      glossaryExternalIdentifierName,
                                      glossaryExternalIdentifierUsage,
                                      glossaryExternalIdentifierSource,
                                      glossaryExternalIdentifierKeyPattern,
                                      mappingProperties,
                                      1);


            List<GlossaryElement> glossaryList = client.getGlossariesByName(userId, assetManagerGUID, assetManagerName, glossaryName, 0, maxPageSize);

            if (glossaryList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Glossary for RetrieveByName)");
            }
            else if (glossaryList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty Glossary list for RetrieveByName)");
            }
            else if (glossaryList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Glossary list for RetrieveByName contains" + glossaryList.size() +
                                                         " elements)");
            }

            retrievedElement = glossaryList.get(0);

            validateElementHeader(retrievedElement.getElementHeader(),
                                  testCaseName,
                                  activityName,
                                  glossaryGUID,
                                  GLOSSARY_TYPE_NAME);

            validateGlossaryProperties(retrievedElement.getGlossaryProperties(),
                                       testCaseName,
                                       activityName,
                                       glossaryName,
                                       glossaryDisplayName,
                                       glossaryDescription,
                                       glossaryUsage,
                                       glossaryLanguage);

            validateCorrelationHeader(retrievedElement.getCorrelationHeaders(),
                                      testCaseName,
                                      activityName,
                                      assetManagerGUID,
                                      assetManagerName,
                                      glossaryExternalIdentifier,
                                      glossaryExternalIdentifierName,
                                      glossaryExternalIdentifierUsage,
                                      glossaryExternalIdentifierSource,
                                      glossaryExternalIdentifierKeyPattern,
                                      mappingProperties,
                                      1);

            return glossaryGUID;
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
