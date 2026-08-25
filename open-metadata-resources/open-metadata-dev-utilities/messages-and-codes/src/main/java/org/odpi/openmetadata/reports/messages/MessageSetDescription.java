/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.util.List;

/**
 * MessageSetDescription captures the content of a single message set - that is, one enum that implements one of
 * the message set interfaces from the audit log framework.
 */
public class MessageSetDescription
{
    private final String                 enumName;
    private final String                 packageName;
    private final MessageSetType         messageSetType;
    private final MessageSetCategory     category;
    private final String                 sourcePath;
    private final String                 description;
    private final List<MessageDescription> messages;


    /**
     * Constructor.
     *
     * @param enumName simple name of the message set enum
     * @param packageName java package that the message set is defined in
     * @param messageSetType which of the message set interfaces the enum implements
     * @param sourcePath path of the source file, relative to the repository root
     * @param description text taken from the class-level javadoc of the message set
     * @param messages the message definitions, in the order that they are declared
     */
    MessageSetDescription(String                   enumName,
                          String                   packageName,
                          MessageSetType           messageSetType,
                          String                   sourcePath,
                          String                   description,
                          List<MessageDescription> messages)
    {
        this.enumName       = enumName;
        this.packageName    = packageName;
        this.messageSetType = messageSetType;
        this.sourcePath     = sourcePath;
        this.category       = MessageSetCategory.getCategory(sourcePath);
        this.description    = description;
        this.messages       = messages;
    }


    /**
     * Return the simple name of the message set enum.
     *
     * @return java identifier
     */
    public String getEnumName() { return enumName; }


    /**
     * Return the java package that the message set is defined in.
     *
     * @return package name
     */
    public String getPackageName() { return packageName; }


    /**
     * Return the fully qualified name of the message set enum.
     *
     * @return qualified class name
     */
    public String getQualifiedName() { return packageName + "." + enumName; }


    /**
     * Return which of the message set interfaces the enum implements.
     *
     * @return message set type
     */
    public MessageSetType getMessageSetType() { return messageSetType; }


    /**
     * Return the part of the documentation that this message set is described in.
     *
     * @return category
     */
    public MessageSetCategory getCategory() { return category; }


    /**
     * Return the path of the source file, relative to the repository root.
     *
     * @return relative path, using "/" separators
     */
    public String getSourcePath() { return sourcePath; }


    /**
     * Return the path of the module that defines this message set, relative to the repository root.
     *
     * @return relative path, using "/" separators
     */
    public String getModulePath()
    {
        int sourceRootPosition = sourcePath.indexOf("/src/main/java/");

        if (sourceRootPosition > 0)
        {
            return sourcePath.substring(0, sourceRootPosition);
        }

        return sourcePath;
    }


    /**
     * Return the text taken from the class-level javadoc of the message set.
     *
     * @return description - may be null if the message set has no class-level javadoc
     */
    public String getDescription() { return description; }


    /**
     * Return the message definitions, in the order that they are declared in the enum.
     *
     * @return list of message descriptions
     */
    public List<MessageDescription> getMessages() { return messages; }


    /**
     * Return the part of the message identifier that is common to every message in this set.  This is the
     * value that someone looks up when they see a message identifier in a log or an exception.  The common
     * part is trimmed back to the last "-" so that it does not include a partial number.
     *
     * @return message identifier prefix - an empty string if the messages have nothing in common
     */
    public String getMessageIdPrefix()
    {
        if (messages.isEmpty())
        {
            return "";
        }

        String prefix = messages.get(0).getMessageId();

        for (MessageDescription message : messages)
        {
            String messageId = message.getMessageId();
            int    matching  = 0;

            while ((matching < prefix.length()) && (matching < messageId.length()) &&
                   (prefix.charAt(matching) == messageId.charAt(matching)))
            {
                matching = matching + 1;
            }

            prefix = prefix.substring(0, matching);
        }

        int lastSeparator = prefix.lastIndexOf('-');

        if (lastSeparator > 0)
        {
            return prefix.substring(0, lastSeparator);
        }

        return prefix;
    }


    /**
     * Return the name of the markdown file that documents this message set.
     *
     * @return file name
     */
    public String getFileName() { return enumName + ".md"; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MessageSetDescription{" + getQualifiedName() + ", messages=" + messages.size() + "}";
    }
}
