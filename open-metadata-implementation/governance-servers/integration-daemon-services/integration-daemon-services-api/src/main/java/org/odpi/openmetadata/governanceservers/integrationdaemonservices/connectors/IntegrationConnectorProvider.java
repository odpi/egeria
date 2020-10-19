/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

/**
 * The IntegrationConnectorProvider provides a base class for the connector provider supporting
 * Integration Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * IntegrationConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class IntegrationConnectorProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "2212a2a0-9ee0-40b3-b046-c20ac6186e97";
    static final String  connectorTypeName = "Integration Connector";
    static final String  connectorTypeDescription = "Connector supports the exchange of metadata between a third party technology and open metadata.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public IntegrationConnectorProvider()
    {
        Class<?> connectorClass = IntegrationConnectorBase.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
        super.setConnectorComponentDescription(OMRSAuditingComponent.INTEGRATION_CONNECTOR);
    }
}