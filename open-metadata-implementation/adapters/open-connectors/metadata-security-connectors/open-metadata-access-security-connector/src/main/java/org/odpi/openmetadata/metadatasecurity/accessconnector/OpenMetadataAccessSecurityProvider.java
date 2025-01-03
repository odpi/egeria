/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.accessconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityProvider;

/**
 * OpenMetadataAccessSecurityProvider is the connector provider to the
 * sample security connector for the Coco Pharmaceuticals scenarios that uses an externalized secrets store to supply
 * user information.
 */
public class OpenMetadataAccessSecurityProvider extends OpenMetadataServerSecurityProvider
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
    private static final String connectorQualifiedName = "Egeria:MetadataSecurity:OpenMetadataAccessSecurity";
    private static final String connectorDisplayName   = "Open Metadata Access Security Connector";
    private static final String connectorDescription   = "Connector supports the authorization of request to the Egeria services using the information from an embedded secrets store connector.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/metadata-security/open-metadata-access-security-connector/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.metadatasecurity.accessconnector.OpenMetadataAccessSecurityConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public OpenMetadataAccessSecurityProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

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
