/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.pasteurfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.pasteur.controls.PasteurConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PasteurActionHistoryCleanupFVT checks what the Pasteur Action History Cleanup removes, and what it leaves alone,
 * when it runs with the configuration the core content pack gives it: engine actions after 7 days, governance
 * action process instances after 14 days and to dos after 90 days, for the COMPLETED, INVALID and FAILED
 * statuses.
 * <p>
 * The actions are created by the test, each with a completionTime set into the past, so that one refresh shows
 * the whole policy: the age limit for each type, the inheritance of a supertype's limit, the statuses that count
 * as finished, the types that are not configured, and the engine actions of a process instance going with
 * their process instance rather than on their own.  Every qualified name includes the run's start time, so the
 * actions from one run cannot be confused with another's; the ones that survive are purged at the end.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PasteurActionHistoryCleanupFVT
{
    private static final long   millisecondsPerDay = 24L * 60L * 60L * 1000L;
    private static final String runId              = Long.toString(System.currentTimeMillis());

    private static final PropertyHelper propertyHelper = new PropertyHelper();

    /*
     * Labels for the actions the test creates.
     */
    private static final String OLD_COMPLETED_TODO          = "oldCompletedToDo";
    private static final String RECENT_COMPLETED_TODO       = "recentCompletedToDo";
    private static final String OLD_IN_PROGRESS_TODO        = "oldInProgressToDo";
    private static final String OLD_CANCELLED_TODO          = "oldCancelledToDo";
    private static final String OLD_FAILED_SHARING_REQUEST  = "oldFailedDataSharingRequest";
    private static final String OLD_COMPLETED_MEETING       = "oldCompletedMeeting";
    private static final String OLD_ENGINE_ACTION           = "oldStandaloneEngineAction";
    private static final String RECENT_ENGINE_ACTION        = "recentStandaloneEngineAction";
    private static final String OLD_PROCESS                 = "oldProcessInstance";
    private static final String OLD_PROCESS_ENGINE_ACTION   = "oldProcessEngineAction";
    private static final String RECENT_PROCESS              = "recentProcessInstance";
    private static final String RECENT_PROCESS_ENGINE_ACTION = "recentProcessEngineAction";
    private static final String PURGED_TODO                 = "purgedToDo";

    private static OpenMetadataStore         openMetadataStore = null;
    private static final Map<String, String> guids             = new LinkedHashMap<>();
    private static final Map<String, String> qualifiedNames    = new LinkedHashMap<>();


    /**
     * Create the actions and run Pasteur once.
     *
     * @throws Exception the fixture could not be created or Pasteur did not refresh
     */
    @BeforeAll
    static void createActionsAndRefresh() throws Exception
    {
        openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        createAction(OLD_COMPLETED_TODO,         OpenMetadataType.TO_DO.typeName,           ActivityStatus.COMPLETED,   100, null);
        createAction(RECENT_COMPLETED_TODO,      OpenMetadataType.TO_DO.typeName,           ActivityStatus.COMPLETED,   10,  null);
        createAction(OLD_IN_PROGRESS_TODO,       OpenMetadataType.TO_DO.typeName,           ActivityStatus.IN_PROGRESS, 100, null);
        createAction(OLD_CANCELLED_TODO,         OpenMetadataType.TO_DO.typeName,           ActivityStatus.CANCELLED,   100, null);
        createAction(OLD_FAILED_SHARING_REQUEST, OpenMetadataType.DATA_SHARING_REQUEST.typeName, ActivityStatus.FAILED, 100, null);
        createAction(OLD_COMPLETED_MEETING,      OpenMetadataType.MEETING.typeName,         ActivityStatus.COMPLETED,   1000, null);
        createAction(OLD_ENGINE_ACTION,          OpenMetadataType.ENGINE_ACTION.typeName,   ActivityStatus.COMPLETED,   8,   null);
        createAction(RECENT_ENGINE_ACTION,       OpenMetadataType.ENGINE_ACTION.typeName,   ActivityStatus.INVALID,     6,   null);

        createAction(OLD_PROCESS,                OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName, ActivityStatus.COMPLETED, 15, null);
        createAction(OLD_PROCESS_ENGINE_ACTION,  OpenMetadataType.ENGINE_ACTION.typeName,   ActivityStatus.COMPLETED,   15,  guids.get(OLD_PROCESS));
        createAction(RECENT_PROCESS,             OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName, ActivityStatus.COMPLETED, 10, null);
        createAction(RECENT_PROCESS_ENGINE_ACTION, OpenMetadataType.ENGINE_ACTION.typeName, ActivityStatus.COMPLETED,   10,  guids.get(RECENT_PROCESS));

        for (String label : guids.keySet())
        {
            assertTrue(isVisible(label), "The fixture action " + label + " was not readable before Pasteur ran");
        }

        OMAGPlatformExtension.refreshPasteur();
    }


    /**
     * Purge the actions this run created, whether or not Pasteur deleted them, so that repeated runs do not pile
     * them up in the repository.
     */
    @AfterAll
    static void purgeActions()
    {
        if (openMetadataStore == null)
        {
            return;
        }

        /*
         * The engine actions anchored to a process instance go with it, so the process instances are purged last.
         */
        for (String label : guids.keySet())
        {
            if (! label.equals(OLD_PROCESS) && ! label.equals(RECENT_PROCESS))
            {
                purge(guids.get(label));
            }
        }

        purge(guids.get(OLD_PROCESS));
        purge(guids.get(RECENT_PROCESS));
    }


    /**
     * A to do that completed more than 90 days ago is deleted.  It has no lineage, so the LOOK_FOR_LINEAGE delete
     * method soft-deletes it rather than archiving it - so it can still be found as a deleted element.
     *
     * @throws Exception problem reading the repository
     */
    @Test
    void oldCompletedToDoIsSoftDeleted() throws Exception
    {
        assertFalse(isVisible(OLD_COMPLETED_TODO), "A to do completed 100 days ago should have been deleted");

        OpenMetadataElement deletedElement = openMetadataStore.getDeletedElementByUniqueName(qualifiedNames.get(OLD_COMPLETED_TODO), null);

        assertNotNull(deletedElement, "The to do has no lineage, so it should have been soft-deleted - not archived or purged");
    }


    /**
     * A to do that completed less than 90 days ago is kept.
     */
    @Test
    void recentCompletedToDoIsKept()
    {
        assertTrue(isVisible(RECENT_COMPLETED_TODO), "A to do completed 10 days ago should have been kept");
    }


    /**
     * The age of an action does not matter unless its status says it has finished.
     */
    @Test
    void unfinishedAndCancelledToDosAreKept()
    {
        assertTrue(isVisible(OLD_IN_PROGRESS_TODO), "A to do that is still in progress should have been kept");
        assertTrue(isVisible(OLD_CANCELLED_TODO), "CANCELLED is not one of the default completion statuses");
    }


    /**
     * A data sharing request has no entry of its own, so it uses the entry for its supertype, ToDo.
     */
    @Test
    void subtypeInheritsSupertypeDays()
    {
        assertFalse(isVisible(OLD_FAILED_SHARING_REQUEST), "A data sharing request that failed 100 days ago should have been deleted");
    }


    /**
     * Types with no entry - here a meeting - are never deleted, however old.
     */
    @Test
    void unconfiguredTypeIsKept()
    {
        assertTrue(isVisible(OLD_COMPLETED_MEETING), "Meetings are not in the default days to clean up");
    }


    /**
     * A standalone engine action goes after 7 days, and not before.
     */
    @Test
    void standaloneEngineActions()
    {
        assertFalse(isVisible(OLD_ENGINE_ACTION), "An engine action completed 8 days ago should have been deleted");
        assertTrue(isVisible(RECENT_ENGINE_ACTION), "An engine action finished 6 days ago should have been kept");
    }


    /**
     * The engine actions of a process instance stay as long as their process instance, and go with it.
     */
    @Test
    void processEngineActionsFollowTheirProcessInstance()
    {
        assertFalse(isVisible(OLD_PROCESS), "A process instance completed 15 days ago should have been deleted");
        assertFalse(isVisible(OLD_PROCESS_ENGINE_ACTION), "The engine action of a deleted process instance should have gone with it");

        assertTrue(isVisible(RECENT_PROCESS), "A process instance completed 10 days ago should have been kept");
        assertTrue(isVisible(RECENT_PROCESS_ENGINE_ACTION),
                   "An engine action older than 7 days should be kept while its process instance is kept");
    }


    /**
     * The deleteMethod configuration property changes the delete method.  With PURGE, an old to do is removed
     * permanently, so it cannot even be found as a deleted element.  The property is set back to the core content
     * pack's LOOK_FOR_LINEAGE afterwards.  The fixture actions that the first refresh kept are not old enough to be
     * removed by the refreshes this test causes, so it does not disturb the other tests.
     *
     * @throws Exception problem with the repository, or Pasteur did not refresh
     */
    @Test
    void deleteMethodPropertyChangesDeleteMethod() throws Exception
    {
        createAction(PURGED_TODO, OpenMetadataType.TO_DO.typeName, ActivityStatus.COMPLETED, 100, null);

        assertTrue(isVisible(PURGED_TODO), "The fixture to do was not readable before Pasteur ran");

        try
        {
            OMAGPlatformExtension.updatePasteurConfiguration(Map.of(PasteurConfigurationProperty.DELETE_METHOD.getName(),
                                                                    DeleteMethod.PURGE.name()));

            assertFalse(isVisible(PURGED_TODO), "A to do completed 100 days ago should have been removed");
            assertFalse(isFoundAsDeleted(PURGED_TODO),
                        "The delete method is PURGE, so the to do should have been purged rather than soft-deleted");

            guids.remove(PURGED_TODO);
        }
        finally
        {
            OMAGPlatformExtension.updatePasteurConfiguration(Map.of(PasteurConfigurationProperty.DELETE_METHOD.getName(),
                                                                    DeleteMethod.LOOK_FOR_LINEAGE.name()));
        }
    }


    /**
     * Create one action.
     *
     * @param label the test's name for it
     * @param typeName type of action
     * @param activityStatus its status
     * @param daysSinceCompletion how long ago it completed
     * @param anchorGUID the process instance it belongs to, or null
     * @throws Exception it could not be created
     */
    private static void createAction(String         label,
                                     String         typeName,
                                     ActivityStatus activityStatus,
                                     long           daysSinceCompletion,
                                     String         anchorGUID) throws Exception
    {
        String qualifiedName = "pasteur-fvt::" + runId + "::" + typeName + "::" + label;

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        qualifiedName);
        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                      label);
        properties = propertyHelper.addEnumProperty(properties,
                                                    OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                    ActivityStatus.getOpenTypeName(),
                                                    activityStatus.name());
        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.COMPLETION_TIME.name,
                                                    new Date(System.currentTimeMillis() - (daysSinceCompletion * millisecondsPerDay)));

        String guid = openMetadataStore.createMetadataElementInStore(typeName,
                                                                     null,
                                                                     anchorGUID,
                                                                     (anchorGUID == null),
                                                                     null,
                                                                     new NewElementProperties(properties),
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     false);

        assertNotNull(guid, "No GUID returned for " + label);

        guids.put(label, guid);
        qualifiedNames.put(label, qualifiedName);
    }


    /**
     * Return whether an action can still be read.  A soft-deleted element cannot be retrieved: the repository
     * reports that it is deleted rather than returning it.
     *
     * @param label the test's name for the action
     * @return boolean
     */
    private static boolean isVisible(String label)
    {
        try
        {
            return (openMetadataStore.getMetadataElementByGUID(guids.get(label)) != null);
        }
        catch (Exception deleted)
        {
            return false;
        }
    }


    /**
     * Return whether an action can be found as a deleted element - that is, it was soft-deleted rather than purged.
     *
     * @param label the test's name for the action
     * @return boolean
     */
    private static boolean isFoundAsDeleted(String label)
    {
        try
        {
            return (openMetadataStore.getDeletedElementByUniqueName(qualifiedNames.get(label), null) != null);
        }
        catch (Exception notFound)
        {
            return false;
        }
    }


    /**
     * Permanently remove one action.  A purge only succeeds on a soft-deleted instance, so the action is
     * soft-deleted first; that step fails harmlessly for one Pasteur has already deleted.
     *
     * @param guid unique identifier of the action
     */
    private static void purge(String guid)
    {
        if (guid == null)
        {
            return;
        }

        DeleteOptions deleteOptions = new DeleteOptions();

        deleteOptions.setCascadedDelete(true);
        deleteOptions.setForLineage(true);
        deleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);

        try
        {
            openMetadataStore.deleteMetadataElementInStore(guid, deleteOptions);
        }
        catch (Exception alreadyDeleted)
        {
            // ordinary - Pasteur may have deleted it, or it went with its process instance
        }

        deleteOptions.setDeleteMethod(DeleteMethod.PURGE);

        try
        {
            openMetadataStore.deleteMetadataElementInStore(guid, deleteOptions);
        }
        catch (Exception error)
        {
            System.out.println("pasteur-fvt: could not purge " + guid + " - " + error.getClass().getSimpleName() + ": " + error.getMessage());
        }
    }
}
