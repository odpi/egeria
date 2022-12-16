/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogEvent;
import org.odpi.openmetadata.adapters.connectors.integration.elasticsearch.ffdc.ElasticsearchIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.integrationservices.search.connector.SearchIntegratorConnector;
import org.odpi.openmetadata.integrationservices.search.connector.SearchIntegratorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static org.odpi.openmetadata.adapters.connectors.integration.elasticsearch.ffdc.ElasticsearchIntegratorErrorCode.BAD_CONFIG;


/**
 * ElasticsearchIntegrationConnector provides common methods for the connector in this module.
 */
public class ElasticsearchIntegrationConnector extends SearchIntegratorConnector {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchIntegrationConnector.class);
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();
    private static final String INDEX_NAME = "indexName";
    private static final String ASSETS_INDEX_NAME = "assets";

    private String targetRootURL = null;
    private String targetRootProtocol = null;
    private SearchIntegratorContext myContext = null;
    private ElasticsearchClient client;
    private String indexName = "test";

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId  - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId, connectionProperties);

        org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null) {
            targetRootURL = endpoint.getAddress();
            targetRootProtocol = endpoint.getProtocol();

        }

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        String configuredIndexName = (String) configurationProperties.get(INDEX_NAME);
        this.indexName = Objects.requireNonNullElse(configuredIndexName, ASSETS_INDEX_NAME);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException {
        super.start();

        final String methodName = "start";

        initializeElasticSearchClient(methodName);

        myContext = super.getContext();

    }

    /**
     * <p>
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public synchronized void refresh() throws ConnectorCheckedException {
        final String methodName = "refresh";
    }


    /**
     * Shutdown monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public synchronized void disconnect() throws ConnectorCheckedException {
        final String methodName = "disconnect";

        log.debug("disconnecting");
        if (auditLog != null) {
            auditLog.logMessage(methodName,
                    ElasticsearchIntegrationConnectorAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }

    public void initializeElasticSearchClient(String callingMethodName) throws ConnectorCheckedException {
        String[] urlParts = targetRootURL.split(":");
        String hostname = urlParts[0];
        int port;
        try {
            port = Integer.parseInt(urlParts[1]);
        } catch (NumberFormatException e) {
            log.debug("received exception trying to determine port " + e.getMessage());
            throw new ConnectorCheckedException(BAD_CONFIG.getMessageDefinition("port", "targetRootURL", callingMethodName, e.getMessage()), this.getClass().getName(),
                    callingMethodName);
        }
        RestClient restClient = RestClient.builder(new HttpHost(hostname, port, targetRootProtocol)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);

    }

    /**
     * Save the events received from asset catalog to the Elasticsearch service
     *
     * @param assetCatalogEvent the event which contains the asset
     */
    @Override
    public void saveAsset(AssetCatalogEvent assetCatalogEvent) {
        Asset asset = assetCatalogEvent.getAsset();
        if (asset == null) {
            return;
        }
        if (asset.getGUID() == null) {
            return;
        }
        log.debug("saving to elasticsearch {}", asset);
        try {
            String jsonAsset = OBJECT_WRITER.writeValueAsString(asset);
            client.index(s-> s.index(jsonAsset).index(indexName).id(asset.getGUID()));

        } catch (IOException ioException) {
            String actionDescription = "The client could not write to the Elasticsearch cluster";
            auditLog.logException(actionDescription, ElasticsearchIntegrationConnectorAuditCode.IO_EXCEPTION.getMessageDefinition(), ioException);
        }
    }
}
