/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.secretsstore.envar;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * EnvVarSecretsStoreProvider is the connector provider for the Secrets Store that uses environment variables.
 */
public class EnvVarSecretsStoreProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 660;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "2b797adb-f776-421f-9d72-ef47b38ddcdd";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:SecretsStoreConnector:EnvironmentVariable";
    private static final String connectorDisplayName   = "Environment Variable Secrets Store Connector";
    private static final String connectorDescription   = "Connector retrieves secrets from environment variables.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/secrets/environment-variable-secrets-store-connector/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.secretsstore.envar.EnvVarSecretsStoreConnector";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public EnvVarSecretsStoreProvider()
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
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setRecognizedConfigurationProperties(SecretsStoreConfigurationProperty.getStaticSecretStoreRecognizedConfProperties());

        super.connectorTypeBean = connectorType;

        super.supportedConfigurationProperties = SecretsStoreConfigurationProperty.getStaticSecretStoreConfigurationPropertyTypes();

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
