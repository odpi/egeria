/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

/**
 * SequentialDiscoveryPipelineProvider is the provider for the SequentialDiscoveryPipeline - an ODF discovery pipeline connector.
 */
public class SequentialSurveyPipelineProvider extends SurveyActionServiceProvider
{
    static final String  connectorTypeGUID = "30f2c202-7f2d-4d5d-9b17-6064f32f3c74";
    static final String  connectorTypeName = "Sequential Survey Pipeline Connector";
    static final String  connectorTypeDescription = "Connector supports the sequential execution of survey action services.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public SequentialSurveyPipelineProvider()
    {
        Class<?> connectorClass = SequentialSurveyPipeline.class;

        super.setConnectorClassName(connectorClass.getName());

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
