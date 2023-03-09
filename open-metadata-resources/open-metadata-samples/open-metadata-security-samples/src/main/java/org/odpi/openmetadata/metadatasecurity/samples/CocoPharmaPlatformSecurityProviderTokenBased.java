/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataPlatformSecurityProvider;

/**
 * CocoPharmaPlatformSecurityProviderTokenBased is the connector provider to the
 * sample platform security connector for the Coco Pharmaceuticals scenarios.
 */
public class CocoPharmaPlatformSecurityProviderTokenBased extends OpenMetadataPlatformSecurityProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 93;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "5e6f852f-5912-44fb-aa6c-784efe25af47";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:Sample:TokenBased:PlatformSecurity:CocoPharmaceuticals";
    private static final String connectorDisplayName   = "Coco Pharmaceuticals Platform Security Connector Token Based";
    private static final String connectorDescription   = "Connector that exposes the usability of custom authorisation headers for Coco Pharmaceuticals.";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = CocoPharmaPlatformSecurityConnectorTokenBased.class;


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public CocoPharmaPlatformSecurityProviderTokenBased()
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
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);

        super.setConnectorComponentDescription(componentDescription);
    }

}
