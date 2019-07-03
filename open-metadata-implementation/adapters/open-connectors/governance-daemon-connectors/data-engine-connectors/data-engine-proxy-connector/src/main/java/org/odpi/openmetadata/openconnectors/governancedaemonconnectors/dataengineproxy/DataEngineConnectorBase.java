/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataEngineConnectorBase extends ConnectorBase implements DataEngineInterface {

    private static final Logger log = LoggerFactory.getLogger(DataEngineConnectorBase.class);

    // TODO: provide base implementation of interface methods
    public DataEngineConnectorBase() { super(); }

    public void sendProcess() {
        log.debug("Placeholder for sending a process in base DataEngineConnector implementation.");
    }

}
