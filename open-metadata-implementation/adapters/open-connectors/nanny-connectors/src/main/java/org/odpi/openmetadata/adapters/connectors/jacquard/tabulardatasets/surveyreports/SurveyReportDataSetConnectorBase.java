/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SurveyReportDataSetConnectorBase provides the common processing for the data sets built from survey reports
 * and their annotations.
 * <br><br>
 * An annotation is anchored to the surveyed asset and reused by later surveys of the same resource, so it can
 * appear in many survey reports.  Its properties are those of the most recent survey.  The data sets that list
 * what each report contained have one row for each report an annotation appears in; the measurement data sets
 * have one row per annotation, linked to the most recent report, since the measurements in an annotation are
 * those of that report and repeating them against earlier reports would misstate what those surveys found.
 */
public abstract class SurveyReportDataSetConnectorBase extends OpenMetadataRowDataSetConnectorBase
{
    /**
     * Parses the JSON measurement beans stored in an annotation's jsonProperties.
     */
    protected static final ObjectReader OBJECT_READER = new ObjectMapper().reader();

    /*
     * The survey reports, retrieved with their subjects and originators.  Loaded on demand for each refresh.
     */
    private Map<String, OpenMetadataRootElement> surveyReports = null;


    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     * @param productDefinition definition of the product that this connector serves
     */
    public SurveyReportDataSetConnectorBase(String            connectorName,
                                            ProductDefinition productDefinition)
    {
        super(connectorName, productDefinition);
    }


    /**
     * Rebuild the rows, starting with a fresh view of the survey reports.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        surveyReports = null;

        try
        {
            collectSurveyRows();
        }
        finally
        {
            surveyReports = null;
        }
    }


    /**
     * Gather the rows of the data set by calling addRow for each one.
     *
     * @throws Exception problem retrieving the metadata
     */
    protected abstract void collectSurveyRows() throws Exception;


    /**
     * Return the survey reports, keyed by unique identifier, with their subjects and originators.
     *
     * @return map of survey reports in creation order
     * @throws Exception problem retrieving the metadata
     */
    protected Map<String, OpenMetadataRootElement> getSurveyReports() throws Exception
    {
        if (surveyReports == null)
        {
            surveyReports = new java.util.LinkedHashMap<>();

            for (OpenMetadataRootElement surveyReport : getAllElements(OpenMetadataType.SURVEY_REPORT.typeName,
                                                                       1,
                                                                       List.of(OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName,
                                                                               OpenMetadataType.REPORT_ORIGINATOR_RELATIONSHIP.typeName)))
            {
                surveyReports.put(surveyReport.getElementHeader().getGUID(), surveyReport);
            }
        }

        return surveyReports;
    }


    /**
     * Return every annotation of the requested type (and its subtypes) with the reports it appears in, its
     * subjects and its action targets.
     *
     * @param annotationTypeName open metadata type name of the annotations
     * @return list of annotations, oldest first
     * @throws Exception problem retrieving the metadata
     */
    protected List<OpenMetadataRootElement> getAnnotations(String annotationTypeName) throws Exception
    {
        return getAllElements(annotationTypeName,
                              1,
                              List.of(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                      OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                      OpenMetadataType.REQUEST_FOR_ACTION_TARGET_RELATIONSHIP.typeName));
    }


    /**
     * Return the annotations of the requested type with the requested annotationType property value.
     *
     * @param annotationTypeName open metadata type name of the annotations
     * @param annotationTypes values of the annotationType property to include
     * @return list of annotations, oldest first
     * @throws Exception problem retrieving the metadata
     */
    protected List<OpenMetadataRootElement> getAnnotations(String       annotationTypeName,
                                                           List<String> annotationTypes) throws Exception
    {
        List<OpenMetadataRootElement> results = new ArrayList<>();

        for (OpenMetadataRootElement annotation : getAnnotations(annotationTypeName))
        {
            if ((annotation.getProperties() instanceof AnnotationProperties annotationProperties) &&
                    (annotationTypes.contains(annotationProperties.getAnnotationType())))
            {
                results.add(annotation);
            }
        }

        return results;
    }


    /**
     * Return the survey reports that an annotation appears in.
     *
     * @param annotation annotation retrieved through getAnnotations
     * @return list of survey report summaries (empty if none)
     */
    protected List<MetadataElementSummary> getReportsForAnnotation(OpenMetadataRootElement annotation)
    {
        List<MetadataElementSummary> reports = new ArrayList<>();

        if (annotation.getFromSurveyReports() != null)
        {
            for (RelatedMetadataElementSummary report : annotation.getFromSurveyReports())
            {
                if ((report != null) && (report.getRelatedElement() != null))
                {
                    reports.add(report.getRelatedElement());
                }
            }
        }

        return reports;
    }


    /**
     * Return the most recent survey report that an annotation appears in.
     *
     * @param annotation annotation retrieved through getAnnotations
     * @return survey report summary, or null if the annotation is in no report
     */
    protected MetadataElementSummary getLatestReportForAnnotation(OpenMetadataRootElement annotation)
    {
        MetadataElementSummary latestReport = null;
        Date                   latestTime   = null;

        for (MetadataElementSummary report : getReportsForAnnotation(annotation))
        {
            Date createTime = report.getElementHeader().getVersions().getCreateTime();

            if ((latestReport == null) || ((createTime != null) && ((latestTime == null) || (createTime.after(latestTime)))))
            {
                latestReport = report;
                latestTime   = createTime;
            }
        }

        return latestReport;
    }


    /**
     * Return the subject of an annotation: the element it is attached to with AssociatedAnnotation or, if it
     * has none, the subject of the report it appears in.
     *
     * @param annotation annotation retrieved through getAnnotations
     * @param surveyReportGUID unique identifier of the report (may be null)
     * @return header of the subject, or null if there is none
     * @throws Exception problem retrieving the metadata
     */
    protected ElementHeader getAnnotationSubject(OpenMetadataRootElement annotation,
                                                 String                  surveyReportGUID) throws Exception
    {
        if (annotation.getAnnotationSubjects() != null)
        {
            for (RelatedMetadataElementSummary subject : annotation.getAnnotationSubjects())
            {
                if ((subject != null) && (subject.getRelatedElement() != null))
                {
                    return subject.getRelatedElement().getElementHeader();
                }
            }
        }

        return getReportSubject(surveyReportGUID);
    }


    /**
     * Return the subject of a survey report.
     *
     * @param surveyReportGUID unique identifier of the report (may be null)
     * @return header of the subject, or null if there is none
     * @throws Exception problem retrieving the metadata
     */
    protected ElementHeader getReportSubject(String surveyReportGUID) throws Exception
    {
        if (surveyReportGUID != null)
        {
            OpenMetadataRootElement surveyReport = getSurveyReports().get(surveyReportGUID);

            if ((surveyReport != null) && (surveyReport.getReportSubjects() != null))
            {
                for (RelatedMetadataElementSummary subject : surveyReport.getReportSubjects())
                {
                    if ((subject != null) && (subject.getRelatedElement() != null))
                    {
                        return subject.getRelatedElement().getElementHeader();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Add the values that describe the survey report itself: its times, subject and the engine action that
     * produced it.
     *
     * @param values values for the row
     * @param surveyReport survey report
     */
    protected void putSurveyReportValues(Map<String, String>     values,
                                         OpenMetadataRootElement surveyReport)
    {
        putValue(values, ProductDataFieldDefinition.SURVEY_REPORT_GUID, surveyReport.getElementHeader().getGUID());
        putValue(values, ProductDataFieldDefinition.CREATED_BY, surveyReport.getElementHeader().getVersions().getCreatedBy());

        if (surveyReport.getProperties() instanceof ReportProperties reportProperties)
        {
            putValue(values, ProductDataFieldDefinition.START_TIMESTAMP, reportProperties.getStartTime());
            putValue(values, ProductDataFieldDefinition.END_TIMESTAMP, reportProperties.getCompletionTime());
        }

        ElementHeader subject = null;

        if (surveyReport.getReportSubjects() != null)
        {
            for (RelatedMetadataElementSummary reportSubject : surveyReport.getReportSubjects())
            {
                if ((reportSubject != null) && (reportSubject.getRelatedElement() != null))
                {
                    subject = reportSubject.getRelatedElement().getElementHeader();
                    break;
                }
            }
        }

        if (subject != null)
        {
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_GUID, subject.getGUID());
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_TYPE_NAME, subject.getType().getTypeName());
        }
        else
        {
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_GUID, null);
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_TYPE_NAME, null);
        }

        RelatedMetadataElementSummary originator = surveyReport.getReportOriginator();

        if ((originator != null) && (originator.getRelatedElement() != null))
        {
            MetadataElementSummary engineAction = originator.getRelatedElement();

            putValue(values, ProductDataFieldDefinition.REPORT_ORIGINATOR_GUID, engineAction.getElementHeader().getGUID());

            if (engineAction.getElementHeader().getVersions() != null)
            {
                putValue(values, ProductDataFieldDefinition.INITIATOR_USER_ID, engineAction.getElementHeader().getVersions().getCreatedBy());
            }

            if (engineAction.getProperties() instanceof EngineActionProperties engineActionProperties)
            {
                putValue(values, ProductDataFieldDefinition.REQUEST_TYPE, engineActionProperties.getRequestType());
                putValue(values, ProductDataFieldDefinition.GOVERNANCE_ENGINE_NAME, engineActionProperties.getExecutorEngineName());
            }
        }
        else
        {
            putValue(values, ProductDataFieldDefinition.REPORT_ORIGINATOR_GUID, null);
        }
    }


    /**
     * Add the values that describe the report an annotation appears in, using the full report if it is known.
     *
     * @param values values for the row
     * @param surveyReportGUID unique identifier of the report (may be null)
     * @throws Exception problem retrieving the metadata
     */
    protected void putSurveyReportValues(Map<String, String> values,
                                         String              surveyReportGUID) throws Exception
    {
        OpenMetadataRootElement surveyReport = null;

        if (surveyReportGUID != null)
        {
            surveyReport = getSurveyReports().get(surveyReportGUID);
        }

        if (surveyReport != null)
        {
            putSurveyReportValues(values, surveyReport);
        }
        else
        {
            putValue(values, ProductDataFieldDefinition.SURVEY_REPORT_GUID, surveyReportGUID);
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_GUID, null);
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_TYPE_NAME, null);
            putValue(values, ProductDataFieldDefinition.REPORT_ORIGINATOR_GUID, null);
            putValue(values, ProductDataFieldDefinition.START_TIMESTAMP, null);
            putValue(values, ProductDataFieldDefinition.END_TIMESTAMP, null);
            putValue(values, ProductDataFieldDefinition.CREATED_BY, null);
        }
    }


    /**
     * Add the values that describe an annotation's subject.  The report subject type name column is filled with
     * the annotation subject's type when the row is not about the report as a whole.
     *
     * @param values values for the row
     * @param subject header of the subject (may be null)
     */
    protected void putAnnotationSubjectValues(Map<String, String> values,
                                              ElementHeader       subject)
    {
        if (subject != null)
        {
            putValue(values, ProductDataFieldDefinition.ANNOTATION_SUBJECT_GUID, subject.getGUID());
            putValue(values, ProductDataFieldDefinition.ANNOTATION_SUBJECT_TYPE_NAME, subject.getType().getTypeName());

            if (subject.getOrigin() != null)
            {
                putValue(values, ProductDataFieldDefinition.METADATA_COLLECTION_ID, subject.getOrigin().getHomeMetadataCollectionId());
            }
            else
            {
                putValue(values, ProductDataFieldDefinition.METADATA_COLLECTION_ID, null);
            }
        }
        else
        {
            putValue(values, ProductDataFieldDefinition.ANNOTATION_SUBJECT_GUID, null);
            putValue(values, ProductDataFieldDefinition.ANNOTATION_SUBJECT_TYPE_NAME, null);
            putValue(values, ProductDataFieldDefinition.METADATA_COLLECTION_ID, null);
        }
    }


    /**
     * Return the value of the annotationType property.
     *
     * @param annotation annotation
     * @return string or null
     */
    protected String getAnnotationType(OpenMetadataRootElement annotation)
    {
        if (annotation.getProperties() instanceof AnnotationProperties annotationProperties)
        {
            return annotationProperties.getAnnotationType();
        }

        return null;
    }


    /**
     * Parse the measurement bean stored in an annotation's jsonProperties.  An annotation whose JSON cannot be
     * parsed is logged and skipped rather than failing the whole data set.
     *
     * @param annotation annotation
     * @param beanClass class of the measurement bean
     * @param <T> type of the measurement bean
     * @return bean or null
     */
    protected <T> T getMeasurement(OpenMetadataRootElement annotation,
                                   Class<T>                beanClass)
    {
        final String methodName = "getMeasurement";

        if ((annotation.getProperties() instanceof AnnotationProperties annotationProperties) &&
                (annotationProperties.getJsonProperties() != null))
        {
            try
            {
                return OBJECT_READER.readValue(annotationProperties.getJsonProperties(), beanClass);
            }
            catch (Exception error)
            {
                logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                     error.getClass().getName(),
                                                                                                     methodName + "(" + annotation.getElementHeader().getGUID() + ")",
                                                                                                     error.getMessage()));
            }
        }

        return null;
    }


    /**
     * Start the values for a measurement row: the most recent report the annotation appears in, the
     * annotation's subject, and the subject's type in the report subject type column.
     *
     * @param annotation annotation
     * @return values (or null if the annotation is in no survey report)
     * @throws Exception problem retrieving the metadata
     */
    protected Map<String, String> getMeasurementValues(OpenMetadataRootElement annotation) throws Exception
    {
        MetadataElementSummary latestReport = getLatestReportForAnnotation(annotation);

        if (latestReport == null)
        {
            return null;
        }

        String surveyReportGUID = latestReport.getElementHeader().getGUID();

        Map<String, String> values = new HashMap<>();

        putValue(values, ProductDataFieldDefinition.SURVEY_REPORT_GUID, surveyReportGUID);

        ElementHeader subject = getAnnotationSubject(annotation, surveyReportGUID);

        putAnnotationSubjectValues(values, subject);

        if (subject != null)
        {
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_TYPE_NAME, subject.getType().getTypeName());
        }
        else
        {
            putValue(values, ProductDataFieldDefinition.REPORT_SUBJECT_TYPE_NAME, null);
        }

        return values;
    }


    /**
     * Return the display name of an element.
     *
     * @param element element
     * @return string or null
     */
    protected static String getDisplayName(OpenMetadataRootElement element)
    {
        if (element.getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            return referenceableProperties.getDisplayName();
        }

        return null;
    }


    /**
     * Return a measurement value as a whole number if it is one, since the numeric value column is an integer.
     * Measurements are stored as strings in the annotation's resource properties.
     *
     * @param value stored value
     * @return number or null if the value is not a whole number
     */
    protected static Long getNumericValue(String value)
    {
        if (value != null)
        {
            try
            {
                return Long.valueOf(value.trim());
            }
            catch (NumberFormatException notWholeNumber)
            {
                try
                {
                    double number = Double.parseDouble(value.trim());

                    if ((number == Math.rint(number)) && (! Double.isInfinite(number)))
                    {
                        return (long) number;
                    }
                }
                catch (NumberFormatException notNumeric)
                {
                    return null;
                }
            }
        }

        return null;
    }
}
