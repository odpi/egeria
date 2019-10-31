/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.governanceservers.openlineage.MainGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphQueryingServices {

    private static final Logger log = LoggerFactory.getLogger(GraphStoringServices.class);
    private MainGraphStore mainGraphStore;

    public GraphQueryingServices(MainGraphStore mainGraphStore) {
        this.mainGraphStore = mainGraphStore;
    }

    /**
     * Returns a lineage subgraph.
     *
     * @param graphName    main, buffer, mock, history.
     * @param scope source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param view        The view queried by the user: hostview, tableview, columnview.
     * @param guid         The guid of the node of which the lineage is queried from.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public String lineage(String graphName, Scope scope, View view, String guid) {
        return mainGraphStore.lineage(graphName, scope, view, guid);
    }

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     */
    public void dumpGraph(String graphName) {
        mainGraphStore.dumpGraph(graphName);
    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @return The queried graph, in graphSON format.
     */
    public String exportGraph(String graphName) {
        return mainGraphStore.exportGraph(graphName);
    }

}
