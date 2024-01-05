/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;


/**
 * CSVFileStoreProvider is the OCF connector provider for the structured file store connector.
 */
public class CSVFileStoreProvider extends ConnectorProviderBase
{
    private static final String  connectorTypeGUID = "108b85fe-d7b8-45c3-9fb8-742ac4e4fb14";
    private static final String  connectorTypeName = "CSV File Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of structured (CSV) files.";

    private static final String  expectedDataFormat = "csv";
    private static final String  assetTypeName = "CSVFile";

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

        Class<?>    connectorClass = CSVFileStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        connectorInterfaces.add(CSVFileStore.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(assetTypeName);
        connectorType.setExpectedDataFormat(expectedDataFormat);
        connectorType.setConnectorInterfaces(connectorInterfaces);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(columnNamesProperty);
        recognizedConfigurationProperties.add(delimiterCharacterProperty);
        recognizedConfigurationProperties.add(quoteCharacterProperty);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
