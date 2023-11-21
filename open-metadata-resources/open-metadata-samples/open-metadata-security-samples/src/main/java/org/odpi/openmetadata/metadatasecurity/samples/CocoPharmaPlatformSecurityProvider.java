/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataPlatformSecurityProvider;

/**
 * CocoPharmaPlatformSecurityProvider is the connector provider to the
 * sample platform security connector for the Coco Pharmaceuticals scenarios.
 */
public class CocoPharmaPlatformSecurityProvider extends OpenMetadataPlatformSecurityProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 91;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "c7026446-8d9e-4149-8da0-5c800cf7fc23";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:Sample:PlatformSecurity:CocoPharmaceuticals";
    private static final String connectorDisplayName   = "Coco Pharmaceuticals Platform Security Connector";
    private static final String connectorDescription   = "Connector supports a hard-coded user registry and authorization rules for Coco Pharmaceuticals.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/runtime/coco-pharma-platform-metadata-security-connector";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = CocoPharmaPlatformSecurityConnector.class;


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public CocoPharmaPlatformSecurityProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClass.getName());

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }

}
