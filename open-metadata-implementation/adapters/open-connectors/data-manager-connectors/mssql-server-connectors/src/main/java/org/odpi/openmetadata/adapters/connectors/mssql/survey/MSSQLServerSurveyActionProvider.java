/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.MSSQLDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.mssql.controls.MSSQLTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;

/**
 * MSSQLServerSurveyActionProvider is the OCF connector provider for the Microsoft SQL Server survey action service.
 */
public class MSSQLServerSurveyActionProvider extends SurveyActionServiceProvider
{
    private static final String connectorClassName = MSSQLServerSurveyActionService.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public MSSQLServerSurveyActionProvider()
    {
        super(EgeriaOpenConnectorDefinition.MSSQL_SERVER_SURVEY_ACTION_SERVICE, connectorClassName, null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(
                new DeployedImplementationTypeDefinition[]{MSSQLDeployedImplementationType.MSSQL_SERVER});
        super.supportedActionTargetTypes = MSSQLTarget.getMSSQLServerActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.PRODUCE_INVENTORY, AnalysisStep.PROFILING_ASSOCIATED_RESOURCES});

        /*
         * Despite the method name, this list of annotation types is generic to any relational database survey,
         * not PostgreSQL-specific - there is no separate MSSQL variant defined in the shared open-survey-framework.
         */
        super.producedAnnotationTypes = SurveyDatabaseAnnotationType.getPostgresServerAnnotationTypeTypes();
    }
}
