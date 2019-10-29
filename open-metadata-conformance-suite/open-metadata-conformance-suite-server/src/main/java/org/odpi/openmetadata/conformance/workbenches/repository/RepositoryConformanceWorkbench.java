/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.tests.repository.connector.TestMetadataCollectionId;
import org.odpi.openmetadata.conformance.tests.repository.connector.TestRepositoryServerIds;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestClassificationHasSupportedEntities;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedClassificationLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedEntityLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedEntityPropertyAdvancedSearch;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedEntityPropertySearch;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedEntityReferenceCopyLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedEntityReidentify;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedReferenceCopyClassificationLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedRelationshipLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedRelationshipPropertyAdvancedSearch;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedRelationshipPropertySearch;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedRelationshipReferenceCopyLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedRelationshipReidentify;
import org.odpi.openmetadata.conformance.tests.repository.types.*;
import org.odpi.openmetadata.conformance.workbenches.OpenMetadataConformanceWorkbench;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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
    private static final Logger     log         = LoggerFactory.getLogger(RepositoryConformanceWorkbench.class);

    private RepositoryConformanceWorkPad   workPad;




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

        final String              methodName = "repositoryWorkbenchThread.constructor";

        this.workPad = workPad;

        OMRSAuditLog              auditLog   = workPad.getAuditLog();

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

        if (repositoryConnector != null) {
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
            List<TestSupportedTypeDef>          typeDefTestCases          = new ArrayList<>();

            List<AttributeTypeDef> attributeTypeDefs = typeDefGalleryTestCase.getAttributeTypeDefs();
            List<TypeDef> typeDefs = typeDefGalleryTestCase.getTypeDefs();

            if (attributeTypeDefs != null) {
                for (AttributeTypeDef attributeTypeDef : attributeTypeDefs) {
                    TestSupportedAttributeTypeDef testAttributeTypeDef = new TestSupportedAttributeTypeDef(workPad,
                            attributeTypeDef,
                            null,
                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

                    attributeTypeDefTestCases.add(testAttributeTypeDef);
                }
            }

            if (typeDefs != null) {
                for (TypeDef typeDef : typeDefs) {
                    TestSupportedTypeDef testTypeDef = new TestSupportedTypeDef(workPad,
                            typeDef,
                            null,
                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

                    typeDefTestCases.add(testTypeDef);
                }
            }

            for (TestSupportedAttributeTypeDef testCase : attributeTypeDefTestCases) {
                testCase.executeTest();
            }

            for (TestSupportedTypeDef testCase : typeDefTestCases) {
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
            List<TestSupportedEntityLifecycle>         entityTestCases         = new ArrayList<>();
            List<TestSupportedRelationshipLifecycle>   relationshipTestCases   = new ArrayList<>();
            List<TestSupportedClassificationLifecycle> classificationTestCases = new ArrayList<>();
            /*
             * Reference Copy Lifecycle testcases
             */
            List<TestSupportedEntityReferenceCopyLifecycle>         entityReferenceCopyTestCases         = new ArrayList<>();
            List<TestSupportedRelationshipReferenceCopyLifecycle>   relationshipReferenceCopyTestCases   = new ArrayList<>();
            List<TestSupportedReferenceCopyClassificationLifecycle> referenceCopyClassificationTestCases = new ArrayList<>();
            /*
             * Reidentification testcases
             */
            List<TestSupportedEntityReidentify>       entityReidentifyTestCases       = new ArrayList<>();
            List<TestSupportedRelationshipReidentify> relationshipReidentifyTestCases = new ArrayList<>();

            /*
             * Search testcases - these are multi-phase tests (create, execute, clean)
             */
            List<TestSupportedEntityPropertySearch>         entityPropertySearchTestCases         = new ArrayList<>();
            List<TestSupportedEntityPropertyAdvancedSearch> entityPropertyAdvancedSearchTestCases = new ArrayList<>();

            List<TestSupportedRelationshipPropertySearch>         relationshipPropertySearchTestCases         = new ArrayList<>();
            List<TestSupportedRelationshipPropertyAdvancedSearch> relationshipPropertyAdvancedSearchTestCases = new ArrayList<>();


            Map<String, EntityDef>  entityDefs         = testFindTypeDefsByCategory.getEntityDefs();
            List<RelationshipDef>   relationshipDefs   = testFindTypeDefsByCategory.getRelationshipDefs();
            List<ClassificationDef> classificationDefs = testFindTypeDefsByCategory.getClassificationDefs();

            /*
             * Resolve the entity inheritance tree to list all subtypes of each entity type.
             * This is useful during find method testcases, to compose expected result.
             * Construct a map of entity type name to list of names of subtypes of that entity type.
             * The map is held in the workpad.
             */

            if (entityDefs != null) {

                for (EntityDef entityDef : entityDefs.values()) {

                    String entityTypeName = entityDef.getName();
                    /*
                     * For this entity type - find ALL its supertypes and add the entity type name to their subtype map entries.
                     */
                    TypeDefLink superType = entityDef.getSuperType();
                    while (superType != null) {

                        String superTypeName = superType.getName();
                        // Add current type (name) to subtype map for superType
                        workPad.addEntitySubType(superTypeName, entityTypeName);

                        superType = entityDefs.get(superTypeName).getSuperType();
                    }
                }
            }

            /*
             * Build the test cases for the entities, relationships and classifications
             */

            if (entityDefs != null) {

                for (EntityDef entityDef : entityDefs.values()) {

                    TestSupportedEntityLifecycle testEntityLifecycle = new TestSupportedEntityLifecycle(workPad, entityDef);

                    entityTestCases.add(testEntityLifecycle);

                    TestSupportedEntityReferenceCopyLifecycle testEntityReferenceCopyLifecycle = new TestSupportedEntityReferenceCopyLifecycle(workPad, entityDef);
                    entityReferenceCopyTestCases.add(testEntityReferenceCopyLifecycle);

                    TestSupportedEntityReidentify testEntityReidentify = new TestSupportedEntityReidentify(workPad, entityDef);
                    entityReidentifyTestCases.add(testEntityReidentify);

                    TestSupportedEntityPropertySearch testEntityPropertySearch = new TestSupportedEntityPropertySearch(workPad, entityDef);
                    entityPropertySearchTestCases.add(testEntityPropertySearch);

                    TestSupportedEntityPropertyAdvancedSearch testEntityPropertyAdvancedSearch = new TestSupportedEntityPropertyAdvancedSearch(workPad, entityDef);
                    entityPropertyAdvancedSearchTestCases.add(testEntityPropertyAdvancedSearch);

                }
            }


            if (relationshipDefs != null) {

                for (RelationshipDef relationshipDef : relationshipDefs) {

                    TestSupportedRelationshipLifecycle testRelationshipLifecycle = new TestSupportedRelationshipLifecycle(workPad, entityDefs, relationshipDef);
                    relationshipTestCases.add(testRelationshipLifecycle);

                    TestSupportedRelationshipReferenceCopyLifecycle testRelationshipReferenceCopyLifecycle = new TestSupportedRelationshipReferenceCopyLifecycle(workPad, relationshipDef);
                    relationshipReferenceCopyTestCases.add(testRelationshipReferenceCopyLifecycle);

                    TestSupportedRelationshipReidentify testRelationshipReidentify = new TestSupportedRelationshipReidentify(workPad, relationshipDef);
                    relationshipReidentifyTestCases.add(testRelationshipReidentify);

                    TestSupportedRelationshipPropertySearch testRelationshipPropertySearch = new TestSupportedRelationshipPropertySearch(workPad, entityDefs, relationshipDef);
                    relationshipPropertySearchTestCases.add(testRelationshipPropertySearch);

                    TestSupportedRelationshipPropertyAdvancedSearch testRelationshipPropertyAdvancedSearch = new TestSupportedRelationshipPropertyAdvancedSearch(workPad, entityDefs, relationshipDef);
                    relationshipPropertyAdvancedSearchTestCases.add(testRelationshipPropertyAdvancedSearch);

                }
            }


            if (classificationDefs != null) {

                for (ClassificationDef classificationDef : classificationDefs) {

                    TestClassificationHasSupportedEntities testClassificationHasSupportedEntities = new TestClassificationHasSupportedEntities(workPad, entityDefs, classificationDef);

                    testClassificationHasSupportedEntities.executeTest();

                    List<EntityDef> supportedEntitiesForClassification = testClassificationHasSupportedEntities.getSupportedEntityDefsForClassification();

                    if (supportedEntitiesForClassification != null) {
                        for (EntityDef entityDef : supportedEntitiesForClassification) {
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
            for (TestSupportedEntityLifecycle testCase : entityTestCases) {
                testCase.executeTest();
            }

            for (TestSupportedRelationshipLifecycle testCase : relationshipTestCases) {
                testCase.executeTest();
            }

            for (TestSupportedClassificationLifecycle testCase : classificationTestCases) {
                testCase.executeTest();
            }


            /*
             * Validate all of the entity and relationship reference copies
             */
            for (TestSupportedEntityReferenceCopyLifecycle testCase : entityReferenceCopyTestCases) {
                testCase.executeTest();
            }

            for (TestSupportedRelationshipReferenceCopyLifecycle testCase : relationshipReferenceCopyTestCases) {
                testCase.executeTest();
            }

            for (TestSupportedReferenceCopyClassificationLifecycle testCase : referenceCopyClassificationTestCases) {
                testCase.executeTest();
            }


            /*
             * Validate all of the entity and relationship reidentify operations
             */
            for (TestSupportedEntityReidentify testCase : entityReidentifyTestCases) {
                testCase.executeTest();
            }

            for (TestSupportedRelationshipReidentify testCase : relationshipReidentifyTestCases) {
                testCase.executeTest();
            }


            /*
             * Validate all of the entity and relationship property searches
             */

            /*
             * Phase 1
             */
            for (TestSupportedEntityPropertySearch testCase : entityPropertySearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CREATE);
            }
            /*
             * Phase 2
             */
            for (TestSupportedEntityPropertySearch testCase : entityPropertySearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.EXECUTE);
            }
            /*
             * Phase 3
             */
            for (TestSupportedEntityPropertySearch testCase : entityPropertySearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CLEAN);
            }

            /*
             * Phase 1
             */
            for (TestSupportedRelationshipPropertySearch testCase : relationshipPropertySearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CREATE);
            }
            /*
             * Phase 2
             */
            for (TestSupportedRelationshipPropertySearch testCase : relationshipPropertySearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.EXECUTE);
            }
            /*
             * Phase 3
             */
            for (TestSupportedRelationshipPropertySearch testCase : relationshipPropertySearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CLEAN);
            }


            /*
             * Validate all of the entity and relationship property advanced searches
             */

            /*
             * Phase 1
             */
            for (TestSupportedEntityPropertyAdvancedSearch testCase : entityPropertyAdvancedSearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CREATE);
            }
            /*
             * Phase 2
             */
            for (TestSupportedEntityPropertyAdvancedSearch testCase : entityPropertyAdvancedSearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.EXECUTE);
            }
            /*
             * Phase 3
             */
            for (TestSupportedEntityPropertyAdvancedSearch testCase : entityPropertyAdvancedSearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CLEAN);
            }

            /*
             * Phase 1
             */
            for (TestSupportedRelationshipPropertyAdvancedSearch testCase : relationshipPropertyAdvancedSearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CREATE);
            }
            /*
             * Phase 2
             */
            for (TestSupportedRelationshipPropertyAdvancedSearch testCase : relationshipPropertyAdvancedSearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.EXECUTE);
            }
            /*
             * Phase 3
             */
            for (TestSupportedRelationshipPropertyAdvancedSearch testCase : relationshipPropertyAdvancedSearchTestCases) {
                testCase.executeTest(OpenMetadataTestCase.TestPhase.CLEAN);
            }

            /*
             * Retrieve the type definitions by external standard mappings
             */
            TestFindTypeDefByExternalId testFindTypeDefByExternalId = new TestFindTypeDefByExternalId(workPad, typeDefs);

            testFindTypeDefByExternalId.executeTest();
        }
    }



    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        final String              methodName = "repositoryWorkbenchThread.run";

        if (workPad !=  null)
        {
            long                      retryCount = 0;
            OMRSAuditLog              auditLog   = workPad.getAuditLog();
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
                catch (InterruptedException  wakeUp)
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
                                       auditCode.getUserAction());                }
            }

            if (workPad.getTutRepositoryConnector() != null)
            {
                runTests();
            }

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
