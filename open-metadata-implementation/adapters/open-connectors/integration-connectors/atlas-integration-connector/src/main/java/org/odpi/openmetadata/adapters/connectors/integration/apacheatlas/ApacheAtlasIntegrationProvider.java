/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * ApacheAtlasIntegrationProvider is the connector provider for the Apache Atlas integration connector that publishes glossary terms to Apache Atlas.
 */
public class ApacheAtlasIntegrationProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 659;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "aeca7da2-80c1-4e2a-baa5-8c30472be766";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Catalog:ApacheAtlas";
    private static final String connectorDisplayName   = "Apache Atlas Integration Connector";
    private static final String connectorDescription   = "Connector publishes active glossary terms to Apache Atlas.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/apache-atlas-integration-connector/";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = ApacheAtlasIntegrationConnector.class;


    /**
     * The configuration property name used to supply the qualified name of an Egeria glossary to synchronize with
     * Apache Atlas.  If this value is null, all Egeria originated glossaries are copied to Apache Atlas.
     */
    static final String EGERIA_GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "egeriaGlossaryQualifiedName";

    /**
     * The configuration property name used to supply the name of the Atlas Glossary to copy into the open metadata
     * ecosystem.  If this value is null, all Apache Atlas originated glossaries are copied into the open metadata ecosystem.
     */
    static final String ATLAS_GLOSSARY_NAME_CONFIGURATION_PROPERTY            = "atlasGlossaryName";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ApacheAtlasIntegrationProvider()
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
        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(EGERIA_GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(ATLAS_GLOSSARY_NAME_CONFIGURATION_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
