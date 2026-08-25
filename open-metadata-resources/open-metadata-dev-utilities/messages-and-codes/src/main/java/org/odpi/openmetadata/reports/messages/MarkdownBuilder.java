/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.util.Locale;

/**
 * MarkdownBuilder accumulates the content of a markdown page and provides the escaping and anchor rules that
 * keep the generated pages rendering correctly on GitHub.
 */
class MarkdownBuilder
{
    /**
     * Header added to the top of every generated page.  The documentation is published under the Creative
     * Commons licence used by the rest of the Egeria documentation.
     */
    private static final String PAGE_HEADER = """
            <!-- SPDX-License-Identifier: CC-BY-4.0 -->
            <!-- Copyright Contributors to the Egeria project. -->

            """;

    /**
     * Footer added to the bottom of every generated page.
     */
    private static final String PAGE_FOOTER = """

            ----

            *This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](%sopen-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

            License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
            Copyright Contributors to the Egeria project.
            """;

    private final StringBuilder content = new StringBuilder(PAGE_HEADER);
    private final int           depth;


    /**
     * Constructor.
     *
     * @param depth how many directories this page sits below the root of the documentation directory
     */
    MarkdownBuilder(int depth)
    {
        this.depth = depth;
    }


    /**
     * Add a heading.
     *
     * @param level heading level - 1 for the page title
     * @param text heading text
     */
    void addHeading(int level, String text)
    {
        content.append("\n").append("#".repeat(level)).append(" ").append(text).append("\n\n");
    }


    /**
     * Add a paragraph of text.
     *
     * @param text paragraph text - ignored if null or blank
     */
    void addParagraph(String text)
    {
        if ((text != null) && (! text.isBlank()))
        {
            content.append(text.strip()).append("\n\n");
        }
    }


    /**
     * Add a block quote.  This is used for the message text itself so that it stands out on the page.
     *
     * @param text quoted text
     */
    void addQuote(String text)
    {
        content.append("> ").append(escapeText(text).replace("\n", "\n> ")).append("\n\n");
    }


    /**
     * Add a row to a markdown table.  The cells are escaped so that any "|" in the message text does not
     * break the table.
     *
     * @param cells the contents of the row's cells
     */
    void addTableRow(String... cells)
    {
        content.append("|");

        for (String cell : cells)
        {
            content.append(" ").append((cell == null) ? "" : cell.replace("|", "\\|")).append(" |");
        }

        content.append("\n");
    }


    /**
     * Add the separator row that turns the first row of a markdown table into its column headings.
     *
     * @param columnCount number of columns in the table
     */
    void addTableSeparator(int columnCount)
    {
        content.append("|").append("---|".repeat(columnCount)).append("\n");
    }


    /**
     * Add a horizontal rule.
     */
    void addHorizontalRule()
    {
        content.append("\n----\n");
    }


    /**
     * Add text that has already been formatted as markdown.
     *
     * @param markdown raw markdown
     */
    void addRawMarkdown(String markdown)
    {
        content.append(markdown);
    }


    /**
     * Return the finished page.
     *
     * @return markdown text
     */
    String getPage()
    {
        return content + String.format(PAGE_FOOTER, "../".repeat(depth + 1));
    }


    /**
     * Escape the characters that markdown would otherwise interpret.  Message text regularly contains angle
     * brackets and asterisks.
     *
     * @param text text to escape - may be null
     * @return escaped text
     */
    static String escapeText(String text)
    {
        if (text == null)
        {
            return "";
        }

        return text.replace("\\", "\\\\")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("*", "\\*")
                   .replace("_", "\\_");
    }


    /**
     * Return the anchor that GitHub generates for a heading, so that the summary tables at the top of a page
     * can link down to the detail below.
     *
     * @param headingText text of the heading
     * @return anchor, without the leading "#"
     */
    static String getAnchor(String headingText)
    {
        StringBuilder anchor = new StringBuilder();

        for (char currentChar : headingText.toLowerCase(Locale.ROOT).toCharArray())
        {
            if (Character.isLetterOrDigit(currentChar))
            {
                anchor.append(currentChar);
            }
            else if ((currentChar == ' ') || (currentChar == '-') || (currentChar == '_'))
            {
                anchor.append('-');
            }
        }

        return anchor.toString();
    }


    /**
     * Turn the body of a javadoc comment into a paragraph of markdown.  The leading asterisks, the javadoc
     * tags and the simplest of the HTML markup are removed.
     *
     * @param javadoc body of a javadoc comment
     * @return plain text description, or null if the comment has no descriptive text
     */
    static String tidyJavadoc(String javadoc)
    {
        StringBuilder description = new StringBuilder();

        for (String line : javadoc.split("\n"))
        {
            String tidiedLine = line.strip();

            if (tidiedLine.startsWith("*"))
            {
                tidiedLine = tidiedLine.substring(1).strip();
            }

            /*
             * The block tags (@param, @return and friends) are not part of the description, and neither is
             * the boilerplate list of the fields in the enum that most message sets carry.
             */
            if ((tidiedLine.startsWith("@")) || (tidiedLine.startsWith("<ul")) ||
                (tidiedLine.matches("(?i)the \\d+ fields? in the enum (are|is).*")))
            {
                break;
            }

            description.append(tidiedLine).append(" ");
        }

        String result = description.toString()
                                   .replaceAll("<[^>]*>", " ")
                                   .replaceAll("\\{@\\w+\\s+([^}]*)}", "$1")
                                   .replaceAll("\\s+", " ")
                                   .strip();

        return result.isEmpty() ? null : result;
    }
}
