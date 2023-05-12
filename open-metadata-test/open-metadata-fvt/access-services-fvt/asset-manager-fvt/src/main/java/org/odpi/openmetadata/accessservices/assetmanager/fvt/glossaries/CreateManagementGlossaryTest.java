/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.glossaries;

import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.common.AssetManagerTestBase;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryCategoryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermCategorization;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateExchangeGlossaryTest calls the GlossaryManagementClient to create a glossary with categories and terms
 * and then retrieve the results.
 */
public class CreateManagementGlossaryTest extends AssetManagerTestBase
{
    private final static String testCaseName       = "CreateManagementGlossaryTest";

    private final static int    maxPageSize        = 100;

    private final static String glossaryName        = "ManagementTestGlossaryCreate";
    private final static String glossaryDisplayName = "ManagementGlossary displayName";
    private final static String glossaryDescription = "ManagementGlossary description";
    private final static String glossaryUsage       = "ManagementGlossary usage";
    private final static String glossaryLanguage    = "ManagementGlossary language";

    private final static String glossaryCategoryName        = "ManagementTestGlossaryCreateCategory";
    private final static String glossaryCategoryDisplayName = "ManagementGlossaryCategory displayName";
    private final static String glossaryCategoryDescription = "ManagementGlossaryCategory description";

    private final static String glossaryTermName        = "ManagementTestGlossaryCreateTerm";
    private final static String glossaryTermDisplayName = "ManagementGlossaryTerm displayName";
    private final static String glossaryTermDescription = "ManagementGlossaryTerm description";


    private final static String controlledGlossaryTermName        = "ManagementTestGlossaryCreateControlledTerm";
    private final static String controlledGlossaryTermDisplayName = "ManagementControlledGlossaryTerm displayName";
    private final static String controlledGlossaryTermDescription = "ManagementControlledGlossaryTerm description";
    private final static String controlledGlossaryTermStatus      = "Truly Awesome";



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
            CreateManagementGlossaryTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateManagementGlossaryTest thisTest = new CreateManagementGlossaryTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());
        
        GlossaryManagementClient client = thisTest.getGlossaryManagementClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String activityName = "getGlossary("+ glossaryName + ")";
        String glossaryGUID = thisTest.getGlossary(client,
                                                   testCaseName,
                                                   activityName,
                                                   userId,
                                                   glossaryName,
                                                   glossaryDisplayName,
                                                   glossaryDescription,
                                                   glossaryUsage,
                                                   glossaryLanguage);

        try
        {
            GlossaryElement glossaryElement = client.getGlossaryByGUID(userId, glossaryGUID, null, false, false);

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

        String glossaryCategoryGUID = thisTest.getGlossaryCategory(client, glossaryGUID, userId);

        String glossaryTermGUID = thisTest.getGlossaryTerm(client, glossaryGUID, userId);
        String controlledGlossaryTermGUID = thisTest.getControlledGlossaryTerm(client, glossaryGUID, userId);

        thisTest.addTermToCategory(client,
                                   glossaryCategoryGUID,
                                   glossaryTermGUID,
                                   userId,
                                   "GlossaryTerm",
                                   glossaryTermName,
                                   glossaryTermDisplayName,
                                   glossaryTermDescription,
                                   null,
                                   ElementStatus.ACTIVE);

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put("userDefinedStatus", controlledGlossaryTermStatus);

        thisTest.addTermToCategory(client,
                                   glossaryCategoryGUID,
                                   controlledGlossaryTermGUID,
                                   userId,
                                   "ControlledGlossaryTerm",
                                   controlledGlossaryTermName,
                                   controlledGlossaryTermDisplayName,
                                   controlledGlossaryTermDescription,
                                   extendedProperties,
                                   ElementStatus.DRAFT);

        activityName = "Bad property("+ glossaryName + ")";

        try
        {
            GlossaryTermProperties properties         = new GlossaryTermProperties();
            extendedProperties = new HashMap<>();

            extendedProperties.put("badPropertyName", controlledGlossaryTermStatus);

            properties.setQualifiedName(controlledGlossaryTermName);
            properties.setDisplayName(controlledGlossaryTermDisplayName);
            properties.setDescription(controlledGlossaryTermDescription);
            properties.setExtendedProperties(extendedProperties);

            glossaryTermGUID = client.createControlledGlossaryTerm(userId,
                                                                   glossaryGUID,
                                                                   properties,
                                                                   GlossaryTermStatus.DRAFT,
                                                                   null,
                                                                   false,
                                                                   false);

            throw new FVTUnexpectedCondition(testCaseName, activityName + " (term created with invalid property " + glossaryTermGUID + ")");
        }
        catch (InvalidParameterException unexpectedError)
        {
            // all good
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " => " + glossaryGUID, unexpectedError);
        }

        try
        {
            client.removeGlossary(userId, glossaryGUID, null, false, false);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + " => " + glossaryGUID, unexpectedError);
        }

        try
        {
            client.getGlossaryByGUID(userId, glossaryGUID, null, false, false);

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
     * @param glossaryGUID unique id of the glossary
     * @param userId calling user
     * @return GUID of glossary
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getGlossaryCategory(GlossaryManagementClient client,
                                       String                   glossaryGUID,
                                       String                   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getGlossaryCategory";

        try
        {
            GlossaryCategoryProperties properties = new GlossaryCategoryProperties();
 
            properties.setQualifiedName(glossaryCategoryName);
            properties.setDisplayName(glossaryCategoryDisplayName);
            properties.setDescription(glossaryCategoryDescription);
            
            String glossaryCategoryGUID = client.createGlossaryCategory(userId,
                                                                        glossaryGUID,
                                                                        properties,
                                                                        true,
                                                                        null,
                                                                        false,
                                                                        false);

            if (glossaryCategoryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }

            GlossaryCategoryElement    retrievedElement = client.getGlossaryCategoryByGUID(userId, glossaryCategoryGUID, null, false, false);
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
            

            List<GlossaryCategoryElement> glossaryCategoryList = client.getGlossaryCategoriesByName(userId, null, glossaryCategoryName, 0, maxPageSize, null, false, false);

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
     * @param glossaryCategoryGUID unique id of the glossary category
     * @param glossaryTermGUID unique id of the glossary term
     * @param userId calling user id
     * @param typeName expected type name of the glossary term
     * @param qualifiedName expected qualified name of the glossary term
     * @param displayName expected display name of the glossary term
     * @param description expected description of the glossary term
     * @param extendedProperties additional properties for subtype
     * @param status expected status of the glossary term
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void addTermToCategory(GlossaryManagementClient client,
                                   String                   glossaryCategoryGUID,
                                   String                   glossaryTermGUID,
                                   String                   userId,
                                   String                   typeName,
                                   String                   qualifiedName,
                                   String                   displayName,
                                   String                   description,
                                   Map<String, Object>      extendedProperties,
                                   ElementStatus            status) throws FVTUnexpectedCondition
    {
        final String activityName = "addTermToCategory";

        try
        {
            GlossaryTermCategorization categorizationProperties = new GlossaryTermCategorization();
            categorizationProperties.setDescription("Category for " + glossaryTermGUID);
            categorizationProperties.setStatus(GlossaryTermRelationshipStatus.ACTIVE);

            client.setupTermCategory(userId, glossaryCategoryGUID, glossaryTermGUID, categorizationProperties, null, false, false);

            List<GlossaryTermElement> glossaryTermList = client.getTermsForGlossaryCategory(userId, glossaryCategoryGUID, null, 0 , 0, null, false, false);

            this.validateGlossaryTermElements(testCaseName,
                                              activityName,
                                              "RetrieveByCategory",
                                              glossaryTermList,
                                              glossaryTermGUID,
                                              typeName,
                                              qualifiedName,
                                              displayName,
                                              description,
                                              extendedProperties,
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
     * @param glossaryGUID unique id of the glossaryCategory
     * @param userId calling user
     * @return GUID of glossary
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getGlossaryTerm(GlossaryManagementClient client,
                                   String                   glossaryGUID,
                                   String                   userId) throws FVTUnexpectedCondition
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

            String glossaryTermGUID = client.createGlossaryTerm(userId,
                                                                glossaryGUID,
                                                                properties,
                                                                null,
                                                                false,
                                                                false);

            if (glossaryTermGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for GlossaryTerm Create)");
            }

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "GlossaryTerm",
                                 glossaryTermName,
                                 glossaryTermDisplayName,
                                 glossaryTermDescription,
                                 null,
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
    private String getControlledGlossaryTerm(GlossaryManagementClient client,
                                             String                   glossaryGUID,
                                             String                   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getControlledGlossaryTerm";

        try
        {
            GlossaryTermProperties properties = new GlossaryTermProperties();
            Map<String, Object>    extendedProperties = new HashMap<>();

            extendedProperties.put("userDefinedStatus", controlledGlossaryTermStatus);

            properties.setQualifiedName(controlledGlossaryTermName);
            properties.setDisplayName(controlledGlossaryTermDisplayName);
            properties.setDescription(controlledGlossaryTermDescription);
            properties.setExtendedProperties(extendedProperties);


            String glossaryTermGUID = client.createControlledGlossaryTerm(userId,
                                                                          glossaryGUID,
                                                                          properties,
                                                                          GlossaryTermStatus.DRAFT,
                                                                          null,
                                                                          false,
                                                                          false);

            if (glossaryTermGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for ControlledGlossaryTerm Create)");
            }

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.DRAFT);

            client.updateGlossaryTermStatus(userId,  glossaryTermGUID,  GlossaryTermStatus.PROPOSED, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.PROPOSED);

            client.updateGlossaryTermStatus(userId, glossaryTermGUID,  GlossaryTermStatus.APPROVED, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.APPROVED);

            client.updateGlossaryTermStatus(userId,  glossaryTermGUID,  GlossaryTermStatus.ACTIVE, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.ACTIVE);

            client.updateGlossaryTermStatus(userId, glossaryTermGUID,  GlossaryTermStatus.PREPARED, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.PREPARED);

            client.updateGlossaryTermStatus(userId, glossaryTermGUID,  GlossaryTermStatus.REJECTED, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.REJECTED);

            client.updateGlossaryTermStatus(userId, glossaryTermGUID, GlossaryTermStatus.OTHER, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.OTHER);

            client.updateGlossaryTermStatus(userId, glossaryTermGUID,  GlossaryTermStatus.DEPRECATED, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
                                 ElementStatus.DEPRECATED);

            /*
             * Set the state back to draft for subsequent tests
             */
            client.updateGlossaryTermStatus(userId, glossaryTermGUID, GlossaryTermStatus.DRAFT, null, false, false);

            validateGlossaryTerm(client,
                                 userId,
                                 activityName,
                                 glossaryGUID,
                                 glossaryTermGUID,
                                 "ControlledGlossaryTerm",
                                 controlledGlossaryTermName,
                                 controlledGlossaryTermDisplayName,
                                 controlledGlossaryTermDescription,
                                 extendedProperties,
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


    private void validateGlossaryTerm(GlossaryManagementClient client,
                                      String                 userId,
                                      String                 activityName,
                                      String                 glossaryGUID,
                                      String                 glossaryTermGUID,
                                      String                 typeName,
                                      String                 qualifiedName,
                                      String                 displayName,
                                      String                 description,
                                      Map<String, Object>    extendedProperties,
                                      ElementStatus          status) throws FVTUnexpectedCondition
    {
        try
        {
            GlossaryTermElement    retrievedElement = client.getGlossaryTermByGUID(userId, glossaryTermGUID, null, false, false);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryTerm from Retrieve)");
            }

            validateGlossaryTermElement(testCaseName,
                                        activityName,
                                        "Retrieve",
                                        retrievedElement,
                                        glossaryTermGUID,
                                        typeName,
                                        qualifiedName,
                                        displayName,
                                        description,
                                        extendedProperties,
                                        status);

            List<GlossaryTermElement> glossaryTermList = client.getGlossaryTermsByName(userId, null, qualifiedName, null,0, maxPageSize, null, false, false);

            validateGlossaryTermElements(testCaseName,
                                         activityName,
                                        "RetrieveByQualifiedName",
                                        glossaryTermList,
                                        glossaryTermGUID,
                                        typeName,
                                        qualifiedName,
                                        displayName,
                                        description,
                                        extendedProperties,
                                        status);

            glossaryTermList = client.getGlossaryTermsByName(userId, null, displayName, null, 0, 0, null, false, false);

            validateGlossaryTermElements(testCaseName,
                                         activityName,
                                         "RetrieveTermsByGlossary",
                                         glossaryTermList,
                                         glossaryTermGUID,
                                         typeName,
                                         qualifiedName,
                                         displayName,
                                         description,
                                         extendedProperties,
                                         status);

            glossaryTermList = client.getTermsForGlossary(userId, glossaryGUID, 0, 0, null, false, false);

            validateGlossaryTermElements(testCaseName,
                                         activityName,
                                         "RetrieveTermsByGlossary",
                                         glossaryTermList,
                                         glossaryTermGUID,
                                         typeName,
                                         qualifiedName,
                                         displayName,
                                         description,
                                         extendedProperties,
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
}
