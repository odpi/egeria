/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyDatabaseAnnotationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresTarget;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;

/**
 * PostgresServerSurveyActionProvider is the OCF connector provider for the PostgreSQL survey action service.
 */
public class PostgresServerSurveyActionProvider extends SurveyActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 673;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID = "3e47db62-5407-4cbd-ba54-1ce6612af6f9";
    private static final String connectorQualifiedName = "Egeria:SurveyActionService:RelationalDatabaseServer:PostgreSQL";
    private static final String connectorDisplayName = "Survey for a PostgreSQL Database Server";
    private static final String connectorTypeDescription = "Surveys the databases, their tables and columns, found in a PostgreSQL database server";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/databases/postgres-database-server-survey-action-service/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName = PostgresServerSurveyActionService.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public PostgresServerSurveyActionProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        /*
         * Information about the type of assets this type of connector works with and the interface it supports.
         */
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_SERVER});
        super.supportedActionTargetTypes = PostgresTarget.getPostgresServerActionTargetTypes();
        super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {AnalysisStep.CHECK_ASSET, AnalysisStep.PROFILING_ASSOCIATED_RESOURCES});
        super.producedAnnotationTypes    = SurveyDatabaseAnnotationType.getPostgresServerAnnotationTypeTypes();
    }
}
