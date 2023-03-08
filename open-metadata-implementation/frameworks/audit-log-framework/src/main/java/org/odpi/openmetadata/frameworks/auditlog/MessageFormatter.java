/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * MessageFormatter is the superclass of audit log implementations.  It provides the ability to format a message.
 */
public class MessageFormatter
{
    private static final Logger log = LoggerFactory.getLogger(MessageFormatter.class);


    /**
     * Default Constructor
     */
    public MessageFormatter()
    {
    }


    /**
     * Format the message.
     *
     * @param messageTemplate message with placeholders
     * @param messageParameters parameters to insert into placeholders
     * @return formatted message
     */
    private String formatMessage(String     messageTemplate,
                                 String[]   messageParameters)
    {
        MessageFormat mf        = new MessageFormat(messageTemplate);
        String formattedMessage = messageTemplate;
        int    parameterCount   = 0;

        if (messageParameters != null)
        {
            parameterCount = messageParameters.length;
            formattedMessage = mf.format(messageParameters);
        }

        if (mf.getFormats().length > parameterCount)
        {
            log.warn("Missing parameter for message {}", messageTemplate);
        }
        else if (mf.getFormats().length < parameterCount)
        {
            log.warn("Too many parameters for message {}", messageTemplate);
        }

        log.debug("New message: {}", formattedMessage);

        return formattedMessage;
    }


    /**
     * Create a formatted message from a message definition instance.  This instance
     * contains the unique message identifier, the default message template and the
     * parameters to insert into it.  The Audit Log controls whether to use the default
     * message template or substitute it for a version in a different language.
     *
     * The method is public to allow external components to format messages - for example,
     * from exceptions.
     *
     * @param messageDefinition details about the message to format.
     * @return formatted message
     */
    String getFormattedMessageText(MessageDefinition messageDefinition)
    {
        return this.formatMessage(messageDefinition.getMessageTemplate(),
                                  messageDefinition.getMessageParams());
    }


    /**
     * Create a formatted message from a message definition instance.  This instance
     * contains the unique message identifier, the default message template and the
     * parameters to insert into it.  The Audit Log controls whether to use the default
     * message template or substitute it for a version in a different language.
     *
     * The method is public to allow external components to format messages - for example,
     * from exceptions.
     *
     * @param messageDefinition details about the message to format.
     * @return formatted message
     */
    public String getFormattedMessage(MessageDefinition messageDefinition)
    {
        return messageDefinition.getMessageId() + " " + this.formatMessage(messageDefinition.getMessageTemplate(),
                                                                           messageDefinition.getMessageParams());
    }
}
