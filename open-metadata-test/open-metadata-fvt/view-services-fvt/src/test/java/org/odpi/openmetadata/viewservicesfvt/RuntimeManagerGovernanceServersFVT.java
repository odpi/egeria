/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringRequestBody;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RuntimeManagerGovernanceServersFVT covers the Runtime Manager requests that operate engine hosts and
 * integration daemons: refreshing governance engine and integration group configuration, reading and
 * changing an integration connector's configuration, refreshing and restarting connectors, and publishing
 * OpenLineage events.
 * <br>
 * This suite runs only a metadata access store and a view server, so there is no engine host or integration
 * daemon for these requests to reach.  An engine host and an integration daemon are catalogued with
 * connections that point at the suite's platform, under server names that are not running there.  Each
 * request then gets as far as the platform - the view server builds the right connector from the catalogue
 * entry and calls the named server - and is refused because that server is not running on the platform.
 * The refusal names the server, which shows the request reached the server-specific client rather than
 * failing earlier on the catalogue entry or the request body.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RuntimeManagerGovernanceServersFVT
{
    private static final String ENGINE_HOST_NAME        = "viewServicesFvtEngineHost";
    private static final String INTEGRATION_DAEMON_NAME = "viewServicesFvtIntegrationDaemon";
    private static final String CONNECTOR_NAME          = "ViewServicesFvtConnector";

    private static RuntimeManagerFvtSupport.CataloguedResource engineHost        = null;
    private static RuntimeManagerFvtSupport.CataloguedResource integrationDaemon = null;


    /**
     * Catalogue the engine host and integration daemon.
     */
    @Test
    @Order(1)
    void catalogServers()
    {
        engineHost        = RuntimeManagerFvtSupport.catalogServer("engine-host",
                                                                   ENGINE_HOST_NAME,
                                                                   RuntimeManagerFvtSupport.ENGINE_HOST,
                                                                   RuntimeManagerFvtSupport.ENGINE_HOST_PROVIDER);
        integrationDaemon = RuntimeManagerFvtSupport.catalogServer("integration-daemon",
                                                                   INTEGRATION_DAEMON_NAME,
                                                                   RuntimeManagerFvtSupport.INTEGRATION_DAEMON,
                                                                   RuntimeManagerFvtSupport.INTEGRATION_DAEMON_PROVIDER);
    }


    /**
     * Refreshing one or all governance engines needs a running engine host.
     */
    @Test
    @Order(2)
    void engineHostRequestsNeedARunningEngineHost()
    {
        String root = "/engine-hosts/" + engineHost.assetGUID() + "/governance-engines";

        assertRefusedBy(ENGINE_HOST_NAME, RuntimeManagerFvtSupport.refuse(HttpMethod.GET, root + "/AssetSurvey/refresh-config", null));
        assertRefusedBy(ENGINE_HOST_NAME, RuntimeManagerFvtSupport.refuse(HttpMethod.GET, root + "/refresh-config", null));
    }


    /**
     * Every integration connector request needs a running integration daemon.
     */
    @Test
    @Order(3)
    void integrationDaemonRequestsNeedARunningIntegrationDaemon()
    {
        String root = "/integration-daemons/" + integrationDaemon.assetGUID();

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.GET, root + "/integration-connectors/" + CONNECTOR_NAME + "/configuration-properties", null));

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.POST,
                                                        root + "/integration-connectors/configuration-properties",
                                                        Map.of("class", "ConnectorConfigPropertiesRequestBody",
                                                               "connectorName", CONNECTOR_NAME,
                                                               "mergeUpdate", true,
                                                               "configurationProperties", Map.of("fvtProperty", "fvtValue"))));

        StringRequestBody networkAddress = new StringRequestBody();

        networkAddress.setString("https://localhost:9443");

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.POST, root + "/integration-connectors/" + CONNECTOR_NAME + "/endpoint-network-address", networkAddress));

        Connection connection = new Connection();

        connection.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Connection", "integration-connector"));
        connection.setDisplayName("Replacement connection for " + CONNECTOR_NAME);

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.POST, root + "/integration-connectors/" + CONNECTOR_NAME + "/connection", connection));

        NameRequestBody connectorName = new NameRequestBody();

        connectorName.setName(CONNECTOR_NAME);

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.POST, root + "/integration-connectors/refresh", connectorName));
        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.POST, root + "/integration-connectors/restart", connectorName));

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.GET, root + "/integration-groups/ViewServicesFvtGroup/refresh-config", null));
    }


    /**
     * Publishing OpenLineage events, as a string or as a run event, needs a running integration daemon.
     */
    @Test
    @Order(4)
    void openLineageEventsNeedARunningIntegrationDaemon()
    {
        String root = "/integration-daemons/" + integrationDaemon.assetGUID() + "/open-lineage-events";

        Map<String, Object> runEvent = Map.of("eventType", "START",
                                              "eventTime", "2026-10-18T10:00:00.000Z",
                                              "producer", "https://github.com/odpi/egeria/view-services-fvt",
                                              "schemaURL", "https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/RunEvent",
                                              "run", Map.of("runId", UUID.randomUUID().toString()),
                                              "job", Map.of("namespace", "view-services-fvt", "name", "runtime-manager-fvt"));

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.POST, root + "/publish-event", runEvent));

        String eventString = "{\"eventType\":\"COMPLETE\",\"eventTime\":\"2026-10-18T10:01:00.000Z\","
                + "\"producer\":\"https://github.com/odpi/egeria/view-services-fvt\","
                + "\"run\":{\"runId\":\"" + UUID.randomUUID() + "\"},"
                + "\"job\":{\"namespace\":\"view-services-fvt\",\"name\":\"runtime-manager-fvt\"}}";

        assertRefusedBy(INTEGRATION_DAEMON_NAME,
                        RuntimeManagerFvtSupport.refuse(HttpMethod.POST, root + "/publish-event-string", eventString));
    }


    /**
     * Remove the catalogue entries.
     */
    @Test
    @Order(99)
    void cleanUp()
    {
        RuntimeManagerFvtSupport.delete(integrationDaemon);
        RuntimeManagerFvtSupport.delete(engineHost);
    }


    /**
     * Check that a refusal came from the named server not being available.
     *
     * @param serverName server the request was routed to
     * @param response refused response
     */
    private static void assertRefusedBy(String   serverName,
                                        JsonNode response)
    {
        String message = response.path("exceptionErrorMessage").asText();

        assertTrue(message.startsWith("OMAG-MULTI-TENANT-404-001") && message.contains(" " + serverName + " "),
                   "the refusal does not say that " + serverName + " is not available: " + response);
    }
}
