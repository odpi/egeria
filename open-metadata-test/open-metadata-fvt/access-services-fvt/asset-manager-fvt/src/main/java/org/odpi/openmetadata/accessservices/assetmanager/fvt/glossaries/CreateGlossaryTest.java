/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.fvt.glossaries;

import org.odpi.openmetadata.accessservices.assetmanager.client.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.fvt.common.AssetManagerTestBase;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
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

    private final static String     externalCategoryIdentifier           = "TestExternalIdentifier";
    private final static String     externalCategoryIdentifierName       = "TestExternalIdentifierName";
    private final static String     externalCategoryIdentifierUsage      = "TestExternalIdentifierUsage";
    private final static KeyPattern externalCategoryIdentifierKeyPattern = KeyPattern.RECYCLED_KEY;
    private final static String     externalCategoryIdentifierSource     = "TestExternalIdentifierSource";


    private final static String     externalTermIdentifier           = "TestExternalTermIdentifier";
    private final static String     externalTermIdentifierName       = "TestExternalTermIdentifierName";
    private final static String     externalTermIdentifierUsage      = "TestExternalTermIdentifierUsage";
    private final static KeyPattern externalTermIdentifierKeyPattern = null;
    private final static String     externalTermIdentifierSource     = "TestExternalTermIdentifierSource";



    private final static String glossaryName        = "TestGlossary";
    private final static String glossaryDisplayName = "Glossary displayName";
    private final static String glossaryDescription = "Glossary description";
    private final static String glossaryUsage       = "Glossary usage";
    private final static String glossaryLanguage    = "Glossary language";

    private final static String glossaryCategoryName        = "TestGlossaryCategory";
    private final static String glossaryCategoryDisplayName = "GlossaryCategory displayName";
    private final static String glossaryCategoryDescription = "GlossaryCategory description";

    private final static String glossaryTermName        = "TestGlossaryTerm";
    private final static String glossaryTermDisplayName = "GlossaryTerm displayName";
    private final static String glossaryTermDescription = "GlossaryTerm description";
    private final static String glossaryTermType        = "GlossaryTerm type";
    private final static String glossaryTermVersion     = "GlossaryTerm version";


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
        CreateGlossaryTest thisTest = new CreateGlossaryTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceWiki());

        String assetManagerGUID = thisTest.getAssetManager(assetManagerName, serverName, serverPlatformRootURL, userId, auditLog, testCaseName);

        GlossaryExchangeClient client = thisTest.getGlossaryExchangeClient(serverName, serverPlatformRootURL, auditLog, testCaseName);

        String activityName = "getGlossary("+ glossaryName +")";
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

        // String glossaryCategoryGUID = thisTest.getGlossaryCategory(client, assetManagerGUID, glossaryGUID, userId);

        try
        {
            client.removeGlossary(userId, assetManagerGUID, assetManagerName, glossaryGUID, externalGlossaryIdentifier);
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
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
        catch (Throwable unexpectedError)
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
            

            String glossaryCategoryGUID = client.createGlossaryCategory(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        glossaryGUID,
                                                                        externalCategoryIdentifier,
                                                                        externalCategoryIdentifierName,
                                                                        externalCategoryIdentifierUsage,
                                                                        externalCategoryIdentifierSource,
                                                                        externalCategoryIdentifierKeyPattern,
                                                                        null,
                                                                        properties);

            if (glossaryCategoryGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }

            GlossaryCategoryElement    retrievedElement = client.getGlossaryCategoryByGUID(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID);
            GlossaryCategoryProperties retrievedSchema  = retrievedElement.getGlossaryCategoryProperties();

            if (retrievedSchema == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryCategory from Retrieve)");
            }

            if (! glossaryCategoryName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! glossaryCategoryDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! glossaryCategoryDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            

            List<GlossaryCategoryElement> glossaryCategoryList = client.getGlossaryCategoriesByName(userId, assetManagerGUID, assetManagerName, glossaryCategoryName, 0, maxPageSize);

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
            retrievedSchema = retrievedElement.getGlossaryCategoryProperties();

            if (! glossaryCategoryName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! glossaryCategoryDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! glossaryCategoryDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            return glossaryCategoryGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a glossary and return its GUID.
     *
     * @param client interface to Asset Manager OMAS
     * @param assetManagerGUID unique id of the asset manager
     * @param glossaryCategoryGUID unique id of the glossaryCategory
     * @param userId calling user
     * @return GUID of glossary
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getGlossaryTerm(GlossaryExchangeClient client,
                                     String               assetManagerGUID,
                                     String               glossaryCategoryGUID,
                                     String               userId) throws FVTUnexpectedCondition
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
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                glossaryCategoryGUID,
                                                                externalTermIdentifier,
                                                                externalTermIdentifierName,
                                                                externalTermIdentifierUsage,
                                                                externalTermIdentifierSource,
                                                                externalTermIdentifierKeyPattern,
                                                                mappingProperties,
                                                                properties);

            if (glossaryTermGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }

            GlossaryTermElement    retrievedElement = client.getGlossaryTermByGUID(userId, assetManagerGUID, assetManagerName, glossaryTermGUID);
            GlossaryTermProperties retrievedTable   = retrievedElement.getGlossaryTermProperties();

            if (retrievedTable == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryTerm from Retrieve)");
            }

            if (! glossaryTermName.equals(retrievedTable.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! glossaryTermDisplayName.equals(retrievedTable.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! glossaryTermDescription.equals(retrievedTable.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }

            List<GlossaryTermElement> glossaryTermList = client.getGlossaryTermsByName(userId, assetManagerGUID, assetManagerName, glossaryTermName, 0, maxPageSize);

            if (glossaryTermList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GlossaryTerm for RetrieveByName)");
            }
            else if (glossaryTermList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty GlossaryTerm list for RetrieveByName)");
            }
            else if (glossaryTermList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(GlossaryTerm list for RetrieveByName contains" + glossaryTermList.size() +
                                                         " elements)");
            }

            retrievedElement = glossaryTermList.get(0);
            retrievedTable = retrievedElement.getGlossaryTermProperties();

            if (! glossaryTermName.equals(retrievedTable.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! glossaryTermDisplayName.equals(retrievedTable.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! glossaryTermDescription.equals(retrievedTable.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            return glossaryTermGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Throwable unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
}
