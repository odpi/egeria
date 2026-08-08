/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.DuckDBDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyDatabaseAnnotationType;

import java.util.ArrayList;
import java.util.List;

/**
 * DuckDBDatabaseSurveyActionProvider is the OCF connector provider for the DuckDB database survey action service.
 */
public class DuckDBDatabaseSurveyActionProvider extends SurveyActionServiceProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = DuckDBDatabaseSurveyActionService.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public DuckDBDatabaseSurveyActionProvider()
    {
        super(EgeriaOpenConnectorDefinition.DUCKDB_DATABASE_SURVEY_ACTION_SERVICE,
              connectorClassName,
              null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DuckDBDeployedImplementationType.DUCKDB_DATABASE});
        super.supportedActionTargetTypes = DuckDBTarget.getDuckDBDatabaseActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.PROFILING_ASSOCIATED_RESOURCES});

        /*
         * The produced annotation types are the vendor-neutral database measurements reused from the
         * open-survey-framework, plus the DuckDB-specific federation annotation types (attached databases and
         * external file/object-store scans) that are always produced alongside the measurements by this service.
         */
        List<AnnotationTypeType> producedAnnotationTypes = new ArrayList<>(SurveyDatabaseAnnotationType.getPostgresDatabaseAnnotationTypeTypes());
        producedAnnotationTypes.addAll(SurveyDuckDBAnnotationType.getDuckDBFederationAnnotationTypeTypes());

        super.producedAnnotationTypes = producedAnnotationTypes;
    }
}
