/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.derby;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator.ViewGeneratorConnectorProvider;

public class ViewGeneratorDerbyConnectorProvider extends ViewGeneratorConnectorProvider {
    private static final String  connectorTypeGUID = "35657b2b-c472-494f-81c5-99f08bbf8a36";
    private static final String  connectorTypeName = "Derby Database Connector";
    private static final String  connectorTypeDescription = "Connector supports retrieving the data via derby connection.";

    public ViewGeneratorDerbyConnectorProvider(){
        Class connectorClass = ViewGeneratorDerbyConnector.class;

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
