/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DataEngineConnectorBase extends ConnectorBase implements DataEngineInterface {

    private static final Logger log = LoggerFactory.getLogger(DataEngineConnectorBase.class);

    public DataEngineConnectorBase() { super(); }

    // TODO: provide base implementation of interface methods once Data Engine OMAS client is available
    public void sendProcess(Process process) {
        log.debug("Placeholder for sending a process in base DataEngineConnector implementation.");
    }

    public void sendLineageMappings(List<LineageMapping> lineageMappingList) {
        log.debug("Placeholder for sending a list of lineage mappings in base DataEngineConnector implementation.");
    }

}
