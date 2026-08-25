/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog.messagesets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MessageDefinition is a container that describes a single instance of a message.
 * It is in fact the superclass of a message for an exception and for the audit log
 * and as such, defines the values that are common to both.  The concrete subclasses
 * have the additional fields relevant to their specific definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MessageDefinition
{
    private final String messageId;
    private final String messageTemplate;
    private final String systemAction;
    private final String userAction;
    private final String url;

    private String[] params;

    /**
     * Constructor to save all the fixed values of a message.  This is typically populated
     * from an Enum message set.  Use the constructor that takes a url if there is a page
     * that explains this message's situation in more detail.
     *
     * @param messageId unique id of the message type.
     * @param messageTemplate template for the message text with placeholders for parameters.
     * @param systemAction description of the actions of the system when the situation arose.
     * @param userAction instructions on what to do next.
     */
    public MessageDefinition(String messageId, String messageTemplate, String systemAction, String userAction)
    {
        this(messageId, messageTemplate, systemAction, userAction, null);
    }


    /**
     * Constructor to save all the fixed values of a message, including a link to further reading.
     * This is typically populated from an Enum message set.
     *
     * @param messageId unique id of the message type.
     * @param messageTemplate template for the message text with placeholders for parameters.
     * @param systemAction description of the actions of the system when the situation arose.
     * @param userAction instructions on what to do next.
     * @param url link to a page that describes the component or concept behind this message -
     *            null if there is no suitable page.
     */
    public MessageDefinition(String messageId, String messageTemplate, String systemAction, String userAction, String url)
    {
        this.messageId       = messageId;
        this.messageTemplate = messageTemplate;
        this.systemAction    = systemAction;
        this.userAction      = userAction;
        this.url             = url;
    }


    /**
     * Return the unique identifier for the message.
     *
     * @return string id
     */
    public String getMessageId() { return messageId; }


    /**
     * Return the template of the message.
     *
     * @return string message text with placeholders
     */
    public String getMessageTemplate() { return messageTemplate; }


    /**
     * Set up the specific values that apply to this message instance.
     *
     * @param params list of string message inserts for the message template
     */
    public void setMessageParameters(String... params) { this.params = params; }


    /**
     * Return the array of values that apply to this message instance.
     *
     * @return array of string message inserts for the message template
     */
    public String[] getMessageParams() { return params; }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions on what to do next, given that this situation has occurred.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }


    /**
     * Returns the link to further reading about the component or concept behind this message.  This gives
     * the reader of a log record or an error response somewhere to go for background on the situation.
     *
     * @return url String - null if the message has no suitable page
     */
    public String getURL()
    {
        return url;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "MessageDefinition{" +
                       "messageId='" + messageId + '\'' +
                       ", messageTemplate='" + messageTemplate + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       ", url='" + url + '\'' +
                       ", params=" + Arrays.toString(params) +
                       '}';
    }
}
