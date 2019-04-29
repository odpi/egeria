/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.structuredfile;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;


/**
 * StructuredFileStoreProvider is the OCF connector provider for the structured file store connector.
 */
public class StructuredFileStoreProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "108b85fe-d7b8-45c3-9fb8-742ac4e4fb14";
    static final String  connectorTypeName = "Structured File Store Connector";
    static final String  connectorTypeDescription = "Connector supports storing of the open metadata cohort registry in a file.";
    static final String  columnNamesProperty = "columnNames";
    static final String  delimiterCharacterProperty = "delimiterCharacter";
    static final String  quoteCharacterProperty = "quoteCharacter";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public StructuredFileStoreProvider()
    {
        Class    connectorClass = StructuredFileStoreConnector.class;

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
