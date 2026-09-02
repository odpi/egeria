/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.governanceservers.enginehostservices.client.EngineHostClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationDaemonStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GovernanceServerServicesFVT covers the control surfaces of the two governance servers: the <b>engine
 * host</b> and the <b>integration daemon</b>.
 * <br><br>
 * Both servers here are running with nothing to run - no content pack is loaded, so the governance engine
 * and the integration group they are configured with have no definitions to find.  See
 * {@link OMAGPlatformExtension} for why that is deliberate.  What it means for this class is that the
 * subject is the <em>control</em> surface rather than the work: can an operator find out what a governance
 * server is doing, and does it answer sensibly when the answer is "nothing".
 * <br><br>
 * That turns out to be the more valuable half to cover here, because it is the half an operator actually
 * uses and the half subscription-fvt does not test - that suite has content packs and therefore always asks
 * these questions of a server that is working.  A governance server that has failed to find its
 * configuration is the state an operator is looking at when they reach for these APIs, and "reports an empty
 * status" and "reports a server error" are very different answers to be given at that moment.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class GovernanceServerServicesFVT
{
    /**
     * An engine host should report on the engines it has been configured with, whether or not they have
     * found their definitions.
     * <br><br>
     * The engine here is configured but undefined, so the expected report is one entry describing an engine
     * that is not running - not an empty list, which would mean the engine host had forgotten what it was
     * configured with, and not a failure, which would leave an operator unable to see the configuration at
     * all at exactly the point they need it.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anEngineHostReportsTheEnginesItIsConfiguredWith() throws Exception
    {
        EngineHostClient client = ServerFvtTestSupport.getEngineHostClient();

        List<GovernanceEngineSummary> summaries = client.getGovernanceEngineSummaries();

        assertNotNull(summaries, "An engine host should report on its governance engines");
        assertFalse(summaries.isEmpty(),
                    "The engine host is configured with one governance engine, so it should report one even though" +
                            " that engine has no definition to load");

        boolean found = false;

        for (GovernanceEngineSummary summary : summaries)
        {
            assertNotNull(summary.getGovernanceEngineName(), "Every engine reported should be named");

            if (OMAGPlatformExtension.GOVERNANCE_ENGINE_NAME.equals(summary.getGovernanceEngineName()))
            {
                found = true;

                assertNotNull(summary.getGovernanceEngineStatus(),
                              "An engine that has not found its definition should still report a status, because" +
                                      " that status is how an operator learns it has not found it");
            }
        }

        assertTrue(found,
                   "The engine host should report the engine it was configured with, " +
                           OMAGPlatformExtension.GOVERNANCE_ENGINE_NAME + ", but reported " + summaries);
    }


    /**
     * An engine host should report on one named engine.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anEngineHostReportsOnASingleEngineByName() throws Exception
    {
        EngineHostClient client = ServerFvtTestSupport.getEngineHostClient();

        GovernanceEngineSummary summary = client.getGovernanceEngineSummary(OMAGPlatformExtension.GOVERNANCE_ENGINE_NAME);

        assertNotNull(summary, "An engine host should report on an engine it is configured with");
        assertEquals(OMAGPlatformExtension.GOVERNANCE_ENGINE_NAME, summary.getGovernanceEngineName(),
                     "The summary should be for the engine that was asked about");
    }


    /**
     * Asking an engine host about an engine it has never been configured with should be reported clearly,
     * and should name the engine.
     * <br><br>
     * The distinction this draws is between "configured but not running" - which
     * {@link #anEngineHostReportsOnASingleEngineByName} covers - and "not configured at all".  Those need
     * different actions from the operator, so they need to be told apart.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void askingAnEngineHostAboutAnUnknownEngineIsReportedClearly() throws Exception
    {
        EngineHostClient client = ServerFvtTestSupport.getEngineHostClient();

        final String unknownEngine = "serverFvtEngineThatWasNeverConfigured";

        Exception error = assertThrows(Exception.class,
                                       () -> client.getGovernanceEngineSummary(unknownEngine),
                                       "Asking about an engine that was never configured should be reported as a failure");

        String message = String.valueOf(error.getMessage());

        assertFalse(message.isBlank(), "A failure at a governance server's control API should say something");
        assertTrue(message.contains(unknownEngine),
                   "The message should name the engine that was asked about, so an operator can see a typo." +
                           "  It said: " + message);
    }


    /**
     * An engine host should accept a request to refresh all of its engines' configuration.
     * <br><br>
     * This call is addressed to the engine host rather than to an engine: "refresh whatever engines you
     * have".  So it is accepted by a host whose engines are not running, and is a no-op when there is nothing
     * to refresh - unlike {@link #refreshingAnUnknownEngineByNameIsRefused}, which names one particular engine
     * and is refused when this host has no such engine configured.
     * <br><br>
     * Like the named form, this retries the retrieval for engines that are configured but have not loaded, so
     * "refresh whatever engines you have" includes giving the stalled ones another chance.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anEngineHostAcceptsARefreshOfAllItsConfiguration() throws Exception
    {
        EngineHostClient client = ServerFvtTestSupport.getEngineHostClient();

        client.refreshConfig();
    }


    /**
     * Refreshing an engine that is configured but has not loaded its definition should be accepted, because
     * that is the request that may bring it into service.
     * <br><br>
     * An engine host holds a placeholder for every engine configured into it, and reports one whose
     * definition it has not managed to retrieve as {@code ASSIGNED}.  Refreshing such an engine by name
     * retries the retrieval - the definition may have been created in the metadata store since the last
     * attempt - so this is the operator's remedy, and refusing it refuses the one thing that could help.
     * <br><br>
     * This failed when it was first written, and the failure was hard to see because the message was
     * plausible: {@code refreshConfig} looked the handler up without retrying, found none, and reported
     * {@code ENGINE-HOST-SERVICES-400-005 ... is not running in the engine host ...} - the same message it
     * gives for an engine name that does not exist here at all.  So the remedy was refused, and it was
     * refused with wording that told the operator they had mistyped the name.
     * <br><br>
     * This suite's engine host has no metadata to load its engine from, so the engine stays {@code ASSIGNED}
     * afterwards.  That is not what is being asserted: the assertion is that the request is accepted and the
     * retry made.  Whether the definition turns up is the metadata store's business, not this call's.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void refreshingAnAssignedEngineByNameIsAccepted() throws Exception
    {
        EngineHostClient client = ServerFvtTestSupport.getEngineHostClient();

        GovernanceEngineSummary before = client.getGovernanceEngineSummary(OMAGPlatformExtension.GOVERNANCE_ENGINE_NAME);

        assertNotNull(before, "The engine host should report on the engine it is configured with");
        assertEquals(GovernanceEngineStatus.ASSIGNED, before.getGovernanceEngineStatus(),
                     "This suite's engine has no definition to load, so the host should report it as ASSIGNED -" +
                             " which is the state this test is about.  It reported " + before.getGovernanceEngineStatus());

        client.refreshConfig(OMAGPlatformExtension.GOVERNANCE_ENGINE_NAME);
    }


    /**
     * Refreshing an engine name the host has nothing configured for should be refused, and the refusal should
     * name it.
     * <br><br>
     * This is the other half of {@link #refreshingAnAssignedEngineByNameIsAccepted}, and the pair is the
     * point: {@code refreshConfig(engineName)} names one particular engine, so a name that does not exist
     * here is the caller's mistake and has to be reported as one - while a name that does exist, even as an
     * engine that has not started, must not be.  Before the fix both produced the same error, so an operator
     * could not tell a typo from an engine waiting for its definition, and the two need different remedies.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void refreshingAnUnknownEngineByNameIsRefused() throws Exception
    {
        EngineHostClient client = ServerFvtTestSupport.getEngineHostClient();

        final String unknownEngine = "serverFvtEngineThatWasNeverConfigured";

        Exception error = assertThrows(Exception.class,
                                       () -> client.refreshConfig(unknownEngine),
                                       "Refreshing an engine this host has nothing configured for should be refused");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.contains(unknownEngine),
                   "The refusal should name the engine that was asked for, because the caller named something" +
                           " particular.  It said: " + message);
    }


    /**
     * A null engine name should be refused rather than treated as "all engines".
     * <br><br>
     * The two calls are next to each other on this client - {@code refreshConfig()} and
     * {@code refreshConfig(name)} - so a null that quietly became the first would turn a caller's mistake
     * into a much larger operation than they asked for.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNullEngineNameIsRefusedRatherThanTreatedAsAll() throws Exception
    {
        EngineHostClient client = ServerFvtTestSupport.getEngineHostClient();

        Exception error = assertThrows(Exception.class,
                                       () -> client.refreshConfig(null),
                                       "A null engine name should be refused rather than refreshing every engine");

        assertNotNull(error.getMessage(), "Refusing a null engine name should say what was wrong");
    }


    /**
     * An integration daemon should report its status.
     * <br><br>
     * This failed when it was first written, with a {@code NullPointerException} raised inside the server:
     * {@code Cannot invoke "java.util.List.iterator()" because "connectorIds" is null}.
     * {@code IntegrationConnectorCacheMap.getConnectorIds()} returns null rather than an empty list when the
     * daemon has no connectors, following the same empty-means-null idiom the response beans use, and three
     * of the four places that iterate it did not check.
     * <br><br>
     * The daemon here has no connectors, because its integration group has no definition to load - and that
     * is exactly the state an operator is in when they call this.  Asking a healthy daemon for its status is
     * not where anybody reaches for this API; asking one that appears to be doing nothing is.  So the one
     * case where the call had to work was the case that raised a null pointer.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anIntegrationDaemonReportsItsStatus() throws Exception
    {
        IntegrationDaemon client = ServerFvtTestSupport.getIntegrationDaemonClient();

        IntegrationDaemonStatus status = client.getIntegrationDaemonStatus();

        assertNotNull(status, "An integration daemon should report its status");
    }


    /**
     * An integration daemon with no connectors should accept a request to refresh all of them.
     * <br><br>
     * This is the same defect as {@link #anIntegrationDaemonReportsItsStatus} reached by a second route, and
     * it is here because the suite did not find it - the fix did.  Once the null list was understood rather
     * than merely reported, {@code refreshConnector(null)} and {@code restartConnector(null)} turned out to
     * iterate it without a check as well, so a daemon with nothing loaded raised a
     * {@code NullPointerException} for these too.
     * <br><br>
     * Refreshing every connector when there are none is a no-op, not a failure: an operator asking a stalled
     * daemon to try again should be told it did, not handed a null pointer.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anIntegrationDaemonWithNoConnectorsAcceptsARefreshOfThemAll() throws Exception
    {
        IntegrationDaemon client = ServerFvtTestSupport.getIntegrationDaemonClient();

        client.refreshConnectors();
    }


    /**
     * An integration daemon with no connectors should accept a request to restart all of them.
     * <br><br>
     * The third route to the same null list - see
     * {@link #anIntegrationDaemonWithNoConnectorsAcceptsARefreshOfThemAll}.  Restarting is what an operator
     * reaches for when refreshing has not helped, so it is the least useful of the three to fail.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anIntegrationDaemonWithNoConnectorsAcceptsARestartOfThemAll() throws Exception
    {
        IntegrationDaemon client = ServerFvtTestSupport.getIntegrationDaemonClient();

        client.restartConnectors();
    }


    /**
     * An integration daemon should report on the integration groups it has been configured with.
     * <br><br>
     * As with the engine host, the group here is configured but undefined, and the expected answer is a
     * report of a group that is not running rather than silence.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anIntegrationDaemonReportsTheGroupsItIsConfiguredWith() throws Exception
    {
        IntegrationDaemon client = ServerFvtTestSupport.getIntegrationDaemonClient();

        List<IntegrationGroupSummary> summaries = client.getIntegrationGroupSummaries();

        assertNotNull(summaries, "An integration daemon should report on its integration groups");
        assertFalse(summaries.isEmpty(),
                    "The integration daemon is configured with one integration group, so it should report one even" +
                            " though that group has no definition to load");

        boolean found = false;

        for (IntegrationGroupSummary summary : summaries)
        {
            if (OMAGPlatformExtension.INTEGRATION_GROUP_NAME.equals(summary.getIntegrationGroupName()))
            {
                found = true;
            }
        }

        assertTrue(found,
                   "The integration daemon should report the group it was configured with, " +
                           OMAGPlatformExtension.INTEGRATION_GROUP_NAME + ", but reported " + summaries);
    }


    /**
     * Asking an integration daemon about a group it has never been configured with should be reported
     * clearly, and should name the group.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void askingAnIntegrationDaemonAboutAnUnknownGroupIsReportedClearly() throws Exception
    {
        IntegrationDaemon client = ServerFvtTestSupport.getIntegrationDaemonClient();

        final String unknownGroup = "serverFvtGroupThatWasNeverConfigured";

        Exception error = assertThrows(Exception.class,
                                       () -> client.getIntegrationGroupSummary(unknownGroup),
                                       "Asking about a group that was never configured should be reported as a failure");

        String message = String.valueOf(error.getMessage());

        assertFalse(message.isBlank(), "A failure at a governance server's control API should say something");
        assertTrue(message.contains(unknownGroup),
                   "The message should name the group that was asked about.  It said: " + message);
    }


    /**
     * Asking an integration daemon to refresh a connector it does not have should be reported clearly.
     * <br><br>
     * This is the most-used operation on an integration daemon - it is how an operator makes a connector run
     * now rather than at its next scheduled refresh - and a typo in the connector name is the obvious way to
     * get it wrong.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void refreshingAConnectorThatIsNotThereIsReportedClearly() throws Exception
    {
        IntegrationDaemon client = ServerFvtTestSupport.getIntegrationDaemonClient();

        final String unknownConnector = "serverFvtConnectorThatWasNeverConfigured";

        Exception error = assertThrows(Exception.class,
                                       () -> client.refreshConnector(unknownConnector),
                                       "Refreshing a connector the daemon does not have should be reported as a failure");

        String message = String.valueOf(error.getMessage());

        assertFalse(message.isBlank(), "A failure at a governance server's control API should say something");
        assertTrue(message.contains(unknownConnector),
                   "The message should name the connector that was asked for.  It said: " + message);
    }


    /**
     * An integration daemon should validate a connector provider class, and refuse one it cannot load by
     * name.
     * <br><br>
     * This is the same operation the four engine services publish, on a different server type - see
     * {@link EngineServicesFVT} - and it is worth asking of both, because they are separate implementations
     * of the same idea and are not obliged to agree.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void anIntegrationDaemonRefusesAnUnknownConnectorByName() throws Exception
    {
        IntegrationDaemon client = ServerFvtTestSupport.getIntegrationDaemonClient();

        final String unknownProvider = "org.odpi.openmetadata.serverfvt.NoSuchConnectorProvider";

        Exception error = assertThrows(Exception.class,
                                       () -> client.validateConnector(unknownProvider),
                                       "An integration daemon should refuse a connector provider it cannot load");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.contains(unknownProvider),
                   "The message should name the class that could not be loaded.  It said: " + message);
    }
}
