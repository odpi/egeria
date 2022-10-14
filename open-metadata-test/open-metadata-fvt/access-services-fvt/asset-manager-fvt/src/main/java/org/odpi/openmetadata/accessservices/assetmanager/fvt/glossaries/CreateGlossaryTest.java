/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.glossaries;

import org.odpi.openmetadata.accessservices.assetmanager.client.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.common.AssetManagerTestBase;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateGlossaryTest calls the GlossaryExchangeClient to create a glossary with categories and terms
 * and then retrieve the results.
 */
public class CreateGlossaryTest extends AssetManagerTestBase
{
    private final static String testCaseName       = "CreateGlossaryTest";

    private final static int    maxPageSize        = 100;

    /*
     * The asset manager name is constant - the guid is created as part of the test.
     */
    private final static String assetManagerName            = "TestGlossaryAssetManager";


    private final static String     externalGlossaryIdentifier           = "TestExternalIdentifier";
    private final static String     externalGlossaryIdentifierName       = "TestExternalIdentifierName";
    private final static String     externalGlossaryIdentifierUsage      = "TestExternalIdentifierUsage";
    private final static KeyPattern externalGlossaryIdentifierKeyPattern = KeyPattern.CALLERS_KEY;
    private final static String     externalGlossaryIdentifierSource     = "TestExternalIdentifierSource";

    private final static String     externalCategoryIdentifier           = "TestExternalCategoryIdentifier";
    private final static String     externalCategoryIdentifierName       = "TestExternalCategoryIdentifierName";
    private final static String     externalCategoryIdentifierUsage      = "TestExternalCategoryIdentifierUsage";
    private final static KeyPattern externalCategoryIdentifierKeyPattern = KeyPattern.RECYCLED_KEY;
    private final static String     externalCategoryIdentifierSource     = "TestExternalCategoryIdentifierSource";


    private final static String     externalTermIdentifier           = "TestExternalTermIdentifier";
    private final static String     externalTermIdentifierName       = "TestExternalTermIdentifierName";
    private final static String     externalTermIdentifierUsage      = "TestExternalTermIdentifierUsage";
    private final static KeyPattern externalTermIdentifierKeyPattern = null;
    private final static String     externalTermIdentifierSource     = "TestExternalTermIdentifierSource";



    private final static String glossaryName        = "TestGlossaryCreate";
    private final static String glossaryDisplayName = "Glossary displayName";
    private final static String glossaryDescription = "Glossary description";
    private final static String glossaryUsage       = "Glossary usage";
    private final static String glossaryLanguage    = "Glossary language";

    private final static String glossaryCategoryName        = "TestGlossaryCreateCategory";
    private final static String glossaryCategoryDisplayName = "GlossaryCategory displayName";
    private final static String glossaryCategoryDescription = "GlossaryCategory description";

    private final static String glossaryTermName        = "TestGlossaryCreateTerm";
    private final static String glossaryTermDisplayName = "GlossaryTerm displayName";
    private final static String glossaryTermDescription = "GlossaryTerm description";


    private final static String controlledGlossaryTermName        = "TestGlossaryCreateControlledTerm";
    private final static String controlledGlossaryTermDisplayName = "ControlledGlossaryTerm displayName";
    private final static String controlledGlossaryTermDescription = "ControlledGlossaryTerm description";



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
            CreateGlossaryTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateGlossaryTest thisTest = new CreateGlossaryTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());

        String assetManagerGUID = thisTest.getAssetManager(assetManagerName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);

        GlossaryExchangeClient client = thisTest.getGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String activityName = "getGlossary("+ glossaryName + ")";
        String glossaryGUID = thisTest.getGlossary(client,
                                                   testCaseName,
                                                   activityName,
                                                   userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   glossaryName,
                                                   glossaryDisplayName,
                                                   glossaryDescription,
                                                   glossaryUsage,
                                                   glossaryLanguage,
                                                   externalGlossaryIdentifier,
                                                   externalGlossaryIdentifierName,
                                                   externalGlossaryIdentifierUsage,
                                                   externalGlossaryIdentifierSource,
                                                   externalGlossaryIdentifierKeyPattern,
                                                   null);

        try
        {
            GlossaryElement glossaryElement = client.getGlossaryByGUID(userId, assetManagerGUID, assetManagerName, glossaryGUID);

            if (glossaryElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no glossary element)");
            }
            else if (glossaryElement.getElementHeader() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no glossary header) " + glossaryElement.toString());
            }
            else if (glossaryElement.getGlossaryProperties() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no glossary properties) " + glossaryElement.toString());
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

        String glossaryCategoryGUID = thisTest.getGlossaryCategory(client, assetManagerGUID, glossaryGUID, userId);

        String glossaryTermGUID = thisTest.getGlossaryTerm(client, assetManagerGUID, glossaryGUID, userId);
        String controlledGlossaryTermGUID = thisTest.getControlledGlossaryTerm(client, glossaryGUID, userId);

        thisTest.addTermToCategory(client,
                                   assetManagerGUID,
                                   glossaryCategoryGUID,
                                   glossaryTermGUID,
                                   userId,
                                   "GlossaryTerm",
                                   glossaryTermName,
                                   glossaryTermDisplayName,
                                   glossaryTermDescription,
                                   ElementStatus.ACTIVE);

        thisTest.addTermToCategory(client,
                                   assetManagerGUID,
                                   glossaryCategoryGUID,
                                   controlledGlossaryTermGUID,
                                   userId,
                                   "ControlledGlossaryTerm",
                                   controlledGlossaryTermName,
                                   controlledGlossaryTermDisplayName,
                                   controlledGlossaryTermDescription,
                                   ElementStatus.DRAFT);


        try
        {
            client.removeGlossary(userId, assetManagerGUID, assetManagerName, glossaryGUID, externalGlossaryIdentifier);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " => " + glossaryGUID, unexpectedError);
        }

        try
        {
            client.getGlossaryByGUID(userId, assetManagerGUID, assetManagerName, glossaryGUID);

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(glossary not deleted)");
        }
        catch (InvalidParameterException notFound)
        {
            // all well
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
     * Create a glossary category and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the asset manager
     * @param glossaryGUID unique id of the glossary
     * @param userId calling user
     * @return GUID of glossary
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getGlossaryCategory(GlossaryExchangeClient client,
                                       String                 assetManagerGUID,
                                       String                 glossaryGUID,
                                       String                 userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getGlossaryCategory";

        try
        {
            GlossaryCategoryProperties properties = new GlossaryCategoryProperties();
 
            properties.setQualifiedName(glossaryCategoryName);
            properties.setDisplayName(glossaryCategoryDisplayName);
            properties.setDescription(glossaryCategoryDescription);

            ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

            externalIdentifierProperties.setExternalIdentifier(externalCategoryIdentifier);
            externalIdentifierProperties.setExternalIdentifierName(externalCategoryIdentifierName);
            externalIdentifierProperties.setExternalIdentifierUsage(externalCategoryIdentifierUsage);
            externalIdentifierProperties.setExternalIdentifierSource(externalCategoryIdentifierSource);
            externalIdentifierProperties.setKeyPattern(externalCategoryIdentifierKeyPattern);
            externalIdentifierProperties.setMappingProperties(null);

            String glossaryCategoryGUID = client.createGlossaryCategory(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        glossaryGUID,
                                                                        externalIdentifierProperties,
                                                                        properties);

            if (glossaryCategoryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }

            GlossaryCategoryElement    retrievedElement = client.getGlossaryCategoryByGUID(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, null, false, false);
            GlossaryCategoryProperties retrievedCategory  = retrievedElement.getGlossaryCategoryProperties();

            if (retrievedCategory == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryCategory from Retrieve) " + retrievedElement.toString());
            }

            if (retrievedElement.getElementHeader() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Header from Retrieve) " + retrievedElement.toString());
            }

            if (! glossaryCategoryName.equals(retrievedCategory.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! glossaryCategoryDisplayName.equals(retrievedCategory.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! glossaryCategoryDescription.equals(retrievedCategory.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            

            List<GlossaryCategoryElement> glossaryCategoryList = client.getGlossaryCategoriesByName(userId, assetManagerGUID, assetManagerName, glossaryCategoryName, 0, maxPageSize, null, false, false);

            if (glossaryCategoryList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryCategory for RetrieveByName)");
            }
            else if (glossaryCategoryList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty GlossaryCategory list for RetrieveByName)");
            }
            else if (glossaryCategoryList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(GlossaryCategory list for RetrieveByName contains" + glossaryCategoryList.size() +
                                                         " elements)");
            }

            retrievedElement = glossaryCategoryList.get(0);
            retrievedCategory = retrievedElement.getGlossaryCategoryProperties();

            if (! glossaryCategoryName.equals(retrievedCategory.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! glossaryCategoryDisplayName.equals(retrievedCategory.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! glossaryCategoryDescription.equals(retrievedCategory.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            return glossaryCategoryGUID;
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
     * Add the term to the category and then retrieve the categories terms and check that the term is there.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the asset manager
     * @param glossaryCategoryGUID unique id of the glossary category
     * @param glossaryTermGUID unique id of the glossary term
     * @param userId calling user id
     * @param typeName expected type name of the glossary term
     * @param qualifiedName expected qualified name of the glossary term
     * @param displayName expected display name of the glossary term
     * @param description expected description of the glossary term
     * @param status expected status of the glossary term
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void addTermToCategory(GlossaryExchangeClient client,
                                   String                 assetManagerGUID,
                                   String                 glossaryCategoryGUID,
                                   String                 glossaryTermGUID,
                                   String                 userId,
                                   String                 typeName,
                                   String                 qualifiedName,
                                   String                 displayName,
                                   String                 description,
                                   ElementStatus          status) throws FVTUnexpectedCondition
    {
        final String activityName = "addTermToCategory";

        try
        {
            GlossaryTermCategorization categorizationProperties = new GlossaryTermCategorization();
            categorizationProperties.setDescription("Category for " + glossaryTermGUID);
            categorizationProperties.setStatus(GlossaryTermRelationshipStatus.ACTIVE);

            client.setupTermCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, glossaryTermGUID, categorizationProperties, null, false, false);

            List<GlossaryTermElement> glossaryTermList = client.getTermsForGlossaryCategory(userId, null, null, glossaryCategoryGUID, 0 , 0, null, false, false);

            this.validateGlossaryTermElements(activityName,
                                              "RetrieveByCategory",
                                              glossaryTermList,
                                              glossaryTermGUID,
                                              typeName,
                                              qualifiedName,
                                              displayName,
                                              description,
                                              status);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a glossary term and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the asset manager
     * @param glossaryGUID unique id of the glossaryCategory
     * @param userId calling user
     * @return GUID of glossary
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getGlossaryTerm(GlossaryExchangeClient client,
                                   String                 assetManagerGUID,
                                   String                 glossaryGUID,
                                   String                 userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getGlossaryTerm";

        try
        {
            GlossaryTermProperties properties = new GlossaryTermProperties();

            properties.setQualifiedName(glossaryTermName);
            properties.setDisplayName(glossaryTermDisplayName);
            properties.setDescription(glossaryTermDescription);

            Map<String, String> mappingProperties = new HashMap<>();
            mappingProperties.put("prop1", "One");
            mappingProperties.put("prop2", "Two");

            ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

            externalIdentifierProperties.setExternalIdentifier(externalTermIdentifier);
            externalIdentifierProperties.setExternalIdentifierName(externalTermIdentifierName);
            externalIdentifierProperties.setExternalIdentifierUsage(externalTermIdentifierUsage);
            externalIdentifierProperties.setExternalIdentifierSource(externalTermIdentifierSource);
            externalIdentifierProperties.setKeyPattern(externalTermIdentifierKeyPattern);
            externalIdentifierProperties.setMappingProperties(mappingProperties);

            String glossaryTermGUID = client.createGlossaryTerm(userId,
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                glossaryGUID,
                                                                externalIdentifierProperties,
                                                                properties);

            if (glossaryTermGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for GlossaryTerm Create)");
            }

            validateGlossaryTerm(client,
                                 userId,
                                 assetManagerGUID,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "GlossaryTerm",
                                 glossaryTermName,
                                 glossaryTermDisplayName,
                                 glossaryTermDescription,
                                 ElementStatus.ACTIVE);

            return glossaryTermGUID;
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
     * Create a local cohort controlled glossary term and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param glossaryGUID unique id of the glossaryCategory
     * @param userId calling user
     * @return GUID of glossary
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getControlledGlossaryTerm(GlossaryExchangeClient client,
                                             String                 glossaryGUID,
                                             String                 userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getControlledGlossaryTerm";

        try
        {
            GlossaryTermProperties properties = new GlossaryTermProperties();

            properties.setQualifiedName(controlledGlossaryTermName);
            properties.setDisplayName(controlledGlossaryTermDisplayName);
            properties.setDescription(controlledGlossaryTermDescription);


            String glossaryTermGUID = client.createControlledGlossaryTerm(userId,
                                                                          null,
                                                                          null,
                                                                          glossaryGUID,
                                                                          null,
                                                                          properties,
                                                                          GlossaryTermStatus.DRAFT);

            if (glossaryTermGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for ControlledGlossaryTerm Create)");
            }

            validateGlossaryTerm(client,
                                 userId,
                                 null,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 ElementStatus.DRAFT);

            client.updateGlossaryTermStatus(userId, null, null, glossaryTermGUID, null, GlossaryTermStatus.PROPOSED, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 null,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 ElementStatus.PROPOSED);

            client.updateGlossaryTermStatus(userId, null, null, glossaryTermGUID, null, GlossaryTermStatus.APPROVED, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 null,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 ElementStatus.APPROVED);

            client.updateGlossaryTermStatus(userId, null, null, glossaryTermGUID, null, GlossaryTermStatus.ACTIVE, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 null,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 ElementStatus.ACTIVE);

            client.updateGlossaryTermStatus(userId, null, null, glossaryTermGUID, null, GlossaryTermStatus.DRAFT, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 null,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 ElementStatus.DRAFT);

            return glossaryTermGUID;
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


    private void validateGlossaryTerm(GlossaryExchangeClient client,
                                      String                 userId,
                                      String                 assetManagerGUID,
                                      String                 activityName,
                                      String                 glossaryGUID,
                                      String                 glossaryTermGUID,
                                      String                 typeName,
                                      String                 qualifiedName,
                                      String                 displayName,
                                      String                 description,
                                      ElementStatus          status) throws FVTUnexpectedCondition
    {
        try
        {
            GlossaryTermElement    retrievedElement = client.getGlossaryTermByGUID(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, null, false, false);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryTerm from Retrieve)");
            }

            validateGlossaryTermElement(activityName,
                                        "Retrieve",
                                        retrievedElement,
                                        glossaryTermGUID,
                                        typeName,
                                        qualifiedName,
                                        displayName,
                                        description,
                                        status);

            List<GlossaryTermElement> glossaryTermList = client.getGlossaryTermsByName(userId, assetManagerGUID, assetManagerName, qualifiedName, 0, maxPageSize, null, false, false);

            validateGlossaryTermElements(activityName,
                                        "RetrieveByQualifiedName",
                                        glossaryTermList,
                                        glossaryTermGUID,
                                        typeName,
                                        qualifiedName,
                                        displayName,
                                        description,
                                        status);

            glossaryTermList = client.getGlossaryTermsByName(userId, assetManagerGUID, assetManagerName, displayName, 0, 0, null, false, false);

            validateGlossaryTermElements(activityName,
                                         "RetrieveTermsByGlossary",
                                         glossaryTermList,
                                         glossaryTermGUID,
                                         typeName,
                                         qualifiedName,
                                         displayName,
                                         description,
                                         status);

            glossaryTermList = client.getTermsForGlossary(userId, null, null, glossaryGUID, 0, 0, null, false, false);

            validateGlossaryTermElements(activityName,
                                         "RetrieveTermsByGlossary",
                                         glossaryTermList,
                                         glossaryTermGUID,
                                         typeName,
                                         qualifiedName,
                                         displayName,
                                         description,
                                         status);
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
     * Validate a returned list of glossary term elements.
     *
     * @param activityName which test
     * @param stepName which step in test
     * @param retrievedElements elements retrieved from the repository
     * @param glossaryTermGUID unique identifier of the element to focus on
     * @param typeName name of type of glossary term
     * @param qualifiedName expected qualified name
     * @param displayName expected display name
     * @param description expected description
     * @param status expected status
     * @throws FVTUnexpectedCondition something was wrong.
     */
    private void validateGlossaryTermElements(String                    activityName,
                                              String                    stepName,
                                              List<GlossaryTermElement> retrievedElements,
                                              String                    glossaryTermGUID,
                                              String                    typeName,
                                              String                    qualifiedName,
                                              String                    displayName,
                                              String                    description,
                                              ElementStatus             status) throws FVTUnexpectedCondition
    {
        if (retrievedElements == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryTerm for " + stepName + ")");
        }
        else if (retrievedElements.isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty GlossaryTerm list for " + stepName + ")");
        }

        boolean elementFound = false;

        for (GlossaryTermElement termElement : retrievedElements)
        {
            if (validateGlossaryTermElement(activityName, stepName, termElement, glossaryTermGUID, typeName, qualifiedName, displayName, description, status))
            {
                elementFound = true;
            }
        }

        if (! elementFound)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Element " + glossaryTermGUID + " not found for " + stepName + ") " + retrievedElements.size() + " elements retrieved: " + retrievedElements.toString());
        }
    }


    /**
     * Verify that an element returned is as expected.
     *
     * @param activityName which test
     * @param stepName which step in test
     * @param retrievedElement element to test
     * @param glossaryTermGUID guid of element of interest
     * @param typeName name of type of glossary term
     * @param qualifiedName expected qualified name
     * @param displayName expected display name
     * @param description expected description
     * @param status expected status
     * @return boolean to indicate that the element matched the supplied GUID
     * @throws FVTUnexpectedCondition something was wrong.
     */
    private boolean validateGlossaryTermElement(String              activityName,
                                                String              stepName,
                                                GlossaryTermElement retrievedElement,
                                                String              glossaryTermGUID,
                                                String              typeName,
                                                String              qualifiedName,
                                                String              displayName,
                                                String              description,
                                                ElementStatus       status) throws FVTUnexpectedCondition
    {
        ElementHeader retrievedHeader = retrievedElement.getElementHeader();

        if (retrievedHeader == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryTerm header from " + stepName + ")" + retrievedElement.toString());
        }

        if (! glossaryTermGUID.equals(retrievedHeader.getGUID()))
        {
            /*
             * Not this element
             */
            return false;
        }

        if (! typeName.equals(retrievedHeader.getType().getTypeName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad GlossaryTerm type of " +  retrievedHeader.getType().getTypeName() + " rather than " + typeName + " from " + stepName + ")");
        }

        if (retrievedHeader.getOrigin().getHomeMetadataCollectionId() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null GlossaryTerm metadata collection id from " + stepName + ")");
        }

        GlossaryTermProperties retrievedTerm   = retrievedElement.getGlossaryTermProperties();

        if (status != retrievedHeader.getStatus())
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad GlossaryTerm status of " +  retrievedHeader.getStatus() + " rather than " + status + " from " + stepName + ")");
        }

        if (retrievedTerm == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No GlossaryTerm from " + stepName + ")");
        }

        if (! qualifiedName.equals(retrievedTerm.getQualifiedName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from " + stepName + ": " + retrievedTerm.getQualifiedName() + " rather than " + qualifiedName + ")");
        }
        if (! displayName.equals(retrievedTerm.getDisplayName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from " + stepName + ": " + retrievedTerm.getDisplayName() + " rather than " + displayName + ")");
        }
        if (! description.equals(retrievedTerm.getDescription()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from " + stepName + ": " + retrievedTerm.getDescription() + " rather than " + description + ")");
        }

        return true;
    }
}
