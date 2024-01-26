/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports;



import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * EgeriaReport provides utilities to allow a report to print to the screen and create a markdown file at the same time.
 */
public class EgeriaReport
{
    private final FileOutputStream fileOutStream;


    /**
     * Work out how many spaces to indent a line in the report.  This is used in the stdout report.
     *
     * @param indentLevel required indentation
     * @return string of blanks representing the indentation
     */
    public String getSpaceIndent(int indentLevel)
    {
        StringBuilder indent = new StringBuilder();

        for (int i=0; i<indentLevel; i++)
        {
            indent.append("   ");
        }

        return indent.toString();
    }


    /**
     * Set up the heading hash characters that represent the heading level of the markdown document.
     *
     * @param indentLevel required heading level
     * @return string to prepend to the heading text
     */
    public String getHeadingLevel(int indentLevel)
    {
        if (indentLevel > 5)
        {
            /*
             * Indent level is more than mark down supports so need to be creative.
             */
            StringBuilder indent = new StringBuilder("###### ");

            for (int i = 5; i < indentLevel; i++)
            {
                indent.append("==");
            }

            return indent.append("> ").toString();
        }
        else
        {
            StringBuilder indent = new StringBuilder("#");

            for (int i = 0; i < indentLevel; i++)
            {
                indent.append("#");
            }

            return indent.append(" ").toString();
        }
    }


    /**
     * Prints out the text for a subheading the report.
     *
     * @param indentLevel number of spaces to indent
     * @param reportTitle location of the platform
     * @throws IOException problem writing file
     */
    public void printReportTitle(int    indentLevel,
                                 String reportTitle) throws IOException
    {
        System.out.println(getSpaceIndent(indentLevel) + reportTitle);

        String reportString = getHeadingLevel(indentLevel) + reportTitle + "\n\n";

        fileOutStream.write(reportString.getBytes());
    }


    /**
     * Prints out the text for a subheading the report.
     *
     * @param indentLevel number of spaces to indent
     * @param titleText text to be included in the subheading
     * @throws IOException problem writing file
     */
    public void printReportSubheading(int    indentLevel,
                                      String titleText) throws IOException
    {
        System.out.println(getSpaceIndent(indentLevel) + titleText);

        String reportString = getHeadingLevel(indentLevel) + titleText + "\n";

        fileOutStream.write(reportString.getBytes());
    }


    /**
     * Prints out the text for a single line of the report.
     *
     * @param indentLevel number of spaces to indent
     * @param elementLabel label of the element
     * @param elementText value of the element
     * @throws IOException problem writing file
     */
    public void printReportLine(int    indentLevel,
                                String elementLabel,
                                String elementText) throws IOException
    {
        if (elementText == null)
        {
            System.out.println(getSpaceIndent(indentLevel) + elementLabel + ": " + "<null>");

            String reportString = "* **" + elementLabel + "**: " + "*null*" + "\n";

            fileOutStream.write(reportString.getBytes());
        }
        else
        {
            System.out.println(getSpaceIndent(indentLevel) + elementLabel + ": " + elementText);

            String reportString = "* **" + elementLabel + "**: " + elementText + "\n";

            fileOutStream.write(reportString.getBytes());
        }
    }


    /**
     * Prints out the text for a single line of the report.
     *
     * @param indentLevel number of spaces to indent
     * @param reportText value to print
     * @throws IOException problem writing file
     */
    public void printReportLine(int    indentLevel,
                                String reportText) throws IOException
    {
        System.out.println(getSpaceIndent(indentLevel) + reportText);

        fileOutStream.write(reportText.getBytes());
        fileOutStream.write("\n".getBytes());
    }


    /**
     * Prints out information about a metadata element as a single line in a table.
     *
     * @param indentLevel number of spaces to indent
     * @param firstElement is the the first element (so column headings needed)
     * @param guid unique identifier
     * @param qualifiedName unique name
     * @param displayName display name
     * @param description description
     * @throws IOException problem writing report
     */
    public void printElementInTable(int     indentLevel,
                                    boolean firstElement,
                                    String  guid,
                                    String  qualifiedName,
                                    String  displayName,
                                    String  description) throws IOException
    {
        if (firstElement)
        {
            printReportLine(indentLevel, "");
            printReportLine(indentLevel, "|----------------------------------+-----------------------------+--------------------+------------------------------|");
            printReportLine(indentLevel, "| Unique identifier (GUID)         | Unique name (qualifiedName) | Display name       | Description                  |");
            printReportLine(indentLevel, "|----------------------------------+-----------------------------+--------------------+------------------------------|");
        }

        printReportLine(indentLevel, "| " + guid);
        printReportLine(indentLevel, " | " + qualifiedName);
        printReportLine(indentLevel, " | " + displayName);
        printReportLine(indentLevel, " | " + description);
        printReportLine(indentLevel, " |");
    }


    /**
     * Prints out information about a metadata element as a single line in a table.
     *
     * @param indentLevel number of spaces to indent
     * @param tableHeadings list of table headings - only needed if they are to be printed
     * @param tableRow list of table values
     * @throws IOException problem writing report
     */
    public void printElementInTable(int          indentLevel,
                                    List<String> tableHeadings,
                                    List<String> tableRow) throws IOException
    {
        if (tableHeadings != null)
        {
            String separatorChar = "|";
            StringBuilder dottedLine  = new StringBuilder();
            StringBuilder headingLine = new StringBuilder("\n| ");

            for (int i=0; i<tableHeadings.size(); i++)
            {
                dottedLine.append(separatorChar);
                separatorChar = " | ";
                dottedLine.append("--------------");
                headingLine.append(tableHeadings.get(i));
                headingLine.append(" |");
            }

            dottedLine.append("|");

            printReportLine(indentLevel, headingLine.toString());
            printReportLine(indentLevel, dottedLine.toString());
        }

        StringBuilder tableRowLine  = new StringBuilder("|");

        for (int i=0; i<tableRow.size(); i++)
        {
            tableRowLine.append(tableRow.get(i));
            tableRowLine.append("|");
        }

        printReportLine(indentLevel, tableRowLine.toString());
    }


    /**
     * Print out details of a connection.
     *
     * @param detailIndentLevel how many spaces to include
     * @param elementLabel name of the connector
     * @param connection connector's connection
     * @throws IOException problem writing report
     */
    public  void printConnection(int        detailIndentLevel,
                                 String     elementLabel,
                                 Connection connection) throws IOException
    {
        if (connection == null)
        {
            printReportLine(detailIndentLevel, elementLabel, null);
        }
        else
        {
            printReportSubheading(detailIndentLevel, elementLabel);

            if (connection.getConnectorType() == null)
            {
                printReportLine(detailIndentLevel + 1, "Implementation", null);
            }
            else
            {
                printReportLine(detailIndentLevel + 1, "Implementation", connection.getConnectorType().getConnectorProviderClassName());
            }

            if (connection.getEndpoint() == null)
            {
                printReportLine(detailIndentLevel + 1, "Location", null);
            }
            else
            {
                printReportLine(detailIndentLevel + 1, "Location", connection.getEndpoint().getAddress());
            }

            if (connection.getConfigurationProperties() != null)
            {
                printReportLine(detailIndentLevel + 1, "Configuration Properties", connection.getConfigurationProperties().toString());
            }
        }
    }


    /**
     * Print out the details of a registered service.
     *
     * @param indentLevel spaces needed in the report line
     * @param registeredOMAGServices list of registered services
     * @throws IOException problem writing the report
     */
    public  void printRegisteredServices(int                         indentLevel,
                                         List<RegisteredOMAGService> registeredOMAGServices) throws IOException
    {
        if (registeredOMAGServices != null)
        {
            for (RegisteredOMAGService serviceDescription : registeredOMAGServices)
            {
                printReportLine(indentLevel, serviceDescription.getServiceName(), serviceDescription.getServiceDescription());
            }
        }
    }


    /**
     * Set up the parameters for the sample.
     *
     * @param reportFileName name of file to wrote markdown content to
     * @throws IOException problem writing file
     */
    public EgeriaReport(String reportFileName) throws IOException
    {
        final String licenseString   = "<!-- SPDX-License-Identifier: CC-BY-4.0 -->\n";
        final String copyrightString = "<!-- Copyright Contributors to the Egeria project. -->\n\n";

        File reportFile = new File(reportFileName);

        if (reportFile.exists())
        {
            if (! reportFile.delete())
            {
                System.out.println("Unable to delete report file: " + reportFileName);
            }
        }

        fileOutStream = new FileOutputStream(reportFile);

        fileOutStream.write(licenseString.getBytes());
        fileOutStream.write(copyrightString.getBytes());
    }


    /**
     * This adds the last line to the report.
     *
     * @throws IOException unable to write the report
     */
    public void closeReport() throws IOException
    {
        final String snippetString   = "\n--8<-- \"snippets/abbr.md\"";

        fileOutStream.write(snippetString.getBytes());
    }
}
