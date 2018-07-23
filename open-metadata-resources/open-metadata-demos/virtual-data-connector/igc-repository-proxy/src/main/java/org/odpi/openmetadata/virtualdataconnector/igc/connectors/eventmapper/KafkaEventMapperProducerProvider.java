/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

public class KafkaEventMapperProducerProvider extends ConnectorProviderBase{

    public KafkaEventMapperProducerProvider() {

        Class connectorClass = KafkaIGCEventMapperProducer.class;

        super.setConnectorClassName(connectorClass.getName());

    }
}
