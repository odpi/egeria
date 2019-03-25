/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

public class RangerSecurityServiceConnectorProvider extends ConnectorProviderBase {

    static final String connectorTypeGUID = "XiFfC6pr-n0er-JWJ8-NGmi-rnZEoJ0c1wlm";
    static final String connectorTypeName = "Ranger Security Server Connector";
    static final String connectorTypeDescription = "Connector supports storing of the open metadata cohort registry in a file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public RangerSecurityServiceConnectorProvider() {
        Class connectorClass = RangerSecurityServiceConnectorProvider.class;
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
