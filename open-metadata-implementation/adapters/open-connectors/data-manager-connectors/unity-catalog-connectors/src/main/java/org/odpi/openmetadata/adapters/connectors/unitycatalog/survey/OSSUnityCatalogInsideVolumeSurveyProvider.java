/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.surveyaction.controls.FolderRequestParameter;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFolderAnnotationType;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.util.ArrayList;

/**
 * FolderSurveyServiceProvider provides the connector provider for the Folder Survey Action Service
 */
public class OSSUnityCatalogInsideVolumeSurveyProvider extends SurveyActionServiceProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClass = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyService";

    static final String  FILE_TARGET_PROPERTY = "fileToSurvey";
    static final String  FILE_TARGET_PROPERTY_DESCRIPTION = "A FileFolder asset representing the directory on the file system to survey.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * survey action implementation.
     */
    public OSSUnityCatalogInsideVolumeSurveyProvider()
    {
        super(EgeriaOpenConnectorDefinition.OSS_UNITY_CATALOG_INSIDE_VOLUME_SURVEY_ACTION_SERVICE,
              connectorClass,
              null);

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(FILE_TARGET_PROPERTY);
        actionTargetType.setDescription(FILE_TARGET_PROPERTY_DESCRIPTION);
        actionTargetType.setOpenMetadataTypeName(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{UnityCatalogDeployedImplementationType.OSS_UC_VOLUME});

        super.supportedActionTargetTypes.add(actionTargetType);
        super.producedAnnotationTypes = SurveyFolderAnnotationType.getAnnotationTypeTypes();
        super.supportedRequestParameters = FolderRequestParameter.getRequestParameterTypes();

        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.MEASURE_RESOURCE,
                AnalysisStep.PRODUCE_ACTIONS,
                AnalysisStep.PRODUCE_INVENTORY});

    }
}
