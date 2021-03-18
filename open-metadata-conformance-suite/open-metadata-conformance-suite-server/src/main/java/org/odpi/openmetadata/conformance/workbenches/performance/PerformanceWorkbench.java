/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.performance;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.tests.performance.classify.TestEntityClassification;
import org.odpi.openmetadata.conformance.tests.performance.create.TestEntityCreation;
import org.odpi.openmetadata.conformance.tests.performance.create.TestRelationshipCreation;
import org.odpi.openmetadata.conformance.tests.performance.environment.TestEnvironment;
import org.odpi.openmetadata.conformance.tests.performance.search.TestClassificationSearch;
import org.odpi.openmetadata.conformance.tests.performance.search.TestEntitySearch;
import org.odpi.openmetadata.conformance.tests.performance.search.TestRelationshipSearch;
import org.odpi.openmetadata.conformance.workbenches.OpenMetadataConformanceWorkbench;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * PerformanceWorkbench provides the thread that drives the performance workbench from the Open Metadata
 * Conformance Suite.  The properties used to initialise the workbench are passed on the constructor.
 * The run() method contains the workbench logic.  It executes until the tests are complete, or it is signaled to
 * shutdown.
 */
public class PerformanceWorkbench extends OpenMetadataConformanceWorkbench
{
    private static final Logger     log         = LoggerFactory.getLogger(PerformanceWorkbench.class);

    private final PerformanceWorkPad workPad;

    /**
     * Constructor receives the work pad with the configuration that provides the location to record the results.
     *
     * @param workPad performance workbench's work pad
     */
    public PerformanceWorkbench(PerformanceWorkPad workPad)
    {
        super(workPad.getWorkbenchId(),
                workPad.getWorkbenchName(),
                workPad.getWorkbenchVersionNumber(),
                workPad.getWorkbenchDocURL());

        final String  methodName = "performanceWorkbenchThread.constructor";

        this.workPad = workPad;

        OMRSAuditLog auditLog   = workPad.getAuditLog();

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
     * Run the performance test cases.
     *
     * @throws Exception on any error
     */
    private void runTests() throws Exception
    {

        final String methodName = "runTests";

        Map<String, EntityDef> entityDefs = new TreeMap<>();
        Map<String, RelationshipDef> relationshipDefs = new TreeMap<>();
        Map<String, ClassificationDef> classificationDefs = new TreeMap<>();

        OMRSMetadataCollection metadataCollection = workPad.getTutRepositoryConnector().getMetadataCollection();

        // 0. Retrieve all of the type definitions known to the technology under test, by category
        List<TypeDef> entityDefsList = metadataCollection.findTypeDefsByCategory(workPad.getLocalServerUserId(), TypeDefCategory.ENTITY_DEF);
        for (TypeDef typeDef : entityDefsList)
        {
            entityDefs.put(typeDef.getName(), (EntityDef)typeDef);
        }

        List<TypeDef> relationshipDefsList = metadataCollection.findTypeDefsByCategory(workPad.getLocalServerUserId(), TypeDefCategory.RELATIONSHIP_DEF);
        for (TypeDef typeDef : relationshipDefsList)
        {
            relationshipDefs.put(typeDef.getName(), (RelationshipDef)typeDef);
        }

        List<TypeDef> classificationDefsList = metadataCollection.findTypeDefsByCategory(workPad.getLocalServerUserId(), TypeDefCategory.CLASSIFICATION_DEF);
        for (TypeDef typeDef : classificationDefsList)
        {
            classificationDefs.put(typeDef.getName(), (ClassificationDef)typeDef);
        }

        // 1. Create entity instances
        for (EntityDef entityDef : entityDefs.values())
        {
            TestEntityCreation testEntityCreation = new TestEntityCreation(workPad, entityDef);
            testEntityCreation.executeTest();
        }

        ConformanceSuiteAuditCode waiting = ConformanceSuiteAuditCode.TEST_EXECUTION_WAITING;
        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000L);

        // 2. Search entity instances
        for (EntityDef entityDef : entityDefs.values())
        {
            TestEntitySearch testEntitySearch = new TestEntitySearch(workPad, entityDef);
            testEntitySearch.executeTest();
        }

        // 3. Create relationship instances
        for (RelationshipDef relationshipDef : relationshipDefs.values())
        {
            TestRelationshipCreation testRelationshipCreation = new TestRelationshipCreation(workPad, relationshipDef);
            testRelationshipCreation.executeTest();
        }

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // 4. Search relationship instances
        for (RelationshipDef relationshipDef : relationshipDefs.values())
        {
            TestRelationshipSearch testRelationshipSearch = new TestRelationshipSearch(workPad, relationshipDef);
            testRelationshipSearch.executeTest();
        }

        // 5. Classify entities
        for (ClassificationDef classificationDef : classificationDefs.values())
        {
            TestEntityClassification testEntityClassification = new TestEntityClassification(workPad, classificationDef);
            testEntityClassification.executeTest();
        }

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // 6. Search entities by classification
        for (ClassificationDef classificationDef : classificationDefs.values())
        {
            TestClassificationSearch testClassificationSearch = new TestClassificationSearch(workPad, classificationDef);
            testClassificationSearch.executeTest();
        }

        // TODO: 7. Update entity instances

        // TODO: 8. Update relationship instances

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 9. Undo entity updates

        // TODO: 10. Undo relationship updates

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 11. Get entity instances (detail, summary, history)

        // TODO: 12. Get relationship instances (current, history)

        // TODO: 13. Search historical entity instances

        // TODO: 14. Search historical relationship instances

        // TODO: 15. Graph query instances

        // TODO: 16. Rehome entity instances

        // TODO: 17. Rehome relationship instances

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 18. Reidentify entity instances

        // TODO: 19. Reidentify entity instances

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 20. Retype entity instances

        // TODO: 21. Retype relationship instances

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 22. Declassify entity instances

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 23. Delete entity instances

        // TODO: 24. Delete relationship instances

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 25. Restore entity instances

        // TODO: 26. Restore relationship instances

        workPad.getAuditLog().logRecord(methodName,
                waiting.getLogMessageId(),
                waiting.getSeverity(),
                waiting.getFormattedLogMessage("" + workPad.getWaitBetweenScenarios()),
                null,
                waiting.getSystemAction(),
                waiting.getUserAction());
        Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        // TODO: 27. Purge entity instances

        // TODO: 28. Purge relationship instances

        TestEnvironment testEnvironment = new TestEnvironment(workPad);
        testEnvironment.executeTest();

    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        final String   methodName = "performanceWorkbenchThread.run";

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
                catch (Exception error)
                {
                    stopRunning();
                    log.error("Unexpected error.", error);

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
                try
                {
                    runTests();
                }
                catch (Exception error)
                {
                    log.error("Unexpected error.", error);
                    stopRunning();
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
