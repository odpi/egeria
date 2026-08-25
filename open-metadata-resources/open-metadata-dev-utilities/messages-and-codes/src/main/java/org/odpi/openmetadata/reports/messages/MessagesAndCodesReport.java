/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * MessagesAndCodesReport generates the markdown documentation for every message that Egeria can produce.  It
 * is run from the gradle build so that the documentation is regenerated whenever a message set changes.
 * <br><br>
 * It takes two arguments:
 * <ul>
 *     <li>the root directory of the Egeria repository - the source tree that is scanned for message sets;</li>
 *     <li>the directory that the documentation is written to - by default the <i>messages-and-codes</i>
 *     directory at the root of the repository.</li>
 * </ul>
 */
public class MessagesAndCodesReport
{
    private static final String DEFAULT_DOCUMENTATION_DIRECTORY = "messages-and-codes";
    private static final String README_FILE_NAME                = "README.md";
    private static final String CONNECTORS_DIRECTORY            = "connectors";
    private static final String CONNECTORS_DISPLAY_NAME         = "Connector";
    private static final String CONNECTORS_DESCRIPTION =
            "Connectors are the pluggable components of Egeria.  They run inside the OMAG Server Platform and " +
                    "call out to the technologies that Egeria is integrating with, so their messages are the " +
                    "ones most often seen when a third party technology misbehaves.";


    /**
     * Generate the documentation.
     *
     * @param args the root directory of the repository, followed by the directory to write to
     */
    public static void main(String[] args)
    {
        Path repositoryRoot = Paths.get((args.length > 0) ? args[0] : ".").toAbsolutePath().normalize();
        Path documentationDirectory = (args.length > 1) ? Paths.get(args[1]).toAbsolutePath().normalize()
                                                        : repositoryRoot.resolve(DEFAULT_DOCUMENTATION_DIRECTORY);

        try
        {
            new MessagesAndCodesReport().generateDocumentation(repositoryRoot, documentationDirectory);
        }
        catch (Exception error)
        {
            System.err.println("The messages and codes documentation could not be generated: " +
                                       error.getMessage());
            error.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Scan the repository for message sets and write out the documentation.
     *
     * @param repositoryRoot root directory of the Egeria repository
     * @param documentationDirectory directory that the documentation is written to
     * @throws IOException the source tree or the documentation directory could not be read or written
     * @throws MessageSetParsingException a message set could not be understood
     */
    void generateDocumentation(Path repositoryRoot,
                               Path documentationDirectory) throws IOException, MessageSetParsingException
    {
        System.out.println("Scanning " + repositoryRoot + " for message sets ...");

        MessageSetScanner                        scanner     = new MessageSetScanner(repositoryRoot);
        List<MessageSetDescription>              messageSets = scanner.scanForMessageSets();
        Map<String, AuditLogSeverityDescription> severities  = scanner.scanForSeverities();

        validateMessageSets(messageSets);

        DocumentationDirectory directory   = new DocumentationDirectory(documentationDirectory);
        IndexPageWriter        indexWriter = new IndexPageWriter(severities);
        MessageSetPageWriter   pageWriter  = new MessageSetPageWriter(severities);

        Files.createDirectories(documentationDirectory);

        directory.savePage(README_FILE_NAME, indexWriter.createRootPage(messageSets));

        for (MessageSetCategory category : getPopulatedCategories(messageSets))
        {
            directory.savePage(category.getDirectoryName() + "/" + README_FILE_NAME,
                               indexWriter.createCategoryPage(category, messageSets));
        }

        List<MessageSetCategory> connectorCategories = getConnectorCategories(messageSets);

        if (! connectorCategories.isEmpty())
        {
            directory.savePage(CONNECTORS_DIRECTORY + "/" + README_FILE_NAME,
                               indexWriter.createGroupPage(CONNECTORS_DIRECTORY,
                                                           CONNECTORS_DISPLAY_NAME,
                                                           CONNECTORS_DESCRIPTION,
                                                           connectorCategories,
                                                           messageSets));
        }

        for (MessageSetDescription messageSet : messageSets)
        {
            directory.savePage(messageSet.getCategory().getDirectoryName() + "/" + messageSet.getFileName(),
                               pageWriter.createPage(messageSet));
        }

        directory.removeStalePages();

        System.out.println("Documented " + countMessages(messageSets) + " messages in " + messageSets.size() +
                                   " message sets - " + directory.getChangeSummary() + ".");
    }


    /**
     * Check that the message sets can be documented and that their message identifiers are unique.  A
     * duplicate message identifier means that someone reading a log cannot tell which situation they are
     * looking at, so it fails the build.
     *
     * @param messageSets every message set in the repository
     * @throws MessageSetParsingException a message identifier is used more than once, or two message sets
     *                                    would be documented on the same page
     */
    private void validateMessageSets(List<MessageSetDescription> messageSets) throws MessageSetParsingException
    {
        Map<String, String> messageIdOwners = new TreeMap<>();
        Set<String>         pageNames       = new HashSet<>();

        for (MessageSetDescription messageSet : messageSets)
        {
            String pageName = messageSet.getCategory().getDirectoryName() + "/" + messageSet.getFileName();

            if (! pageNames.add(pageName))
            {
                throw new MessageSetParsingException(messageSet.getSourcePath(),
                                                     "another message set of the same name is documented on " +
                                                             pageName + "; rename one of them so that both " +
                                                             "can be documented");
            }

            for (MessageDescription message : messageSet.getMessages())
            {
                String previousOwner = messageIdOwners.put(message.getMessageId(),
                                                           messageSet.getQualifiedName());

                if (previousOwner != null)
                {
                    throw new MessageSetParsingException(messageSet.getSourcePath(),
                                                         "message identifier " + message.getMessageId() +
                                                                 " is also used by " + previousOwner);
                }
            }
        }
    }


    /**
     * Return the categories that at least one message set belongs to, in declaration order.
     *
     * @param messageSets every message set in the repository
     * @return populated categories
     */
    private List<MessageSetCategory> getPopulatedCategories(List<MessageSetDescription> messageSets)
    {
        Map<MessageSetCategory, Integer> counts = new HashMap<>();

        for (MessageSetDescription messageSet : messageSets)
        {
            counts.merge(messageSet.getCategory(), 1, Integer::sum);
        }

        List<MessageSetCategory> populatedCategories = new ArrayList<>();

        for (MessageSetCategory category : MessageSetCategory.values())
        {
            if (counts.containsKey(category))
            {
                populatedCategories.add(category);
            }
        }

        return populatedCategories;
    }


    /**
     * Return the populated categories that sit inside the connectors directory.
     *
     * @param messageSets every message set in the repository
     * @return connector categories
     */
    private List<MessageSetCategory> getConnectorCategories(List<MessageSetDescription> messageSets)
    {
        List<MessageSetCategory> connectorCategories = new ArrayList<>();

        for (MessageSetCategory category : getPopulatedCategories(messageSets))
        {
            if (category.getDirectoryName().startsWith(CONNECTORS_DIRECTORY + "/"))
            {
                connectorCategories.add(category);
            }
        }

        return connectorCategories;
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
