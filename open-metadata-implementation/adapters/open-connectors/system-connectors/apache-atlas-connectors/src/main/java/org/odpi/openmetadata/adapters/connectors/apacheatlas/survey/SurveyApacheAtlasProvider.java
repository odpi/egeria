/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apacheatlas.survey;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls.AtlasAnnotationType;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls.AtlasRequestParameter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;

import java.util.ArrayList;
import java.util.List;


/**
 * SurveyApacheAtlasProvider is the connector provider for the SurveyApacheAtlasConnector that publishes insights about
 * the types and instances in an Apache Atlas server.
 */
public class SurveyApacheAtlasProvider extends SurveyActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 665;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "095de44e-fe18-4849-bf08-4d91d9ea3e35";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:SurveyActionService:SurveyApacheAtlas";
    private static final String connectorDisplayName   = "Apache Atlas Survey Action Service";
    private static final String connectorDescription   = "Discovers the types and number of instances within an Apache Atlas server.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/apache-atlas/apache-atlas-survey-action-service/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.SurveyApacheAtlasConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public SurveyApacheAtlasProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(SurveyActionServiceProvider.supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(AtlasRequestParameter.FINAL_ANALYSIS_STEP.getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;

        supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.MEASURE_RESOURCE,
                AnalysisStep.SCHEMA_EXTRACTION,
                AnalysisStep.PROFILE_DATA });

        producedAnnotationTypes = AtlasAnnotationType.getAnnotationTypeTypes();

        supportedRequestParameters = AtlasRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName("*");
        actionTargetType.setDescription("Any Software Server entity that represents an Apache Atlas server linked to the connection for the Apache Atlas REST API connector.");
        actionTargetType.setTypeName(AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{AtlasDeployedImplementationType.APACHE_ATLAS_SERVER});


        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
