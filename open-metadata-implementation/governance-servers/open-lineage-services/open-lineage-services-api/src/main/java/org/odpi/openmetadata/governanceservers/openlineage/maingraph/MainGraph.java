/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.maingraph;

import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphConnector;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

public interface MainGraph extends OpenLineageGraphConnector {

    /**
     * Returns a lineage subgraph.
     *
     * @param scope                  The specific lineage query.
     * @param guid                   The guid of the node of which the lineage is queried from.
     * @param displayNameMustContain Used to filter out nodes which displayname does not contain this value.
     * @param includeProcesses       Will filter out all processes and subprocesses from the response if false.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    LineageResponse lineage(Scope scope, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException;

    /**
     * Initialize the mainGraph database.
     */
    void initializeGraphDB() throws OpenLineageException;


    /**
     * Allows the BufferGraphConnector to obtain an instance of mainGraph. Is of type Object so that
     * OpenLineageServerOperationalServices will not require a dependency on Janusgraph.
     *
     * @return main graph object
     */
    Object getMainGraph();
}

