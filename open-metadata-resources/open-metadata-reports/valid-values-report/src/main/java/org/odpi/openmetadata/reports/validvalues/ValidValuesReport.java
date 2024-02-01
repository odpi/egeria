/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.validvalues;


import org.odpi.openmetadata.accessservices.digitalarchitecture.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.ReferenceDataManager;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ValidValueElement;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.reports.EgeriaReport;

import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * ValidValuesReport extracts the valid values defined in the open metadata ecosystem.
 * It connects to a Metadata Access Server running the Digital Architecture OMAS.
 */
public class ValidValuesReport
{
    private final String       serverName;
    private final String       platformURLRoot;
    private final String       clientUserId;
    private final EgeriaReport report;

    /**
     * Set up the parameters for the sample.
     *
     * @param serverName server to call
     * @param platformURLRoot location of server
     * @param clientUserId userId to access the server
     * @throws IOException problem writing file
     */
    private ValidValuesReport(String serverName,
                              String platformURLRoot,
                              String clientUserId) throws IOException
    {
        final String reportFileName  = "valid-values-report.md";

        this.serverName = serverName;
        this.platformURLRoot = platformURLRoot;
        this.clientUserId = clientUserId;

        report = new EgeriaReport(reportFileName);
    }


    /**
     * This runs the report.
     */
    private void run()
    {
        int indentLevel = 0;

        try
        {
            /*
             * These clients are from the Digital Architecture OMAS.
             */
            ReferenceDataManager    referenceDataManager = new ReferenceDataManager(serverName, platformURLRoot);
            OpenMetadataStoreClient openMetadataStoreClient = new OpenMetadataStoreClient(serverName, platformURLRoot);

            /*
             * This outputs the report title
             */
            final String reportTitle = "Valid Values report for: ";

            report.printReportTitle(indentLevel, reportTitle + serverName + " on " + platformURLRoot);

            int detailIndentLevel = indentLevel + 1;

            report.printReportSubheading(detailIndentLevel, "Valid Metadata Values");

            int startFrom = 0;
            int maxPageSize = 100;

            List<ValidValueElement> validValueElements = referenceDataManager.findValidValues(clientUserId, ".*", startFrom, maxPageSize);

            while (validValueElements != null)
            {
                for (ValidValueElement validValueElement : validValueElements)
                {
                    if (validValueElement != null)
                    {
                        /*
                         * It is possible to decide if the valid value is for metadata because the qualified name begins "Egeria:ValidMetadataValue:"
                         */
                        if (validValueElement.getValidValueProperties().getQualifiedName().startsWith(OpenMetadataValidValues.VALID_METADATA_VALUES_QUALIFIED_NAME_PREFIX))
                        {
                            /*
                             * Valid metadata value.
                             */

                        }
                        else
                        {
                            /*
                             * Valid value for data.
                             */
                        }
                    }
                }

                startFrom = startFrom + maxPageSize;

                validValueElements = referenceDataManager.findValidValues(clientUserId, ".*", startFrom, maxPageSize);
            }



            report.closeReport();
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * Main program that controls the operation of the valid values report.  The parameters are passed space separated.
     * They are used to override the report's default values.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. server name,
     */
    public static void main(String[] args)
    {
        String  serverName = "simple-metadata-store";
        String  platformURLRoot = "https://localhost:9443";
        String  clientUserId = "erinoverview";

        if (args.length > 0)
        {
            platformURLRoot = args[0];
        }

        if (args.length > 1)
        {
            clientUserId = args[1];
        }

        if (args.length > 2)
        {
            serverName = args[2];
        }

        System.out.println("===============================");
        System.out.println("Valid Values Report:    " + new Date());
        System.out.println("===============================");
        System.out.println("Running against platform: " + platformURLRoot);
        System.out.println("Focused on server: " + serverName);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        HttpHelper.noStrictSSL();

        try
        {
            ValidValuesReport report = new ValidValuesReport(serverName, platformURLRoot, clientUserId);

            report.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
