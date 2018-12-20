/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance;

import org.apache.log4j.varia.NullAppender;

/**
 * OpenMetadataTestReport provides a human consumable report from the conformance test in text or markdown format.
 */
public class OpenMetadataTestReport
{


    /**
     * Main entry point reviews the arguments and initiates the process to create the report.
     * It is basically a 3 step process:
     * 1) Find the report and load it into Java object in memory.
     * 2) Select the output style.
     * 3) Create the report.
     *
     * @param args [results-filename] [format] [-r report-filename]
     *
     *             the default results filename is "openmetadata.functional.testlab.results"
     *             format is either -t for text or -m for markdown - default is -m
     *             the default report filename is stdout
     */
    public static void main(String[] args)
    {
        String  serverURLRoot;
        String  resultsFileName = "openmetadata.functional.testlab.results";

        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        if ((args == null) || (args.length == 0))
        {
            System.out.println("Please specify the server's URL root in the first parameter");
            System.exit(-1);
        }

        serverURLRoot = args[0];
    }
}
