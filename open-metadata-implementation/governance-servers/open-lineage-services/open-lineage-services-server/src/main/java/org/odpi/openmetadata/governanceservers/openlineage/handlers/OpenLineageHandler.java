/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.handlers;

import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.maingraph.MainGraph;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

public class OpenLineageHandler {

    private MainGraph mainGraph;

    public OpenLineageHandler(MainGraph mainGraph) {
        this.mainGraph = mainGraph;
    }

    /**
     * Returns a lineage subgraph.
     *
     * @param graphName    main, buffer, mock, history.
     * @param scope source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param view        The view queried by the user: hostview, tableview, columnview.
     * @param guid         The guid of the node of which the lineage is queried from.
     * @param displayNameMustContain
     * @param includeProcesses
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public LineageResponse lineage(GraphName graphName, Scope scope, View view, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException {
        return mainGraph.lineage(graphName, scope, view, guid, displayNameMustContain, includeProcesses);
    }

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     */
    public void dumpGraph(GraphName graphName) throws OpenLineageException {
        mainGraph.dumpGraph(graphName);
    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @return The queried graph, in graphSON format.
     */
    public String exportGraph(GraphName graphName) throws OpenLineageException {
        return mainGraph.exportGraph(graphName);
    }

}
