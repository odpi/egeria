/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.surveyreport;


import org.odpi.openmetadata.accessservices.assetowner.client.AssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ArrayTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MapTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;
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
    private final String serverName;
    private final String platformURLRoot;
    private final String clientUserId;
    private final String assetGUID;
    private final String reportFileName;

    private final Date reportDate = new Date();

    /**
     * Set up the parameters for the sample.
     *
     * @param serverName      server to call
     * @param platformURLRoot location of server
     * @param clientUserId    userId to access the server
     */
    private SurveyReport(String serverName,
                         String platformURLRoot,
                         String clientUserId,
                         String assetGUID)
    {
        this.serverName      = serverName;
        this.platformURLRoot = platformURLRoot;
        this.clientUserId    = clientUserId;
        this.assetGUID       = assetGUID;

        this.reportFileName = "survey-report-for-asset-" + assetGUID + "-" + reportDate + ".md";
    }


    /**
     * Converts "Sun Jan 28 16:50:14 GMT 2024* to "#survey-report-Sun-Jan-28-165014-GMT-2024".
     *
     * @param creationTime creation time of report
     * @return link to the section describing the report
     */
    private String getReportLinkURI(Date creationTime)
    {
        String noSpaces = creationTime.toString().replace(' ', 'x');
        String noColons = noSpaces.replaceAll("[^a-zA-Z0-9]", "");

        return "#survey-report-" + noColons.replace('x', '-');
    }


    /**
     * Return the title to use on an annotation section of the report.
     *
     * @param annotationElement element describing the annotation
     * @return title string
     */
    public String getAnnotationTitle(OpenMetadataElement annotationElement)
    {
        String annotationTitle = annotationElement.getType().getTypeName() + ": GUID " + annotationElement.getElementGUID();

        if (annotationElement.getElementProperties() != null)
        {
            Map<String, String> annotationProperties = annotationElement.getElementProperties().getPropertiesAsStrings();

            if (annotationProperties.get(OpenMetadataProperty.ANNOTATION_TYPE.name) != null)
            {
                annotationTitle = annotationElement.getType().getTypeName() + ": " + annotationProperties.get(OpenMetadataProperty.ANNOTATION_TYPE.name);
            }
        }

        return annotationTitle;
    }


    /**
     * Return the URL to use to link to an annotation section of the report.
     *
     * @param annotationElement element describing the annotation
     * @return title string
     */
    public String getAnnotationURL(OpenMetadataElement annotationElement)
    {
        String annotationURL = "#" + annotationElement.getType().getTypeName() + "-GUID-" + annotationElement.getElementGUID();

        if (annotationElement.getElementProperties() != null)
        {
            Map<String, String> annotationProperties = annotationElement.getElementProperties().getPropertiesAsStrings();

            if (annotationProperties.get(OpenMetadataProperty.ANNOTATION_TYPE.name) != null)
            {
                String annotationType = annotationProperties.get(OpenMetadataProperty.ANNOTATION_TYPE.name);
                annotationURL = "#" + annotationElement.getType().getTypeName() + "-" + annotationType.replace(' ', '-');
            }
        }

        return annotationURL;
    }


    /**
     * Print out the contents of a single survey report.
     *
     * @param reportIndentLevel how much to indent the contents
     * @param openMetadataStoreClient access to open metadata
     * @param surveyReportLinkElement description of the survey report
     * @param outputReport destination to write to
     * @throws IOException problem writing report
     * @throws InvalidParameterException problem accessing open metadata
     * @throws PropertyServerException repository problem accessing open metadata
     * @throws UserNotAuthorizedException security problem accessing open metadata
     */
    private void printReport(int                     reportIndentLevel,
                             OpenMetadataStoreClient openMetadataStoreClient,
                             RelatedMetadataElement  surveyReportLinkElement,
                             EgeriaReport            outputReport) throws IOException,
                                                                          InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        int annotationIndentLevel = reportIndentLevel + 1;

        OpenMetadataElement surveyReportElement = surveyReportLinkElement.getElement();

        outputReport.printReportSubheading(reportIndentLevel, "Survey report: " + surveyReportElement.getVersions().getCreateTime());

        /*
         * Print out the properties of the report
         */
        if (surveyReportElement.getElementProperties() != null)
        {
            Map<String, String> surveyReportProperties = surveyReportElement.getElementProperties().getPropertiesAsStrings();

            if (surveyReportProperties != null)
            {
                for (String propertyName : surveyReportProperties.keySet())
                {
                    outputReport.printReportLine(reportIndentLevel, propertyName, surveyReportProperties.get(propertyName));
                }
            }
        }

        outputReport.printReportLine(reportIndentLevel, "\n**Annotations**");

        /*
         * Create a table of contents for the annotations
         */


        int startFrom   = 0;
        int maxPageSize = 100;

        RelatedMetadataElementList annotationElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                             surveyReportLinkElement.getElement().getElementGUID(),
                                                                                                             1,
                                                                                                             OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                             null,
                                                                                                             null,
                                                                                                             null,
                                                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                                                             false,
                                                                                                             false,
                                                                                                             reportDate,
                                                                                                             startFrom,
                                                                                                             maxPageSize);

        if ((annotationElements != null) && (annotationElements.getElementList() != null))
        {
            while ((annotationElements != null) && (annotationElements.getElementList() != null))
            {
                for (RelatedMetadataElement reportedAnnotationLink : annotationElements.getElementList())
                {
                    if ((reportedAnnotationLink != null) && (reportedAnnotationLink.getElement() != null))
                    {
                        OpenMetadataElement reportedAnnotationElement = reportedAnnotationLink.getElement();

                        outputReport.printReportLine(reportIndentLevel, "* [" + this.getAnnotationTitle(reportedAnnotationElement) + "](" + this.getAnnotationURL(reportedAnnotationElement) +  ")");
                    }
                }

                startFrom = startFrom + maxPageSize;

                annotationElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                        surveyReportLinkElement.getElement().getElementGUID(),
                                                                                        1,
                                                                                        OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                                                        false,
                                                                                        false,
                                                                                        reportDate,
                                                                                        startFrom,
                                                                                        maxPageSize);
            }
        }
        else
        {
            outputReport.printReportLine(annotationIndentLevel, "\nNone.");
        }


        /*
         * Print out the contents of each annotation
         */
        startFrom = 0;

        annotationElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                surveyReportLinkElement.getElement().getElementGUID(),
                                                                                1,
                                                                                OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                false,
                                                                                false,
                                                                                reportDate,
                                                                                startFrom,
                                                                                maxPageSize);

        while ((annotationElements != null) && (annotationElements.getElementList() != null))
        {
            for (RelatedMetadataElement reportedAnnotationElement : annotationElements.getElementList())
            {
                if ((reportedAnnotationElement != null) && (reportedAnnotationElement.getElement() != null))
                {
                    this.printAnnotation(annotationIndentLevel,
                                         openMetadataStoreClient,
                                         reportedAnnotationElement,
                                         outputReport);

                }
            }

            startFrom = startFrom + maxPageSize;

            annotationElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                    surveyReportLinkElement.getElement().getElementGUID(),
                                                                                    1,
                                                                                    OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    false,
                                                                                    false,
                                                                                    reportDate,
                                                                                    startFrom,
                                                                                    maxPageSize);
        }
    }


    /**
     * Print out the contents of a map property in an annotation report.
     *
     * @param indentLevel how much to indent the contents
     * @param elementProperties description of the annotation
     * @param outputReport destination to write to
     * @throws IOException problem writing report
     */
    private void printMapProperty(int               indentLevel,
                                  String            propertyName,
                                  ElementProperties elementProperties,
                                  EgeriaReport      outputReport) throws IOException
    {
        int annotationPropertyIndentLevel = indentLevel + 1;

        if (elementProperties != null)
        {
            if (elementProperties.getPropertyValue(propertyName) != null)
            {
                List<String> tableHeadings = new ArrayList<>(Arrays.asList("Name", "Value"));

                outputReport.printReportLine(indentLevel, "* **" + propertyName + "**:");

                PropertyValue propertyValue = elementProperties.getPropertyValue(propertyName);

                if (propertyValue instanceof MapTypePropertyValue mapTypePropertyValue)
                {
                    if (mapTypePropertyValue.getMapValues() != null)
                    {
                        Map<String, String> mapValueProperties = mapTypePropertyValue.getMapValues().getPropertiesAsStrings();

                        for (String mapPropertyName : mapValueProperties.keySet())
                        {
                            outputReport.printElementInTable(annotationPropertyIndentLevel,
                                                             tableHeadings,
                                                             new ArrayList<>(Arrays.asList(mapPropertyName,
                                                                                           mapValueProperties.get(mapPropertyName))));
                            tableHeadings = null;
                        }
                    }
                }
            }
        }
    }


    /**
     * Print out the contents of a map property in an annotation report.
     *
     * @param indentLevel how much to indent the contents
     * @param elementProperties description of the annotation
     * @param outputReport destination to write to
     * @throws IOException problem writing report
     */
    private void printArrayProperty(int               indentLevel,
                                    String            propertyName,
                                    ElementProperties elementProperties,
                                    EgeriaReport      outputReport) throws IOException
    {
        int annotationPropertyIndentLevel = indentLevel + 1;

        if (elementProperties != null)
        {
            if (elementProperties.getPropertyValue(propertyName) != null)
            {
                List<String> tableHeadings = new ArrayList<>(List.of("Values"));

                outputReport.printReportLine(indentLevel, "* **" + propertyName + "**:");

                PropertyValue propertyValue = elementProperties.getPropertyValue(propertyName);

                if (propertyValue instanceof ArrayTypePropertyValue arrayTypePropertyValue)
                {
                    if (arrayTypePropertyValue.getArrayValues() != null)
                    {
                        Map<String, String> propertiesAsStrings = arrayTypePropertyValue.getArrayValues().getPropertiesAsStrings();

                        for (String mapPropertyName : propertiesAsStrings.keySet())
                        {
                            outputReport.printElementInTable(annotationPropertyIndentLevel,
                                                             tableHeadings,
                                                             new ArrayList<>(Collections.singletonList(propertiesAsStrings.get(mapPropertyName))));
                            tableHeadings = null;
                        }
                    }
                }
            }
        }
    }


    /**
     * Print out the contents of a single annotation.
     *
     * @param annotationIndentLevel how much to indent the contents
     * @param openMetadataStoreClient access to open metadata
     * @param reportedAnnotationElement description of the survey report
     * @param outputReport destination to write to
     * @throws IOException problem writing report
     * @throws InvalidParameterException problem accessing open metadata
     * @throws PropertyServerException repository problem accessing open metadata
     * @throws UserNotAuthorizedException security problem accessing open metadata
     */
    private void printAnnotation(int                     annotationIndentLevel,
                                 OpenMetadataStoreClient openMetadataStoreClient,
                                 RelatedMetadataElement  reportedAnnotationElement,
                                 EgeriaReport            outputReport) throws IOException,
                                                                              InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        int annotationPropertyIndentLevel = annotationIndentLevel + 1;

        int startFrom   = 0;
        int maxPageSize = 100;

        List<String> tableHeadings = new ArrayList<>(Arrays.asList("Property Name", "Property Value"));

        OpenMetadataElement annotationElement = reportedAnnotationElement.getElement();

        outputReport.printReportSubheading(annotationIndentLevel, this.getAnnotationTitle(annotationElement));
        outputReport.printReportLine(annotationIndentLevel, "Creation Time", annotationElement.getVersions().getCreateTime().toString());

        if (annotationElement.getElementProperties() != null)
        {
            List<String> processedProperties = new ArrayList<>();
            Map<String, String> annotationProperties = annotationElement.getElementProperties().getPropertiesAsStrings();

            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.ANNOTATION_TYPE.name, annotationProperties.get(OpenMetadataProperty.ANNOTATION_TYPE.name));
            processedProperties.add(OpenMetadataProperty.ANNOTATION_TYPE.name);
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.SUMMARY.name, annotationProperties.get(OpenMetadataProperty.SUMMARY.name));
            processedProperties.add(OpenMetadataProperty.SUMMARY.name);
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.ANNOTATION_STATUS.name, annotationProperties.get(OpenMetadataProperty.ANNOTATION_STATUS.name));
            processedProperties.add(OpenMetadataProperty.ANNOTATION_STATUS.name);
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.CONFIDENCE_LEVEL.name, annotationProperties.get(OpenMetadataProperty.CONFIDENCE_LEVEL.name));
            processedProperties.add(OpenMetadataProperty.CONFIDENCE_LEVEL.name);
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.EXPRESSION.name, annotationProperties.get(OpenMetadataProperty.EXPRESSION.name));
            processedProperties.add(OpenMetadataProperty.EXPRESSION.name);
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.EXPLANATION.name, annotationProperties.get(OpenMetadataProperty.EXPLANATION.name));
            processedProperties.add(OpenMetadataProperty.EXPLANATION.name);
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.ANALYSIS_STEP.name, annotationProperties.get(OpenMetadataProperty.ANALYSIS_STEP.name));
            processedProperties.add(OpenMetadataProperty.ANALYSIS_STEP.name);
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.JSON_PROPERTIES.name, annotationProperties.get(OpenMetadataProperty.JSON_PROPERTIES.name));
            processedProperties.add(OpenMetadataProperty.JSON_PROPERTIES.name);

            this.printMapProperty(annotationIndentLevel,
                                  OpenMetadataProperty.VALUE_COUNT.name,
                                  annotationElement.getElementProperties(),
                                  outputReport);
            processedProperties.add(OpenMetadataProperty.VALUE_COUNT.name);

            this.printArrayProperty(annotationIndentLevel,
                                   OpenMetadataProperty.VALUE_LIST.name,
                                   annotationElement.getElementProperties(),
                                   outputReport);
            processedProperties.add(OpenMetadataProperty.VALUE_COUNT.name);

            this.printMapProperty(annotationIndentLevel,
                                  OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                  annotationElement.getElementProperties(),
                                  outputReport);
            processedProperties.add(OpenMetadataProperty.PROFILE_PROPERTIES.name);

            this.printMapProperty(annotationIndentLevel,
                                  OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                  annotationElement.getElementProperties(),
                                  outputReport);
            processedProperties.add(OpenMetadataProperty.ADDITIONAL_PROPERTIES.name);

            outputReport.printReportLine(annotationIndentLevel, "* **Other Properties**:");

            for (String propertyName : annotationProperties.keySet())
            {
                if (! processedProperties.contains(propertyName))
                {
                    outputReport.printElementInTable(annotationPropertyIndentLevel,
                                                     tableHeadings,
                                                     new ArrayList<>(Arrays.asList(propertyName,
                                                                                   annotationProperties.get(propertyName))));
                    tableHeadings = null;
                }
            }
        }

        RelatedMetadataElementList associatedElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                             reportedAnnotationElement.getElement().getElementGUID(),
                                                                                                             2,
                                                                                                             OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                             null,
                                                                                                             null,
                                                                                                             null,
                                                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                                                             false,
                                                                                                             false,
                                                                                                             reportDate,
                                                                                                             startFrom,
                                                                                                             maxPageSize);

        while ((associatedElements != null) && (associatedElements.getElementList() != null))
        {
            int associatedElementIndentLevel = annotationIndentLevel + 1;

            for (RelatedMetadataElement associatedElement : associatedElements.getElementList())
            {
                if (associatedElement != null)
                {
                    outputReport.printReportLine(associatedElementIndentLevel, "Associated " + associatedElement.getElement().getType().getTypeName() + " Element: " + associatedElement.getElement().getElementGUID());

                    if (associatedElement.getElement().getElementProperties() != null)
                    {
                        Map<String, String> associatedElementProperties = associatedElement.getElement().getElementProperties().getPropertiesAsStrings();

                        for (String propertyName : associatedElementProperties.keySet())
                        {
                            outputReport.printReportLine(associatedElementIndentLevel + 1, propertyName, associatedElementProperties.get(propertyName));
                        }
                    }
                }
            }

            startFrom = startFrom + maxPageSize;

            associatedElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                    reportedAnnotationElement.getElement().getElementGUID(),
                                                                                    2,
                                                                                    OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    false,
                                                                                    false,
                                                                                    reportDate,
                                                                                    startFrom,
                                                                                    maxPageSize);
        }
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
            OpenMetadataStoreClient openMetadataStoreClient = new OpenMetadataStoreClient(serverName, platformURLRoot, 100);
            AssetOwner              assetOwnerClient        = new AssetOwner(serverName, platformURLRoot);

            /*
             * Check that the asset is valid
             */
            AssetElement asset = assetOwnerClient.getAssetSummary(clientUserId, assetGUID);

            if (asset != null)
            {
                EgeriaReport outputReport = new EgeriaReport(reportFileName);

                outputReport.printReportTitle(indentLevel, "Asset survey report");
                outputReport.printReportLine(indentLevel, "Date: " + reportDate);

                int detailIndentLevel = indentLevel + 1;

                outputReport.printReportSubheading(detailIndentLevel, "Asset summary");

                outputReport.printReportLine(detailIndentLevel, "Unique identifier", assetGUID);
                outputReport.printReportLine(detailIndentLevel, "Type", asset.getElementHeader().getType().getTypeName());
                if ((asset.getProperties().getExtendedProperties() != null) && (asset.getProperties().getExtendedProperties().get(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name) != null))
                {
                    outputReport.printReportLine(detailIndentLevel, "Deployed Implementation Type",
                                                 asset.getProperties().getExtendedProperties().get(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name).toString());
                }
                outputReport.printReportLine(detailIndentLevel, "Qualified Name", asset.getProperties().getQualifiedName());
                outputReport.printReportLine(detailIndentLevel, "Display Name", asset.getProperties().getDisplayName());
                outputReport.printReportLine(detailIndentLevel, "Description", asset.getProperties().getDescription());

                outputReport.printReportSubheading(detailIndentLevel, "Survey report summaries");

                RelatedMetadataElementList surveyReportElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                                     assetGUID,
                                                                                                                     1,
                                                                                                                     OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName,
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     null,
                                                                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                     false,
                                                                                                                     false,
                                                                                                                     reportDate,
                                                                                                                     0,
                                                                                                                     0);

                if ((surveyReportElements != null) && (surveyReportElements.getElementList() != null))
                {
                    int reportIndentLevel = detailIndentLevel + 1;

                    /*
                     * First loop creates table of contents
                     */
                    for (RelatedMetadataElement surveyReportElement : surveyReportElements.getElementList())
                    {
                        if (surveyReportElement != null)
                        {
                            outputReport.printReportLine(reportIndentLevel, "* [" + surveyReportElement.getVersions().getCreateTime() + "](" + getReportLinkURI(surveyReportElement.getVersions().getCreateTime()) + ")");
                        }
                    }

                    /*
                     * Now print out the contents of each report
                     */
                    for (RelatedMetadataElement surveyReportElement : surveyReportElements.getElementList())
                    {
                        if (surveyReportElement != null)
                        {
                            this.printReport(reportIndentLevel,
                                             openMetadataStoreClient,
                                             surveyReportElement,
                                             outputReport);
                        }
                    }
                }

                outputReport.closeReport();
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
        String serverName      = "active-metadata-store";
        String platformURLRoot = "https://localhost:9443";
        String clientUserId    = "erinoverview";
        String assetGUID       = null;

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
        catch (Exception error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
