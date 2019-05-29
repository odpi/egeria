/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.securitytagconnector;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

public class SecurityTagConnectorProvider extends ConnectorProviderBase {

    static final String connectorTypeGUID = "jghsfXyV-bEhX-G4cO-gNl2-qqEKqbqCUbWS";
    static final String connectorTypeName = "Security Tag Server Connector";
    static final String connectorTypeDescription = "Connector supports storing of the open metadata cohort registry in a file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public SecurityTagConnectorProvider() {
        Class connectorClass = SecurityTagConnector.class;
        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}