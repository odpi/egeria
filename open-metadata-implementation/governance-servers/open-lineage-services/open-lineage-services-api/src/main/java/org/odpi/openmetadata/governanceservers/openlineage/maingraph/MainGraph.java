/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.maingraph;

import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraph;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

public interface MainGraph extends OpenLineageGraph {

    /**
     * Returns a lineage subgraph.
     *
     * @param scope     source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param view      The view queried by the user: hostview, tableview, columnview.
     * @param guid      The guid of the node of which the lineage is queried from.
     * @param displayNameMustContain
     * @param includeProcesses
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    LineageResponse lineage(Scope scope, View view, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException;

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     */
    void dumpMainGraph() throws OpenLineageException;

    void initializeGraphDB() throws OpenLineageException;

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @return The queried graph, in graphSON format.
     */
    String exportMainGraph() throws OpenLineageException;

    Object getMainGraph();
}

