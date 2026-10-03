/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.wso2mi.survey.controls.WSO2MIAnnotationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.util.ArrayList;


/**
 * SurveyWSO2MIServerProvider is the connector provider for the SurveyWSO2MIServerConnector that publishes
 * insights about the REST APIs deployed on a WSO2 Micro Integrator instance.
 *
 * <p>There is no WSO2-specific {@code DeployedImplementationType} yet (open item tracked in
 * {@code SCAFFOLD_NOTES.md}), so the action target is described against the generic
 * {@link DeployedImplementationType#SOFTWARE_SERVER}, matching what {@code WSO2MIResourceProvider} and
 * {@code WSO2MIIntegrationProvider} already do.</p>
 */
public class SurveyWSO2MIServerProvider extends SurveyActionServiceProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.wso2mi.survey.SurveyWSO2MIServerConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public SurveyWSO2MIServerProvider()
    {
        super(EgeriaOpenConnectorDefinition.WSO2MI_API_SURVEY_SERVICE,
              connectorClassName,
              null);

        supportedRequestParameters = null;
        supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                AnalysisStep.PRODUCE_INVENTORY});

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName("*");
        actionTargetType.setDescription("Any Software Server entity that represents a WSO2 Micro Integrator instance linked to the connection for the WSO2MIResourceConnector.");
        actionTargetType.setOpenMetadataTypeName(DeployedImplementationType.SOFTWARE_SERVER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.SOFTWARE_SERVER.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);

        producedAnnotationTypes = WSO2MIAnnotationType.getAnnotationTypeTypes();
    }
}
