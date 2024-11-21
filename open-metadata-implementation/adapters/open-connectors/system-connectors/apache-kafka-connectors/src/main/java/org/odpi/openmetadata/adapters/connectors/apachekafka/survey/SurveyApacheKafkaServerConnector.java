/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apachekafka.survey;


import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminConnector;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls.KafkaAnnotationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.ffdc.KafkaSurveyErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileAnnotation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * This connector builds a profile of the types and instances in an Apache Kafka server.
 */
public class SurveyApacheKafkaServerConnector extends SurveyActionServiceConnector
{
    /**
     * Indicates that the discovery service is completely configured and can begin processing.
     * This is where the function of the discovery service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            surveyContext.getAnnotationStore().setAnalysisStep(AnalysisStep.CHECK_ASSET.getName());

            connector = super.performCheckAssetAnalysisStep(ApacheKafkaAdminConnector.class,
                                                            KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getAssociatedTypeName());

            ApacheKafkaAdminConnector kafkaConnector = (ApacheKafkaAdminConnector) connector;

            AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

            Set<String> topicNames = kafkaConnector.getTopicList();
            ResourceProfileAnnotation resourceProfileAnnotation = new ResourceProfileAnnotation();

            setUpAnnotation(resourceProfileAnnotation, KafkaAnnotationType.TOPIC_LIST);

            resourceProfileAnnotation.setValueList(new ArrayList<>(topicNames));

            annotationStore.addAnnotation(resourceProfileAnnotation, surveyContext.getAssetGUID());

            annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

            super.writeNameListInventory(KafkaAnnotationType.TOPIC_INVENTORY,
                                         "topicList",
                                         new ArrayList<>(topicNames),
                                         surveyContext.getAnnotationStore().getSurveyReportGUID());
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(KafkaSurveyErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Transfer common properties into the annotation.
     *
     * @param annotation output annotation
     * @param kafkaAnnotationType annotation type definition
     */
    private void setUpAnnotation(Annotation          annotation,
                                 KafkaAnnotationType kafkaAnnotationType)
    {
        annotation.setAnnotationType(kafkaAnnotationType.getName());
        annotation.setAnalysisStep(kafkaAnnotationType.getAnalysisStep());
        annotation.setSummary(kafkaAnnotationType.getSummary());
        annotation.setExplanation(kafkaAnnotationType.getExplanation());
    }



    /**
     * Add a map of metrics to the consolidated metrics.
     *
     * @param namedMetrics raw metrics map
     * @param metricNamePrefix name of the group of metrics
     * @param consolidatedMetrics consolidated metrics map
     */
    private void addMapMetrics(Map<String, Integer> namedMetrics,
                               String               metricNamePrefix,
                               Map<String, String>  consolidatedMetrics)
    {
        int elementCount = 0;

        for (String elementName : namedMetrics.keySet())
        {
            elementCount ++;
            consolidatedMetrics.put(metricNamePrefix + ": " + elementName, Integer.toString(namedMetrics.get(elementName)));
        }

        consolidatedMetrics.put(metricNamePrefix, Integer.toString(elementCount));
    }
}
