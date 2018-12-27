/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.conformance.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.OpenMetadataTestWorkbench;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCaseResult;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCaseSummary;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestWorkbenchResults;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataRepositoryTestWorkbench provides the workbench for testing the OMRS REST API.
 * It operates as a test pipeline, accumulating information about the repository's capabilities
 * and passing them on to subsequent tests.
 */
public class OpenMetadataRepositoryTestWorkbench extends OpenMetadataTestWorkbench
{
    private static final String workbenchId            = "repository-workbench";
    private static final String workbenchName          = "Open Metadata Repository Test Workbench";
    private static final String workbenchVersionNumber = "V0.1 SNAPSHOT";
    private static final String workbenchDocURL        = "https://odpi.github.io/egeria/open-metadata-conformance-suite/docs/" + workbenchId;

    private static final String dummyMetadataCollectionId = "dummyMetadataCollectionId-workbench";

    /*
     * This value is retrieved from the server.  If the workbench can not get the metadata collection Id then
     * all other tests are skipped.
     */
    private String  metadataCollectionId = null;

    /**
     * Constructor received the URL root for the server being tested.
     *
     * @param serverName name of server to test.
     * @param serverURLRoot string
     */
    public OpenMetadataRepositoryTestWorkbench(String  serverName,
                                               String  serverURLRoot)
    {
        super(workbenchName, workbenchVersionNumber, workbenchDocURL, serverName, serverURLRoot);
    }



    /**
     * Create a connector to the repository.
     *
     * @param metadataCollectionId identifier for the metadata collection
     * @return OMRSRepositoryConnector
     */
    private OMRSRepositoryConnector   getRepositoryConnector(String metadataCollectionId)
    {
        try
        {
            ConnectorConfigurationFactory factory = new ConnectorConfigurationFactory();

            Connection connection = factory.getDefaultLocalRepositoryRemoteConnection(serverName,
                                                                                      serverURLRoot);

            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMRSRepositoryConnector  repositoryConnector = (OMRSRepositoryConnector)connector;

            repositoryConnector.setMetadataCollectionId(metadataCollectionId);
            repositoryConnector.start();

            return repositoryConnector;
        }
        catch (Throwable  exc)
        {
            System.out.println("Unable to create connector " + exc.getMessage());
            return null;
        }
    }


    /**
     * Run the registered test cases and return the accumulated results.  Notice that some test cases deliver
     * information that is used to generate more test cases.  So if early test cases fail then the
     * total number of test cases may appear lower than expected.
     *
     * @return OpenMetadataWorkbenchResults bean
     */
    public OpenMetadataTestWorkbenchResults runTests()
    {
        List<OpenMetadataRepositoryTestCase>  testCases = new ArrayList<>();
        OpenMetadataTestWorkbenchResults      workbenchResults = new OpenMetadataTestWorkbenchResults(this);

        OMRSRepositoryConnector  repositoryConnector = this.getRepositoryConnector(dummyMetadataCollectionId);

        if (repositoryConnector != null)
        {
            TestMetadataCollectionId testCase = new TestMetadataCollectionId(workbenchId);
            testCases.add(testCase);

            testCase.setConnector(repositoryConnector);
            testCase.executeTest();

            metadataCollectionId = testCase.getMetadataCollectionId();
        }

        if (metadataCollectionId != null)
        {
            repositoryConnector = this.getRepositoryConnector(metadataCollectionId);

            /*
             * Validate all of the type definitions
             */

            TestGetTypeDefGallery typeDefGalleryTestCase = new TestGetTypeDefGallery(workbenchId);
            testCases.add(typeDefGalleryTestCase);

            typeDefGalleryTestCase.setConnector(repositoryConnector);
            typeDefGalleryTestCase.executeTest();

            List<TestSupportedAttributeTypeDef> attributeTypeDefTestCases = new ArrayList<>();
            List<TestSupportedTypeDef>          typeDefTestCases          = new ArrayList<>();
            List<AttributeTypeDef>              attributeTypeDefs         = typeDefGalleryTestCase.getAttributeTypeDefs();
            List<TypeDef>                       typeDefs                  = typeDefGalleryTestCase.getTypeDefs();

            if (attributeTypeDefs != null)
            {
                for (AttributeTypeDef  attributeTypeDef : attributeTypeDefs)
                {
                    TestSupportedAttributeTypeDef testAttributeTypeDef = new TestSupportedAttributeTypeDef(workbenchId, attributeTypeDef);

                    testCases.add(testAttributeTypeDef);
                    attributeTypeDefTestCases.add(testAttributeTypeDef);
                }
            }

            if (typeDefs != null)
            {
                for (TypeDef  typeDef : typeDefs)
                {
                    TestSupportedTypeDef testTypeDef = new TestSupportedTypeDef(workbenchId, typeDef);

                    testCases.add(testTypeDef);
                    typeDefTestCases.add(testTypeDef);
                }
            }

            for (TestSupportedAttributeTypeDef testCase : attributeTypeDefTestCases)
            {
                testCase.setConnector(repositoryConnector);
                testCase.executeTest();
            }

            for (TestSupportedTypeDef testCase : typeDefTestCases)
            {
                testCase.setConnector(repositoryConnector);
                testCase.executeTest();
            }

            /*
             * Retrieve the attribute type definitions by category.
             */
            TestFindAttributeTypeDefsByCategory  testFindAttributeTypeDefsByCategory = new TestFindAttributeTypeDefsByCategory(workbenchId, attributeTypeDefs);
            testCases.add(testFindAttributeTypeDefsByCategory);

            testFindAttributeTypeDefsByCategory.setConnector(repositoryConnector);
            testFindAttributeTypeDefsByCategory.executeTest();

            /*
             * Retrieve the type definitions by category.
             */
            TestFindTypeDefsByCategory  testFindTypeDefsByCategory = new TestFindTypeDefsByCategory(workbenchId, typeDefs);
            testCases.add(testFindTypeDefsByCategory);

            testFindTypeDefsByCategory.setConnector(repositoryConnector);
            testFindTypeDefsByCategory.executeTest();


            List<TestSupportedEntityLifecycle>         entityTestCases         = new ArrayList<>();
            List<TestSupportedRelationshipLifecycle>   relationshipTestCases   = new ArrayList<>();
            List<TestSupportedClassificationLifecycle> classificationTestCases = new ArrayList<>();

            Map<String, EntityDef>  entityDefs         = testFindTypeDefsByCategory.getEntityDefs();
            List<RelationshipDef>   relationshipDefs   = testFindTypeDefsByCategory.getRelationshipDefs();
            List<ClassificationDef> classificationDefs = testFindTypeDefsByCategory.getClassificationDefs();

            /*
             * Build the test cases for the entities, relationships and classifications
             */
            if (entityDefs != null)
            {
                for (EntityDef  entityDef : entityDefs.values())
                {
                    TestSupportedEntityLifecycle testEntityLifecycle = new TestSupportedEntityLifecycle(workbenchId, metadataCollectionId, entityDef);

                    testCases.add(testEntityLifecycle);
                    entityTestCases.add(testEntityLifecycle);
                }
            }

            if (relationshipDefs != null)
            {
                for (RelationshipDef  relationshipDef : relationshipDefs)
                {
                    TestSupportedRelationshipLifecycle testRelationshipLifecycle = new TestSupportedRelationshipLifecycle(workbenchId, metadataCollectionId, entityDefs, relationshipDef);

                    testCases.add(testRelationshipLifecycle);
                    relationshipTestCases.add(testRelationshipLifecycle);
                }
            }

            if (classificationDefs != null)
            {
                for (ClassificationDef  classificationDef : classificationDefs)
                {
                    TestSupportedClassificationLifecycle testClassificationLifecycle = new TestSupportedClassificationLifecycle(workbenchId, metadataCollectionId, entityDefs, classificationDef);

                    testCases.add(testClassificationLifecycle);
                    classificationTestCases.add(testClassificationLifecycle);
                }
            }

            /*
             * Validate all of the entities, relationships and classifications
             */
            for (TestSupportedEntityLifecycle testCase : entityTestCases)
            {
                testCase.setConnector(repositoryConnector);
                testCase.executeTest();
            }

            for (TestSupportedRelationshipLifecycle testCase : relationshipTestCases)
            {
                testCase.setConnector(repositoryConnector);
                testCase.executeTest();
            }

            for (TestSupportedClassificationLifecycle testCase : classificationTestCases)
            {
                testCase.setConnector(repositoryConnector);
                testCase.executeTest();
            }

            /*
             * Retrieve the type definitions by external standard mappings
             */
            TestFindTypeDefByExternalId testFindTypeDefByExternalId = new TestFindTypeDefByExternalId(workbenchId, typeDefs);

            testCases.add(testFindTypeDefByExternalId);
            testFindTypeDefByExternalId.setConnector(repositoryConnector);
            testFindTypeDefByExternalId.executeTest();
        }


        /*
         * Work through the test cases and extract the results
         */
        List<OpenMetadataTestCaseResult>  passedTestCases  = new ArrayList<>();
        List<OpenMetadataTestCaseResult>  failedTestCases  = new ArrayList<>();
        List<OpenMetadataTestCaseSummary> skippedTestCases = new ArrayList<>();

        /*
         * Executing tests only if there is a repository connector
         */
        for (OpenMetadataTestCase testCase : testCases)
        {
            if (testCase.isTestRan())
            {
                if (testCase.isTestPassed())
                {
                    passedTestCases.add(testCase.getResult());
                }
                else
                {
                    failedTestCases.add(testCase.getResult());
                }
            }
            else
            {
                skippedTestCases.add(testCase.getSummary());
            }
        }

        if (! passedTestCases.isEmpty())
        {
            workbenchResults.setPassedTestCases(passedTestCases);
        }
        if (! failedTestCases.isEmpty())
        {
            workbenchResults.setFailedTestCases(failedTestCases);
        }
        if (! skippedTestCases.isEmpty())
        {
            workbenchResults.setSkippedTestCases(skippedTestCases);
        }


        return workbenchResults;
    }
}
