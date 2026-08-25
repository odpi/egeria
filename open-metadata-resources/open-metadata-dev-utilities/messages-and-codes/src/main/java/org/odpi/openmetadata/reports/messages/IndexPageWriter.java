/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * IndexPageWriter builds the README pages that let a reader navigate the documentation - the page at the root
 * of the documentation directory, and one page for each of its directories.
 */
class IndexPageWriter
{
    private final Map<String, AuditLogSeverityDescription> severities;


    /**
     * Constructor.
     *
     * @param severities the audit log severities, read from the audit log framework
     */
    IndexPageWriter(Map<String, AuditLogSeverityDescription> severities)
    {
        this.severities = severities;
    }


    /**
     * Build the page at the root of the documentation directory.  It explains how Egeria's messages are put
     * together and indexes every message set by the prefix of its message identifiers, because that is what
     * a reader has in their hand when they arrive from a log file or an error response.
     *
     * @param messageSets every message set in the repository
     * @return markdown content
     */
    String createRootPage(List<MessageSetDescription> messageSets)
    {
        MarkdownBuilder builder = new MarkdownBuilder(0);

        builder.addHeading(1, "Egeria Messages and Codes");

        builder.addParagraph(
                "Egeria practises *first failure data capture* (FFDC).  When something notable happens - " +
                        "whether it is a failure, a decision, or a step in a long-running process - the " +
                        "component involved raises a message that carries enough information to understand " +
                        "and act on the situation without having to reproduce it.");
        builder.addParagraph(
                "These pages document every message that Egeria can produce.  They are generated from the " +
                        "message set definitions in the Egeria source, so they always describe the messages " +
                        "of the release they are shipped with.");

        addAnatomySection(builder);
        addMessageTypeSection(builder, messageSets);
        addSeveritySection(builder);
        addHttpErrorCodeSection(builder);
        addCategoryIndex(builder, messageSets);
        addPrefixIndex(builder, messageSets);

        return builder.getPage();
    }


    /**
     * Explain the fields that make up a message.
     *
     * @param builder page being built
     */
    private void addAnatomySection(MarkdownBuilder builder)
    {
        builder.addHeading(2, "The anatomy of a message");

        builder.addParagraph(
                "Every Egeria message is defined once, as a constant in a *message set*.  A message set is a " +
                        "java enum that implements one of the interfaces in the audit log framework's " +
                        "`org.odpi.openmetadata.frameworks.auditlog.messagesets` package.  Each message " +
                        "definition supplies the following fields.");

        builder.addTableRow("Field", "Description");
        builder.addTableSeparator(2);
        builder.addTableRow("Message identifier",
                            "Uniquely identifies the message.  It is the value to search for in these pages " +
                                    "when a message appears in a log or an error response.  The identifier " +
                                    "never changes, even when the wording of the message is improved.");
        builder.addTableRow("Message text",
                            "Describes what happened.  The `{0}`, `{1}` ... markers in the text show where " +
                                    "the *message inserts* are placed.  The inserts carry the values - " +
                                    "server names, element identifiers, exception messages - that are only " +
                                    "known at the moment the message is raised.");
        builder.addTableRow("System action",
                            "Describes what Egeria did as a result of the situation.  This is how to tell " +
                                    "whether the request was abandoned, retried, or carried on regardless.");
        builder.addTableRow("User action",
                            "Describes what the reader should do next.  For an informational message this " +
                                    "is often \"nothing\"; for a failure it explains how to investigate and " +
                                    "correct the problem.");
        builder.addTableRow("Further reading",
                            "An optional link to the page on this site, or in the Egeria repository, that " +
                                    "describes the component or concept the message is about.  Not every " +
                                    "message has one.  Where a message does have a link, it travels with the " +
                                    "message: it is written to the audit log record, and it is carried on the " +
                                    "exception and in the error response of a REST API call.");
        builder.addTableRow("Severity or HTTP error code",
                            "An audit log message carries a *severity* that says what kind of activity is " +
                                    "being reported.  An exception message carries an *HTTP error code* so " +
                                    "that the exception survives a REST API call.");
        builder.addRawMarkdown("\n");
    }


    /**
     * Describe the three types of message set, with a count of each.
     *
     * @param builder page being built
     * @param messageSets every message set in the repository
     */
    private void addMessageTypeSection(MarkdownBuilder builder, List<MessageSetDescription> messageSets)
    {
        builder.addHeading(2, "Types of message");

        builder.addTableRow("Type", "Message sets", "Messages", "Description");
        builder.addTableSeparator(4);

        for (MessageSetType messageSetType : MessageSetType.values())
        {
            List<MessageSetDescription> matchingSets = getMessageSets(messageSets, messageSetType);

            builder.addTableRow(messageSetType.getDisplayName(),
                                Integer.toString(matchingSets.size()),
                                Integer.toString(countMessages(matchingSets)),
                                messageSetType.getDescription());
        }

        builder.addRawMarkdown("\n");
    }


    /**
     * List the audit log severities.
     *
     * @param builder page being built
     */
    private void addSeveritySection(MarkdownBuilder builder)
    {
        if (severities.isEmpty())
        {
            return;
        }

        builder.addHeading(2, "Audit log severities");

        builder.addParagraph(
                "The severity of an audit log message says what kind of activity is being reported.  It is " +
                        "used to decide which audit log destinations a message is written to, and it is the " +
                        "quickest way to pick the significant records out of a busy log.");

        builder.addTableRow("Severity", "Shown in the log as", "Description");
        builder.addTableSeparator(3);

        for (AuditLogSeverityDescription severity : severities.values())
        {
            builder.addTableRow("`" + severity.name() + "`", severity.displayName(), severity.description());
        }

        builder.addRawMarkdown("\n");
    }


    /**
     * List the HTTP error codes used by the exception messages.
     *
     * @param builder page being built
     */
    private void addHttpErrorCodeSection(MarkdownBuilder builder)
    {
        builder.addHeading(2, "HTTP error codes");

        builder.addParagraph(
                "An exception message carries an HTTP error code so that an exception raised deep inside a " +
                        "server can be returned over a REST API call and rebuilt as the equivalent exception " +
                        "in the client.  The code also indicates who is best placed to fix the problem: the " +
                        "4xx codes point at the caller's request, the 5xx codes at the server.");

        builder.addTableRow("Code", "Meaning");
        builder.addTableSeparator(2);

        for (Map.Entry<Integer, String> httpErrorCode : HttpErrorCodeDescription.getAllDescriptions().entrySet())
        {
            builder.addTableRow(Integer.toString(httpErrorCode.getKey()), httpErrorCode.getValue());
        }

        builder.addRawMarkdown("\n");
    }


    /**
     * Index the directories of the documentation.
     *
     * @param builder page being built
     * @param messageSets every message set in the repository
     */
    private void addCategoryIndex(MarkdownBuilder builder, List<MessageSetDescription> messageSets)
    {
        builder.addHeading(2, "Where the messages come from");

        builder.addParagraph(
                "The message sets are grouped to match the part of Egeria that defines them.");

        builder.addTableRow("Area", "Message sets", "Messages", "Description");
        builder.addTableSeparator(4);

        for (MessageSetCategory category : MessageSetCategory.values())
        {
            List<MessageSetDescription> matchingSets = getMessageSets(messageSets, category);

            if (! matchingSets.isEmpty())
            {
                builder.addTableRow("[" + category.getDisplayName() + "](" + category.getDirectoryName() + ")",
                                    Integer.toString(matchingSets.size()),
                                    Integer.toString(countMessages(matchingSets)),
                                    category.getDescription());
            }
        }

        builder.addRawMarkdown("\n");
    }


    /**
     * Index every message set by the prefix of its message identifiers.  This is the table to use when a
     * message identifier has been found in a log file or an error response.
     *
     * @param builder page being built
     * @param messageSets every message set in the repository
     */
    private void addPrefixIndex(MarkdownBuilder builder, List<MessageSetDescription> messageSets)
    {
        builder.addHeading(2, "Finding a message identifier");

        builder.addParagraph(
                "Every message identifier begins with a prefix that names the component that raised it.  " +
                        "Find the prefix in the table below to reach the page that documents the message.");

        List<MessageSetDescription> sortedSets = new ArrayList<>(messageSets);

        sortedSets.sort(Comparator.comparing(MessageSetDescription::getMessageIdPrefix)
                                  .thenComparing(MessageSetDescription::getEnumName));

        builder.addTableRow("Message identifiers", "Type", "Messages", "Message set");
        builder.addTableSeparator(4);

        for (MessageSetDescription messageSet : sortedSets)
        {
            builder.addTableRow("`" + messageSet.getMessageIdPrefix() + "-`",
                                messageSet.getMessageSetType().getDisplayName(),
                                Integer.toString(messageSet.getMessages().size()),
                                "[" + messageSet.getEnumName() + "](" +
                                        messageSet.getCategory().getDirectoryName() + "/" +
                                        messageSet.getFileName() + ")");
        }

        builder.addRawMarkdown("\n");
    }


    /**
     * Build the README page for one of the directories of the documentation.
     *
     * @param category the category that the directory holds
     * @param messageSets every message set in the repository
     * @return markdown content
     */
    String createCategoryPage(MessageSetCategory category, List<MessageSetDescription> messageSets)
    {
        List<MessageSetDescription> matchingSets = getMessageSets(messageSets, category);
        int                         depth        = category.getDirectoryName().split("/").length;
        MarkdownBuilder             builder      = new MarkdownBuilder(depth);

        builder.addHeading(1, category.getDisplayName() + " Messages");
        builder.addParagraph(category.getDescription());
        builder.addParagraph("This directory documents " + countMessages(matchingSets) + " messages in " +
                                     matchingSets.size() + " message sets.  Return to the " +
                                     "[messages and codes index](" + "../".repeat(depth) + "README.md).");

        builder.addHeading(2, "Message sets");

        builder.addTableRow("Message set", "Type", "Message identifiers", "Messages", "Further reading");
        builder.addTableSeparator(5);

        for (MessageSetDescription messageSet : matchingSets)
        {
            String url = getCommonUrl(messageSet);

            builder.addTableRow("[" + messageSet.getEnumName() + "](" + messageSet.getFileName() + ")",
                                messageSet.getMessageSetType().getDisplayName(),
                                "`" + messageSet.getMessageIdPrefix() + "-`",
                                Integer.toString(messageSet.getMessages().size()),
                                (url == null) ? "" : "<" + url + ">");
        }

        builder.addRawMarkdown("\n");

        return builder.getPage();
    }


    /**
     * Build the README page for a directory that only holds other directories.
     *
     * @param directoryName location of the directory within the documentation
     * @param displayName name of the directory used in the documentation
     * @param description explanation of the directory used in the documentation
     * @param categories the categories held in the directory
     * @param messageSets every message set in the repository
     * @return markdown content
     */
    String createGroupPage(String                   directoryName,
                           String                   displayName,
                           String                   description,
                           List<MessageSetCategory> categories,
                           List<MessageSetDescription> messageSets)
    {
        int             depth   = directoryName.split("/").length;
        MarkdownBuilder builder = new MarkdownBuilder(depth);

        builder.addHeading(1, displayName + " Messages");
        builder.addParagraph(description);
        builder.addParagraph("Return to the [messages and codes index](" + "../".repeat(depth) + "README.md).");

        builder.addTableRow("Area", "Message sets", "Messages", "Description");
        builder.addTableSeparator(4);

        for (MessageSetCategory category : categories)
        {
            List<MessageSetDescription> matchingSets = getMessageSets(messageSets, category);
            String                      subdirectory = category.getDirectoryName()
                                                               .substring(directoryName.length() + 1);

            builder.addTableRow("[" + category.getDisplayName() + "](" + subdirectory + ")",
                                Integer.toString(matchingSets.size()),
                                Integer.toString(countMessages(matchingSets)),
                                category.getDescription());
        }

        builder.addRawMarkdown("\n");

        return builder.getPage();
    }


    /**
     * Return the link to further reading that every message in the set shares.
     *
     * @param messageSet the message set of interest
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
     * Return the message sets that belong to a category.
     *
     * @param messageSets every message set in the repository
     * @param category the category of interest
     * @return matching message sets, in name order
     */
    private List<MessageSetDescription> getMessageSets(List<MessageSetDescription> messageSets,
                                                       MessageSetCategory          category)
    {
        List<MessageSetDescription> matchingSets = new ArrayList<>();

        for (MessageSetDescription messageSet : messageSets)
        {
            if (messageSet.getCategory() == category)
            {
                matchingSets.add(messageSet);
            }
        }

        return matchingSets;
    }


    /**
     * Return the message sets of a particular type.
     *
     * @param messageSets every message set in the repository
     * @param messageSetType the type of interest
     * @return matching message sets, in name order
     */
    private List<MessageSetDescription> getMessageSets(List<MessageSetDescription> messageSets,
                                                       MessageSetType              messageSetType)
    {
        List<MessageSetDescription> matchingSets = new ArrayList<>();

        for (MessageSetDescription messageSet : messageSets)
        {
            if (messageSet.getMessageSetType() == messageSetType)
            {
                matchingSets.add(messageSet);
            }
        }

        return matchingSets;
    }


    /**
     * Count the messages defined by a list of message sets.
     *
     * @param messageSets message sets to count
     * @return number of messages
     */
    private int countMessages(List<MessageSetDescription> messageSets)
    {
        int messageCount = 0;

        for (MessageSetDescription messageSet : messageSets)
        {
            messageCount = messageCount + messageSet.getMessages().size();
        }

        return messageCount;
    }
}
