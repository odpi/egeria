/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.IncomingEvent;

/**
 * {@link IncomingEvent} that came from Kafka
 */
public class KafkaIncomingEvent extends IncomingEvent
{
    private final long offset;
    
    /**
     * Constructor
     * 
     * @param json message content
     * @param offset the kafka offset of the message
     */
    public KafkaIncomingEvent(String json, long offset)
    {
        //use the offset as the message id
        super(json, String.valueOf(offset));
        this.offset = offset;
    }

    /**
     * Gets the kafka offset of this message
     * 
     * @return offset
     */
    public long getOffset()
    {
        return offset;
    }
}