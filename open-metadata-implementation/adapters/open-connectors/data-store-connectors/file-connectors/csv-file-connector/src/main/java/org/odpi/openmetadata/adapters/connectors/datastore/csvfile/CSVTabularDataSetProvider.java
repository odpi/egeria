/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.frameworks.openmetadata.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;


/**
 * CSVTabularDataSetProvider is the OCF connector provider for the Tabular Data Set connector that manages data in a CSV File.
 * It expects an embedded CSVFileStore connector.
 */
public class CSVTabularDataSetProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 712;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "fabaf243-e0ed-4f30-9df0-1cba38d90df4";
    private static final String  connectorQualifiedName = "Egeria::ResourceConnector::TabularDataSet::CSVFile";
    private static final String  connectorTypeName = "CSV File Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of a structured (CSV) file as a tabular data set.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/csv-tabular-data-set-connector/";


    private static final String  connectorClass = CSVTabularDataSetConnector.class.getName();


    private static final String  expectedDataFormat = "csv";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public CSVTabularDataSetProvider()
    {
        super();

        super.setConnectorClassName(connectorClass);

        connectorInterfaces.add(ReadableTabularDataSource.class.getName());
        connectorInterfaces.add(WritableTabularDataSource.class.getName());

        ConnectorType connectorType = new ConnectorType();

        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.CSV_TABULAR_DATA_SET.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.CSV_TABULAR_DATA_SET.getDeployedImplementationType());
        connectorType.setExpectedDataFormat(expectedDataFormat);
        connectorType.setConnectorInterfaces(connectorInterfaces);

        connectorType.setRecognizedConfigurationProperties(CSVFileConfigurationProperty.getCSVTabularDataSetConfigPropertyNames());

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

        super.supportedConfigurationProperties = CSVFileConfigurationProperty.getCSVTabularDataSetConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.CSV_TABULAR_DATA_SET});
    }
}
