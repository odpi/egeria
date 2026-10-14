/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFileAnnotationType;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.util.ArrayList;

/**
 * FileSurveyServiceProvider provides the connector provider for the File Survey Action Service
 */
public class FileSurveyServiceProvider extends SurveyActionServiceProvider
{
    /*
     * Class of the connector.
     */
    private static final String   connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile.FileSurveyService";

    static final String  FILE_TARGET_PROPERTY = "fileToSurvey";
    static final String  FILE_TARGET_PROPERTY_DESCRIPTION = "The DataFile asset that describes the physical file to survey.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OCF Connector implementation.
     */
    public FileSurveyServiceProvider()
    {
        super(EgeriaOpenConnectorDefinition.FILE_SURVEY_SERVICE,
              connectorClass,
              null);

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(FILE_TARGET_PROPERTY);
        actionTargetType.setDescription(FILE_TARGET_PROPERTY_DESCRIPTION);
        actionTargetType.setOpenMetadataTypeName(DeployedImplementationType.FILE.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.MEASURE_RESOURCE});
        super.producedAnnotationTypes = SurveyFileAnnotationType.getFileSurveyAnnotationTypeTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.DATA_FILE});

        super.setFinalAnalysisSteps(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.MEASURE_RESOURCE});
    }
}
