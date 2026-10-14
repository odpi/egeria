/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.survey;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls.KafkaAnnotationType;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.util.ArrayList;


/**
 * SurveyApacheKafkaServerProvider is the connector provider for the SurveyApacheKafkaServerConnector that publishes
 * insights about the topics it supports.
 */
public class SurveyApacheKafkaServerProvider extends SurveyActionServiceProvider
{
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
        super(EgeriaOpenConnectorDefinition.APACHE_KAFKA_TOPIC_SURVEY_SERVICE,
              connectorClassName,
              null);

        supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                AnalysisStep.PRODUCE_INVENTORY});

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName("*");
        actionTargetType.setDescription("Any Software Server entity that represents an Apache Kafka server linked to the connection for the Apache Kafka Admin connector.");
        actionTargetType.setOpenMetadataTypeName(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getAssociatedTypeName());
        actionTargetType.setDeployedImplementationType(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getDeployedImplementationType());

        super.supportedActionTargetTypes.add(actionTargetType);

        producedAnnotationTypes = KafkaAnnotationType.getAnnotationTypeTypes();

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{KafkaDeployedImplementationType.APACHE_KAFKA_SERVER, KafkaDeployedImplementationType.APACHE_KAFKA_EVENT_BROKER, DeployedImplementationType.APACHE_KAFKA_TOPIC});

        super.setFinalAnalysisSteps(new AnalysisStep[] {
                AnalysisStep.CHECK_ASSET,
                AnalysisStep.PROFILING_ASSOCIATED_RESOURCES,
                AnalysisStep.PRODUCE_INVENTORY});
    }
}
