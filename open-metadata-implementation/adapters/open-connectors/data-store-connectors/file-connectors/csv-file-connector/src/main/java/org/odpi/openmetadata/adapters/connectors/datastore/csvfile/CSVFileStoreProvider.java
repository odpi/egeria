/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;


/**
 * CSVFileStoreProvider is the OCF connector provider for the structured file store connector.
 */
public class CSVFileStoreProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 96;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "108b85fe-d7b8-45c3-9fb8-742ac4e4fb14";
    private static final String  connectorQualifiedName = "Egeria:ResourceConnector:CSVFile";
    private static final String  connectorTypeName = "CSV File Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of structured (CSV) files.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/csv-file-resource-connector/";


    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector";


    private static final String  expectedDataFormat = "csv";

    /**
     * columnNames configuration property
     */
    public static final String  columnNamesProperty = "columnNames";

    /**
     * delimiterCharacter configuration property
     */
    public static final String  delimiterCharacterProperty = "delimiterCharacter";

    /**
     * quote character configuration property
     */
    public static final String  quoteCharacterProperty = "quoteCharacter";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public CSVFileStoreProvider()
    {
        super();

        super.setConnectorClassName(connectorClass);

        connectorInterfaces.add(CSVFileStore.class.getName());

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.CSV_FILE.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.CSV_FILE.getDeployedImplementationType());
        connectorType.setExpectedDataFormat(expectedDataFormat);
        connectorType.setConnectorInterfaces(connectorInterfaces);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(columnNamesProperty);
        recognizedConfigurationProperties.add(delimiterCharacterProperty);
        recognizedConfigurationProperties.add(quoteCharacterProperty);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorTypeName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
