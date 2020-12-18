/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.ConfigException;

/**
 * Wrapper class for kafka configuration that allows us to get at
 * its properties.  This is in org.apache.kafka.clients.consumer so
 * that we can call the package private constructor. 
 * 
 */
public class KafkaConfigurationWrapper
{
    private final ConsumerConfig config;
    
    public KafkaConfigurationWrapper(Properties properties)
    {
        config = new ConsumerConfig(properties);
    }


    /**
     * In Kafka 0.10.0.0 (which we are using), session.timeout.ms.config is used for this.  In Kafka 0.10.1.0 and
     * later, we need to use the property max.poll.interval.ms
     * See https://kafka.apache.org/0100/documentation.html
     *
     * @return int
     */
    public int getMaxPollIntervalMs()
    {
        try
        {
            //if this property is valid, use it
            return config.getInt("max.poll.interval.ms");            
        }
        catch(ConfigException ex)
        {
            //property does not exist.  That is ok.  That means we are using an older
            //version of Kafka
        }

        return config.getInt(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG);
    }
}
