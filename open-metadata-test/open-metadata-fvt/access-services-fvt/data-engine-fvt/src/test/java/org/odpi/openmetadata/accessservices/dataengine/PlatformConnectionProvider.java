/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.junit.jupiter.params.provider.Arguments;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

public class PlatformConnectionProvider {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static final String USER = "user";
    private static final String BASE_URL = "base.url";
    private static final String SERVER_IN_MEMORY_NAME = "server.in-memory-graph.name";
    private static final String SERVER_IN_MEMORY_ENABLED = "server.in-memory-graph.enabled";
    private static final String SERVER_LOCAL_GRAPH_ENABLED = "server.local-graph.enabled";
    private static final String SERVER_LOCAL_GRAPH_NAME = "server.local-graph.name";

    private static DataEngineRESTClient dataEngineRESTClientInMemory;
    private static RepositoryService repositoryServiceInMemory;

    private static DataEngineRESTClient dataEngineRESTClientLocalGraph;
    private static RepositoryService repositoryServiceLocalGraph;

    protected static Stream<Arguments> getConnectionDetails() throws IOException, InvalidParameterException, NoSuchAlgorithmException, KeyStoreException,
            KeyManagementException {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String appConfigPath = rootPath + APPLICATION_PROPERTIES;

        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        String userId = appProps.getProperty(USER);
        String serverPlatformRootURL = appProps.getProperty(BASE_URL);

        List<Arguments> servers = new ArrayList<>();
        if(Boolean.parseBoolean(appProps.getProperty(SERVER_IN_MEMORY_ENABLED))) {
            String inMemoryServerName = appProps.getProperty(SERVER_IN_MEMORY_NAME);
            if(dataEngineRESTClientInMemory == null) {
                dataEngineRESTClientInMemory = new DataEngineRESTClient(inMemoryServerName, serverPlatformRootURL);
            }
            if(repositoryServiceInMemory == null) {
                repositoryServiceInMemory = new RepositoryService(inMemoryServerName, userId, serverPlatformRootURL);
            }
            servers.add(Arguments.of(userId, dataEngineRESTClientInMemory, repositoryServiceInMemory));
        }

        if(Boolean.parseBoolean(appProps.getProperty(SERVER_LOCAL_GRAPH_ENABLED))) {
            String localGraphServerName = appProps.getProperty(SERVER_LOCAL_GRAPH_NAME);
            if(dataEngineRESTClientLocalGraph == null) {
                dataEngineRESTClientLocalGraph = new DataEngineRESTClient(localGraphServerName, serverPlatformRootURL);
            }
            if(repositoryServiceLocalGraph == null) {
                repositoryServiceLocalGraph = new RepositoryService(localGraphServerName, userId, serverPlatformRootURL);
            }
            servers.add(Arguments.of(userId, dataEngineRESTClientLocalGraph, repositoryServiceLocalGraph));
        }
        return servers.stream();
    }
}
