/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.GovernanceEngineDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that the Files content pack loaded, and that the two governance servers picked up the parts of
 * it that they are configured to run.
 * <br>
 * This is the suite's foundation, and it is deliberately the least interesting test in it.  Everything else
 * here asks a governance server to <em>do</em> something and then checks what arrived in the repository.  That
 * only means anything if the definitions those servers work from are present and correct, so this test
 * establishes that first: an engine action that is never claimed and a connector that is never registered both
 * look like the same silence from a test that starts by asking for work.
 * <br>
 * What the content pack is supposed to contain is not written out here.  The definitions in
 * {@code core-content-pack} are the source the archive was generated from, so the test iterates those,
 * filtered to the Files pack, and checks the repository against them.  Adding a connector, an engine or a
 * request type to the pack therefore extends this test's coverage without anybody editing it - and, more to
 * the point, adding one and forgetting to regenerate the archive fails here.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ContentPackFVT
{
    /**
     * The integration connectors the Files content pack defines.  Supplied to the parameterized test
     * below; a change to the pack changes the number of cases that run.
     *
     * @return the pack's integration connectors
     */
    static Stream<IntegrationConnectorDefinition> filesIntegrationConnectors()
    {
        return Stream.of(IntegrationConnectorDefinition.values())
                     .filter(definition -> definition.getContentPackDefinition() == ContentPackDefinition.FILES_CONTENT_PACK);
    }


    /**
     * The governance engines the Files content pack defines.
     *
     * @return the pack's governance engines
     */
    static Stream<GovernanceEngineDefinition> filesGovernanceEngines()
    {
        return Stream.of(GovernanceEngineDefinition.values())
                     .filter(definition -> definition.getContentPackDefinition() == ContentPackDefinition.FILES_CONTENT_PACK);
    }


    /**
     * The request types the Files content pack defines - one governance action type each, joining a
     * request type to the governance service that answers it.
     *
     * @return the pack's request types
     */
    static Stream<RequestTypeDefinition> filesRequestTypes()
    {
        return Stream.of(RequestTypeDefinition.values())
                     .filter(definition -> definition.getContentPackDefinition() == ContentPackDefinition.FILES_CONTENT_PACK);
    }


    @Test
    @DisplayName("The Files integration group is in the repository")
    public void testIntegrationGroupIsDefined() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement integrationGroup =
                openMetadataStore.getMetadataElementByUniqueName(OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName(),
                                                                 OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(integrationGroup,
                      "Integration group " + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName()
                              + " is not in the repository - the Files content pack did not load.");

        assertEquals(OMAGPlatformExtension.INTEGRATION_GROUP.getGUID(),
                     integrationGroup.getElementGUID(),
                     "Integration group " + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName()
                             + " is in the repository under a different GUID than the content pack defines.");
    }


    /**
     * Every integration connector the pack defines is in the repository, under the identity the pack gives it.
     *
     * @param connectorDefinition connector to check
     * @throws Exception the repository could not be read
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("filesIntegrationConnectors")
    @DisplayName("Each PostgreSQL integration connector is in the repository")
    public void testIntegrationConnectorIsDefined(IntegrationConnectorDefinition connectorDefinition) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement connector = openMetadataStore.getMetadataElementByUniqueName(connectorDefinition.getQualifiedName(),
                                                                                          OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(connector,
                      "Integration connector " + connectorDefinition.getQualifiedName() + " is not in the repository.");

        assertEquals(connectorDefinition.getGUID(),
                     connector.getElementGUID(),
                     "Integration connector " + connectorDefinition.getDisplayName()
                             + " is in the repository under a different GUID than the content pack defines.");
    }


    /**
     * Every governance engine the pack defines is in the repository, under the identity the pack gives it.
     *
     * @param engineDefinition engine to check
     * @throws Exception the repository could not be read
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("filesGovernanceEngines")
    @DisplayName("Each PostgreSQL governance engine is in the repository")
    public void testGovernanceEngineIsDefined(GovernanceEngineDefinition engineDefinition) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement engine = openMetadataStore.getMetadataElementByUniqueName(engineDefinition.getName(),
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(engine, "Governance engine " + engineDefinition.getName() + " is not in the repository.");

        assertEquals(engineDefinition.getGUID(),
                     engine.getElementGUID(),
                     "Governance engine " + engineDefinition.getName()
                             + " is in the repository under a different GUID than the content pack defines.");

        assertEquals(engineDefinition.getType(),
                     engine.getType().getTypeName(),
                     "Governance engine " + engineDefinition.getName() + " is the wrong open metadata type."
                             + "  The engine type decides which engine service runs its services, so a survey engine"
                             + " defined as a governance action engine would accept its request types and then fail to"
                             + " run any of them.");
    }


    /**
     * Every request type the pack defines has a governance action type in the repository - the element a
     * curator names when asking for that request type to be run.
     * <br>
     * This is the join between the two halves of the pack: a request type names a governance engine and a
     * governance service, and the governance action type is what makes that pairing addressable.  Its
     * qualified name is built from both, which is why it is derived here rather than typed out.
     *
     * @param requestTypeDefinition request type to check
     * @throws Exception the repository could not be read
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("filesRequestTypes")
    @DisplayName("Each PostgreSQL request type has a governance action type")
    public void testRequestTypeIsDefined(RequestTypeDefinition requestTypeDefinition) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        String qualifiedName = requestTypeDefinition.getGovernanceEngine().getName()
                                       + "::" + requestTypeDefinition.getGovernanceRequestType();

        OpenMetadataElement governanceActionType = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(governanceActionType,
                      "Governance action type " + qualifiedName + " is not in the repository, so request type '"
                              + requestTypeDefinition.getGovernanceRequestType() + "' cannot be asked for by name.");
    }




    /**
     * The integration daemon found the group, started every connector registered with it, and none of them
     * failed.
     * <br>
     * A connector that reports a failing exception here is almost always a class loading problem: the
     * connector's provider class is named in the content pack but its implementation is not on the integration
     * daemon's classpath.  The message says so, because the fix is in this module's build.gradle rather than
     * anywhere the audit log would point at.
     *
     * @throws Exception the integration daemon could not be reached
     */
    @Test
    @DisplayName("The integration daemon is running the PostgreSQL integration group")
    public void testIntegrationDaemonIsRunningTheGroup() throws Exception
    {
        IntegrationGroupSummary summary = OMAGPlatformExtension.getIntegrationDaemonClient()
                                                  .getIntegrationGroupSummary(OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName());

        assertNotNull(summary,
                      "Integration daemon " + OMAGPlatformExtension.INTEGRATION_DAEMON_NAME + " reports no integration group named "
                              + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName() + ".");

        assertEquals(IntegrationGroupStatus.RUNNING,
                     summary.getIntegrationGroupStatus(),
                     "Integration group " + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName() + " is not running.");

        List<String> runningConnectors = new ArrayList<>();

        if (summary.getIntegrationConnectorReports() != null)
        {
            for (IntegrationConnectorReport connectorReport : summary.getIntegrationConnectorReports())
            {
                assertNull(connectorReport.getFailingExceptionMessage(),
                           "Integration connector " + connectorReport.getConnectorName() + " failed to start: "
                                   + connectorReport.getFailingExceptionMessage());

                runningConnectors.add(connectorReport.getConnectorName());
            }
        }

        /*
         * Checked against the content pack's own definitions rather than a fixed list, so that a connector
         * added to the group has to be running for this test to pass.
         */
        for (IntegrationConnectorDefinition connectorDefinition : filesIntegrationConnectors().toList())
        {
            assertTrue(runningConnectors.contains(connectorDefinition.getConnectorName()),
                       "The integration daemon did not start connector '" + connectorDefinition.getConnectorName()
                               + "' from group " + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName()
                               + ".  It started: " + runningConnectors);
        }
    }


    /**
     * The engine host found both engines and, for each one, the full set of request types the content pack
     * says that engine answers.
     * <br>
     * The request types are the part that matters.  An engine that reaches RUNNING has found its definition;
     * an engine that has also collected its request types has found the governance services behind them and
     * built a connection for each.  A request type that is missing here is one that a curator can ask for and
     * that will then sit unclaimed for ever.
     *
     * @throws Exception the engine host could not be reached
     */
    @Test
    @DisplayName("The engine host is running the PostgreSQL governance engines with all their request types")
    public void testEngineHostIsRunningTheEngines() throws Exception
    {
        List<GovernanceEngineSummary> summaries = OMAGPlatformExtension.getEngineHostClient().getGovernanceEngineSummaries();

        assertNotNull(summaries, "Engine host " + OMAGPlatformExtension.ENGINE_HOST_NAME + " reports no governance engines.");

        for (GovernanceEngineDefinition engineDefinition : filesGovernanceEngines().toList())
        {
            GovernanceEngineSummary engineSummary = null;

            for (GovernanceEngineSummary summary : summaries)
            {
                if (engineDefinition.getName().equals(summary.getGovernanceEngineName()))
                {
                    engineSummary = summary;
                    break;
                }
            }

            assertNotNull(engineSummary,
                          "Engine host " + OMAGPlatformExtension.ENGINE_HOST_NAME + " is not running engine "
                                  + engineDefinition.getName() + ".");

            assertEquals(GovernanceEngineStatus.RUNNING,
                         engineSummary.getGovernanceEngineStatus(),
                         "Governance engine " + engineDefinition.getName() + " is not running.");

            List<String> supportedRequestTypes = (engineSummary.getGovernanceRequestTypes() == null)
                                                         ? List.of() : engineSummary.getGovernanceRequestTypes();

            for (RequestTypeDefinition requestTypeDefinition : filesRequestTypes().toList())
            {
                if (requestTypeDefinition.getGovernanceEngine() == engineDefinition)
                {
                    assertTrue(supportedRequestTypes.contains(requestTypeDefinition.getGovernanceRequestType()),
                               "Governance engine " + engineDefinition.getName() + " is running but does not support request type '"
                                       + requestTypeDefinition.getGovernanceRequestType() + "'.  It supports: " + supportedRequestTypes);
                }
            }
        }
    }
}
