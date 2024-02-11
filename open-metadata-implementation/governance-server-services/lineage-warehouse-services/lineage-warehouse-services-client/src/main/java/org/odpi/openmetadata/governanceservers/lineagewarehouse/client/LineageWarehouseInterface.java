/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.Scope;

import java.security.InvalidParameterException;

public interface LineageWarehouseInterface
{

    /**
     * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
     * the condensed path by querying a different method.
     *
     * @param userId calling user.
     * @param scope ULTIMATE_SOURCE, ULTIMATE_DESTINATION, SOURCE_AND_DESTINATION, VERTICAL, END_TO_END.
     * @param guid The guid of the node of which the lineage is queried of.
     * @param includeProcesses  Will filter out all processes and subprocesses from the response if false.
     * @return A subgraph containing all relevant paths, in graphSON format.
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem contacting the metadata server
     * @throws LineageWarehouseException open lineage exception
     */
    LineageVerticesAndEdges lineage(String userId, Scope scope, String guid, boolean includeProcesses)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, LineageWarehouseException;
}
