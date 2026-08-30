/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CataloguersFVT checks the integration connectors the Files content pack ships - that the integration daemon
 * has every one of them, and that each one started.
 * <br>
 * {@link FolderCatalogFVT} takes one of them, the General Folder Cataloguer, all the way through cataloguing
 * a directory.  That cannot be done for the rest of them here: each of the others monitors a fixed directory
 * named in its own connection - {@code content-packs}, {@code loading-bay/sample-data}, {@code secrets} - which
 * belongs to a real Egeria deployment's layout rather than to a test, and the last of them, the Maintain Last
 * Update Date connector, watches metadata rather than a directory at all.  Pointing them somewhere else would
 * be testing a configuration this suite invented.
 * <br>
 * What is worth asserting is what the daemon reports about them, and it is not a formality.  An integration
 * connector is instantiated from a connection stored in the repository, by class name: a connector missing
 * from the runtime classpath, or one whose connection is wrong, does not fail the build - it fails to start,
 * inside a server, and is only visible in a status report or the audit log.  A connector that has stopped
 * reports the exception that stopped it, and that is checked here too: a cataloguer that failed on its own
 * missing directory would otherwise look identical to one that is working.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class CataloguersFVT
{
    /**
     * The integration connectors the Files content pack defines, taken from the pack's own definitions so
     * that adding one to the pack extends this test without anybody editing it.
     *
     * @return the connectors that should be running
     */
    static Stream<IntegrationConnectorDefinition> filesIntegrationConnectors()
    {
        return Stream.of(IntegrationConnectorDefinition.values())
                     .filter(definition -> definition.getIntegrationGroupDefinition() == OMAGPlatformExtension.INTEGRATION_GROUP);
    }


    @Test
    @DisplayName("The Files content pack defines integration connectors")
    void theContentPackDefinesIntegrationConnectors()
    {
        assertTrue(filesIntegrationConnectors().findAny().isPresent(),
                   "No integration connector in the content pack definitions belongs to "
                           + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName()
                           + ", so the checks below are testing nothing.");
    }


    /**
     * Check that the integration daemon has this connector, and that it started.
     *
     * @param connector connector under test
     * @throws Exception problem talking to the integration daemon
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("filesIntegrationConnectors")
    void theIntegrationDaemonIsRunningTheConnector(IntegrationConnectorDefinition connector) throws Exception
    {
        IntegrationGroupSummary summary = OMAGPlatformExtension.getIntegrationDaemonClient()
                .getIntegrationGroupSummary(OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName());

        assertNotNull(summary,
                      "The integration daemon reported nothing for group "
                              + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName() + ".");

        assertNotNull(summary.getIntegrationConnectorReports(),
                      "The integration daemon reported group "
                              + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName() + " with no connectors in it.");

        IntegrationConnectorReport report = null;

        for (IntegrationConnectorReport candidate : summary.getIntegrationConnectorReports())
        {
            if ((candidate != null) && (connector.getConnectorName().equals(candidate.getConnectorName())))
            {
                report = candidate;
                break;
            }
        }

        assertNotNull(report,
                      "The integration daemon is not running " + connector.getConnectorName() + ", although the "
                              + "Files content pack puts it in " + OMAGPlatformExtension.INTEGRATION_GROUP.getQualifiedName()
                              + ".  Connectors it is running: " + getConnectorNames(summary));

        /*
         * A connector that failed to start records why.  Reporting that message is the whole point of this
         * check - the alternative is a cataloguer that silently never runs.
         */
        assertNull(report.getFailingExceptionMessage(),
                   connector.getConnectorName() + " is registered with the integration daemon but is not running: "
                           + report.getFailingExceptionMessage());

        assertEquals(connector.getConnectorName(), report.getConnectorName(),
                     "The integration daemon reported a connector under a different name than the content pack gives it.");
    }


    /**
     * Return the names of the connectors the daemon is running, for a failure message that says what was
     * found instead.
     *
     * @param summary what the daemon reported
     * @return readable list of connector names
     */
    private String getConnectorNames(IntegrationGroupSummary summary)
    {
        List<String> names = new ArrayList<>();

        if (summary.getIntegrationConnectorReports() != null)
        {
            for (IntegrationConnectorReport report : summary.getIntegrationConnectorReports())
            {
                if (report != null)
                {
                    names.add(report.getConnectorName());
                }
            }
        }

        return names.toString();
    }
}
