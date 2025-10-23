/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;


/**
 * CSVTabularDataSetCollectionProvider is the OCF connector provider for the Tabular Data Set Collection connector
 * that manages multiple CSV tabular data sets.
 * It expects an embedded CSVFileStore connector.
 */
public class CSVTabularDataSetCollectionProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 712;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "32c25bc2-e0bf-4d78-87ab-ed3c5aead169";
    private static final String  connectorQualifiedName = "Egeria::ResourceConnector::TabularDataSetCollection::CSVFile";
    private static final String  connectorTypeName = "CSV Tabular Data Set Collection Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of structured (CSV) files as a collection of tabular data sets.";
    private static final String  connectorWikiPage = "https://egeria-project.org/connectors/resource/csv-tabular-data-set-collection-connector/";

    private static final String  expectedDataFormat = "csv";


    private static final String  connectorClass = CSVTabularDataSetCollectionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public CSVTabularDataSetCollectionProvider()
    {
        super();

        super.setConnectorClassName(connectorClass);

        connectorInterfaces.add(ReadableTabularDataSource.class.getName());
        connectorInterfaces.add(WritableTabularDataSource.class.getName());
        connectorInterfaces.add(TabularDataCollection.class.getName());

        ConnectorType connectorType = new ConnectorType();

        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType());
        connectorType.setExpectedDataFormat(expectedDataFormat);
        connectorType.setConnectorInterfaces(connectorInterfaces);

        connectorType.setRecognizedConfigurationProperties(CSVFileConfigurationProperty.getCSVTabularDataSetCollectionConfigPropertyNames());

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

        super.supportedConfigurationProperties = CSVFileConfigurationProperty.getCSVTabularDataSetCollectionConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION});
    }
}
