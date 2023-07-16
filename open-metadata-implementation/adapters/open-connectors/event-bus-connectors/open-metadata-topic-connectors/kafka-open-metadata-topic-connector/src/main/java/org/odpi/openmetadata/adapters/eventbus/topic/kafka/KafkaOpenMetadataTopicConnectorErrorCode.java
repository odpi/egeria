/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The KafkaOpenMetadataTopicConnectorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Apache Kafka connector.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 *     <li><ul>
 *         <li>500 - internal error</li>
 *         <li>400 - invalid parameters</li>
 *         <li>404 - not found</li>
 *         <li>409 - data conflict errors - eg item already defined</li>
 *     </ul></li>
 *     <li>Error Message Identifier - to uniquely identify the message</li>
 *     <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>SystemAction - describes the result of the error</li>
 *     <li>UserAction - describes how a consumer should correct the error</li>
 * </ul>
 */
public enum KafkaOpenMetadataTopicConnectorErrorCode implements ExceptionMessageSet
{
    ERROR_SENDING_EVENT(400, "OCF-KAFKA-TOPIC-CONNECTOR-400-001 ",
            "An unexpected {0} exception was caught while sending an event to topic {1}.  The message in the exception was: {2}",
            "The system is unable to send the event.",
            "Review the exception that was returned from the send."),

    ERROR_ATTEMPTING_KAFKA_INITIALIZATION(400, "OCF-KAFKA-TOPIC-CONNECTOR-400-002 ",
            "Egeria was unable to initialize a connection to a Kafka cluster.  The message in the exception was: {0}",
            "The system is unable initialize.",
            "Ensure that Kafka is available"),

    ERROR_CONNECTING_KAFKA_PRODUCER(400, "OCF-KAFKA-TOPIC-CONNECTOR-400-003 ",
            "Egeria encountered an exception while attempting to connect a message producer to a Kafka.  The message in the exception was: {0}",
            "Egeria is unable to produce events",
            "Ensure that the Kafka service is available and that the connection properties are valid.")
        ;
        private final ExceptionMessageDefinition messageDefinition;


    /**
     * The constructor for KafkaOpenMetadataTopicConnectorErrorCode expects to be passed one of the enumeration rows defined in
     * KafkaOpenMetadataTopicConnectorErrorCode above.   For example:
     *
     *     KafkaOpenMetadataTopicConnectorErrorCode   errorCode = KafkaOpenMetadataTopicConnectorErrorCode.ERROR_SENDING_EVENT;
     *
     * This will expand out to the 5 parameters shown below.
     *
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique identifier for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    KafkaOpenMetadataTopicConnectorErrorCode(int  httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                errorMessageId,
                                                                errorMessage,
                                                                systemAction,
                                                                userAction);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "KafkaOpenMetadataTopicConnectorErrorCode{" +
                       "messageDefinition=" + messageDefinition +
                       '}';
    }
}
