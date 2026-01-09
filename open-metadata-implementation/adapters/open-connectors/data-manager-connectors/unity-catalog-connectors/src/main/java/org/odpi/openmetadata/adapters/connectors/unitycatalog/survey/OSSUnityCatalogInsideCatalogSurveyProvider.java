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

/**
 * Connector provider for the OSSUnityCatalogInsideCatalogSurveyConnector.
 */
public class OSSUnityCatalogInsideCatalogSurveyProvider extends SurveyActionServiceProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideCatalogSurveyService";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public OSSUnityCatalogInsideCatalogSurveyProvider()
    {
        super(EgeriaOpenConnectorDefinition.OSS_UNITY_CATALOG_INSIDE_CATALOG_SURVEY_ACTION_SERVICE,
              connectorClassName,
              null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{UnityCatalogDeployedImplementationType.OSS_UC_CATALOG});
        super.supportedActionTargetTypes = UnityCatalogTarget.getCatalogActionTargetTypes();
        super.supportedRequestParameters = UnityCatalogSurveyRequestParameter.getInsideCatalogSurveyRequestParameterTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.MEASURE_RESOURCE, AnalysisStep.PROFILING_ASSOCIATED_RESOURCES, AnalysisStep.PRODUCE_INVENTORY});
        super.producedAnnotationTypes    = UnityCatalogAnnotationType.getCatalogAnnotationTypeTypes();
    }
}
