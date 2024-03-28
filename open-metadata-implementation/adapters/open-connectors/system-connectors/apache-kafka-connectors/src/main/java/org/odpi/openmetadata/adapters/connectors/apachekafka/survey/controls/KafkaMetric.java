/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls;

import java.util.ArrayList;
import java.util.List;

/**
 * KafkaMetric describes the metrics from Apache Kafka that are captured by the Apache Kafka Survey Action Service.
 */
public enum KafkaMetric
{
    ENTITY_INSTANCE_COUNT ("entityInstanceCount", "Number of entities stored in the Apache Kafka server."),
    CLASSIFICATION_COUNT ("classificationCount", "Number of tags (classifications) attached to entities stored in the Apache Kafka server."),
    TYPE_UNUSED_COUNT ("typeUnusedCount", "Number of types that are not used in the Apache Kafka server."),
    TYPE_COUNT ("typeCount", "Number of types defined in the Apache Kafka server."),
    CLASSIFIED_ENTITY_COUNT ("classifiedEntityCount:", "Number of entities with tags (classifications) attached in the Apache Kafka server."),
    ENTITY_TYPE_INSTANCE_COUNT ("entityInstanceCount:typeName", "Number of active entity instances stored in the Apache Kafka server for each entity type."),
    ENTITY_WITH_SUB_TYPE_INSTANCE_COUNT ("entityWithSubtypesInstanceCount:typeName", "Number of active entity instances stored in the Apache Kafka server for each entity type (with its subtypes included in the count)."),
    ENTITY_DEF_COUNT ("entityDefs", "Number of entity type definitions (EntityDefs) defined in the Apache Kafka server."),
    RELATIONSHIP_DEF_COUNT ("relationshipDefs", "Number of relationship type definitions (RelationshipDefs) defined in the Apache Kafka server."),
    CLASSIFICATION_DEF_COUNT ("classificationDefs", "Number of classification type definitions (ClassificationDefs) defined in the Apache Kafka server."),
    BUSINESS_METADATA_DEF_COUNT ("businessMetadataDefs", "Number of business metadata type definitions (BusinessMetadataDefs) defined in the Apache Kafka server."),


    ;

    public final String name;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request type
     * @param description description of the request type
     */
    KafkaMetric(String name,
                String description)
    {
        this.name        = name;
        this.description = description;
    }


    /**
     * Return the name of the metric.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the metric.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the defined metrics as a list
     *
     * @return list
     */
    static List<KafkaMetric> getKafkaMetrics()
    {
        return new ArrayList<>(List.of(KafkaMetric.values()));
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "KafkaMetric{" + name + "}";
    }
}
