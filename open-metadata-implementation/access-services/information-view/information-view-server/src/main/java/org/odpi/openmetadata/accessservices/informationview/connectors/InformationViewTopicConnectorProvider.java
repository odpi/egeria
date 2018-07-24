/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.connectors;


import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

public class InformationViewTopicConnectorProvider extends ConnectorProviderBase {

    public InformationViewTopicConnectorProvider() {
        super.setConnectorClassName(InformationViewTopicConnectorImpl.class.getName());
    }

    @Override
    public Connector getConnector(Connection connection) throws ConnectionCheckedException, ConnectorCheckedException {
        return super.getConnector(connection);
    }
}

