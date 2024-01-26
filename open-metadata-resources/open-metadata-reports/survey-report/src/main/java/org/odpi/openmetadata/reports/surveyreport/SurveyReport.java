/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.surveyreport;


import org.odpi.openmetadata.accessservices.assetowner.client.AssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.AssetElement;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.reports.EgeriaReport;

import java.io.IOException;
import java.util.*;


/**
 * SurveyReport extracts the survey reports for an asset defined in the open metadata ecosystem.
 * It connects to a Metadata Access Server running the Asset Owner OMAS.
 */
public class SurveyReport
{
    private final String       serverName;
    private final String       platformURLRoot;
    private final String       clientUserId;
    private final String       assetGUID;
    private final String       reportFileName;

    private final Date         reportDate = new Date();

    /**
     * Set up the parameters for the sample.
     *
     * @param serverName server to call
     * @param platformURLRoot location of server
     * @param clientUserId userId to access the server
     */
    private SurveyReport(String serverName,
                         String platformURLRoot,
                         String clientUserId,
                         String assetGUID)
    {
        this.serverName = serverName;
        this.platformURLRoot = platformURLRoot;
        this.clientUserId = clientUserId;
        this.assetGUID = assetGUID;

        this.reportFileName  = "survey-report-for-asset-" + assetGUID + "-" + reportDate + ".md";
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
             * The clients are from the Asset Owner OMAS.
             */
            OpenMetadataStoreClient openMetadataStoreClient = new OpenMetadataStoreClient(serverName, platformURLRoot);
            AssetOwner              assetOwnerClient        = new AssetOwner(serverName, platformURLRoot);

            /*
             * Check that the asset is valid
             */
            AssetElement asset = assetOwnerClient.getAssetSummary(clientUserId, assetGUID);

            if (asset != null)
            {
                EgeriaReport report = new EgeriaReport(reportFileName);

                /*
                 * This outputs the report title
                 */
                final String reportTitle = "Survey report for asset: ";

                report.printReportTitle(indentLevel, reportTitle + assetGUID + " on " + reportDate);

                int detailIndentLevel = indentLevel + 1;

                report.printReportSubheading(detailIndentLevel, "Asset summary");

                report.printReportLine(detailIndentLevel, "Unique identifier", assetGUID);
                report.printReportLine(detailIndentLevel, "Type", asset.getElementHeader().getType().getTypeName());
                report.printReportLine(detailIndentLevel, "Qualified Name", asset.getAssetProperties().getQualifiedName());
                report.printReportLine(detailIndentLevel, "Display Name", asset.getAssetProperties().getName());
                report.printReportLine(detailIndentLevel, "Description", asset.getAssetProperties().getDescription());

                report.printReportSubheading(detailIndentLevel, "Survey report summaries");

                List<RelatedMetadataElement> surveyReportElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                                       assetGUID,
                                                                                                                       1,
                                                                                                                       OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName,
                                                                                                                       false,
                                                                                                                       false,
                                                                                                                       reportDate,
                                                                                                                       0,
                                                                                                                       0);

                if (surveyReportElements != null)
                {
                    int reportIndentLevel = detailIndentLevel + 1;
                    int annotationIndentLevel = reportIndentLevel + 1;
                    int annotationPropertyIndentLevel = annotationIndentLevel + 1;

                    for (RelatedMetadataElement surveyReportElement : surveyReportElements)
                    {
                        if (surveyReportElement != null)
                        {
                            report.printReportSubheading(reportIndentLevel, "Survey report: " + surveyReportElement.getVersions().getCreateTime());

                            int startFrom   = 0;
                            int maxPageSize = 100;

                            List<RelatedMetadataElement> annotationElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                                                 surveyReportElement.getElement().getElementGUID(),
                                                                                                                                 1,
                                                                                                                                 OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                                                 false,
                                                                                                                                 false,
                                                                                                                                 reportDate,
                                                                                                                                 startFrom,
                                                                                                                                 maxPageSize);

                            while (annotationElements != null)
                            {
                                for (RelatedMetadataElement reportedAnnotationElement : annotationElements)
                                {
                                    if ((reportedAnnotationElement != null) && (reportedAnnotationElement.getElement() != null))
                                    {
                                        List<String> tableHeadings = new ArrayList<>(Arrays.asList("Property Name", "Property Value"));

                                        OpenMetadataElement annotationElement = reportedAnnotationElement.getElement();

                                        report.printReportSubheading(annotationIndentLevel, annotationElement.getType().getTypeName() + " GUID: " + annotationElement.getElementGUID() + " at " + annotationElement.getVersions().getCreateTime().toString());

                                        Map<String, String> annotationProperties = annotationElement.getElementProperties().getPropertiesAsStrings();

                                        for (String propertyName : annotationProperties.keySet())
                                        {
                                            report.printElementInTable(annotationPropertyIndentLevel,
                                                                       tableHeadings,
                                                                       new ArrayList<>(Arrays.asList(propertyName,
                                                                                                     annotationProperties.get(propertyName))));
                                            tableHeadings = null;
                                        }

                                        List<RelatedMetadataElement> associatedElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                                                             reportedAnnotationElement.getElement().getElementGUID(),
                                                                                                                                             2,
                                                                                                                                             OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                                                             false,
                                                                                                                                             false,
                                                                                                                                             reportDate,
                                                                                                                                             startFrom,
                                                                                                                                             maxPageSize);

                                        if (associatedElements != null)
                                        {
                                            int associatedElementIndentLevel = annotationIndentLevel + 1;

                                            for (RelatedMetadataElement associatedElement : associatedElements)
                                            {
                                                if (associatedElement != null)
                                                {
                                                    report.printReportLine(associatedElementIndentLevel, "Associated " + associatedElement.getElement().getType().getTypeName() + " Element: " + associatedElement.getElement().getElementGUID());

                                                    if (associatedElement.getElement().getElementProperties() != null)
                                                    {
                                                        Map<String, String> associatedElementProperties = associatedElement.getElement().getElementProperties().getPropertiesAsStrings();

                                                        for (String propertyName : associatedElementProperties.keySet())
                                                        {
                                                            report.printReportLine(associatedElementIndentLevel + 1, propertyName, associatedElementProperties.get(propertyName));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                startFrom = startFrom + maxPageSize;

                                annotationElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                        surveyReportElement.getElement().getElementGUID(),
                                                                                                        1,
                                                                                                        OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                        false,
                                                                                                        false,
                                                                                                        reportDate,
                                                                                                        startFrom,
                                                                                                        maxPageSize);
                            }
                        }
                    }
                }

                report.closeReport();
            }
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
        String  serverName = "active-metadata-store";
        String  platformURLRoot = "https://localhost:9443";
        String  clientUserId = "erinoverview";
        String  assetGUID = null;

        if (args.length > 0)
        {
            assetGUID = args[0];
        }
        else
        {
            System.out.println("Please provide the unique identifier (guid) in the first parameter.  Other parameters are optional.\n");
            System.out.println("    SurveyReport <assetGUID> <platformURLRoot> <clientUserId> <serverName>");
            System.exit(-1);
        }

        if (args.length > 1)
        {
            platformURLRoot = args[1];
        }

        if (args.length > 2)
        {
            clientUserId = args[2];
        }

        if (args.length > 3)
        {
            serverName = args[3];
        }

        System.out.println("===============================");
        System.out.println("Survey Report for asset:    " + assetGUID);
        System.out.println("===============================");
        System.out.println("Running against platform: " + platformURLRoot);
        System.out.println("Focused on server: " + serverName);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        HttpHelper.noStrictSSL();

        try
        {
            SurveyReport report = new SurveyReport(serverName, platformURLRoot, clientUserId, assetGUID);

            report.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }


    static class AnnotationSummary
    {
        private String annotationType = null;
        private Date   annotationDate = null;
    }
}
