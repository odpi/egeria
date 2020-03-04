/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.handlers;

import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.maingraph.MainGraph;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

public class OpenLineageHandler {

    private MainGraph mainGraph;

    public OpenLineageHandler(MainGraph mainGraph) {
        this.mainGraph = mainGraph;
    }

    /**
     * Returns a lineage subgraph.
     *
     * @param scope                  source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param guid                   The guid of the node of which the lineage is queried from.
     * @param displayNameMustContain
     * @param includeProcesses
     * @param includeGlossaryTerms
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public LineageResponse lineage(Scope scope, String guid, String displayNameMustContain, boolean includeProcesses,
                                   boolean includeGlossaryTerms) throws OpenLineageException {
        return mainGraph.lineage(scope, guid, displayNameMustContain, includeProcesses, includeGlossaryTerms);
    }

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     */
    public void dumpMainGraph() throws OpenLineageException {
        mainGraph.dumpMainGraph();
    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @return The queried graph, in graphSON format.
     */
    public String exportMainGraph() throws OpenLineageException {
        return mainGraph.exportMainGraph();
    }

}
