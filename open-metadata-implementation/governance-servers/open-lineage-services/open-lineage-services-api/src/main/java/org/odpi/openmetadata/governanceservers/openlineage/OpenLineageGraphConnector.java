/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageVertexResponse;

public interface OpenLineageGraphConnector {

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    void start() throws ConnectorCheckedException;

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    void disconnect() throws ConnectorCheckedException;

    /**
     * Initialize the connectors
     * @throws OpenLineageException
     */
    void initializeGraphDB(AuditLog auditLog) throws OpenLineageException;

    LineageResponse lineage(Scope scope, String guid, String displayNameMustContain, boolean includeProcesses) throws OpenLineageException;

    /**
     * Gets entity details.
     *
     * @param guid the guid
     * @return the entity details
     */
    LineageVertexResponse getEntityDetails(String guid);
}
