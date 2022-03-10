/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.tests.repository.connector.TestMetadataCollectionId;
import org.odpi.openmetadata.conformance.tests.repository.connector.TestRepositoryServerIds;
import org.odpi.openmetadata.conformance.tests.repository.instances.*;
import org.odpi.openmetadata.conformance.tests.repository.types.*;
import org.odpi.openmetadata.conformance.workbenches.OpenMetadataConformanceWorkbench;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RepositoryConformanceWorkbench provides the thread that drives the repository workbench from the Open Metadata
 * Conformance Suite.  The properties used to initialise the workbench are passed on the constructor.
 * The run() method contains the workbench logic.  It executes until the tests are complete, or it is signaled to
 * shutdown.
 */
public class RepositoryConformanceWorkbench extends OpenMetadataConformanceWorkbench
{
    private static final Logger log = LoggerFactory.getLogger(RepositoryConformanceWorkbench.class);

    private RepositoryConformanceWorkPad workPad;


    /**
     * Constructor receives the work pad with the configuration that provides the location to record the results.
     *
     * @param workPad repository workbench's work pad
     */
    public RepositoryConformanceWorkbench(RepositoryConformanceWorkPad workPad)
    {
        super(workPad.getWorkbenchId(),
              workPad.getWorkbenchName(),
              workPad.getWorkbenchVersionNumber(),
              workPad.getWorkbenchDocURL());

        /*
         * On construction of the workbench reset runningFlag to true.
         */
        runningFlag = true;

        final String methodName = "repositoryWorkbenchThread.constructor";

        this.workPad = workPad;

        OMRSAuditLog auditLog = workPad.getAuditLog();

        ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.WORKBENCH_INITIALIZING;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(workbenchId, workbenchDocumentationURL),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }


    /**
     * Run the registered test cases and return the accumulated results.  Notice that some test cases deliver
     * information that is used to generate more test cases.  So if early test cases fail then the
     * total number of test cases may appear lower than expected.
     */
    private void runTests()
    {
        OMRSRepositoryConnector repositoryConnector = workPad.getTutRepositoryConnector();

        if (repositoryConnector != null)
        {
            TestRepositoryServerIds testRepositoryServerIds = new TestRepositoryServerIds(workPad);
            testRepositoryServerIds.executeTest();

            TestMetadataCollectionId testMetadataCollectionId = new TestMetadataCollectionId(workPad);
            testMetadataCollectionId.executeTest();

            /*
             * Validate all of the type definitions
             */

            TestGetTypeDefGallery typeDefGalleryTestCase = new TestGetTypeDefGallery(workPad);

            typeDefGalleryTestCase.executeTest();

            List<TestSupportedAttributeTypeDef> attributeTypeDefTestCases = new ArrayList<>();
            List<TestSupportedTypeDef> typeDefTestCases = new ArrayList<>();

            List<AttributeTypeDef> attributeTypeDefs = typeDefGalleryTestCase.getAttributeTypeDefs();
            List<TypeDef> typeDefs = typeDefGalleryTestCase.getTypeDefs();

            if (attributeTypeDefs != null)
            {
                for (AttributeTypeDef attributeTypeDef : attributeTypeDefs)
                {
                    TestSupportedAttributeTypeDef testAttributeTypeDef = new TestSupportedAttributeTypeDef(workPad,
                                                                                                           attributeTypeDef,
                                                                                                           null,
                                                                                                           RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                                                                                           RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

                    attributeTypeDefTestCases.add(testAttributeTypeDef);
                }
            }

            if (typeDefs != null)
            {
                for (TypeDef typeDef : typeDefs)
                {
                    TestSupportedTypeDef testTypeDef = new TestSupportedTypeDef(workPad,
                                                                                typeDef,
                                                                                null,
                                                                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                                                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

                    typeDefTestCases.add(testTypeDef);
                }
            }

            for (TestSupportedAttributeTypeDef testCase : attributeTypeDefTestCases)
            {
                testCase.executeTest();
            }

            for (TestSupportedTypeDef testCase : typeDefTestCases)
            {
                testCase.executeTest();
            }

            /*
             * Retrieve the attribute type definitions by category.
             */
            TestFindAttributeTypeDefsByCategory
                    testFindAttributeTypeDefsByCategory = new TestFindAttributeTypeDefsByCategory(workPad, attributeTypeDefs);

            testFindAttributeTypeDefsByCategory.executeTest();

            /*
             * Retrieve the type definitions by category.
             */
            TestFindTypeDefsByCategory testFindTypeDefsByCategory = new TestFindTypeDefsByCategory(workPad, typeDefs);

            testFindTypeDefsByCategory.executeTest();


            /*
             * Lifecycle testcases
             */
            List<TestSupportedEntityLifecycle> entityTestCases = new ArrayList<>();
            List<TestSupportedRelationshipLifecycle> relationshipTestCases = new ArrayList<>();
            List<TestSupportedClassificationLifecycle> classificationTestCases = new ArrayList<>();
            /*
             * Reference Copy Lifecycle testcases
             */
            List<TestSupportedEntityReferenceCopyLifecycle> entityReferenceCopyTestCases = new ArrayList<>();
            List<TestSupportedRelationshipReferenceCopyLifecycle> relationshipReferenceCopyTestCases = new ArrayList<>();
            List<TestSupportedEntityProxyLifecycle> entityProxyTestCases = new ArrayList<>();
            List<TestSupportedReferenceCopyClassificationLifecycle> referenceCopyClassificationTestCases = new ArrayList<>();

            /*
             * Reidentification testcases
             */
            List<TestSupportedEntityReidentify> entityReidentifyTestCases = new ArrayList<>();
            List<TestSupportedRelationshipReidentify> relationshipReidentifyTestCases = new ArrayList<>();

            /*
             * Retype testcases
             * This currently only tests Entity Types - there is no testing (yet) of retyping of a relationship
             */
            List<TestSupportedEntityRetype> entityRetypeTestCases = new ArrayList<>();




            /*
             * Search testcases - these are multi-phase tests (create, execute, clean)
             */

            List<TestSupportedEntitySearch> entitySearchTestCases = new ArrayList<>();
            List<TestSupportedRelationshipSearch> relationshipSearchTestCases = new ArrayList<>();

            Map<String, EntityDef> entityDefs = testFindTypeDefsByCategory.getEntityDefs();
            List<RelationshipDef> relationshipDefs = testFindTypeDefsByCategory.getRelationshipDefs();
            List<ClassificationDef> classificationDefs = testFindTypeDefsByCategory.getClassificationDefs();

            /*
             * Resolve the entity inheritance tree to list all subtypes of each entity type.
             * This is useful during find method testcases, to compose expected result.
             * Construct a map of entity type name to list of names of subtypes of that entity type.
             * The map is held in the workpad.
             */

            if (entityDefs != null)
            {

                for (EntityDef entityDef : entityDefs.values())
                {

                    String entityTypeName = entityDef.getName();
                    /*
                     * For this entity type - find ALL its supertypes and add the entity type name to their subtype map entries.
                     */
                    OMRSRepositoryConnector cohortRepositoryConnector = null;
                    OMRSRepositoryHelper repositoryHelper = null;
                    if (workPad != null)
                    {
                        cohortRepositoryConnector = workPad.getTutRepositoryConnector();
                        repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
                    }

                    /*
                     * All entity defs are read from the known types (using the repository helper), not from the gallery returned by
                     * the repository
                     */
                    EntityDef knownDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), entityTypeName);

                    TypeDefLink superType = knownDef.getSuperType();

                    while (superType != null)
                    {

                        String superTypeName = superType.getName();
                        // Add current type (name) to subtype map for superType
                        workPad.addEntitySubType(superTypeName, entityTypeName);
                        knownDef = (EntityDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), superTypeName);
                        superType = knownDef.getSuperType();

                    }
                }
            }

            /*
             * Resolve the relationship type into a map from relationship type (name) to the pair of entity types (names)
             * and a corresponding reverse map from entity type to relationship types.
             * These maps are useful during graph query testcases, to compose a supported graph.
             * The maps are held in the workpad.
             */

            if (relationshipDefs != null)
            {

                for (RelationshipDef relationshipDef : relationshipDefs)
                {

                    String relationshipTypeName = relationshipDef.getName();
                    /*
                     * For this relationship type - find both the end types and add the entity type names to the map.
                     * No check is made that the repository supports the entity types - this is part of the test.
                     */
                    String entityOneTypeName = relationshipDef.getEndDef1().getEntityType().getName();
                    String entityTwoTypeName = relationshipDef.getEndDef2().getEntityType().getName();
                    workPad.addRelationshipEndTypes(relationshipTypeName, entityOneTypeName, entityTwoTypeName);
                    workPad.addEntityRelationshipType(entityOneTypeName, relationshipTypeName, 1);
                    workPad.addEntityRelationshipType(entityTwoTypeName, relationshipTypeName, 2);

                    /*
                     * For this relationship type - find ALL its supertypes and add the relationship type name to their subtype map entries.
                     */
                    OMRSRepositoryConnector cohortRepositoryConnector = null;
                    OMRSRepositoryHelper repositoryHelper = null;
                    if (workPad != null)
                    {
                        cohortRepositoryConnector = workPad.getTutRepositoryConnector();
                        repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
                    }

                    /*
                     * All relationship defs are read from the known types (using the repository helper), not from the gallery returned by
                     * the repository
                     */
                    RelationshipDef knownDef = (RelationshipDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), relationshipTypeName);

                    TypeDefLink superType = knownDef.getSuperType();

                    while (superType != null)
                    {

                        String superTypeName = superType.getName();
                        // Add current type (name) to subtype map for superType
                        workPad.addRelationshipSubType(superTypeName, relationshipTypeName);
                        knownDef = (RelationshipDef) repositoryHelper.getTypeDefByName(workPad.getLocalServerUserId(), superTypeName);
                        superType = knownDef.getSuperType();

                    }

                }

            }



            /*
             * Build the test cases for the entities, relationships and classifications
             */

            if (entityDefs != null)
            {

                for (EntityDef entityDef : entityDefs.values())
                {

                    TestSupportedEntityLifecycle testEntityLifecycle = new TestSupportedEntityLifecycle(workPad, entityDef);
                    entityTestCases.add(testEntityLifecycle);

                    TestSupportedEntityReferenceCopyLifecycle testEntityReferenceCopyLifecycle = new TestSupportedEntityReferenceCopyLifecycle(workPad, entityDef);
                    entityReferenceCopyTestCases.add(testEntityReferenceCopyLifecycle);

                    TestSupportedEntityReidentify testEntityReidentify = new TestSupportedEntityReidentify(workPad, entityDef);
                    entityReidentifyTestCases.add(testEntityReidentify);

                    TestSupportedEntityRetype testEntityRetype = new TestSupportedEntityRetype(workPad, entityDef);
                    entityRetypeTestCases.add(testEntityRetype);

                    TestSupportedEntitySearch testEntitySearch = new TestSupportedEntitySearch(workPad, entityDef);
                    entitySearchTestCases.add(testEntitySearch);
                }

            }


            if (relationshipDefs != null)
            {

                for (RelationshipDef relationshipDef : relationshipDefs)
                {

                    TestSupportedRelationshipLifecycle testRelationshipLifecycle = new TestSupportedRelationshipLifecycle(workPad, entityDefs, relationshipDef);
                    relationshipTestCases.add(testRelationshipLifecycle);

                    /* It might seem like entity proxy tests should be incarnated in the entity test loop above - but we want a relationship def to test that a proxy can be used */
                    TestSupportedEntityProxyLifecycle testEntityProxyLifecycle = new TestSupportedEntityProxyLifecycle(workPad, entityDefs, relationshipDef);
                    entityProxyTestCases.add(testEntityProxyLifecycle);

                    TestSupportedRelationshipReferenceCopyLifecycle testRelationshipReferenceCopyLifecycle = new TestSupportedRelationshipReferenceCopyLifecycle(workPad, entityDefs, relationshipDef);
                    relationshipReferenceCopyTestCases.add(testRelationshipReferenceCopyLifecycle);

                    TestSupportedRelationshipReidentify testRelationshipReidentify = new TestSupportedRelationshipReidentify(workPad, entityDefs, relationshipDef);
                    relationshipReidentifyTestCases.add(testRelationshipReidentify);

                    TestSupportedRelationshipSearch testRelationshipSearch = new TestSupportedRelationshipSearch(workPad, entityDefs, relationshipDef);
                    relationshipSearchTestCases.add(testRelationshipSearch);


                }
            }


            if (classificationDefs != null)
            {

                for (ClassificationDef classificationDef : classificationDefs)
                {

                    TestClassificationHasSupportedEntities testClassificationHasSupportedEntities = new TestClassificationHasSupportedEntities(workPad, entityDefs, classificationDef);

                    testClassificationHasSupportedEntities.executeTest();

                    List<EntityDef> supportedEntitiesForClassification = testClassificationHasSupportedEntities.getSupportedEntityDefsForClassification();

                    if (supportedEntitiesForClassification != null)
                    {
                        for (EntityDef entityDef : supportedEntitiesForClassification)
                        {
                            TestSupportedClassificationLifecycle testClassificationLifecycle =
                                    new TestSupportedClassificationLifecycle(workPad, entityDef, classificationDef);

                            classificationTestCases.add(testClassificationLifecycle);

                            TestSupportedReferenceCopyClassificationLifecycle testReferenceCopyClassificationLifecycle =
                                    new TestSupportedReferenceCopyClassificationLifecycle(workPad, entityDef, classificationDef);

                            referenceCopyClassificationTestCases.add(testReferenceCopyClassificationLifecycle);
                        }
                    }
                }

            }





            /*
             * Validate all of the entities, relationships and classifications
             */

            for (TestSupportedEntityLifecycle testCase : entityTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }

            for (TestSupportedRelationshipLifecycle testCase : relationshipTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }

            for (TestSupportedClassificationLifecycle testCase : classificationTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }


            /*
             * Validate all of the entity and relationship reference copies
             */
            for (TestSupportedEntityReferenceCopyLifecycle testCase : entityReferenceCopyTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }

            for (TestSupportedRelationshipReferenceCopyLifecycle testCase : relationshipReferenceCopyTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }

            for (TestSupportedReferenceCopyClassificationLifecycle testCase : referenceCopyClassificationTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }


            /*
             * Validate all the entity proxies
             */
            for (TestSupportedEntityProxyLifecycle testCase : entityProxyTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }


            /*
             * Validate all of the entity and relationship reidentify operations
             */
            for (TestSupportedEntityReidentify testCase : entityReidentifyTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }

            for (TestSupportedRelationshipReidentify testCase : relationshipReidentifyTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }

            /*
             * Validate all of the entity retype operations
             */
            for (TestSupportedEntityRetype testCase : entityRetypeTestCases)
            {
                testCase.executeTest();
                testCase.cleanTest();
            }



            /*
             * Validate all of the entity and relationship searches
             */

            /*
             * Phase 1
             */
            for (TestSupportedEntitySearch testCase : entitySearchTestCases)
            {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.SEED);
            }
            /*
             * Phase 2
             */
            for (TestSupportedEntitySearch testCase : entitySearchTestCases)
            {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.EXECUTE);
            }
            /*
             * Phase 3
             */
            for (TestSupportedEntitySearch testCase : entitySearchTestCases)
            {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CLEAN);
            }


            /*
             * Phase 1
             */
            for (TestSupportedRelationshipSearch testCase : relationshipSearchTestCases)
            {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.SEED);
            }
            /*
             * Phase 2
             */
            for (TestSupportedRelationshipSearch testCase : relationshipSearchTestCases)
            {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.EXECUTE);
            }
            /*
             * Phase 3
             */
            for (TestSupportedRelationshipSearch testCase : relationshipSearchTestCases)
            {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CLEAN);
            }


            /*
             * Retrieve the type definitions by external standard mappings
             */
            TestFindTypeDefByExternalId testFindTypeDefByExternalId = new TestFindTypeDefByExternalId(workPad, typeDefs);
            testFindTypeDefByExternalId.executeTest();


            /*
             * Perform graph query tests on a set of types that the repository under test supports.
             * Start with the set of supported relationships and explores to create a graph from types that the repository supports.
             */
            if (relationshipDefs != null)
            {
                TestGraphQueries testGraphQueries = new TestGraphQueries(workPad, relationshipDefs, entityDefs);
                testGraphQueries.executeTest();
            }

        }

    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        final String methodName = "repositoryWorkbenchThread.run";

        if (workPad != null)
        {
            long retryCount = 0;
            OMRSAuditLog auditLog = workPad.getAuditLog();
            ConformanceSuiteAuditCode auditCode;

            auditCode = ConformanceSuiteAuditCode.WORKBENCH_INITIALIZED;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(workPad.getWorkbenchId()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            while (super.isRunning() && (workPad.getTutRepositoryConnector() == null))
            {
                try
                {
                    /*
                     * Wait for server to connect to the cohort
                     */
                    if (retryCount == 100)
                    {
                        retryCount = 0;
                    }
                    else
                    {
                        if (retryCount == 0)
                        {
                            auditCode = ConformanceSuiteAuditCode.WORKBENCH_WAITING_TO_START;
                            auditLog.logRecord(methodName,
                                               auditCode.getLogMessageId(),
                                               auditCode.getSeverity(),
                                               auditCode.getFormattedLogMessage(workPad.getWorkbenchId(),
                                                                                workPad.getTutServerName()),
                                               null,
                                               auditCode.getSystemAction(),
                                               auditCode.getUserAction());
                        }
                        retryCount++;
                    }
                    Thread.sleep(1000);
                }
                catch (InterruptedException wakeUp)
                {
                    /*
                     * Test again.
                     */
                }
                catch (Throwable error)
                {
                    stopRunning();
                    log.error(String.format("Unexpected error: %s", error.getMessage()), error);

                    auditCode = ConformanceSuiteAuditCode.WORKBENCH_FAILURE;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(workPad.getWorkbenchId(),
                                                                        error.getMessage()),
                                       error.toString(),
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());
                }
            }

            if (workPad.getTutRepositoryConnector() != null)
            {
                runTests();
            }

            workPad.setWorkbenchComplete();

            auditCode = ConformanceSuiteAuditCode.WORKBENCH_SYNC_COMPLETED;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(workPad.getWorkbenchId()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }
}
