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
