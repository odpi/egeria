/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.DB2LUWDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;

/**
 * DB2LUWDatabaseSurveyActionProvider is the OCF connector provider for the Db2 for Linux, UNIX and Windows database survey action service.
 */
public class DB2LUWDatabaseSurveyActionProvider extends SurveyActionServiceProvider
{
    private static final String connectorClassName = DB2LUWDatabaseSurveyActionService.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public DB2LUWDatabaseSurveyActionProvider()
    {
        super(EgeriaOpenConnectorDefinition.DB2LUW_DATABASE_SURVEY_ACTION_SERVICE,
              connectorClassName, null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(
                new DeployedImplementationTypeDefinition[]{DB2LUWDeployedImplementationType.DB2LUW_DATABASE});
        super.supportedActionTargetTypes = DB2LUWTarget.getDB2LUWDatabaseActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET, AnalysisStep.PROFILING_ASSOCIATED_RESOURCES});

        /*
         * Despite the method name, this list of annotation types is generic to any relational database survey,
         * not PostgreSQL-specific - there is no separate Db2 variant defined in the shared open-survey-framework.
         */
        super.producedAnnotationTypes = SurveyDatabaseAnnotationType.getPostgresDatabaseAnnotationTypeTypes();
    }
}
