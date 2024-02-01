/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.catalogtarget.CatalogTargetType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

public class CSVLineageImporterProvider extends IntegrationConnectorProvider
{
    private static final String connectorTypeGUID      = "b6e5fda2-f5ef-4fc9-861f-2e724e74e6ac";
    private static final int    connectorComponentId   = 671;
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Files:CSVLineageImporter";
    private static final String connectorDisplayName   = "CSV Lineage Importer Integration Connector";
    private static final String connectorDescription   = "Connector reads the content of a CSV file that contains lineage information and catalogues the content.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/csv-lineage-importer-integration-connector/";
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.CSVLineageImporterConnector";

    /**
     * The name of the catalog target that contains the file to monitor.
     */
    static public final String CATALOG_TARGET_NAME    = "fileToLoad";

    public CSVLineageImporterProvider()
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


        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setDeployedImplementationType(DeployedImplementationType.LINEAGE_INTEGRATION_CONNECTOR.getDeployedImplementationType());

        super.connectorTypeBean = connectorType;

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setTypeName(OpenMetadataType.CSV_FILE.typeName);
        catalogTargetType.setDeployedImplementationType(DeployedImplementationType.DATA_FILE.getDeployedImplementationType());

        super.catalogTargetTypes.put(CATALOG_TARGET_NAME, catalogTargetType);
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
