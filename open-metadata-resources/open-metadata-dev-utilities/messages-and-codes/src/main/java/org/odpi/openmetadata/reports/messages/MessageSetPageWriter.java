/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.util.List;
import java.util.Map;

/**
 * MessageSetPageWriter builds the markdown page that documents a single message set.  The page opens with a
 * summary table of every message in the set - which is what someone scanning for a message identifier needs -
 * and then gives the full detail of each message underneath.
 */
class MessageSetPageWriter
{
    private final Map<String, AuditLogSeverityDescription> severities;


    /**
     * Constructor.
     *
     * @param severities the audit log severities, read from the audit log framework
     */
    MessageSetPageWriter(Map<String, AuditLogSeverityDescription> severities)
    {
        this.severities = severities;
    }


    /**
     * Build the page for a message set.
     *
     * @param messageSet the message set to document
     * @return markdown content
     */
    String createPage(MessageSetDescription messageSet)
    {
        int             depth   = countDirectories(messageSet.getCategory().getDirectoryName());
        MarkdownBuilder builder = new MarkdownBuilder(depth);
        String          pathToRepositoryRoot = "../".repeat(depth + 1);

        builder.addHeading(1, messageSet.getEnumName());
        builder.addParagraph(messageSet.getDescription());

        String commonUrl = getCommonUrl(messageSet);

        addSummaryTable(builder, messageSet, pathToRepositoryRoot, commonUrl);
        addMessageTable(builder, messageSet);

        for (MessageDescription message : messageSet.getMessages())
        {
            addMessageDetail(builder, messageSet, message, commonUrl);
        }

        return builder.getPage();
    }


    /**
     * Add the table that describes the message set itself - where it lives and what its messages look like.
     *
     * @param builder page being built
     * @param messageSet the message set being documented
     * @param pathToRepositoryRoot relative path from this page back to the root of the repository
     * @param commonUrl link to further reading shared by every message in the set; null if they differ
     */
    private void addSummaryTable(MarkdownBuilder       builder,
                                 MessageSetDescription messageSet,
                                 String                pathToRepositoryRoot,
                                 String                commonUrl)
    {
        builder.addTableRow("", "");
        builder.addTableSeparator(2);
        builder.addTableRow("**Type of message**",
                            messageSet.getMessageSetType().getDisplayName());
        builder.addTableRow("**Number of messages**",
                            Integer.toString(messageSet.getMessages().size()));

        if (! messageSet.getMessageIdPrefix().isEmpty())
        {
            builder.addTableRow("**Message identifiers begin**",
                                "`" + messageSet.getMessageIdPrefix() + "-`");
        }

        builder.addTableRow("**Java class**",
                            "`" + messageSet.getQualifiedName() + "`");
        builder.addTableRow("**Module**",
                            "[" + messageSet.getModulePath() + "](" + pathToRepositoryRoot +
                                    messageSet.getModulePath() + ")");
        builder.addTableRow("**Source**",
                            "[" + messageSet.getEnumName() + ".java](" + pathToRepositoryRoot +
                                    messageSet.getSourcePath() + ")");

        if (commonUrl != null)
        {
            builder.addTableRow("**Further reading**", "<" + commonUrl + ">");
        }

        builder.addRawMarkdown("\n");
    }


    /**
     * Add the table that lists every message in the set, with a link down to its detail.
     *
     * @param builder page being built
     * @param messageSet the message set being documented
     */
    private void addMessageTable(MarkdownBuilder builder, MessageSetDescription messageSet)
    {
        boolean isException = (messageSet.getMessageSetType() == MessageSetType.EXCEPTION);
        boolean isAuditLog  = (messageSet.getMessageSetType() == MessageSetType.AUDIT_LOG);

        builder.addHeading(2, "Messages");

        if (messageSet.getMessages().isEmpty())
        {
            builder.addParagraph("This message set does not define any messages.");
            return;
        }

        if (isException)
        {
            builder.addTableRow("Message Id", "HTTP Code", "Message");
            builder.addTableSeparator(3);
        }
        else if (isAuditLog)
        {
            builder.addTableRow("Message Id", "Severity", "Message");
            builder.addTableSeparator(3);
        }
        else
        {
            builder.addTableRow("Message Id", "Message");
            builder.addTableSeparator(2);
        }

        for (MessageDescription message : messageSet.getMessages())
        {
            String link = "[" + message.getMessageId() + "](#" +
                                  MarkdownBuilder.getAnchor(message.getMessageId()) + ")";
            String text = MarkdownBuilder.escapeText(message.getMessageTemplate());

            if (isException)
            {
                builder.addTableRow(link, Integer.toString(message.getHttpErrorCode()), text);
            }
            else if (isAuditLog)
            {
                builder.addTableRow(link, message.getSeverity(), text);
            }
            else
            {
                builder.addTableRow(link, text);
            }
        }
    }


    /**
     * Add the full detail of a single message.
     *
     * @param builder page being built
     * @param messageSet the message set being documented
     * @param message the message being documented
     * @param commonUrl link to further reading shared by every message in the set; null if they differ
     */
    private void addMessageDetail(MarkdownBuilder       builder,
                                  MessageSetDescription messageSet,
                                  MessageDescription    message,
                                  String                commonUrl)
    {
        builder.addHorizontalRule();
        builder.addHeading(3, message.getMessageId());
        builder.addQuote(message.getMessageTemplate());

        builder.addTableRow("", "");
        builder.addTableSeparator(2);
        builder.addTableRow("**Java constant**",
                            "`" + messageSet.getEnumName() + "." + message.getEnumConstantName() + "`");

        if (messageSet.getMessageSetType() == MessageSetType.EXCEPTION)
        {
            String httpErrorCodeDescription = HttpErrorCodeDescription.getDescription(message.getHttpErrorCode());

            builder.addTableRow("**HTTP error code**",
                                message.getHttpErrorCode() +
                                        (httpErrorCodeDescription.isEmpty() ? "" : " - " + httpErrorCodeDescription));
        }
        else if (messageSet.getMessageSetType() == MessageSetType.AUDIT_LOG)
        {
            AuditLogSeverityDescription severity = severities.get(message.getSeverity());

            builder.addTableRow("**Severity**",
                                message.getSeverity() +
                                        ((severity == null) ? "" : " - " + severity.description()));
        }

        builder.addTableRow("**Message inserts**", getInsertsDescription(message));

        if ((message.getUrl() != null) && (! message.getUrl().equals(commonUrl)))
        {
            builder.addTableRow("**Further reading**", "<" + message.getUrl() + ">");
        }

        builder.addRawMarkdown("\n");

        builder.addParagraph("**System action**");
        builder.addParagraph(MarkdownBuilder.escapeText(message.getSystemAction()));
        builder.addParagraph("**User action**");
        builder.addParagraph(MarkdownBuilder.escapeText(message.getUserAction()));
    }


    /**
     * Describe the inserts that the message text expects.  The inserts are the values that the component
     * raising the message supplies through {@code getMessageDefinition(String...)}.
     *
     * @param message the message being documented
     * @return description of the inserts
     */
    private String getInsertsDescription(MessageDescription message)
    {
        List<Integer> inserts = message.getMessageInserts();

        if (inserts.isEmpty())
        {
            return "none";
        }

        StringBuilder description = new StringBuilder();

        for (Integer insert : inserts)
        {
            if (! description.isEmpty())
            {
                description.append(", ");
            }

            description.append("`{").append(insert).append("}`");
        }

        return description.toString();
    }


    /**
     * Return the link to further reading that every message in the set shares.  Most message sets point all
     * of their messages at the page that describes the component, and repeating that link under every
     * message would be noise, so it is shown once at the top of the page instead.
     *
     * @param messageSet the message set being documented
     * @return url shared by every message in the set; null if the messages differ or none supply a url
     */
    private String getCommonUrl(MessageSetDescription messageSet)
    {
        String commonUrl = null;

        for (MessageDescription message : messageSet.getMessages())
        {
            if (message.getUrl() == null)
            {
                return null;
            }

            if (commonUrl == null)
            {
                commonUrl = message.getUrl();
            }
            else if (! commonUrl.equals(message.getUrl()))
            {
                return null;
            }
        }

        return commonUrl;
    }


    /**
     * Return the number of directories in a path.
     *
     * @param directoryName path using "/" separators
     * @return number of directories
     */
    private int countDirectories(String directoryName)
    {
        return directoryName.split("/").length;
    }
}
