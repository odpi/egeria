/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.OracleDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.oracle.controls.OracleTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;

/**
 * OracleDatabaseSurveyActionProvider is the OCF connector provider for the Oracle pluggable database survey action service.
 */
public class OracleDatabaseSurveyActionProvider extends SurveyActionServiceProvider
{
    private static final String connectorClassName = OracleDatabaseSurveyActionService.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public OracleDatabaseSurveyActionProvider()
    {
        super(EgeriaOpenConnectorDefinition.ORACLE_DATABASE_SURVEY_ACTION_SERVICE,
              connectorClassName, null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(
                new DeployedImplementationTypeDefinition[]{OracleDeployedImplementationType.ORACLE_DATABASE});
        super.supportedActionTargetTypes = OracleTarget.getOracleDatabaseActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.PROFILING_ASSOCIATED_RESOURCES});

        /*
         * Despite the method name, this list of annotation types is generic to any relational database survey,
         * not PostgreSQL-specific - there is no separate Oracle variant defined in the shared open-survey-framework.
         */
        super.producedAnnotationTypes = SurveyDatabaseAnnotationType.getPostgresDatabaseAnnotationTypeTypes();
    }
}
