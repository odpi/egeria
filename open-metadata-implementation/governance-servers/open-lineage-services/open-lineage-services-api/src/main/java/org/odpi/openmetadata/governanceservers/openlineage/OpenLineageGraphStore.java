/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

public interface OpenLineageGraphStore {

    void start() throws ConnectorCheckedException;
}
