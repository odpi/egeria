/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

/**
 * FileSurveyServiceProvider provides the connector provider for the File Survey Action Service
 */
public class FileSurveyServiceProvider extends SurveyActionServiceProvider
{
    static final String  connectorTypeGUID = "0c06ebb3-0a8f-476f-b8f8-602c01643523";
    static final String  connectorTypeName = "File Survey Action Service Connector";
    static final String  connectorTypeDescription = "Connector supports the extractions of basic file properties.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OCF Connector implementation.
     */
    public FileSurveyServiceProvider()
    {
        super();

        String   connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile.FileSurveyService";

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
