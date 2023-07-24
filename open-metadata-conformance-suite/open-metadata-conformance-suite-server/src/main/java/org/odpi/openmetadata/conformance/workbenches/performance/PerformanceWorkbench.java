/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.performance;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.tests.performance.classify.TestEntityClassification;
import org.odpi.openmetadata.conformance.tests.performance.classify.TestEntityDeclassification;
import org.odpi.openmetadata.conformance.tests.performance.create.TestEntityCreation;
import org.odpi.openmetadata.conformance.tests.performance.create.TestRelationshipCreation;
import org.odpi.openmetadata.conformance.tests.performance.delete.TestEntityDelete;
import org.odpi.openmetadata.conformance.tests.performance.delete.TestRelationshipDelete;
import org.odpi.openmetadata.conformance.tests.performance.environment.TestEnvironment;
import org.odpi.openmetadata.conformance.tests.performance.graph.TestGraphHistoryQueries;
import org.odpi.openmetadata.conformance.tests.performance.graph.TestGraphQueries;
import org.odpi.openmetadata.conformance.tests.performance.purge.*;
import org.odpi.openmetadata.conformance.tests.performance.rehome.TestEntityReHome;
import org.odpi.openmetadata.conformance.tests.performance.rehome.TestRelationshipReHome;
import org.odpi.openmetadata.conformance.tests.performance.reidentify.TestEntityReIdentify;
import org.odpi.openmetadata.conformance.tests.performance.reidentify.TestRelationshipReIdentify;
import org.odpi.openmetadata.conformance.tests.performance.restore.TestEntityRestore;
import org.odpi.openmetadata.conformance.tests.performance.restore.TestRelationshipRestore;
import org.odpi.openmetadata.conformance.tests.performance.retrieve.TestEntityHistoryRetrieval;
import org.odpi.openmetadata.conformance.tests.performance.retrieve.TestEntityRetrieval;
import org.odpi.openmetadata.conformance.tests.performance.retrieve.TestRelationshipHistoryRetrieval;
import org.odpi.openmetadata.conformance.tests.performance.retrieve.TestRelationshipRetrieval;
import org.odpi.openmetadata.conformance.tests.performance.retype.*;
import org.odpi.openmetadata.conformance.tests.performance.search.*;
import org.odpi.openmetadata.conformance.tests.performance.undo.TestEntityUndo;
import org.odpi.openmetadata.conformance.tests.performance.undo.TestRelationshipUndo;
import org.odpi.openmetadata.conformance.tests.performance.update.TestClassificationUpdate;
import org.odpi.openmetadata.conformance.tests.performance.update.TestEntityUpdate;
import org.odpi.openmetadata.conformance.tests.performance.update.TestRelationshipUpdate;
import org.odpi.openmetadata.conformance.workbenches.OpenMetadataConformanceWorkbench;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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

        AuditLog auditLog = workPad.getAuditLog();

        auditLog.logMessage(methodName,
                            ConformanceSuiteAuditCode.WORKBENCH_INITIALIZING.getMessageDefinition(workbenchId,
                                                                                                  workbenchDocumentationURL));
    }

    /**
     * Run the performance test cases.
     *
     * @throws Exception on any error
     */
    private void runTests() throws Exception
    {

        final String methodName = "runTests";

        List<String> profilesToSkip = workPad.getProfilesToSkip();
        ConformanceSuiteAuditCode waiting = ConformanceSuiteAuditCode.TEST_EXECUTION_WAITING;

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
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_CREATION.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityCreation testEntityCreation = new TestEntityCreation(workPad, entityDef);
                testEntityCreation.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                                             waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000L);
        }

        // 2. Search entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_SEARCH.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntitySearch testEntitySearch = new TestEntitySearch(workPad, entityDef);
                testEntitySearch.executeTest();
            }
        }

        // 3. Create relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_CREATION.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipCreation testRelationshipCreation = new TestRelationshipCreation(workPad, relationshipDef);
                testRelationshipCreation.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 4. Search relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_SEARCH.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipSearch testRelationshipSearch = new TestRelationshipSearch(workPad, relationshipDef);
                testRelationshipSearch.executeTest();
            }
        }

        // 5. Classify entities
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_CLASSIFICATION.getProfileName()))
        {
            for (ClassificationDef classificationDef : classificationDefs.values())
            {
                TestEntityClassification testEntityClassification = new TestEntityClassification(workPad, classificationDef);
                testEntityClassification.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                                             waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 6. Search entities by classification
        if (!profilesToSkip.contains(PerformanceProfile.CLASSIFICATION_SEARCH.getProfileName()))
        {
            for (ClassificationDef classificationDef : classificationDefs.values())
            {
                TestClassificationSearch testClassificationSearch = new TestClassificationSearch(workPad, classificationDef);
                testClassificationSearch.executeTest();
            }
        }

        // Record the date and time prior to any instance updates
        Date priorToInstanceUpdates = new Date();

        // 7. Update entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_UPDATE.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityUpdate testEntityUpdate = new TestEntityUpdate(workPad, entityDef);
                testEntityUpdate.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 8. Update relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_UPDATE.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipUpdate testRelationshipUpdate = new TestRelationshipUpdate(workPad, relationshipDef);
                testRelationshipUpdate.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 9. Update classification properties
        if (!profilesToSkip.contains(PerformanceProfile.CLASSIFICATION_UPDATE.getProfileName()))
        {
            for (ClassificationDef classificationDef : classificationDefs.values())
            {
                TestClassificationUpdate testClassificationUpdate = new TestClassificationUpdate(workPad, classificationDef);
                testClassificationUpdate.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // Record the date and time prior to any undo operations
        Date priorToInstanceUndos = new Date();

        // 10. Undo entity updates
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_UNDO.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityUndo testEntityUndo = new TestEntityUndo(workPad, entityDef);
                testEntityUndo.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 11. Undo relationship updates
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_UNDO.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipUndo testRelationshipUndo = new TestRelationshipUndo(workPad, relationshipDef);
                testRelationshipUndo.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 12-13. Get entity instances (detail, summary, history)
        for (EntityDef entityDef : entityDefs.values())
        {
            if (!profilesToSkip.contains(PerformanceProfile.ENTITY_RETRIEVAL.getProfileName()))
            {
                TestEntityRetrieval testEntityRetrieval = new TestEntityRetrieval(workPad, entityDef);
                testEntityRetrieval.executeTest();
            }
            if (!profilesToSkip.contains(PerformanceProfile.ENTITY_HISTORY_RETRIEVAL.getProfileName()))
            {
                TestEntityHistoryRetrieval testEntityHistoryRetrieval = new TestEntityHistoryRetrieval(workPad, entityDef, priorToInstanceUpdates);
                testEntityHistoryRetrieval.executeTest();
            }
        }

        // 14-15. Get relationship instances (current, history)
        for (RelationshipDef relationshipDef : relationshipDefs.values())
        {
            if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_RETRIEVAL.getProfileName()))
            {
                TestRelationshipRetrieval testRelationshipRetrieval = new TestRelationshipRetrieval(workPad, relationshipDef);
                testRelationshipRetrieval.executeTest();
            }
            if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_HISTORY_RETRIEVAL.getProfileName()))
            {
                TestRelationshipHistoryRetrieval testRelationshipHistoryRetrieval = new TestRelationshipHistoryRetrieval(workPad, relationshipDef, priorToInstanceUpdates);
                testRelationshipHistoryRetrieval.executeTest();
            }
        }

        // 16. Search historical entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_HISTORY_SEARCH.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityHistorySearch testEntityHistoricalSearch = new TestEntityHistorySearch(workPad, entityDef, priorToInstanceUpdates);
                testEntityHistoricalSearch.executeTest();
            }
        }

        // 17. Search historical relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_HISTORY_SEARCH.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipHistorySearch testRelationshipHistorySearch = new TestRelationshipHistorySearch(workPad, relationshipDef, priorToInstanceUpdates);
                testRelationshipHistorySearch.executeTest();
            }
        }

        // 18-19. Graph query instances
        for (EntityDef entityDef : entityDefs.values())
        {
            if (!profilesToSkip.contains(PerformanceProfile.GRAPH_QUERIES.getProfileName()))
            {
                TestGraphQueries testGraphQueries = new TestGraphQueries(workPad, entityDef);
                testGraphQueries.executeTest();
            }
            if (!profilesToSkip.contains(PerformanceProfile.GRAPH_HISTORY_QUERIES.getProfileName()))
            {
                TestGraphHistoryQueries testGraphHistoryQueries = new TestGraphHistoryQueries(workPad, entityDef, priorToInstanceUpdates);
                testGraphHistoryQueries.executeTest();
            }
        }

        // 20. Re-home entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_RE_HOME.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityReHome testEntityReHome = new TestEntityReHome(workPad, entityDef);
                testEntityReHome.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 21. Re-home relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_RE_HOME.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipReHome testRelationshipReHome = new TestRelationshipReHome(workPad, relationshipDef);
                testRelationshipReHome.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 22. Declassify entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_DECLASSIFY.getProfileName()))
        {
            for (ClassificationDef classificationDef : classificationDefs.values())
            {
                TestEntityDeclassification testEntityDeclassification = new TestEntityDeclassification(workPad, classificationDef);
                testEntityDeclassification.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 23. Retype entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_RETYPE.getProfileName()))
        {

            // 23a. null properties
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityRetypeNullProperties nullProperties = new TestEntityRetypeNullProperties(workPad, entityDef);
                nullProperties.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

            // 23b. re-type to subtype
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityRetypeSub retypeSub = new TestEntityRetypeSub(workPad, entityDef);
                retypeSub.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

            // 23c. re-type to supertype
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityRetypeSuper retypeSuper = new TestEntityRetypeSuper(workPad, entityDef);
                retypeSuper.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

        }

        // 24. Retype relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_RETYPE.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipRetype testRelationshipRetype = new TestRelationshipRetype(workPad, relationshipDef);
                testRelationshipRetype.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 25. Re-identify entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_RE_IDENTIFY.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityReIdentify testEntityReIdentify = new TestEntityReIdentify(workPad, entityDef);
                testEntityReIdentify.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 26. Re-identify relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_RE_IDENTIFY.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipReIdentify testRelationshipReIdentify = new TestRelationshipReIdentify(workPad, relationshipDef);
                testRelationshipReIdentify.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 27. Delete relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_DELETE.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipDelete testRelationshipDelete = new TestRelationshipDelete(workPad, relationshipDef);
                testRelationshipDelete.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 28. Delete entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_DELETE.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityDelete testEntityDelete = new TestEntityDelete(workPad, entityDef);
                testEntityDelete.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 29. Restore entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_RESTORE.getProfileName()))
        {
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityRestore testEntityRestore = new TestEntityRestore(workPad, entityDef);
                testEntityRestore.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 30. Restore relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_RESTORE.getProfileName()))
        {
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipRestore testRelationshipRestore = new TestRelationshipRestore(workPad, relationshipDef);
                testRelationshipRestore.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);
        }

        // 31. Purge relationship instances
        if (!profilesToSkip.contains(PerformanceProfile.RELATIONSHIP_PURGE.getProfileName()))
        {

            // 31a. soft-delete relationships
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipPurgeSoft testRelationshipPurge = new TestRelationshipPurgeSoft(workPad, relationshipDef);
                testRelationshipPurge.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

            // 31b. purge relationships
            for (RelationshipDef relationshipDef : relationshipDefs.values())
            {
                TestRelationshipPurgeHard testRelationshipPurge = new TestRelationshipPurgeHard(workPad, relationshipDef);
                testRelationshipPurge.executeTest();
            }

        }

        // 32. Purge entity instances
        if (!profilesToSkip.contains(PerformanceProfile.ENTITY_PURGE.getProfileName()))
        {

            // 32a. soft-delete entities
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityPurgeSoft testEntityPurge = new TestEntityPurgeSoft(workPad, entityDef);
                testEntityPurge.executeTest();
            }

            workPad.getAuditLog().logMessage(methodName,
                    waiting.getMessageDefinition("" + workPad.getWaitBetweenScenarios()));
            Thread.sleep(workPad.getWaitBetweenScenarios() * 1000);

            // 32b. purge entities
            for (EntityDef entityDef : entityDefs.values())
            {
                TestEntityPurgeHard testEntityPurge = new TestEntityPurgeHard(workPad, entityDef);
                testEntityPurge.executeTest();
            }

        }

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
            AuditLog auditLog = workPad.getAuditLog();

            auditLog.logMessage(methodName,
                                ConformanceSuiteAuditCode.WORKBENCH_INITIALIZED.getMessageDefinition(workPad.getWorkbenchId()));

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
                            auditLog.logMessage(methodName,
                                                ConformanceSuiteAuditCode.WORKBENCH_WAITING_TO_START.getMessageDefinition(workPad.getWorkbenchId(), workPad.getTutServerName()));
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

                    auditLog.logMessage(methodName,
                                        ConformanceSuiteAuditCode.WORKBENCH_FAILURE.getMessageDefinition(workPad.getWorkbenchId(), error.getMessage()),
                                        error.toString());
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

                    auditLog.logMessage(methodName,
                                        ConformanceSuiteAuditCode.WORKBENCH_FAILURE.getMessageDefinition(workPad.getWorkbenchId(),
                                                                                                         error.getMessage()),
                                        error.toString());
                }
            }

            workPad.setWorkbenchComplete();

            auditLog.logMessage(methodName,
                                ConformanceSuiteAuditCode.WORKBENCH_SYNC_COMPLETED.getMessageDefinition(workPad.getWorkbenchId()));
        }
    }
}
