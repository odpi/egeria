/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apacheatlas.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls.AtlasAnnotationType;
import org.odpi.openmetadata.adapters.connectors.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.util.ArrayList;


/**
 * SurveyApacheAtlasProvider is the connector provider for the SurveyApacheAtlasConnector that publishes insights about
 * the types and instances in an Apache Atlas server.
 */
public class SurveyApacheAtlasProvider extends SurveyActionServiceProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.SurveyApacheAtlasConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public SurveyApacheAtlasProvider()
    {
        super(EgeriaOpenConnectorDefinition.APACHE_ATLAS_SURVEY_SERVICE,
              connectorClassName,
              null);

        supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[]{
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.MEASURE_RESOURCE,
                AnalysisStep.SCHEMA_EXTRACTION,
                AnalysisStep.PROFILE_DATA});

        producedAnnotationTypes = AtlasAnnotationType.getAnnotationTypeTypes();

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName("*");
        actionTargetType.setDescription("Any Software Server entity that represents an Apache Atlas server linked to the connection for the Apache Atlas REST API connector.");
        actionTargetType.setOpenMetadataTypeName(AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{AtlasDeployedImplementationType.APACHE_ATLAS_SERVER});

        super.setFinalAnalysisSteps(new AnalysisStep[]{
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.MEASURE_RESOURCE,
                AnalysisStep.SCHEMA_EXTRACTION,
                AnalysisStep.PROFILE_DATA});
    }
}