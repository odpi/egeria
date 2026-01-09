/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

/**
 * PostgresServerSurveyActionProvider is the OCF connector provider for the PostgreSQL survey action service.
 */
public class PostgresServerSurveyActionProvider extends SurveyActionServiceProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = PostgresServerSurveyActionService.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public PostgresServerSurveyActionProvider()
    {
        super(EgeriaOpenConnectorDefinition.POSTGRES_SERVER_SURVEY_ACTION_SERVICE,
              connectorClassName,
              null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_SERVER});
        super.supportedActionTargetTypes = PostgresTarget.getPostgresServerActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {AnalysisStep.CHECK_ASSET, AnalysisStep.PRODUCE_INVENTORY, AnalysisStep.PROFILING_ASSOCIATED_RESOURCES});
        super.producedAnnotationTypes    = SurveyDatabaseAnnotationType.getPostgresServerAnnotationTypeTypes();
    }
}
