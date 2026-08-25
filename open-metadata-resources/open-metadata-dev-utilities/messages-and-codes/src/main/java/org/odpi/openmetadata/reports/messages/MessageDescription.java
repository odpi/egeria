/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MessageDescription captures the content of a single message definition found in a message set.  A message
 * definition is one of the enum constants of the message set.  Its values are literal constants in the source
 * of the message set and so they are extracted directly from the source file.
 */
public class MessageDescription
{
    private static final Pattern INSERT_PATTERN = Pattern.compile("\\{(\\d+)}");

    private final String enumConstantName;
    private final String messageId;
    private final String messageTemplate;
    private final String systemAction;
    private final String userAction;
    private final int    httpErrorCode;
    private final String severity;
    private final String url;


    /**
     * Constructor.
     *
     * @param enumConstantName name of the enum constant that defines this message
     * @param messageId unique identifier of the message
     * @param messageTemplate message text, including any {n} inserts
     * @param systemAction description of what the system did
     * @param userAction description of what the caller should do
     * @param httpErrorCode HTTP error code for an exception message; zero for an audit log or notification message
     * @param severity audit log severity for an audit log message; null for the other types of message
     * @param url link to a page that describes the component or concept behind this message; null if the
     *            message definition does not supply one
     */
    MessageDescription(String enumConstantName,
                       String messageId,
                       String messageTemplate,
                       String systemAction,
                       String userAction,
                       int    httpErrorCode,
                       String severity,
                       String url)
    {
        this.enumConstantName = enumConstantName;
        this.messageId        = messageId;
        this.messageTemplate  = messageTemplate;
        this.systemAction     = systemAction;
        this.userAction       = userAction;
        this.httpErrorCode    = httpErrorCode;
        this.severity         = severity;
        this.url              = url;
    }


    /**
     * Return the name of the enum constant that defines this message.  This is the name that a developer uses
     * when they raise the message.
     *
     * @return java identifier
     */
    public String getEnumConstantName() { return enumConstantName; }


    /**
     * Return the unique identifier of the message.  This is the value that appears in the log or exception.
     *
     * @return message identifier
     */
    public String getMessageId() { return messageId; }


    /**
     * Return the message text.  The text includes {n} markers showing where the message inserts are placed.
     *
     * @return message template
     */
    public String getMessageTemplate() { return messageTemplate; }


    /**
     * Return the description of what the system did when this situation occurred.
     *
     * @return system action
     */
    public String getSystemAction() { return systemAction; }


    /**
     * Return the description of what the caller should do in response to this message.
     *
     * @return user action
     */
    public String getUserAction() { return userAction; }


    /**
     * Return the HTTP error code returned when this message is used in a REST API response.
     *
     * @return HTTP status code; zero if this is not an exception message
     */
    public int getHttpErrorCode() { return httpErrorCode; }


    /**
     * Return the name of the audit log severity used when this message is logged.
     *
     * @return severity name; null if this is not an audit log message
     */
    public String getSeverity() { return severity; }


    /**
     * Return the link to further reading about the component or concept behind this message.
     *
     * @return url; null if the message definition does not supply one
     */
    public String getUrl() { return url; }


    /**
     * Return the numbers of the message inserts used in the message template, in ascending order.
     * For example, a template of "Connector {1} failed for asset {0}" returns [0, 1].
     *
     * @return ordered list of insert numbers
     */
    public List<Integer> getMessageInserts()
    {
        List<Integer> inserts = new ArrayList<>();
        Matcher       matcher = INSERT_PATTERN.matcher(messageTemplate);

        while (matcher.find())
        {
            Integer insertNumber = Integer.valueOf(matcher.group(1));

            if (! inserts.contains(insertNumber))
            {
                inserts.add(insertNumber);
            }
        }

        inserts.sort(null);

        return inserts;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MessageDescription{messageId='" + messageId + "', enumConstantName='" + enumConstantName + "'}";
    }
}
