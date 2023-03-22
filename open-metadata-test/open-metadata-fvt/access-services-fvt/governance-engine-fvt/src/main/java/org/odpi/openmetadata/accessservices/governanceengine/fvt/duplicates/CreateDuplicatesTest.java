/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.fvt.duplicates;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateDuplicatesTest calls the GovernanceEngineClient to create different types of duplicates
 * and then retrieve the results with different options.
 */
public class CreateDuplicatesTest
{
    private final static String testCaseName                 = "CreateDuplicatesTest";
    private final static String testCaseNameProperty         = "TestCaseName";
    private final static String qualifiedNameProperty        = "qualifiedName";
    private final static String nameProperty                 = "name";
    private final static String displayNameProperty          = "displayName";
    private final static String keywordProperty              = "keyword";
    private final static String descriptionProperty          = "description";
    private final static String additionalPropertiesProperty = "additionalProperties";

    private final static int    maxPageSize        = 100;

    private final static String assetTypeName           = "Asset";
    private final static String keywordTypeName         = "SearchKeyword";
    private final static String keywordLinkTypeName     = "SearchKeywordLink";
    private final static String schemaTypeTypeName      = "SchemaType";
    private final static String assetSchemaTypeTypeName = "AssetSchemaType";

    private final static String firstAssetName        = "FirstAsset";
    private final static String firstAssetDescription = "FirstAssetDescription";
    private final static String duplicatePrefix       = "Dup:";
    private final static String mementoPrefix         = "Memento:";
    private final static String ineffectivePrefix     = "Ineffective:";
    private final static String expiredPrefix         = "Expired:";

    final PropertyHelper      propertyHelper = new PropertyHelper();


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
            CreateDuplicatesTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
                              FVTAuditLogDestination auditLogDestination) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException,
                                                                                 FVTUnexpectedCondition
    {
        CreateDuplicatesTest thisTest = new CreateDuplicatesTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceWiki());

        GovernanceEngineRESTClient restClient = new GovernanceEngineRESTClient(serverName, serverPlatformRootURL, auditLog);
        GovernanceEngineClient     governanceEngineClient = new GovernanceEngineClient(serverName, serverPlatformRootURL, restClient, maxPageSize);
        OpenMetadataStoreClient    openMetadataStoreClient     = new OpenMetadataStoreClient(serverName, serverPlatformRootURL);

        String activityName;

        activityName = "SimpleDuplicate - create first entity";
        String firstAssetGUID = thisTest.createAsset(openMetadataStoreClient, userId, assetTypeName, "", firstAssetName, firstAssetDescription, null, null, null, activityName, testCaseName);

        activityName = "SimpleDuplicate - create duplicate entity";
        try
        {
            /*
             * The sleep ensures a later creation time
             */
            Thread.sleep(1);
        }
        catch (Exception interruption)
        {
            System.out.println("Interrupted sleep in: " + activityName + " Exception: " + interruption);
        }

        String firstAssetDuplicateGUID = thisTest.createAsset(openMetadataStoreClient, userId, assetTypeName, duplicatePrefix, firstAssetName, firstAssetDescription, null, null, null, activityName, testCaseName);

        activityName = "SimpleDuplicate - link duplicate entities";

        thisTest.linkDuplicates(governanceEngineClient, userId, firstAssetGUID, firstAssetDuplicateGUID, 1, testCaseName, activityName);

        activityName = "SimpleDuplicate - retrieve first entity - deDup=true";

        OpenMetadataElement firstAsset = thisTest.getMetadataElementByGUID(openMetadataStoreClient,
                                                                           userId,
                                                                           assetTypeName,
                                                                           firstAssetGUID,
                                                                           firstAssetGUID,
                                                                           "",
                                                                           firstAssetName,
                                                                           false,
                                                                           true,
                                                                           null,
                                                                           activityName,
                                                                           testCaseName);
        thisTest.validateMetadataElement(firstAsset, assetTypeName, firstAssetGUID, "", firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - retrieve second entity - deDup=true";

        OpenMetadataElement firstAssetDuplicate = thisTest.getMetadataElementByGUID(openMetadataStoreClient,
                                                                                    userId,
                                                                                    assetTypeName,
                                                                                    firstAssetDuplicateGUID,
                                                                                    firstAssetDuplicateGUID,
                                                                                    duplicatePrefix,
                                                                                    firstAssetName,
                                                                                    false,
                                                                                    true,
                                                                                    null,
                                                                                    activityName,
                                                                                    testCaseName);

        thisTest.validateMetadataElement(firstAssetDuplicate, assetTypeName, firstAssetDuplicateGUID, duplicatePrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - retrieve first entity - deDup=false - second entity returned";

        OpenMetadataElement deDuplicatedAsset = openMetadataStoreClient.getMetadataElementByGUID(userId, firstAssetGUID, false, false, null);

        thisTest.validateMetadataElement(deDuplicatedAsset, assetTypeName, firstAssetDuplicateGUID, duplicatePrefix, firstAssetName, activityName, testCaseName);

        List<OpenMetadataElement> retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, firstAssetName, false, false, null, 0 , 0);

        thisTest.validateMetadataElements(retrievedElements, assetTypeName, firstAssetDuplicateGUID, duplicatePrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - create memento asset";

        String mementoAssetGUID = thisTest.createAsset(openMetadataStoreClient, userId, assetTypeName, mementoPrefix, firstAssetName, firstAssetDescription, null, null, null, activityName, testCaseName);

        activityName = "SimpleDuplicate - link memento entity as duplicate";

        thisTest.linkDuplicates(governanceEngineClient, userId, firstAssetGUID, mementoAssetGUID, 1, testCaseName, activityName);

        openMetadataStoreClient.classifyMetadataElementInStore(userId, mementoAssetGUID, "Memento", true, true, null, null, null, null);

        try
        {
            openMetadataStoreClient.getMetadataElementByGUID(userId, mementoAssetGUID, false, true, null);
            throw new FVTUnexpectedCondition(testCaseName, "Memento metadata element returned by " + activityName);
        }
        catch (Exception exception)
        {
            System.out.println(activityName + ": metadata element " + mementoAssetGUID + " hidden by memento classification");
        }

        OpenMetadataElement mementoElement = openMetadataStoreClient.getMetadataElementByGUID(userId, mementoAssetGUID, true, true, null);

        thisTest.validateMetadataElement(mementoElement, assetTypeName, mementoAssetGUID, mementoPrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - retrieve first entity - deDup=false - second entity returned because not lineage";

        deDuplicatedAsset = openMetadataStoreClient.getMetadataElementByGUID(userId, firstAssetGUID, false, false, null);

        thisTest.validateMetadataElement(deDuplicatedAsset, assetTypeName, firstAssetDuplicateGUID, duplicatePrefix, firstAssetName, activityName, testCaseName);

        retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, firstAssetName, false, false, null, 0 , 0);

        thisTest.validateMetadataElements(retrievedElements, assetTypeName, firstAssetDuplicateGUID, duplicatePrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - retrieve first entity - deDup=false; lineage=true - memento entity returned";

        deDuplicatedAsset = openMetadataStoreClient.getMetadataElementByGUID(userId, firstAssetGUID, true, false, null);

        thisTest.validateMetadataElement(deDuplicatedAsset, assetTypeName, mementoAssetGUID, mementoPrefix, firstAssetName, activityName, testCaseName);

        retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, firstAssetName, true, false, null, 0 , 0);

        thisTest.validateMetadataElements(retrievedElements, assetTypeName, mementoAssetGUID, mementoPrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - create ineffective asset";

        String ineffectiveAssetGUID = thisTest.createAsset(openMetadataStoreClient, userId, assetTypeName, ineffectivePrefix, firstAssetName, firstAssetDescription, null, new Date(10), null, activityName, testCaseName);

        activityName = "SimpleDuplicate - link ineffective entity as duplicate";

        thisTest.linkDuplicates(governanceEngineClient, userId, firstAssetGUID, ineffectiveAssetGUID, 1, testCaseName, activityName);

        try
        {
            openMetadataStoreClient.getMetadataElementByGUID(userId, ineffectiveAssetGUID, false, true, new Date());
            throw new FVTUnexpectedCondition(testCaseName, "Ineffective metadata element returned by " + activityName);
        }
        catch (Exception exception)
        {
            System.out.println(activityName + ": metadata element " + ineffectiveAssetGUID + " hidden by effectivity date");
        }

        OpenMetadataElement ineffectiveElement = openMetadataStoreClient.getMetadataElementByGUID(userId, ineffectiveAssetGUID, true, true, null);

        thisTest.validateMetadataElement(ineffectiveElement, assetTypeName, ineffectiveAssetGUID, ineffectivePrefix, firstAssetName, activityName, testCaseName);

        System.out.println("Effective Date: " + ineffectiveElement.getEffectiveToTime());
        activityName = "SimpleDuplicate - retrieve first entity - deDup=false; lineage=false; effectivity date now  - second entity returned because not lineage and NOW";

        deDuplicatedAsset = openMetadataStoreClient.getMetadataElementByGUID(userId, firstAssetGUID, false, false, new Date());

        System.out.println(" firstAssetGUID: " + firstAssetGUID);
        System.out.println(" firstAssetDuplicateGUID: " + firstAssetDuplicateGUID);
        System.out.println(" mementoAssetGUID: " + mementoAssetGUID);
        System.out.println(" ineffectiveAssetGUID: " + ineffectiveAssetGUID);

        thisTest.validateMetadataElement(deDuplicatedAsset, assetTypeName, firstAssetDuplicateGUID, duplicatePrefix, firstAssetName, activityName, testCaseName);

        retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, firstAssetName, false, false, new Date(), 0 , 0);

        thisTest.validateMetadataElements(retrievedElements, assetTypeName, firstAssetDuplicateGUID, duplicatePrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - retrieve first entity - deDup=false; lineage=true; effectivity date now - memento entity returned";

        deDuplicatedAsset = openMetadataStoreClient.getMetadataElementByGUID(userId, firstAssetGUID, true, false, new Date());

        thisTest.validateMetadataElement(deDuplicatedAsset, assetTypeName, mementoAssetGUID, mementoPrefix, firstAssetName, activityName, testCaseName);

        retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, firstAssetName, true, false, new Date(), 0 , 0);

        thisTest.validateMetadataElements(retrievedElements, assetTypeName, mementoAssetGUID, mementoPrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - retrieve first entity - deDup=false; lineage=false; no effectivity date - ineffective entity returned";

        deDuplicatedAsset = openMetadataStoreClient.getMetadataElementByGUID(userId, firstAssetGUID, false, false, null);

        thisTest.validateMetadataElement(deDuplicatedAsset, assetTypeName, ineffectiveAssetGUID, ineffectivePrefix, firstAssetName, activityName, testCaseName);

        retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, firstAssetName, false, false, null, 0 , 0);

        thisTest.validateMetadataElements(retrievedElements, assetTypeName, ineffectiveAssetGUID, ineffectivePrefix, firstAssetName, activityName, testCaseName);

        activityName = "SimpleDuplicate - retrieve first entity - deDup=false; lineage=true; no effectivity date - ineffective entity returned";

        deDuplicatedAsset = openMetadataStoreClient.getMetadataElementByGUID(userId, firstAssetGUID, true, false, null);

        thisTest.validateMetadataElement(deDuplicatedAsset, assetTypeName, ineffectiveAssetGUID, ineffectivePrefix, firstAssetName, activityName, testCaseName);

        retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, firstAssetName, true, false, null, 0 , 0);

        thisTest.validateMetadataElements(retrievedElements, assetTypeName, ineffectiveAssetGUID, ineffectivePrefix, firstAssetName, activityName, testCaseName);

        /*---------------------------------
        * Keywords are many-to-many
        */
        activityName = "Keyword Creation";

        String firstAssetKeywordIneffectiveGUID = thisTest.createSearchKeyword(openMetadataStoreClient, userId, expiredPrefix, expiredPrefix + "Keyword for firstAsset" , null, new Date(), firstAssetGUID, activityName, testCaseName);
        String firstAssetDuplicateKeywordGUID = thisTest.createSearchKeyword(openMetadataStoreClient, userId, duplicatePrefix, duplicatePrefix + "Keyword for firstAsset", null, null, firstAssetDuplicateGUID, activityName, testCaseName);
        String firstAssetMementoKeywordGUID = thisTest.createSearchKeyword(openMetadataStoreClient, userId, mementoPrefix, mementoPrefix + "Keyword for firstAsset" , null, null, mementoAssetGUID, activityName, testCaseName);
        String firstAssetIneffectiveKeywordGUID = thisTest.createSearchKeyword(openMetadataStoreClient, userId, ineffectivePrefix, ineffectivePrefix + "Keyword for firstAsset" , null, null, ineffectiveAssetGUID, activityName, testCaseName);

        List<RelatedMetadataElement> relatedMetadataElements;

        activityName = "Keyword test - all returned";
        relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId, firstAssetGUID, 1, keywordLinkTypeName, true, false, null, 0, 0);

        if (relatedMetadataElements == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "No keywords returned by " + activityName);
        }
        else if (relatedMetadataElements.size() != 4)
        {
            throw new FVTUnexpectedCondition(testCaseName, relatedMetadataElements.size() + " keywords: " + relatedMetadataElements + " returned by " + activityName);
        }

        activityName = "Keyword test - memento not returned";
        relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId, firstAssetGUID, 1, keywordLinkTypeName, false, false, null, 0, 0);
        if (relatedMetadataElements == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "No keywords returned by " + activityName);
        }
        else if (relatedMetadataElements.size() != 3)
        {
            throw new FVTUnexpectedCondition(testCaseName, relatedMetadataElements.size() + " keywords: " + relatedMetadataElements + " returned by " + activityName);
        }

        activityName = "Keyword test - none returned";
        relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId, firstAssetGUID, 1, keywordLinkTypeName, false, true, new Date(), 0, 0);
        if (relatedMetadataElements != null)
        {
            throw new FVTUnexpectedCondition(testCaseName, relatedMetadataElements.size() + " unexpected keywords returned by " + activityName);
        }

        activityName = "Keyword test - ineffective not returned";
        relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId, firstAssetGUID, 1, keywordLinkTypeName, true, false, new Date(), 0, 0);
        if (relatedMetadataElements == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "No keywords returned by " + activityName);
        }
        else if (relatedMetadataElements.size() != 2)
        {
            throw new FVTUnexpectedCondition(testCaseName, relatedMetadataElements.size() + " keywords returned by " + activityName);
        }

        activityName = "Keyword test - active returned";
        relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId, firstAssetGUID, 1, keywordLinkTypeName, false, false, new Date(), 0, 0);
        if (relatedMetadataElements == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "No keywords returned by " + activityName);
        }
        else if (relatedMetadataElements.size() != 1)
        {
            throw new FVTUnexpectedCondition(testCaseName, relatedMetadataElements.size() + " keywords returned by " + activityName);
        }

    }


    private String createAsset(OpenMetadataStoreClient openMetadataStoreClient,
                               String                  userId,
                               String                  typeName,
                               String                  prefix,
                               String                  name,
                               String                  description,
                               Date                    effectiveFrom,
                               Date                    effectiveTo,
                               String                  templateGUID,
                               String                  activityName,
                               String                  testCaseName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException,
                                                                            FVTUnexpectedCondition
    {
        ElementProperties properties;
        properties = propertyHelper.addStringProperty(null, qualifiedNameProperty, getQualifiedName(typeName, prefix, name));
        properties = propertyHelper.addStringProperty(properties, nameProperty, name);
        properties = propertyHelper.addStringProperty(properties, descriptionProperty, description);
        properties = propertyHelper.addStringMapProperty(properties, additionalPropertiesProperty, getAdditionalProperties(testCaseName));

        String assetGUID = openMetadataStoreClient.createMetadataElementInStore(userId, typeName, ElementStatus.ACTIVE, effectiveFrom, effectiveTo, properties, templateGUID);

        this.getMetadataElementByGUID(openMetadataStoreClient, userId, typeName, assetGUID, assetGUID, prefix, name, false, false, null, activityName, testCaseName);
        this.getMetadataElementByName(openMetadataStoreClient, userId, typeName, name, assetGUID, prefix, name, false, false, null, activityName, testCaseName);

        return assetGUID;
    }


    private String createSearchKeyword(OpenMetadataStoreClient openMetadataStoreClient,
                                       String                  userId,
                                       String                  keyword,
                                       String                  description,
                                       Date                    effectiveFrom,
                                       Date                    effectiveTo,
                                       String                  assetGUID,
                                       String                  activityName,
                                       String                  testCaseName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException,
                                                                                    FVTUnexpectedCondition
    {
        ElementProperties properties;

        properties = propertyHelper.addStringProperty(null, keywordProperty, keyword);
        properties = propertyHelper.addStringProperty(properties, descriptionProperty, description);

        String keywordGUID = openMetadataStoreClient.createMetadataElementInStore(userId, keywordTypeName, ElementStatus.ACTIVE, effectiveFrom, effectiveTo, properties, null);

        OpenMetadataElement retrievedElement = openMetadataStoreClient.getMetadataElementByGUID(userId, keywordGUID, false, false, null);

        if (! keywordGUID.equals(retrievedElement.getElementGUID()))
        {
            throw new FVTUnexpectedCondition(testCaseName, "Different Keyword GUID - Metadata element GUID of " + retrievedElement.getElementGUID() + " rather than " + keywordGUID + " returned by " + activityName);
        }

        openMetadataStoreClient.createRelatedElementsInStore(userId, keywordLinkTypeName, assetGUID, keywordGUID, true, true, effectiveFrom, effectiveTo, null, null);

        return keywordGUID;
    }


    private String createSchemaType(OpenMetadataStoreClient openMetadataStoreClient,
                                    String                  userId,
                                    String                  prefix,
                                    String                  name,
                                    String                  description,
                                    String                  assetGUID,
                                    Date                    effectiveFrom,
                                    Date                    effectiveTo,
                                    String                  activityName,
                                    String                  testCaseName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException,
                                                                                 FVTUnexpectedCondition
    {
        ElementProperties properties;
        properties = propertyHelper.addStringProperty(null, qualifiedNameProperty, getQualifiedName(keywordTypeName, prefix, name));
        properties = propertyHelper.addStringProperty(properties, displayNameProperty, prefix+name);
        properties = propertyHelper.addStringProperty(properties, descriptionProperty, description);
        properties = propertyHelper.addStringMapProperty(properties, additionalPropertiesProperty, getAdditionalProperties(testCaseName));

        String schemaTypeGUID = openMetadataStoreClient.createMetadataElementInStore(userId, keywordTypeName, ElementStatus.ACTIVE, effectiveFrom, effectiveTo, properties, null);

        this.getMetadataElementByGUID(openMetadataStoreClient, userId, schemaTypeTypeName, schemaTypeGUID, schemaTypeGUID, prefix, name, false, false, null, activityName, testCaseName);
        this.getMetadataElementByName(openMetadataStoreClient, userId, schemaTypeTypeName, name, schemaTypeGUID, prefix, name, false, false, null, activityName, testCaseName);

        openMetadataStoreClient.createRelatedElementsInStore(userId, assetSchemaTypeTypeName, assetGUID, schemaTypeGUID, true, true, null, null, null, null);

        return schemaTypeGUID;
    }


    private OpenMetadataElement getMetadataElementByGUID(OpenMetadataStoreClient openMetadataStoreClient,
                                                         String                  userId,
                                                         String                  typeName,
                                                         String                  guid,
                                                         String                  retrievedGUID,
                                                         String                  retrievedPrefix,
                                                         String                  retrievedName,
                                                         boolean                 forLineage,
                                                         boolean                 forDeduplication,
                                                         Date                    effectiveDate,
                                                         String                  activityName,
                                                         String                  testCaseName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException,
                                                                                                      FVTUnexpectedCondition
    {
        OpenMetadataElement retrievedElement = openMetadataStoreClient.getMetadataElementByGUID(userId, guid, forLineage, forDeduplication, effectiveDate);

        validateMetadataElement(retrievedElement, typeName, retrievedGUID, retrievedPrefix, retrievedName, activityName, testCaseName);

        return retrievedElement;
    }


    private OpenMetadataElement getMetadataElementByName(OpenMetadataStoreClient openMetadataStoreClient,
                                                         String                  userId,
                                                         String                  typeName,
                                                         String                  name,
                                                         String                  retrievedGUID,
                                                         String                  retrievedPrefix,
                                                         String                  retrievedName,
                                                         boolean                 forLineage,
                                                         boolean                 forDeduplication,
                                                         Date                    effectiveDate,
                                                         String                  activityName,
                                                         String                  testCaseName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException,
                                                                                                      FVTUnexpectedCondition
    {
        List<OpenMetadataElement> retrievedElements = openMetadataStoreClient.findMetadataElementsWithString(userId, name, forLineage, forDeduplication, effectiveDate, 0 , 0);

        return validateMetadataElements(retrievedElements, typeName, retrievedGUID, retrievedPrefix, retrievedName, activityName, testCaseName);
    }

    private void linkDuplicates(GovernanceEngineClient client,
                                String                 userId,
                                String                 element1GUID,
                                String                 element2GUID,
                                int                    statusIdentifier,
                                String                 testCaseName,
                                String                 activityName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        client.linkElementsAsPeerDuplicates(userId, element1GUID, element2GUID, statusIdentifier, testCaseName, null, null, testCaseName, null, true);

        System.out.println(activityName + ": linking " + element1GUID + " to " + element2GUID);
    }


    private void validateMetadataElement(OpenMetadataElement metadataElement,
                                         String              typeName,
                                         String              retrievedGUID,
                                         String              retrievedPrefix,
                                         String              retrievedName,
                                         String              activityName,
                                         String              testCaseName) throws FVTUnexpectedCondition
    {
        final String methodName = "validateMetadataElement";

        if (metadataElement == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Null metadata element returned by " + activityName);
        }

        if (metadataElement.getElementGUID() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Null metadata element GUID returned by " + activityName);
        }

        if (! metadataElement.getElementGUID().equals(retrievedGUID))
        {
            throw new FVTUnexpectedCondition(testCaseName, "Different GUID - Metadata element GUID of " + metadataElement.getElementGUID() + " rather than " + retrievedGUID + " returned by " + activityName);
        }

        if (! metadataElement.getType().getTypeName().equals(typeName))
        {
            throw new FVTUnexpectedCondition(testCaseName, "Different TypeName - Metadata element GUID of " + metadataElement.getType() + " rather than " + typeName + " returned by " + activityName);
        }

        if (metadataElement.getElementProperties() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Null metadata element properties returned by " + activityName);
        }

        validateQualifiedName(typeName, retrievedPrefix, retrievedName, activityName,  testCaseName, propertyHelper.getStringProperty(testCaseName, qualifiedNameProperty, metadataElement.getElementProperties(), methodName));
        validateDisplayName(retrievedName, activityName, testCaseName, propertyHelper.getStringProperty(testCaseName, nameProperty, metadataElement.getElementProperties(), methodName));
        validateAdditionalProperties(activityName, testCaseName, propertyHelper.getStringMapFromProperty(testCaseName, additionalPropertiesProperty, metadataElement.getElementProperties(), methodName));
    }


    /**
     * Validate that the expected metadata element is returned only once in the results list.  It gets grumpy if the element is missing or
     * occurs multiple times.
     *
     * @param metadataElements list of results
     * @param typeName expected type name
     * @param retrievedGUID expected guid - this is the first test on a matching element
     * @param retrievedPrefix expected prefix used to create the metadata element
     * @param retrievedName expected display name
     * @param activityName activity name for logging
     * @param testCaseName test case name for logging and testing additional properties
     * @return matching element
     * @throws FVTUnexpectedCondition something not right
     */
    private OpenMetadataElement validateMetadataElements(List<OpenMetadataElement> metadataElements,
                                                         String                    typeName,
                                                         String                    retrievedGUID,
                                                         String                    retrievedPrefix,
                                                         String                    retrievedName,
                                                         String                    activityName,
                                                         String                    testCaseName) throws FVTUnexpectedCondition
    {
        OpenMetadataElement matchingElement = null;

        if (metadataElements == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Null metadata elements returned by " + activityName);
        }

        if (metadataElements.isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, "Empty metadata elements returned by " + activityName);
        }

        for (OpenMetadataElement metadataElement : metadataElements)
        {
            if (metadataElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, "Null metadata element returned in list by " + activityName);
            }

            if (metadataElement.getElementGUID() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, "Null metadata element GUID returned in list by " + activityName);
            }

            if (metadataElement.getElementGUID().equals(retrievedGUID))
            {
                if (matchingElement == null)
                {
                    matchingElement = metadataElement;

                    validateMetadataElement(metadataElement,
                                            typeName,
                                            retrievedGUID,
                                            retrievedPrefix,
                                            retrievedName,
                                            activityName,
                                            testCaseName);
                }
                else
                {
                    throw new FVTUnexpectedCondition(testCaseName, "Duplicate metadata element GUID returned in list by " + activityName);
                }
            }
        }

        if (matchingElement == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "No matching metadata element returned in list by " + activityName + ".  Elements: " + metadataElements);
        }

        return matchingElement;
    }


    private String getQualifiedName(String typeName,
                                    String prefix,
                                    String name)
    {
        return typeName + ":" + prefix + name;
    }


    private void validateQualifiedName(String typeName,
                                       String prefix,
                                       String name,
                                       String activityName,
                                       String testCaseName,
                                       String qualifiedName) throws FVTUnexpectedCondition
    {
        if (qualifiedName == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Null qualifiedName returned by " + activityName);
        }

        if (qualifiedName.isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, "Empty qualifiedName returned by " + activityName);
        }

        if (! qualifiedName.equals(getQualifiedName(typeName, prefix, name)))
        {
            throw new FVTUnexpectedCondition(testCaseName, "Invalid qualified name of " + qualifiedName + " returned by " + activityName + ".  Expected value is: " + getQualifiedName(typeName, prefix, name));
        }
    }


    private void validateDisplayName(String name,
                                     String activityName,
                                     String testCaseName,
                                     String displayName) throws FVTUnexpectedCondition
    {
        if (displayName == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Null displayName returned by " + activityName);
        }

        if (displayName.isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, "Empty displayName returned by " + activityName);
        }

        if (! displayName.equals(name))
        {
            throw new FVTUnexpectedCondition(testCaseName, "Invalid display name of " + displayName + " returned by " + activityName + ".  Expected value is: " + name);
        }
    }


    private Map<String, String> getAdditionalProperties(String testCaseName)
    {
        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(testCaseNameProperty, testCaseName);

        return  additionalProperties;
    }


    private void validateAdditionalProperties(String              activityName,
                                              String              testCaseName,
                                              Map<String, String> additionalProperties) throws FVTUnexpectedCondition
    {
        if (additionalProperties == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Null additional properties returned by " + activityName);
        }

        if (additionalProperties.isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, "Empty additional properties returned by " + activityName);
        }

        String testCaseNamePropertyValue = additionalProperties.get(testCaseNameProperty);

        if (testCaseNamePropertyValue == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, "Missing test case name in additional properties returned by " + activityName + ".  Content is: " + additionalProperties);
        }

        if (! testCaseNamePropertyValue.equals(testCaseName))
        {
            throw new FVTUnexpectedCondition(testCaseName, "Invalid test case name of " + testCaseNamePropertyValue + " in additional properties returned by " + activityName + ".  Content is: " + additionalProperties);
        }
    }
}
