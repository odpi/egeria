/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.junit.jupiter.params.provider.Arguments;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * The class is used to generate the arguments for the FVT tests. The arguments are taken from a properties file and used to
 * create proper helper services objects and proper data engine clients. The numner of Arguments in the Stream retrieved
 * gives the number of runs for each of the FVT methods. If in the properties file we have both in memory and local graph servers
 * enabled, then we will have 2 Arguments retrieved. For local runs of FVTs, we can disable one of the servers and run
 * against local platform.
 */
public class PlatformConnectionProvider {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static final String USER = "user";
    private static final String BASE_URL = "base.url";
    private static final String PORT = "server.port";
    private static final String SERVER_IN_MEMORY_NAME = "server.in-memory-graph.name";
    private static final String SERVER_IN_MEMORY_ENABLED = "server.in-memory-graph.enabled";
    private static final String SERVER_LOCAL_GRAPH_ENABLED = "server.local-graph.enabled";
    private static final String SERVER_LOCAL_GRAPH_NAME = "server.local-graph.name";

    private static DataEngineRESTClient dataEngineRESTClientInMemory;
    private static RepositoryService repositoryServiceInMemory;

    private static DataEngineRESTClient dataEngineRESTClientLocalGraph;
    private static RepositoryService repositoryServiceLocalGraph;

    protected static Stream<Arguments> getConnectionDetails() throws IOException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String appConfigPath = rootPath + APPLICATION_PROPERTIES;

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
        String userId = properties.getProperty(USER);
        String serverPlatformRootURL = properties.getProperty(BASE_URL) + ":" + properties.getProperty(PORT);

        List<Arguments> servers = new ArrayList<>();
        if (Boolean.parseBoolean(properties.getProperty(SERVER_IN_MEMORY_ENABLED))) {
            String inMemoryServerName = properties.getProperty(SERVER_IN_MEMORY_NAME);
            if (dataEngineRESTClientInMemory == null) {
                dataEngineRESTClientInMemory = new DataEngineRESTClient(inMemoryServerName, serverPlatformRootURL);
            }
            if (repositoryServiceInMemory == null) {
                repositoryServiceInMemory = new RepositoryService(inMemoryServerName, userId, serverPlatformRootURL);
            }
            servers.add(Arguments.of(userId, dataEngineRESTClientInMemory, repositoryServiceInMemory));
        }

        if(Boolean.parseBoolean(properties.getProperty(SERVER_LOCAL_GRAPH_ENABLED))) {
            String localGraphServerName = properties.getProperty(SERVER_LOCAL_GRAPH_NAME);
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
