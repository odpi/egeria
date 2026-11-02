/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionTarget;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogRequestParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * ElementWatchdogFVT runs the generic element watchdog in the EgeriaWatchdog engine and checks that it
 * reacts to changes - not just that it starts.
 * <br>
 * The watchdog's job is to start a governance action process each time an element it is watching changes.
 * To see which processes it started, the test defines its own processes, each with a single step that runs
 * on a governance engine that no engine host runs (the same arrangement as ExternalGovernanceEngineFVT).
 * Every engine action the watchdog causes therefore waits at APPROVED, where the test reads it through the
 * Governance Engine OMVS: the step's request type says which process ran, its {@code receivedElement}
 * action target says which element it was about, and its request parameters carry what the watchdog
 * passed on.
 * <ul>
 *     <li>{@code watch-for-element-change} watches one element (the {@code instanceToMonitor} action target),
 *     starts the update process for the first change, and then completes with {@code monitoring-completed}.
 *     A change to another element must not trigger it, and a second change to the watched element must not
 *     start a second process.</li>
 *     <li>{@code watch-for-element-changes} watches every element of one type (the {@code interestingTypeName}
 *     request parameter) and keeps running: new, updated, classified and deleted elements each start their
 *     own process, and an element of another type starts nothing.  A deleted element can no longer be an
 *     action target, so its delete process is given its GUID and type name as request parameters.</li>
 * </ul>
 * The watchdog hears about changes through the metadata access store's out topic, so these tests need the
 * event bus and are skipped by the {@code -PrunFilesFvtNoKafka} run.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ElementWatchdogFVT
{
    private static final RequestTypeDefinition WATCH_FOR_ELEMENT_CHANGE  = RequestTypeDefinition.WATCH_FOR_ELEMENT_CHANGE;
    private static final RequestTypeDefinition WATCH_FOR_ELEMENT_CHANGES = RequestTypeDefinition.WATCH_FOR_ELEMENT_CHANGES;

    private static final String ENGINE_QUALIFIED_NAME  = FilesFvtTestSupport.TEST_MARKER + "::WatchdogReactionEngine";
    private static final String SERVICE_QUALIFIED_NAME = FilesFvtTestSupport.TEST_MARKER + "::WatchdogReactionService";

    /*
     * One process per kind of change.  The request type of its only step is how the test tells them apart.
     */
    private static final String CREATED_REQUEST_TYPE    = "watched-element-created";
    private static final String UPDATED_REQUEST_TYPE    = "watched-element-updated";
    private static final String CLASSIFIED_REQUEST_TYPE = "watched-element-classified";
    private static final String DELETED_REQUEST_TYPE    = "watched-element-deleted";

    private static final List<String> REQUEST_TYPES = List.of(CREATED_REQUEST_TYPE,
                                                              UPDATED_REQUEST_TYPE,
                                                              CLASSIFIED_REQUEST_TYPE,
                                                              DELETED_REQUEST_TYPE);

    /**
     * The action target name the watchdog gives the changed element when it starts a process.
     */
    private static final String RECEIVED_ELEMENT = "receivedElement";

    /**
     * The request parameters the watchdog uses to name a deleted element.
     */
    private static final String DELETED_ELEMENT_GUID      = "ElementGUID";
    private static final String DELETED_ELEMENT_TYPE_NAME = "ElementTypeName";

    /**
     * The type the multiple-event watchdog watches.  Nothing else in this suite creates glossary terms, so
     * every event for one is caused by this test.
     */
    private static final String WATCHED_TYPE_NAME = OpenMetadataType.GLOSSARY_TERM.typeName;

    /**
     * How long the test waits after a change it expects the watchdog to ignore before it accepts that
     * nothing happened.
     */
    private static final long QUIET_MILLISECONDS = 10000;

    private static String governanceEngineGUID = null;

    private final GovernanceEngineClient governanceEngineClient = new GovernanceEngineClient();


    /**
     * Define the governance engine and the processes the watchdog starts.  The names carry the suite's
     * marker, so the clean-up at start-up removes the previous run's definitions.
     *
     * @throws Exception problem creating the definitions
     */
    @BeforeAll
    static void defineReactionProcesses() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        PropertyHelper    propertyHelper    = new PropertyHelper();

        OpenMetadataElement existingEngine = openMetadataStore.getMetadataElementByUniqueName(ENGINE_QUALIFIED_NAME,
                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name);

        if (existingEngine != null)
        {
            governanceEngineGUID = existingEngine.getElementGUID();
        }
        else
        {
            governanceEngineGUID = defineEngine(openMetadataStore, propertyHelper);
        }

        for (String requestType : REQUEST_TYPES)
        {
            if (openMetadataStore.getMetadataElementByUniqueName(processQualifiedName(requestType),
                                                                 OpenMetadataProperty.QUALIFIED_NAME.name) == null)
            {
                defineProcess(openMetadataStore, propertyHelper, requestType);
            }
        }
    }


    /**
     * Define the governance engine, and the governance service through which it declares the request types
     * of the process steps.  No engine host runs it.
     *
     * @param openMetadataStore store to create the definitions in
     * @param propertyHelper helper
     * @return unique identifier of the governance engine
     * @throws Exception problem creating the definitions
     */
    private static String defineEngine(OpenMetadataStore openMetadataStore,
                                       PropertyHelper    propertyHelper) throws Exception
    {
        ElementProperties engineProperties = propertyHelper.addStringProperty(null,
                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                              ENGINE_QUALIFIED_NAME);
        engineProperties = propertyHelper.addStringProperty(engineProperties,
                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                            "Watchdog reaction engine");
        engineProperties = propertyHelper.addStringProperty(engineProperties,
                                                            OpenMetadataProperty.DESCRIPTION.name,
                                                            "A governance engine that no engine host runs; the engine actions of the processes the element watchdog starts wait on it for files-fvt to inspect.");

        String engineGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_ENGINE.typeName,
                                                                           new NewElementProperties(engineProperties));

        ElementProperties serviceProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               SERVICE_QUALIFIED_NAME);
        serviceProperties = propertyHelper.addStringProperty(serviceProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             "Watchdog reaction service");

        String governanceServiceGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_SERVICE.typeName,
                                                                                      new NewElementProperties(serviceProperties));

        for (String requestType : REQUEST_TYPES)
        {
            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                           engineGUID,
                                                           governanceServiceGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null,
                                                                                            OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                            requestType));
        }

        return engineGUID;
    }


    /**
     * Define a governance action process with a single step that runs on the reaction engine.
     *
     * @param openMetadataStore store to create the definitions in
     * @param propertyHelper helper
     * @param requestType request type of the step, which also names the process
     * @throws Exception problem creating the definitions
     */
    private static void defineProcess(OpenMetadataStore openMetadataStore,
                                      PropertyHelper    propertyHelper,
                                      String            requestType) throws Exception
    {
        String processQualifiedName = processQualifiedName(requestType);

        ElementProperties processProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               processQualifiedName);
        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             "Watchdog reaction: " + requestType);

        String processGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                            null,
                                                                            null,
                                                                            true,
                                                                            null,
                                                                            new NewElementProperties(processProperties),
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            false);

        ElementProperties stepProperties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            processQualifiedName + ":processStep1");
        stepProperties = propertyHelper.addStringProperty(stepProperties,
                                                          OpenMetadataProperty.DISPLAY_NAME.name,
                                                          "Watchdog reaction step");

        String stepGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                         null,
                                                                         processGUID,
                                                                         false,
                                                                         null,
                                                                         new NewElementProperties(stepProperties),
                                                                         processGUID,
                                                                         OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                         null,
                                                                         true);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                       stepGUID,
                                                       governanceEngineGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null,
                                                                                        OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                        requestType));
    }


    /**
     * Return the qualified name of the process whose only step has this request type.
     *
     * @param requestType request type of the step
     * @return qualified name
     */
    private static String processQualifiedName(String requestType)
    {
        return FilesFvtTestSupport.TEST_MARKER + "::WatchdogReaction::" + requestType;
    }


    @Test
    @DisplayName("watch-for-element-change starts one process for the first change to the watched element, then completes")
    void singleEventWatchdogReactsOnceToItsElement() throws Exception
    {
        assumeTrue(OMAGPlatformExtension.isEventBusConfigured(),
                   "The element watchdog hears about changes through the out topic, and this run has no event bus.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        String watchedGUID   = createTerm(openMetadataStore, "single-watched");
        String unwatchedGUID = createTerm(openMetadataStore, "single-unwatched");
        String watchdogGUID  = null;

        List<String> reactionGUIDs = new ArrayList<>();

        try
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WatchdogRequestParameter.UPDATED_ELEMENT_PROCESS_NAME.getName(),
                                  processQualifiedName(UPDATED_REQUEST_TYPE));

            watchdogGUID = startWatchdog(WATCH_FOR_ELEMENT_CHANGE,
                                         requestParameters,
                                         List.of(FilesFvtTestSupport.newActionTarget(WatchdogActionTarget.INSTANCE_TO_MONITOR.getName(),
                                                                                     watchedGUID)));

            /*
             * A change to an element the watchdog is not watching, and then one to the element it is.  Events
             * are delivered in order, so once the watched element's process has started, the event for the
             * other element has been seen - and must not have started anything.  If it had, the watchdog would
             * have completed on the wrong element and never seen the one it was asked to watch.
             */
            updateDescription(openMetadataStore, unwatchedGUID, "Changed while nobody was watching it");
            updateDescription(openMetadataStore, watchedGUID, "The first change");

            EngineActionElement reaction = waitForReaction(UPDATED_REQUEST_TYPE, watchedGUID);

            reactionGUIDs.add(reaction.getElementHeader().getGUID());

            assertTrue(findReactions(UPDATED_REQUEST_TYPE, unwatchedGUID).isEmpty(),
                       "The watchdog started a process for an element it was not asked to watch.");

            /*
             * The process has the element as its receivedElement action target and the changed property passed on.
             */
            assertNotNull(reaction.getRequestParameters(), "The update process received no request parameters");
            assertNotNull(reaction.getRequestParameters().get(WatchdogRequestParameter.CHANGED_PROPERTIES.getName()),
                          "The watchdog did not pass on which properties changed: " + reaction.getRequestParameters());
            assertTrue(reaction.getRequestParameters().get(WatchdogRequestParameter.CHANGED_PROPERTIES.getName())
                               .contains(OpenMetadataProperty.DESCRIPTION.name),
                       "The changed properties should name the description: " + reaction.getRequestParameters());

            /*
             * ... and the watchdog is finished.
             */
            EngineActionElement watchdog = new EngineActionWaiter().waitForTerminalStatus(watchdogGUID,
                                                                                         FilesFvtTestSupport.governanceActionTypeQualifiedName(WATCH_FOR_ELEMENT_CHANGE));

            assertEquals(ActivityStatus.COMPLETED, watchdog.getActionStatus(),
                         "The single-event watchdog should complete after its event.  It said: " + watchdog.getCompletionMessage());
            assertNotNull(watchdog.getCompletionGuards(), "The watchdog recorded no completion guards");
            assertTrue(watchdog.getCompletionGuards().contains(WatchdogActionGuard.MONITORING_COMPLETED.getName()),
                       "The watchdog's completion guards: " + watchdog.getCompletionGuards());

            /*
             * Having completed, it no longer listens.
             */
            updateDescription(openMetadataStore, watchedGUID, "The second change");

            Thread.sleep(QUIET_MILLISECONDS);

            List<EngineActionElement> reactions = findReactions(UPDATED_REQUEST_TYPE, watchedGUID);

            for (EngineActionElement engineAction : reactions)
            {
                if (! reactionGUIDs.contains(engineAction.getElementHeader().getGUID()))
                {
                    reactionGUIDs.add(engineAction.getElementHeader().getGUID());
                }
            }

            assertEquals(1, reactions.size(), "The single-event watchdog started a process for more than one change.");
        }
        finally
        {
            cleanUp(openMetadataStore, watchdogGUID, reactionGUIDs, List.of(watchedGUID, unwatchedGUID));
        }
    }


    @Test
    @DisplayName("watch-for-element-changes starts a process for each change to an element of its type, and keeps running")
    void multipleEventWatchdogReactsToEveryChangeOfItsType() throws Exception
    {
        assumeTrue(OMAGPlatformExtension.isEventBusConfigured(),
                   "The element watchdog hears about changes through the out topic, and this run has no event bus.");

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        String       watchdogGUID  = null;
        List<String> elementGUIDs  = new ArrayList<>();
        List<String> reactionGUIDs = new ArrayList<>();

        try
        {
            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(WatchdogRequestParameter.INTERESTING_TYPE_NAME.getName(), WATCHED_TYPE_NAME);
            requestParameters.put(WatchdogRequestParameter.NEW_ELEMENT_PROCESS_NAME.getName(),
                                  processQualifiedName(CREATED_REQUEST_TYPE));
            requestParameters.put(WatchdogRequestParameter.UPDATED_ELEMENT_PROCESS_NAME.getName(),
                                  processQualifiedName(UPDATED_REQUEST_TYPE));
            requestParameters.put(WatchdogRequestParameter.CLASSIFIED_ELEMENT_PROCESS_NAME.getName(),
                                  processQualifiedName(CLASSIFIED_REQUEST_TYPE));
            requestParameters.put(WatchdogRequestParameter.DELETED_ELEMENT_PROCESS_NAME.getName(),
                                  processQualifiedName(DELETED_REQUEST_TYPE));

            watchdogGUID = startWatchdog(WATCH_FOR_ELEMENT_CHANGES, requestParameters, null);

            /*
             * Two new elements of the watched type: each starts the new-element process.
             */
            String firstGUID  = createTerm(openMetadataStore, "multiple-first");
            elementGUIDs.add(firstGUID);
            String secondGUID = createTerm(openMetadataStore, "multiple-second");
            elementGUIDs.add(secondGUID);

            reactionGUIDs.add(waitForReaction(CREATED_REQUEST_TYPE, firstGUID).getElementHeader().getGUID());
            reactionGUIDs.add(waitForReaction(CREATED_REQUEST_TYPE, secondGUID).getElementHeader().getGUID());

            /*
             * An element of another type starts nothing.
             */
            String otherTypeGUID = createCollection(openMetadataStore, "multiple-other-type");
            elementGUIDs.add(otherTypeGUID);

            /*
             * An update, a classification and a delete each start their own process.
             */
            updateDescription(openMetadataStore, firstGUID, "Changed while the watchdog was watching");

            EngineActionElement updated = waitForReaction(UPDATED_REQUEST_TYPE, firstGUID);

            reactionGUIDs.add(updated.getElementHeader().getGUID());

            assertNotNull(updated.getRequestParameters(), "The update process received no request parameters");
            assertTrue(String.valueOf(updated.getRequestParameters().get(WatchdogRequestParameter.CHANGED_PROPERTIES.getName()))
                               .contains(OpenMetadataProperty.DESCRIPTION.name),
                       "The changed properties should name the description: " + updated.getRequestParameters());

            openMetadataStore.classifyMetadataElementInStore(firstGUID,
                                                             OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                             null,
                                                             null);

            EngineActionElement classified = waitForClassificationReaction(firstGUID,
                                                                           OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName);

            reactionGUIDs.add(classified.getElementHeader().getGUID());

            openMetadataStore.deleteMetadataElementInStore(secondGUID, false);

            EngineActionElement deleted = waitForReaction(DELETED_REQUEST_TYPE, secondGUID);

            reactionGUIDs.add(deleted.getElementHeader().getGUID());

            assertEquals(WATCHED_TYPE_NAME, deleted.getRequestParameters().get(DELETED_ELEMENT_TYPE_NAME),
                         "The delete process should be told the type of the deleted element: " + deleted.getRequestParameters());

            /*
             * By now the collection's events have long been delivered.
             */
            for (String requestType : REQUEST_TYPES)
            {
                assertTrue(findReactions(requestType, otherTypeGUID).isEmpty(),
                           "The watchdog started " + requestType + " for an element that is not a " + WATCHED_TYPE_NAME);
            }

            /*
             * Several events later, it is still watching.
             */
            EngineActionElement watchdog = getEngineAction(watchdogGUID);

            assertEquals(ActivityStatus.IN_PROGRESS, watchdog.getActionStatus(),
                         "The multiple-event watchdog stopped watching.  It said: " + watchdog.getCompletionMessage());
        }
        finally
        {
            /*
             * Pick up anything the watchdog started that the assertions above did not reach.
             */
            try
            {
                for (EngineActionElement engineAction : governanceEngineClient.getApprovedEngineActions(governanceEngineGUID))
                {
                    if (! reactionGUIDs.contains(engineAction.getElementHeader().getGUID()))
                    {
                        reactionGUIDs.add(engineAction.getElementHeader().getGUID());
                    }
                }
            }
            catch (Exception error)
            {
                System.out.println("files-fvt: unable to list the reaction engine's engine actions: " + error.getMessage());
            }

            cleanUp(openMetadataStore, watchdogGUID, reactionGUIDs, elementGUIDs);
        }
    }


    /**
     * Start a watchdog and wait until it is running.
     *
     * @param requestType the watchdog's governance action type
     * @param requestParameters what to watch for and which processes to start
     * @param actionTargets elements to watch, or null
     * @return the watchdog's engine action
     * @throws Exception it did not start
     */
    private String startWatchdog(RequestTypeDefinition    requestType,
                                 Map<String, String>      requestParameters,
                                 List<NewActionTarget>    actionTargets) throws Exception
    {
        String actionName = FilesFvtTestSupport.governanceActionTypeQualifiedName(requestType);

        String watchdogGUID = new AutomatedCurationClient().initiateGovernanceActionType(actionName,
                                                                                         requestParameters,
                                                                                         actionTargets);

        assertNotNull(watchdogGUID, "The Automated Curation service returned no engine action for " + actionName);

        FilesFvtTestSupport.waitFor(actionName + " to be claimed by the EgeriaWatchdog engine and start running",
                                    "files.fvt.engine.action.timeout.seconds",
                                    300,
                                    () ->
                                    {
                                        EngineActionElement watchdog = getEngineAction(watchdogGUID);

                                        if ((watchdog.getActionStatus() == ActivityStatus.FAILED) ||
                                                (watchdog.getActionStatus() == ActivityStatus.INVALID))
                                        {
                                            throw new AssertionError(actionName + " ended as " + watchdog.getActionStatus()
                                                                             + " instead of watching.  It said: " + watchdog.getCompletionMessage());
                                        }

                                        return (watchdog.getActionStatus() == ActivityStatus.IN_PROGRESS);
                                    });

        /*
         * IN_PROGRESS is set as the service starts; give it a moment to register its listener before the test
         * makes the changes it should see.
         */
        Thread.sleep(2000);

        return watchdogGUID;
    }


    /**
     * Return the engine actions waiting on the reaction engine for one request type and one received element.
     *
     * @param requestType request type of the process step
     * @param elementGUID the element the watchdog reacted to
     * @return matching engine actions
     * @throws Exception problem reading them
     */
    private List<EngineActionElement> findReactions(String requestType,
                                                    String elementGUID) throws Exception
    {
        List<EngineActionElement> reactions = new ArrayList<>();

        for (EngineActionElement engineAction : governanceEngineClient.getApprovedEngineActions(governanceEngineGUID))
        {
            /*
             * A deleted element cannot be an action target, so the watchdog names it in the request
             * parameters instead.
             */
            if ((requestType.equals(engineAction.getRequestType())) &&
                    ((elementGUID.equals(EngineActionWaiter.getActionTargetGUID(engineAction, RECEIVED_ELEMENT))) ||
                     ((engineAction.getRequestParameters() != null) &&
                      (elementGUID.equals(engineAction.getRequestParameters().get(DELETED_ELEMENT_GUID))))))
            {
                reactions.add(engineAction);
            }
        }

        return reactions;
    }


    /**
     * Wait for the watchdog to start the process for one request type and one element.
     *
     * @param requestType request type of the process step
     * @param elementGUID the element the watchdog should react to
     * @return the process step's engine action
     * @throws Exception it never appeared
     */
    private EngineActionElement waitForReaction(String requestType,
                                                String elementGUID) throws Exception
    {
        List<EngineActionElement> holder = new ArrayList<>();

        FilesFvtTestSupport.waitFor("The element watchdog to start " + processQualifiedName(requestType) + " for " + elementGUID,
                                    "files.fvt.engine.action.timeout.seconds",
                                    300,
                                    () ->
                                    {
                                        holder.addAll(findReactions(requestType, elementGUID));

                                        return ! holder.isEmpty();
                                    });

        return holder.get(0);
    }


    /**
     * Wait for the watchdog to start the classified-element process for one classification of one element.
     * Creating an element can classify it too (its Anchors classification, for example), so the
     * classification name is part of the match.
     *
     * @param elementGUID the element the watchdog should react to
     * @param classificationName the classification that was added
     * @return the process step's engine action
     * @throws Exception it never appeared
     */
    private EngineActionElement waitForClassificationReaction(String elementGUID,
                                                              String classificationName) throws Exception
    {
        List<EngineActionElement> holder = new ArrayList<>();

        FilesFvtTestSupport.waitFor("The element watchdog to start " + processQualifiedName(CLASSIFIED_REQUEST_TYPE)
                                            + " for the " + classificationName + " classification of " + elementGUID,
                                    "files.fvt.engine.action.timeout.seconds",
                                    300,
                                    () ->
                                    {
                                        for (EngineActionElement engineAction : findReactions(CLASSIFIED_REQUEST_TYPE, elementGUID))
                                        {
                                            if ((engineAction.getRequestParameters() != null) &&
                                                    (classificationName.equals(engineAction.getRequestParameters().get("ClassificationName"))))
                                            {
                                                holder.add(engineAction);
                                                return true;
                                            }
                                        }

                                        return false;
                                    });

        return holder.get(0);
    }


    /**
     * Create a glossary term for the watchdog to see.
     *
     * @param openMetadataStore store to create it in
     * @param purpose short label, part of its qualified name
     * @return unique identifier of the term
     * @throws Exception problem creating it
     */
    private String createTerm(OpenMetadataStore openMetadataStore,
                              String            purpose) throws Exception
    {
        return createElement(openMetadataStore, WATCHED_TYPE_NAME, purpose);
    }


    /**
     * Create an element of a type the multiple-event watchdog is not watching.
     *
     * @param openMetadataStore store to create it in
     * @param purpose short label, part of its qualified name
     * @return unique identifier of the collection
     * @throws Exception problem creating it
     */
    private String createCollection(OpenMetadataStore openMetadataStore,
                                    String            purpose) throws Exception
    {
        return createElement(openMetadataStore, OpenMetadataType.COLLECTION.typeName, purpose);
    }


    /**
     * Create an element with a qualified name carrying the suite's marker.
     *
     * @param openMetadataStore store to create it in
     * @param typeName type of element
     * @param purpose short label, part of its qualified name
     * @return unique identifier of the element
     * @throws Exception problem creating it
     */
    private String createElement(OpenMetadataStore openMetadataStore,
                                 String            typeName,
                                 String            purpose) throws Exception
    {
        PropertyHelper propertyHelper = new PropertyHelper();

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        FilesFvtTestSupport.TEST_MARKER + "::ElementWatchdog::" + purpose);
        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                      "Element watchdog " + purpose);

        return openMetadataStore.createMetadataElementInStore(typeName, new NewElementProperties(properties));
    }


    /**
     * Change an element's description.
     *
     * @param openMetadataStore store to update it in
     * @param elementGUID element to change
     * @param description new description
     * @throws Exception problem updating it
     */
    private void updateDescription(OpenMetadataStore openMetadataStore,
                                   String            elementGUID,
                                   String            description) throws Exception
    {
        openMetadataStore.updateMetadataElementInStore(elementGUID,
                                                       false,
                                                       new PropertyHelper().addStringProperty(null,
                                                                                              OpenMetadataProperty.DESCRIPTION.name,
                                                                                              description));
    }


    /**
     * Read an engine action.
     *
     * @param engineActionGUID engine action
     * @return the engine action
     * @throws Exception problem reading it
     */
    private EngineActionElement getEngineAction(String engineActionGUID) throws Exception
    {
        EngineActionElement engineAction = new EngineActionWaiter().getEngineAction(engineActionGUID);

        assertNotNull(engineAction, "Engine action " + engineActionGUID + " could not be read");

        return engineAction;
    }


    /**
     * Stop the watchdog, cancel the processes it started (nothing would ever run them), and remove the
     * elements it watched.
     *
     * @param openMetadataStore store to delete through
     * @param watchdogGUID watchdog's engine action, or null
     * @param reactionGUIDs engine actions the watchdog caused
     * @param elementGUIDs elements the test created
     */
    private void cleanUp(OpenMetadataStore openMetadataStore,
                         String            watchdogGUID,
                         List<String>      reactionGUIDs,
                         List<String>      elementGUIDs)
    {
        AutomatedCurationClient automatedCurationClient = new AutomatedCurationClient();

        if (watchdogGUID != null)
        {
            try
            {
                automatedCurationClient.cancelEngineAction(watchdogGUID);
            }
            catch (Exception error)
            {
                System.out.println("files-fvt: unable to cancel the watchdog engine action " + watchdogGUID + ": " + error.getMessage());
            }
        }

        for (String reactionGUID : reactionGUIDs)
        {
            try
            {
                automatedCurationClient.cancelEngineAction(reactionGUID);
            }
            catch (Exception error)
            {
                System.out.println("files-fvt: unable to cancel engine action " + reactionGUID + ": " + error.getMessage());
            }
        }

        for (String elementGUID : elementGUIDs)
        {
            FilesFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }
}
