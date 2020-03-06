/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.maingraph;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

public abstract class MainGraphConnectorBase extends ConnectorBase implements MainGraph {


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void initializeGraphDB() throws OpenLineageException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract LineageResponse lineage(Scope scope, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Object getMainGraph();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void dumpMainGraph();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String exportMainGraph();
}
