/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogSurveyRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

public class OSSUnityCatalogServerSurveyProvider extends SurveyActionServiceProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogServerSurveyService";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public OSSUnityCatalogServerSurveyProvider()
    {
        super(EgeriaOpenConnectorDefinition.OSS_UNITY_CATALOG_SERVER_SURVEY_ACTION_SERVICE,
              connectorClassName,
              null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER});
        super.supportedActionTargetTypes = UnityCatalogTarget.getServerActionTargetTypes();
        super.supportedRequestParameters = UnityCatalogSurveyRequestParameter.getServerSurveyRequestParameterTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.MEASURE_RESOURCE,
                AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                AnalysisStep.PRODUCE_INVENTORY});
        super.producedAnnotationTypes    = UnityCatalogAnnotationType.getServerAnnotationTypeTypes();
    }
}
