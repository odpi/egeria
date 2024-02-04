/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.graph.LineageGraph;

public interface LineageWarehouseGraphConnector
{

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
     * @param auditLog logging destination
     * @throws LineageWarehouseException internal error
     */
    void initializeGraphDB(AuditLog auditLog) throws LineageWarehouseException;

    /**
     * Task that the scheduler performs based on the interval
     */
    void performLineageGraphJob();

    LineageWarehouseQueryService getLineageQueryService();

    LineageGraph getLineageStorageService();
}
