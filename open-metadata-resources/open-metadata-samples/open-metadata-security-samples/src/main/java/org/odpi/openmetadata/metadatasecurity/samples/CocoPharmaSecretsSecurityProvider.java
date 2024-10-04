/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityProvider;

/**
 * CocoPharmaSecretsSecurityProvider is the connector provider to the
 * sample security connector for the Coco Pharmaceuticals scenarios that uses an externalized secrets store to supply
 * user information.
 */
public class CocoPharmaSecretsSecurityProvider extends OpenMetadataServerSecurityProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 98;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "d732d735-8092-4b34-8111-7647d9d208e7";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:Sample:SecretsStoreSecurity:CocoPharmaceuticals";
    private static final String connectorDisplayName   = "Coco Pharmaceuticals Secrets Store Security Connector";
    private static final String connectorDescription   = "Connector supports an externalized user registry and authorization rules for Coco Pharmaceuticals via an embedded secrets connection.";
    private static final String connectorWikiPage      = "https://github.com/odpi/egeria/tree/main/open-metadata-resources/open-metadata-samples/open-metadata-security-samples";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = CocoPharmaSecretsSecurityConnector.class;


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public CocoPharmaSecretsSecurityProvider()
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
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
