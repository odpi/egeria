/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bitolfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
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
 * Checks that the Bitol content pack loaded, and that the integration daemon started every connector it
 * defines.  What the pack is supposed to contain is not written out here: the test iterates the definitions in
 * {@code core-content-pack} filtered to the Bitol pack, so adding a connector extends this test's coverage
 * without editing it - and adding one without regenerating the archive fails here.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ContentPackFVT
{
    /**
     * The integration connectors the Bitol content pack defines.
     *
     * @return the pack's integration connectors
     */
    static Stream<IntegrationConnectorDefinition> bitolIntegrationConnectors()
    {
        return Stream.of(IntegrationConnectorDefinition.values())
                     .filter(definition -> definition.getContentPackDefinition() == ContentPackDefinition.BITOL_CONTENT_PACK);
    }


    @Test
    @DisplayName("The Bitol integration group is in the repository")
    public void testIntegrationGroupIsDefined() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement integrationGroup = openMetadataStore.getMetadataElementByUniqueName(OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName(),
                                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(integrationGroup, "Integration group " + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName() + " is not in the repository - the Bitol content pack did not load.");
        assertEquals(OMAGPlatformExtension.INTEGRATION_GROUP.getGUID(), integrationGroup.getElementGUID(), "Integration group is in the repository under a different GUID than the content pack defines.");
    }


    /**
     * Every integration connector the pack defines is in the repository under the identity the pack gives it.
     *
     * @param connectorDefinition connector to check
     * @throws Exception the repository could not be read
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("bitolIntegrationConnectors")
    @DisplayName("Each Bitol integration connector is in the repository")
    public void testIntegrationConnectorIsDefined(IntegrationConnectorDefinition connectorDefinition) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement connector = openMetadataStore.getMetadataElementByUniqueName(connectorDefinition.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(connector, "Integration connector " + connectorDefinition.getQualifiedName() + " is not in the repository.");
        assertEquals(connectorDefinition.getGUID(), connector.getElementGUID(), "Integration connector " + connectorDefinition.getDisplayName() + " is in the repository under a different GUID than the content pack defines.");
    }


    /**
     * The integration daemon found the group, started every connector registered with it, and none failed.
     *
     * @throws Exception the integration daemon could not be reached
     */
    @Test
    @DisplayName("The integration daemon is running the Bitol integration group")
    public void testIntegrationDaemonIsRunningTheGroup() throws Exception
    {
        IntegrationGroupSummary summary = OMAGPlatformExtension.getIntegrationDaemonClient().getIntegrationGroupSummary(OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName());

        assertNotNull(summary, "Integration daemon reports no integration group named " + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName() + ".");
        assertEquals(IntegrationGroupStatus.RUNNING, summary.getIntegrationGroupStatus(), "Integration group is not running.");

        List<String> runningConnectors = new ArrayList<>();

        if (summary.getIntegrationConnectorReports() != null)
        {
            for (IntegrationConnectorReport connectorReport : summary.getIntegrationConnectorReports())
            {
                assertNull(connectorReport.getFailingExceptionMessage(), "Integration connector " + connectorReport.getConnectorName() + " failed to start: " + connectorReport.getFailingExceptionMessage());
                runningConnectors.add(connectorReport.getConnectorName());
            }
        }

        for (IntegrationConnectorDefinition connectorDefinition : bitolIntegrationConnectors().toList())
        {
            assertTrue(runningConnectors.contains(connectorDefinition.getConnectorName()),
                       "The integration daemon did not start connector '" + connectorDefinition.getConnectorName() + "'.  It started: " + runningConnectors);
        }
    }
}
