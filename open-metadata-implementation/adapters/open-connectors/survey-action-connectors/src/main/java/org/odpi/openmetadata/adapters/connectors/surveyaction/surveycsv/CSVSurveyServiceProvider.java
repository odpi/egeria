/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

/**
 * CSVSurveyServiceProvider provides the connector provider for the CSV Survey Action Service
 */
public class CSVSurveyServiceProvider extends SurveyActionServiceProvider
{
    static final String  connectorTypeGUID = "2a844ac9-bb86-4765-9f3c-04df148c05a5";
    static final String  connectorTypeName = "CSV Survey Action Service Connector";
    static final String  connectorTypeDescription = "Connector supports the schema extraction and profiling of data in a CSV file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OCF Connector implementation.
     */
    public CSVSurveyServiceProvider()
    {
        super();

        String   connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv.CSVSurveyService";

        super.setConnectorClassName(connectorClass);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;
    }
}
