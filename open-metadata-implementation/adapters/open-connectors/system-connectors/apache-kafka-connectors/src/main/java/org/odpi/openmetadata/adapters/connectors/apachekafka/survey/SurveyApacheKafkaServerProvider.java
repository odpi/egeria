/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.survey;

import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls.KafkaAnnotationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;

import java.util.ArrayList;


/**
 * SurveyApacheKafkaServerProvider is the connector provider for the SurveyApacheKafkaServerConnector that publishes
 * insights about the topics it supports.
 */
public class SurveyApacheKafkaServerProvider extends SurveyActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 677;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "a6f9e92f-a16a-494c-bac6-d3618de12a6a";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:SurveyActionService:SurveyApacheKafkaServer";
    private static final String connectorDisplayName   = "Apache Kafka Server Survey Action Service";
    private static final String connectorDescription   = "Discovers the topics supported by the Apache Kafka Server.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/apache-kafka/apache-kafka-server-survey-action-service/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public SurveyApacheKafkaServerProvider()
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
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(SurveyActionServiceProvider.supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        supportedRequestParameters = null;
        supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                AnalysisStep.PRODUCE_INVENTORY});

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName("*");
        actionTargetType.setDescription("Any Software Server entity that represents an Apache Kafka server linked to the connection for the Apache Kafka Admin connector.");
        actionTargetType.setTypeName(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);

        producedAnnotationTypes = KafkaAnnotationType.getAnnotationTypeTypes();

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{KafkaDeployedImplementationType.APACHE_KAFKA_SERVER, KafkaDeployedImplementationType.APACHE_KAFKA_EVENT_BROKER, DeployedImplementationType.APACHE_KAFKA_TOPIC});
    }
}
