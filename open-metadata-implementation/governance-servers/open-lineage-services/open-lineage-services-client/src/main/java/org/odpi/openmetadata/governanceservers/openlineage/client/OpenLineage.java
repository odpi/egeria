/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client;

import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;

public class OpenLineage  {

    private String serverName;
    private String serverURL;
    private RestTemplate restTemplate;

    /**
     * Create a new OpenLineage client.
     *
     * @param serverName   name of the server to connect to.
     * @param newServerURL the network address of the server running the governance engine.
     */
    public OpenLineage(String serverName,
                       String newServerURL) {
        this.serverName = serverName;
        this.serverURL = newServerURL;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
     * the condensed path by querying a different method.
     *
     * @param userId calling user.
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @param scope ULTIMATE_SOURCE, ULTIMATE_DESTINATION, GLOSSARY.
     * @param view TABLE_VIEW, COLUMN_VIEW.
     * @param guid The guid of the node of which the lineage is queried of.
     * @return A subgraph containing all relevant paths, in graphSON format.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
  //  public LineageVerticesAndEdges lineage(String userId, GraphName graphName, Scope scope, View view, String guid) throws InvalidParameterException {
    public String lineage(String userId, GraphName graphName, Scope scope, View view, String guid) throws InvalidParameterException {
        String methodName = "lineage";
        String url = "/servers/{0}/open-metadata/open-lineage/users/{1}/lineage/sources/{2}/scopes/{3}/views/{4}/entities/{5}";
        return getRestCall(url, String.class, serverName, userId, graphName.getText(), scope.getValue(), view.getValue(), guid);
//        LineageResponse lineageResponse = getRestCall(url, LineageResponse.class, serverName, userId, graphName.getText(), scope.getValue(), view.getValue(), guid);
//        LineageVerticesAndEdges lineageVerticesAndEdges = lineageResponse.getLineageVerticesAndEdges();
//        return lineageVerticesAndEdges;
    }

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param userId calling user.
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @return Voidresponse.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public VoidResponse dumpGraph(String userId, GraphName graphName) throws InvalidParameterException {
        String methodName = "dumpGraph";
        String url = "/servers/{0}/open-metadata/open-lineage/users/{1}/dump/graphs/{2}";
        return getRestCall(url, VoidResponse.class, serverName, userId, graphName.getText());

    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @param userId calling user.
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @return The queried graph, in graphSON format.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public String exportGraph(String userId, GraphName graphName) throws InvalidParameterException {
        String methodName = "exportGraph";
        String url = "/servers/{0}/open-metadata/open-lineage/users/{1}/export/graphs/{2}";
        return getRestCall(url, String.class, serverName, userId, graphName.getText());

    }

    /**
     * Generate the MOCK graph, which can be used for performance testing, or demoing lineage with large amounts of
     * data.
     *
     * @param userId calling user.
     * @return Voidresponse.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public String generateMockGraph(String userId) throws InvalidParameterException {
        String methodName = "generateMockGraph";
        String url = "/servers/{0}/open-metadata/open-lineage/users/{1}/generate-mock-graph";
        return getRestCall(url, String.class, serverName, userId);
    }

    private <T> T getRestCall(String url, Class<T> clazz, Object... params){
        return restTemplate.getForObject(serverURL + url, clazz, params);
    }

}