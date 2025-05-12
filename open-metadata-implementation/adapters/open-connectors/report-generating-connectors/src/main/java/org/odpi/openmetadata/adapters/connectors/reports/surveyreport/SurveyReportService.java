/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.reports.surveyreport;


import org.odpi.openmetadata.adapters.connectors.reports.ReportRequestParameter;
import org.odpi.openmetadata.adapters.connectors.reports.ffdc.ReportsAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.controls.Guard;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.reports.EgeriaReport;

import java.io.IOException;
import java.util.*;


/**
 * SurveyReportService extracts the requested survey report and turns it into a Markdown document.
 */
public class SurveyReportService extends GeneralGovernanceActionService
{
    private final Date reportDate = new Date();

    private final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Default Constructor
     */
    public SurveyReportService()
    {
    }


    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            String reportDirectory = "surveys/survey-reports";

            if ((governanceContext.getRequestParameters() != null) &&
                    (governanceContext.getRequestParameters().get(ReportRequestParameter.REPORT_DIRECTORY.getName()) != null))
            {
                reportDirectory = governanceContext.getRequestParameters().get(ReportRequestParameter.REPORT_DIRECTORY.getName());
            }

            for (ActionTargetElement actionTargetRelationship : governanceContext.getActionTargetElements())
            {
                if ((actionTargetRelationship != null) && (actionTargetRelationship.getTargetElement() != null))
                {
                    if (propertyHelper.isTypeOf(actionTargetRelationship.getTargetElement(),
                                                OpenMetadataType.SURVEY_REPORT.typeName))
                    {
                        this.createReport(reportDirectory,
                                          actionTargetRelationship.getTargetElement());
                    }
                }
            }
        }
        catch (Exception error)
        {
            super.handleUnexpectedException(methodName, error);
        }
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
                annotationTitle = annotationProperties.get(OpenMetadataProperty.ANNOTATION_TYPE.name);
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
                annotationURL = "#" + annotationType.replace(' ', '-');
            }
        }

        return annotationURL;
    }


    /**
     * Print out the contents of a single survey report.
     *
     * @param reportIndentLevel how much to indent the contents
     * @param openMetadataStoreClient access to open metadata
     * @param surveyReportElement description of the survey report
     * @param outputReport destination to write to
     * @throws IOException problem writing report
     * @throws InvalidParameterException problem accessing open metadata
     * @throws PropertyServerException repository problem accessing open metadata
     * @throws UserNotAuthorizedException security problem accessing open metadata
     */
    private void printReport(int                     reportIndentLevel,
                             OpenMetadataStore       openMetadataStoreClient,
                             OpenMetadataElement     surveyReportElement,
                             EgeriaReport            outputReport) throws IOException,
                                                                          InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "printReport";

        int annotationIndentLevel = reportIndentLevel + 1;

        outputReport.printReportLine(reportIndentLevel, "* Report Start Time: " + surveyReportElement.getVersions().getCreateTime());
        outputReport.printReportLine(reportIndentLevel, "* Report Completion Time: " +
                propertyHelper.getDateProperty(governanceServiceName,
                                               OpenMetadataProperty.COMPLETION_DATE.name,
                                               surveyReportElement.getElementProperties(),
                                               methodName));

        outputReport.printReportLine(reportIndentLevel, "* Final Analysis Step: " +
                propertyHelper.getStringProperty(governanceServiceName,
                                                 OpenMetadataProperty.ANALYSIS_STEP.name,
                                                 surveyReportElement.getElementProperties(),
                                                 methodName));


        outputReport.printReportLine(reportIndentLevel, "\n" +
                propertyHelper.getStringProperty(governanceServiceName,
                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                 surveyReportElement.getElementProperties(),
                                                 methodName));



        outputReport.printReportSubheading(reportIndentLevel, "Annotations");

        /*
         * Create a table of contents for the annotations
         */


        int startFrom   = 0;
        int maxPageSize = 100;

        RelatedMetadataElementList annotationElements = openMetadataStoreClient.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                                           1,
                                                                                                           OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
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

                annotationElements = openMetadataStoreClient.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                        1,
                                                                                        OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
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

        annotationElements = openMetadataStoreClient.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                1,
                                                                                OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
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

            annotationElements = openMetadataStoreClient.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                    1,
                                                                                    OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
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

                outputReport.printReportLine(indentLevel, "\n**" + propertyName + "**:\n");

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
                                 OpenMetadataStore       openMetadataStoreClient,
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
            Map<String, String> annotationProperties = annotationElement.getElementProperties().getPropertiesAsStrings();

            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.SUMMARY.name, annotationProperties.get(OpenMetadataProperty.SUMMARY.name));
            if (annotationProperties.get(OpenMetadataProperty.ANNOTATION_STATUS.name) != null)
            {
                outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.ANNOTATION_STATUS.name, annotationProperties.get(OpenMetadataProperty.ANNOTATION_STATUS.name));
            }
            if (annotationProperties.get(OpenMetadataProperty.CONFIDENCE_LEVEL.name) != null)
            {
                outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.CONFIDENCE_LEVEL.name, annotationProperties.get(OpenMetadataProperty.CONFIDENCE_LEVEL.name));
            }
            if (annotationProperties.get(OpenMetadataProperty.EXPRESSION.name) != null)
            {
                outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.EXPRESSION.name, annotationProperties.get(OpenMetadataProperty.EXPRESSION.name));
            }
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.EXPLANATION.name, annotationProperties.get(OpenMetadataProperty.EXPLANATION.name));
            outputReport.printReportLine(annotationIndentLevel, OpenMetadataProperty.ANALYSIS_STEP.name, annotationProperties.get(OpenMetadataProperty.ANALYSIS_STEP.name));

            this.printMapProperty(annotationIndentLevel,
                                  OpenMetadataProperty.VALUE_COUNT.name,
                                  annotationElement.getElementProperties(),
                                  outputReport);

            this.printArrayProperty(annotationIndentLevel,
                                   OpenMetadataProperty.VALUE_LIST.name,
                                   annotationElement.getElementProperties(),
                                   outputReport);

            this.printMapProperty(annotationIndentLevel,
                                  OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                  annotationElement.getElementProperties(),
                                  outputReport);

            this.printMapProperty(annotationIndentLevel,
                                  OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                  annotationElement.getElementProperties(),
                                  outputReport);

            this.printMapProperty(annotationIndentLevel,
                                  OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                  annotationElement.getElementProperties(),
                                  outputReport);
        }

        RelatedMetadataElementList associatedElements = openMetadataStoreClient.getRelatedMetadataElements(reportedAnnotationElement.getElement().getElementGUID(),
                                                                                                           2,
                                                                                                           OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
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

            associatedElements = openMetadataStoreClient.getRelatedMetadataElements(reportedAnnotationElement.getElement().getElementGUID(),
                                                                                    2,
                                                                                    OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                    startFrom,
                                                                                    maxPageSize);
        }
    }


    /**
     * This runs the report.
     *
     * @param surveyReport report to print out
     */
    private void createReport(String              surveyReportDirectory,
                              OpenMetadataElement surveyReport) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "createReport";

        int indentLevel = 0;

        try
        {
            OpenMetadataStore openMetadataStoreClient = governanceContext.getOpenMetadataStore();

            if (surveyReport != null)
            {
                String reportDisplayName =  propertyHelper.getStringProperty(governanceServiceName,
                                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                                             surveyReport.getElementProperties(),
                                                                             methodName);
                if (reportDisplayName == null)
                {
                    reportDisplayName = "Asset survey report";
                }

                String reportFileName = reportDisplayName + "-" + reportDate + ".md";

                EgeriaReport outputReport = new EgeriaReport(surveyReportDirectory + "/" + reportFileName, false);

                outputReport.printReportTitle(indentLevel, reportDisplayName);
                outputReport.printReportLine(indentLevel, "* Report Print Time: " + reportDate);

                int reportIndentLevel = indentLevel + 1;

                this.printReport(reportIndentLevel,
                                 openMetadataStoreClient,
                                 surveyReport,
                                 outputReport);

                outputReport.closeReport();

                governanceContext.recordCompletionStatus(Guard.SERVICE_COMPLETED.getCompletionStatus(),
                                                         List.of(Guard.SERVICE_COMPLETED.getName()),
                                                         null,
                                                         null,
                                                         ReportsAuditCode.REPORT_CREATED.getMessageDefinition(governanceServiceName,
                                                                                                              reportFileName));
            }
        }
        catch (Exception error)
        {
            governanceContext.recordCompletionStatus(Guard.SERVICE_FAILED.getCompletionStatus(),
                                                     List.of(Guard.SERVICE_FAILED.getName()),
                                                     null,
                                                     null,
                                                     ReportsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()));
        }
    }
}
