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
    static final String  connectorTypeGUID = "108b85fe-d7b8-45c3-9fb8-742ac4e4fb14";
    static final String  connectorTypeName = "Structured File Store Connector";
    static final String  connectorTypeDescription = "Connector supports reading of structured (CSV) files.";

    public static final String  columnNamesProperty = "columnNames";
    public static final String  delimiterCharacterProperty = "delimiterCharacter";
    public static final String  quoteCharacterProperty = "quoteCharacter";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public CSVFileStoreProvider()
    {
        Class<?>    connectorClass = CSVFileStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());


        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(columnNamesProperty);
        recognizedConfigurationProperties.add(delimiterCharacterProperty);
        recognizedConfigurationProperties.add(quoteCharacterProperty);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
