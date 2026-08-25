/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

/**
 * MessageSetType describes the three flavours of message set defined in the audit log framework's
 * <i>org.odpi.openmetadata.frameworks.auditlog.messagesets</i> package.  The interface that a message set
 * enum implements determines both the values held in each message definition and the way the messages are
 * surfaced to the people running Egeria.
 */
public enum MessageSetType
{
    /**
     * Messages used to fill out the exceptions thrown by Egeria.
     */
    EXCEPTION("ExceptionMessageSet",
              "Exception messages",
              "These messages are used to fill out the exceptions thrown by Egeria.  Each message carries an " +
                      "HTTP error code so that the exception can be faithfully passed across a REST API call " +
                      "and rebuilt by the client."),

    /**
     * Messages written to the audit log destinations.
     */
    AUDIT_LOG("AuditLogMessageSet",
              "Audit log messages",
              "These messages are written to the audit log destinations configured for the OMAG Server Platform.  " +
                      "Each message carries a severity that describes the type of activity being reported and " +
                      "is used to route the message to the appropriate audit log destinations."),

    /**
     * Messages sent to people rather than to the audit log or an exception.
     */
    NOTIFICATION("MessageSet",
                 "Notification messages",
                 "These messages are the general purpose message sets.  They are used for message content that is " +
                         "neither an exception nor an audit log record - such as the notifications sent to a " +
                         "subscriber."),

    ;

    private final String interfaceName;
    private final String displayName;
    private final String description;


    /**
     * Constructor.
     *
     * @param interfaceName simple name of the interface that the message set enum implements
     * @param displayName short name used in the documentation
     * @param description longer explanation used in the documentation
     */
    MessageSetType(String interfaceName, String displayName, String description)
    {
        this.interfaceName = interfaceName;
        this.displayName   = displayName;
        this.description   = description;
    }


    /**
     * Return the simple name of the interface that the message set enum implements.
     *
     * @return interface name
     */
    public String getInterfaceName() { return interfaceName; }


    /**
     * Return the short name used in the documentation.
     *
     * @return display name
     */
    public String getDisplayName() { return displayName; }


    /**
     * Return the longer explanation used in the documentation.
     *
     * @return description
     */
    public String getDescription() { return description; }


    /**
     * Return the message set type that matches the supplied interface name.
     *
     * @param interfaceName simple name of an interface from an "implements" clause
     * @return matching message set type or null if this is not a message set interface
     */
    public static MessageSetType getMessageSetType(String interfaceName)
    {
        for (MessageSetType messageSetType : MessageSetType.values())
        {
            if (messageSetType.interfaceName.equals(interfaceName))
            {
                return messageSetType;
            }
        }

        return null;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MessageSetType{" + interfaceName + "}";
    }
}
