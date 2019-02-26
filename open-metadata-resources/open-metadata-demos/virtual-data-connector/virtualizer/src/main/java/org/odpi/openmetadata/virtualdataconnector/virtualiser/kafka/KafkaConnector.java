/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConnector extends ConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(KafkaConnector.class);

    public KafkaConnector(){
        super();
    }

    @Override
    public void start() throws ConnectorCheckedException {
        super.start();
        log.info(this.connectionBean.toString());
    }
}
