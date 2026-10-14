/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFileAnnotationType;

import java.util.ArrayList;

/**
 * CSVSurveyServiceProvider provides the connector provider for the CSV Survey Action Service
 */
public class CSVSurveyServiceProvider extends SurveyActionServiceProvider
{
    /*
     * Class of the connector.
     */
    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv.CSVSurveyService";

    static final String  FILE_TARGET_PROPERTY = "fileToSurvey";
    static final String  FILE_TARGET_PROPERTY_DESCRIPTION = "The CSVFile asset that describes the physical file to survey";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OCF Connector implementation.
     */
    public CSVSurveyServiceProvider()
    {
        super(EgeriaOpenConnectorDefinition.CSV_FILE_SURVEY_SERVICE,
              connectorClass,
              null);

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(FILE_TARGET_PROPERTY);
        actionTargetType.setDescription(FILE_TARGET_PROPERTY_DESCRIPTION);
        actionTargetType.setOpenMetadataTypeName(DeployedImplementationType.CSV_FILE.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.CSV_FILE.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.CSV_FILE});

        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.MEASURE_RESOURCE, AnalysisStep.SCHEMA_EXTRACTION, AnalysisStep.PROFILE_DATA});
        super.producedAnnotationTypes    = SurveyFileAnnotationType.getCSVSurveyAnnotationTypeTypes();

        super.setFinalAnalysisSteps(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.MEASURE_RESOURCE, AnalysisStep.SCHEMA_EXTRACTION, AnalysisStep.PROFILE_DATA});
    }
}
