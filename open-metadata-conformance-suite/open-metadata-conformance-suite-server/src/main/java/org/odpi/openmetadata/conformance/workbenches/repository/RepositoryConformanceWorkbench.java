/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.tests.repository.connector.TestMetadataCollectionId;
import org.odpi.openmetadata.conformance.tests.repository.connector.TestRepositoryServerIds;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestClassificationHasSupportedEntities;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedClassificationLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedEntityLifecycle;
import org.odpi.openmetadata.conformance.tests.repository.instances.TestSupportedRelationshipLifecycle;
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
            List<TestSupportedTypeDef>          typeDefTestCases          = new ArrayList<>();
            List<AttributeTypeDef>              attributeTypeDefs         = typeDefGalleryTestCase.getAttributeTypeDefs();
            List<TypeDef>                       typeDefs                  = typeDefGalleryTestCase.getTypeDefs();

            if (attributeTypeDefs != null)
            {
                for (AttributeTypeDef  attributeTypeDef : attributeTypeDefs)
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
                for (TypeDef  typeDef : typeDefs)
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


            List<TestSupportedEntityLifecycle>           entityTestCases                 = new ArrayList<>();
            List<TestSupportedRelationshipLifecycle>     relationshipTestCases           = new ArrayList<>();
            List<TestClassificationHasSupportedEntities> classificationEntitiesTestCases = new ArrayList<>();
            List<TestSupportedClassificationLifecycle>   classificationTestCases         = new ArrayList<>();

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
                    TestSupportedEntityLifecycle testEntityLifecycle = new TestSupportedEntityLifecycle(workPad, entityDef);

                    entityTestCases.add(testEntityLifecycle);
                }
            }

            if (relationshipDefs != null)
            {
                for (RelationshipDef  relationshipDef : relationshipDefs)
                {
                    TestSupportedRelationshipLifecycle testRelationshipLifecycle = new TestSupportedRelationshipLifecycle(workPad, entityDefs, relationshipDef);

                    relationshipTestCases.add(testRelationshipLifecycle);
                }
            }

            if (classificationDefs != null)
            {
                for (ClassificationDef  classificationDef : classificationDefs)
                {
                    TestClassificationHasSupportedEntities testClassificationHasSupportedEntities = new TestClassificationHasSupportedEntities(workPad, entityDefs, classificationDef);

                    testClassificationHasSupportedEntities.executeTest();

                    List<EntityDef>  supportedEntitiesForClassification = testClassificationHasSupportedEntities.getSupportedEntityDefsForClassification();

                    if (supportedEntitiesForClassification != null)
                    {
                        for (EntityDef entityDef : supportedEntitiesForClassification)
                        {
                            TestSupportedClassificationLifecycle testClassificationLifecycle =
                                    new TestSupportedClassificationLifecycle(workPad, entityDef, classificationDef);

                            classificationTestCases.add(testClassificationLifecycle);
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
            }

            for (TestSupportedRelationshipLifecycle testCase : relationshipTestCases)
            {
                testCase.executeTest();
            }

            for (TestSupportedClassificationLifecycle testCase : classificationTestCases)
            {
                testCase.executeTest();
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
                catch(InterruptedException  wakeUp)
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
