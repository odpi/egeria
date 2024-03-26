/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

/**
 * SequentialDiscoveryPipelineProvider is the provider for the SequentialDiscoveryPipeline - an ODF discovery pipeline connector.
 */
public class SequentialSurveyPipelineProvider extends SurveyActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 667;

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String  connectorTypeGUID = "30f2c202-7f2d-4d5d-9b17-6064f32f3c74";
    private static final String  connectorTypeQualifiedName = "Egeria:SurveyActionService:SequentialSurveyPipelineService";
    private static final String  connectorTypeName = "Sequential Survey Pipeline Connector";
    private static final String  connectorTypeDescription = "Connector supports the sequential execution of survey action services.";
    private static final String  connectorWikiPage  = "https://egeria-project.org/connectors/survey-action/sequential-survey-pipeline-service/";


    /*
     * Class of the connector.
     */
    private static final String connectorClass = "org.odpi.openmetadata.frameworks.surveyaction.SequentialSurveyPipeline";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * survey action service implementation.
     */
    public SequentialSurveyPipelineProvider()
    {
        super.setConnectorClassName(connectorClass);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorTypeName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
