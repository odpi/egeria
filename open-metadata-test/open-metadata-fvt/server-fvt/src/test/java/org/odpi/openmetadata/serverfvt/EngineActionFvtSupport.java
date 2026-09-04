/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * EngineActionFvtSupport drives the engine action lifecycle for {@link EngineActionLifecycleFVT}, so that the
 * tests read as the sequence of events they are describing rather than as client plumbing.
 * <br><br>
 * Two clients are involved and it is worth knowing which does what.  The governance engine each action names
 * has to exist before an action can be created for it - {@code initiateEngineAction} looks it up by qualified
 * name and refuses an unknown one - and the Open Governance Framework's own clients only read governance
 * engines, so the engines here are created as plain metadata elements through the Open Metadata Framework's
 * store client.  Everything after that is the Open Governance client.
 * <br><br>
 * Every name is made unique with a UUID.  The tests share one metadata access store and run in whatever order
 * JUnit chooses, so a fixed name would make one test's engine actions visible to another's assertions - and
 * {@code getActiveEngineActions} is deliberately unscoped, so it sees every live action in the repository.
 * That is also why the assertions ask "is this GUID in the answer" rather than "how many came back".
 */
final class EngineActionFvtSupport
{
    /**
     * The request type every engine created here supports, unless a test asks for another.
     */
    static final String DEFAULT_REQUEST_TYPE = "server-fvt-request-type";

    private static final PropertyHelper propertyHelper = new PropertyHelper();

    private EngineActionFvtSupport()
    {
        // no instances
    }


    /**
     * Return a client for the open governance services of this suite's metadata access store.
     *
     * @return client
     * @throws Exception the client could not be created
     */
    private static EgeriaOpenGovernanceClient getGovernanceClient() throws Exception
    {
        return new EgeriaOpenGovernanceClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                              OMAGPlatformExtension.getPlatformURLRoot(),
                                              ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                              OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                              OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                              ServerFvtTestSupport.MAX_PAGE_SIZE,
                                              null);
    }


    /**
     * Return a client for the open metadata store of this suite's metadata access store.
     *
     * @return client
     * @throws Exception the client could not be created
     */
    private static EgeriaOpenMetadataStoreClient getStoreClient() throws Exception
    {
        return new EgeriaOpenMetadataStoreClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                 OMAGPlatformExtension.getPlatformURLRoot(),
                                                 ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                                 OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                                 OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                 ServerFvtTestSupport.MAX_PAGE_SIZE,
                                                 null);
    }


    /**
     * Create a governance engine for engine actions to be raised against.
     *
     * @param label short label making the engine recognisable in a failure message
     * @return the engine's qualified name
     * @throws Exception the engine could not be created
     */
    static String createGovernanceEngine(String label) throws Exception
    {
        return createGovernanceEngine(label, DEFAULT_REQUEST_TYPE);
    }


    /**
     * Create a governance engine that supports a given request type, together with the governance service that
     * supports it.
     * <br><br>
     * The service and the relationship are not decoration.  {@code initiateEngineAction} refuses to create an
     * action for an engine that supports no request types, and refuses again if the engine supports some but
     * not the one asked for - it walks the engine's {@code SupportedGovernanceService} relationships and
     * matches the {@code requestType} carried on each.  So the smallest engine an action can be raised
     * against is one with a service attached and the request type recorded on the link between them.
     * <br><br>
     * Nothing ever runs the service: no engine host is watching this metadata access store, so every status
     * change in these tests is made explicitly.  That is what keeps the suite hermetic and quick, and it is
     * also what makes the states observable one at a time - a real engine host would move an action from
     * claimed to finished faster than a test could look at it.
     *
     * @param label short label making the engine recognisable in a failure message
     * @param requestType governance request type the engine should support
     * @return the engine's qualified name
     * @throws Exception the engine could not be created
     */
    static String createGovernanceEngine(String label,
                                         String requestType) throws Exception
    {
        EgeriaOpenMetadataStoreClient storeClient = getStoreClient();

        String qualifiedName = "serverFvtGovernanceEngine:" + label + ":" + UUID.randomUUID();

        ElementProperties engineProperties = propertyHelper.addStringProperty(null,
                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                              qualifiedName);

        engineProperties = propertyHelper.addStringProperty(engineProperties,
                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                            "server-fvt " + label + " governance engine");

        String engineGUID = storeClient.createMetadataElementInStore(OMAGPlatformExtension.USER_ID,
                                                                     OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                     new NewElementOptions(),
                                                                     null,
                                                                     new NewElementProperties(engineProperties),
                                                                     null);

        ElementProperties serviceProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               qualifiedName + ":service");

        serviceProperties = propertyHelper.addStringProperty(serviceProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             "server-fvt " + label + " governance service");

        String serviceGUID = storeClient.createMetadataElementInStore(OMAGPlatformExtension.USER_ID,
                                                                      OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                      new NewElementOptions(),
                                                                      null,
                                                                      new NewElementProperties(serviceProperties),
                                                                      null);

        ElementProperties linkProperties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.REQUEST_TYPE.name,
                                                                            requestType);

        storeClient.createRelatedElementsInStore(OMAGPlatformExtension.USER_ID,
                                                 OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                 engineGUID,
                                                 serviceGUID,
                                                 null,
                                                 new NewElementProperties(linkProperties));

        return qualifiedName;
    }


    /**
     * Return the unique identifier of a governance engine.
     *
     * @param governanceEngineName qualified name of the engine
     * @return its guid
     * @throws Exception the engine could not be read
     */
    static String getGovernanceEngineGUID(String governanceEngineName) throws Exception
    {
        /*
         * Read through an engine action rather than by looking the engine up directly: the engine action
         * records the engine's guid as the executor, which is the same value getActiveClaimedEngineActions
         * is asked about, so this cannot disagree with what the query matches on.
         */
        String probeGUID = initiateEngineAction(governanceEngineName, "guid-probe");

        return getEngineAction(probeGUID).getGovernanceEngineGUID();
    }


    /**
     * Create an engine action for a governance engine.
     *
     * @param governanceEngineName engine that should run it
     * @param label short label making the action recognisable in a failure message
     * @return the new engine action's guid
     * @throws Exception the engine action could not be created
     */
    static String initiateEngineAction(String governanceEngineName,
                                       String label) throws Exception
    {
        return initiateEngineAction(governanceEngineName, label, DEFAULT_REQUEST_TYPE);
    }


    /**
     * Create an engine action for a governance engine, with a given request type.
     *
     * @param governanceEngineName engine that should run it
     * @param label short label making the action recognisable in a failure message
     * @param requestType governance request type to record
     * @return the new engine action's guid
     * @throws Exception the engine action could not be created
     */
    static String initiateEngineAction(String governanceEngineName,
                                       String label,
                                       String requestType) throws Exception
    {
        return getGovernanceClient().initiateEngineAction(OMAGPlatformExtension.USER_ID,
                                                          "serverFvtEngineAction:" + label + ":" + UUID.randomUUID(),
                                                          0,
                                                          "server-fvt " + label,
                                                          "Engine action created by server-fvt.",
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          governanceEngineName,
                                                          requestType,
                                                          null,
                                                          null,
                                                          null,
                                                          "server-fvt",
                                                          "server-fvt",
                                                          governanceEngineName);
    }


    /**
     * Create an engine action and take it as far as being claimed, which is where most of these tests want to
     * start from.
     *
     * @param governanceEngineName engine that should run it
     * @param label short label making the action recognisable in a failure message
     * @return the claimed engine action's guid
     * @throws Exception the engine action could not be created or claimed
     */
    static String claimedAction(String governanceEngineName,
                                String label) throws Exception
    {
        String engineActionGUID = initiateEngineAction(governanceEngineName, label);

        /*
         * No setStatus(APPROVED) first: initiateEngineAction already leaves the action APPROVED, which is
         * the state claimEngineAction requires.
         */
        claim(engineActionGUID);

        return engineActionGUID;
    }


    /**
     * Claim an engine action, as an engine host does when it picks up work.
     *
     * @param engineActionGUID action to claim
     * @throws Exception the action could not be claimed
     */
    static void claim(String engineActionGUID) throws Exception
    {
        getGovernanceClient().claimEngineAction(OMAGPlatformExtension.USER_ID, engineActionGUID);
    }


    /**
     * Move an engine action to a status.
     *
     * @param engineActionGUID action to move
     * @param activityStatus status it should have
     * @throws Exception the status could not be set
     */
    static void setStatus(String         engineActionGUID,
                          ActivityStatus activityStatus) throws Exception
    {
        getGovernanceClient().updateEngineActionStatus(OMAGPlatformExtension.USER_ID, engineActionGUID, activityStatus);
    }


    /**
     * Move an engine action to a status as a <em>different</em> user, standing in for a second engine host.
     * <br><br>
     * Used to reach the contention case: an engine action claimed by one engine host and then updated by
     * another, which is what happens when two hosts running the same governance engine both go for it.
     *
     * @param engineActionGUID action to move
     * @param activityStatus status it should have
     * @throws Exception the status could not be set
     */
    static void setStatusAsOtherUser(String         engineActionGUID,
                                     ActivityStatus activityStatus) throws Exception
    {
        EgeriaOpenGovernanceClient otherClient =
                new EgeriaOpenGovernanceClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                               OMAGPlatformExtension.getPlatformURLRoot(),
                                               ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                               OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                               OMAGPlatformExtension.OTHER_CLIENT_TOKEN_COLLECTION,
                                               ServerFvtTestSupport.MAX_PAGE_SIZE,
                                               null);

        otherClient.updateEngineActionStatus(OMAGPlatformExtension.OTHER_USER_ID, engineActionGUID, activityStatus);
    }


    /**
     * Finish an engine action, as a governance service does when it has run.
     *
     * @param engineActionGUID action to finish
     * @param completionStatus how it ended
     * @throws Exception the completion could not be recorded
     */
    static void complete(String           engineActionGUID,
                         CompletionStatus completionStatus) throws Exception
    {
        getGovernanceClient().recordCompletionStatus(OMAGPlatformExtension.USER_ID,
                                                     engineActionGUID,
                                                     null,
                                                     completionStatus,
                                                     null,
                                                     null,
                                                     "Completed by server-fvt as " + completionStatus.getName());
    }


    /**
     * Read an engine action back.
     *
     * @param engineActionGUID action to read
     * @return the action
     * @throws Exception the action could not be read
     */
    static EngineActionElement getEngineAction(String engineActionGUID) throws Exception
    {
        return getGovernanceClient().getEngineAction(OMAGPlatformExtension.USER_ID, engineActionGUID);
    }


    /**
     * Return the status an engine action currently holds.
     *
     * @param engineActionGUID action to read
     * @return its status
     * @throws Exception the action could not be read
     */
    static ActivityStatus getStatus(String engineActionGUID) throws Exception
    {
        return getEngineAction(engineActionGUID).getActionStatus();
    }


    /**
     * Is this a status an engine action does not come back from?
     *
     * @param activityStatus status to examine
     * @return true when the action has finished
     */
    static boolean isTerminal(ActivityStatus activityStatus)
    {
        return (activityStatus == ActivityStatus.COMPLETED) || (activityStatus == ActivityStatus.INVALID) ||
                (activityStatus == ActivityStatus.IGNORED) || (activityStatus == ActivityStatus.FAILED) ||
                (activityStatus == ActivityStatus.CANCELLED) || (activityStatus == ActivityStatus.ABANDONED);
    }


    /**
     * Return the guids of every engine action the store currently reports as active.
     *
     * @return guids
     * @throws Exception the query failed
     */
    static List<String> activeActionGUIDs() throws Exception
    {
        return toGUIDs(getGovernanceClient().getActiveEngineActions(OMAGPlatformExtension.USER_ID,
                                                                    0,
                                                                    ServerFvtTestSupport.MAX_PAGE_SIZE));
    }


    /**
     * Return the guids of every engine action reported as claimed and in flight for a governance engine.
     *
     * @param governanceEngineGUID engine to ask about
     * @return guids
     * @throws Exception the query failed
     */
    static List<String> claimedActionGUIDs(String governanceEngineGUID) throws Exception
    {
        return toGUIDs(getGovernanceClient().getActiveClaimedEngineActions(OMAGPlatformExtension.USER_ID,
                                                                           governanceEngineGUID,
                                                                           0,
                                                                           ServerFvtTestSupport.MAX_PAGE_SIZE));
    }


    /**
     * Is this engine action reported as active?
     *
     * @param engineActionGUID action to look for
     * @return true when the active list holds it
     * @throws Exception the query failed
     */
    static boolean isActive(String engineActionGUID) throws Exception
    {
        return activeActionGUIDs().contains(engineActionGUID);
    }


    /**
     * Is this engine action reported as claimed and in flight for a governance engine?
     *
     * @param engineActionGUID action to look for
     * @param governanceEngineGUID engine to ask about
     * @return true when that engine's claimed list holds it
     * @throws Exception the query failed
     */
    static boolean isClaimedBy(String engineActionGUID,
                               String governanceEngineGUID) throws Exception
    {
        return claimedActionGUIDs(governanceEngineGUID).contains(engineActionGUID);
    }


    /**
     * Page through the active engine actions a page at a time and collect what comes back.
     * <br><br>
     * The loop ends when a page comes back empty, which is what a caller has to do: these queries return null
     * rather than an empty list when nothing matches.  It is also capped, so that a query which failed to
     * advance produces a failed assertion rather than a test that never finishes.
     *
     * @param pageSize how many to ask for at a time
     * @return guids, in the order the pages returned them
     * @throws Exception the query failed
     */
    static List<String> pageThroughActiveActions(int pageSize) throws Exception
    {
        EgeriaOpenGovernanceClient client = getGovernanceClient();

        List<String> collected = new ArrayList<>();
        int          startFrom = 0;
        int          pagesRead = 0;

        while (pagesRead < ServerFvtTestSupport.MAX_PAGE_SIZE)
        {
            List<EngineActionElement> page = client.getActiveEngineActions(OMAGPlatformExtension.USER_ID,
                                                                           startFrom,
                                                                           pageSize);

            if ((page == null) || (page.isEmpty()))
            {
                break;
            }

            collected.addAll(toGUIDs(page));

            startFrom = startFrom + pageSize;
            pagesRead++;
        }

        return collected;
    }


    /**
     * Reduce a list of engine actions to their guids.
     *
     * @param engineActions actions to reduce, possibly null
     * @return guids, never null
     */
    private static List<String> toGUIDs(List<EngineActionElement> engineActions)
    {
        List<String> guids = new ArrayList<>();

        if (engineActions != null)
        {
            for (EngineActionElement engineAction : engineActions)
            {
                if ((engineAction != null) && (engineAction.getElementHeader() != null))
                {
                    guids.add(engineAction.getElementHeader().getGUID());
                }
            }
        }

        return guids;
    }
}
