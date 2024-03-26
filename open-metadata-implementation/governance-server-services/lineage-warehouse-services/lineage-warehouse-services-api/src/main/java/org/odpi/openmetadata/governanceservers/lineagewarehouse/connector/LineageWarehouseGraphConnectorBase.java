/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.connector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;


public interface LineageWarehouseGraphConnectorBase
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
     * Initialize the graph services.
     *
     * @param auditLog logging destination
     * @throws LineageWarehouseException internal error
     */
    void initializeGraphDB(AuditLog auditLog) throws LineageWarehouseException;


    /**
     * Task that the scheduler performs based on the interval
     */
    void performLineageGraphJob();


    /**
     * Return the query service.
     *
     * @return query service implementation
     */
    LineageWarehouseQueryService getLineageQueryService();


    /**
     * Return the storage service implementation.
     *
     * @return graph storage
     */
    LineageWarehouseGraphStorageService getLineageStorageService();
}
