/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apachekafka.survey;


import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminConnector;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls.KafkaAnnotationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.ffdc.KafkaSurveyAuditCode;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.ffdc.KafkaSurveyErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.NestedSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.SchemaAttributes;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceMeasureAnnotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileAnnotation;

import java.util.*;

/**
 * This connector builds a profile of the types and instances in an Apache Kafka server.
 */
public class SurveyApacheKafkaServerConnector extends SurveyActionServiceConnector
{
    final static PropertyHelper propertyHelper = new PropertyHelper();


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
            String           assetGUID  = surveyContext.getAssetGUID();
            SurveyAssetStore assetStore = surveyContext.getAssetStore();

            surveyContext.getAnnotationStore().setAnalysisStep(AnalysisStep.CHECK_ASSET.getName());

            Connector connectorToAsset  = assetStore.getConnectorToAsset();

            if (connectorToAsset instanceof ApacheKafkaAdminConnector kafkaConnector)
            {
                kafkaConnector.start();

                AnnotationStore   annotationStore   = surveyContext.getAnnotationStore();

                annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

                Set<String> topicNames = kafkaConnector.getTopicList();
                ResourceProfileAnnotation resourceProfileAnnotation = new ResourceProfileAnnotation();

                resourceProfileAnnotation.setAnnotationType(KafkaAnnotationType.TOPIC_LIST.getName());
                resourceProfileAnnotation.setSummary(KafkaAnnotationType.TOPIC_LIST.getSummary());
                resourceProfileAnnotation.setExplanation(KafkaAnnotationType.TOPIC_LIST.getExplanation());

                resourceProfileAnnotation.setValueList(new ArrayList<>(topicNames));

                annotationStore.addAnnotation(resourceProfileAnnotation, surveyContext.getAssetGUID());
            }
            else
            {
                String connectorToAssetClassName = "null";

                if (connectorToAsset != null)
                {
                    connectorToAssetClassName = connectorToAsset.getClass().getName();
                }

                logWrongTypeOfConnector(connectorToAssetClassName,
                                        ApacheKafkaAdminConnector.class.getName(),
                                        assetGUID,
                                        methodName);
            }
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
