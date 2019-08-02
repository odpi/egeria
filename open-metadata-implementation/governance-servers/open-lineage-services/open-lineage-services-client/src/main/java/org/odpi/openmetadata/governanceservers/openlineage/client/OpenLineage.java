/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client;

import org.odpi.openmetadata.governanceservers.openlineage.model.Graphs;
import org.odpi.openmetadata.governanceservers.openlineage.model.Queries;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;

public class OpenLineage  {

    private String serverName;
    private String omasServerURL;
    private RestTemplate restTemplate;

    /**
     * Create a new OpenLineage client.
     *
     * @param serverName   name of the server to connect to
     * @param newServerURL the network address of the server running the OMAS REST servers
     */
    public OpenLineage(String serverName,
                       String newServerURL) {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
        this.restTemplate = new RestTemplate();
    }

    public String initialGraph(String userId, Queries lineageQuery, Graphs graph, String guid) throws InvalidParameterException {
        String methodName = "initialGraph";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/initial-graph/{2}/{3}/{4}";
        return getRestCall(url, String.class, userId, serverName, lineageQuery, graph, guid);

    }

    public String dumpGraph(String userId, Graphs graph) throws InvalidParameterException {
        String methodName = "dumpGraph";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/dump/{2}";
        return getRestCall(url, String.class, userId, serverName, graph);

    }

    public String exportGraph(String userId, Graphs graph) throws InvalidParameterException {
        String methodName = "exportGraph";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/export/{2}";
        return getRestCall(url, String.class, userId, serverName, graph);

    }

    public String generateMockGraph(String userId) throws InvalidParameterException {
        String methodName = "generateMockGraph";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/generate-mock-graph";
        return getRestCall(url, String.class, userId, serverName);
    }

    private <T> T getRestCall(String url, Class<T> clazz, Object... params){
        return restTemplate.getForObject(omasServerURL + url, clazz, params);
    }

}