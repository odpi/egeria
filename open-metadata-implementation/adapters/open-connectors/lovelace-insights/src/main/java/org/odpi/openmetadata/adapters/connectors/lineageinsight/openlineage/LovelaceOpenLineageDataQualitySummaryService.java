/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AnnotationClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.SurveyReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.QualityAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LovelaceOpenLineageDataQualitySummaryService analyses the data quality assertions and tests reported in an
 * OpenLineage log store and records, for each dataset (and for each job's tests), the pass rate of each quality
 * dimension over the analysis window.  The OpenLineage cataloguer records every assertion of every run as it
 * arrives; this service produces one survey report per element with one QualityAnnotation per dimension whose
 * score is the pass rate, so that the current quality of a dataset can be read without scanning the run history.
 */
public class LovelaceOpenLineageDataQualitySummaryService extends LovelaceOpenLineageAnalysisServiceBase
{
    private static final String REPORT_QUALIFIED_NAME_PREFIX = "OpenLineageDataQualitySummary::";
    private static final String ANNOTATION_TYPE              = "OpenLineage Data Quality Summary";


    /**
     * Default constructor
     */
    public LovelaceOpenLineageDataQualitySummaryService()
    {
    }


    /**
     * Describe what the service produces.
     *
     * @return description
     */
    @Override
    protected String getAnalysisDescription()
    {
        return "data quality summaries";
    }


    /**
     * Pass/fail counts for one quality dimension.
     */
    private static class DimensionSummary
    {
        int    passed          = 0;
        int    failed          = 0;
        int    skipped         = 0;
        Date   lastFailure     = null;
        Date   lastEvaluation  = null;
        String severity        = null;
        String lastResult      = null;

        /**
         * Record one result.
         *
         * @param time time
         * @param result pass, fail or skip
         * @param severity severity (may be null)
         */
        void record(Date time, String result, String severity)
        {
            if ("pass".equals(result))
            {
                passed++;
            }
            else if ("fail".equals(result))
            {
                failed++;

                if ((lastFailure == null) || (time.after(lastFailure)))
                {
                    lastFailure = time;
                }
            }
            else
            {
                skipped++;
            }

            if ((lastEvaluation == null) || (! time.before(lastEvaluation)))
            {
                lastEvaluation = time;
                lastResult     = result;
            }

            if (severity != null)
            {
                this.severity = severity;
            }
        }

        /**
         * Return the pass rate as a percentage of the evaluated (non-skipped) results.
         *
         * @return 0-100
         */
        int score()
        {
            int evaluated = passed + failed;

            return (evaluated == 0) ? 0 : (int) Math.round(100.0 * passed / evaluated);
        }
    }


    /**
     * Summarise the data quality results for each dataset and job.
     *
     * @param history events in the analysis window
     * @return number of survey reports created
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    @Override
    protected int analyse(OpenLineageRunHistory history) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        int reports = 0;

        for (OpenLineageRunHistory.DataSetHistory datasetHistory : history.getDataSets().values())
        {
            if (datasetHistory.assertions.isEmpty())
            {
                continue;
            }

            OpenMetadataRootElement asset = findDataAsset(datasetHistory.dataset);

            if (asset == null)
            {
                continue;
            }

            Map<String, DimensionSummary> dimensions = new LinkedHashMap<>();

            for (OpenLineageRunHistory.AssertionRecord assertion : datasetHistory.assertions)
            {
                dimensions.computeIfAbsent(assertion.dimension(), key -> new DimensionSummary()).record(assertion.time(), assertion.success() ? "pass" : "fail", assertion.severity());
            }

            createSummaryReport(asset, datasetHistory.dataset.display(), "dataset", dimensions, false);
            reports++;
        }

        for (OpenLineageRunHistory.JobHistory jobHistory : history.getJobs().values())
        {
            if (jobHistory.tests.isEmpty())
            {
                continue;
            }

            OpenMetadataRootElement process = findProcess(jobHistory.job);

            if (process == null)
            {
                continue;
            }

            Map<String, DimensionSummary> dimensions = new LinkedHashMap<>();

            for (OpenLineageRunHistory.TestRecord test : jobHistory.tests)
            {
                String dimension = (test.type() != null) ? test.type() + "." + test.name() : test.name();
                String result    = (test.status() == null) ? "skip" : test.status().toLowerCase();

                dimensions.computeIfAbsent(dimension, key -> new DimensionSummary()).record(test.time(), result, test.severity());
            }

            createSummaryReport(process, jobHistory.job.display(), "job", dimensions, true);
            reports++;
        }

        return reports;
    }


    /**
     * Create a survey report holding a quality annotation per dimension, anchored to and describing the element.
     *
     * @param element dataset asset or job process
     * @param elementName OpenLineage name for messages
     * @param elementKind dataset or job
     * @param dimensions summaries by dimension
     * @param originator whether the element is the originator (a process) rather than the subject (a dataset) of the report
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void createSummaryReport(OpenMetadataRootElement       element,
                                     String                        elementName,
                                     String                        elementKind,
                                     Map<String, DimensionSummary> dimensions,
                                     boolean                       originator) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "createSummaryReport";

        AssetClient      reportClient     = governanceContext.getAssetClient(OpenMetadataType.SURVEY_REPORT.typeName);
        AnnotationClient annotationClient = governanceContext.getAnnotationClient();

        String elementGUID          = element.getElementHeader().getGUID();
        String elementQualifiedName = (element.getProperties() instanceof ReferenceableProperties properties) ? properties.getQualifiedName() : elementGUID;

        int totalPassed = 0;
        int totalFailed = 0;

        for (DimensionSummary summary : dimensions.values())
        {
            totalPassed += summary.passed;
            totalFailed += summary.failed;
        }

        int overallScore = (totalPassed + totalFailed == 0) ? 0 : (int) Math.round(100.0 * totalPassed / (totalPassed + totalFailed));

        /*
         * The report
         */
        SurveyReportProperties reportProperties = new SurveyReportProperties();

        reportProperties.setTypeName(OpenMetadataType.SURVEY_REPORT.typeName);
        reportProperties.setQualifiedName(REPORT_QUALIFIED_NAME_PREFIX + elementQualifiedName + "::" + formatDate(analysisTime));
        reportProperties.setDisplayName("OpenLineage data quality summary for " + elementName);
        reportProperties.setDescription("Pass rates of the data quality " + ((originator) ? "tests" : "assertions") + " reported in OpenLineage events for " + elementKind + " " + elementName +
                                                " between " + formatDate(windowStart) + " and " + formatDate(windowEnd) + ".  Overall pass rate " + overallScore + "%.");
        reportProperties.setPurpose("Summarise the data quality results observed by the runs of data pipelines over a period.");
        reportProperties.setAnalysisStep("SUMMARISE");
        reportProperties.setStartTime(windowStart);
        reportProperties.setCompletionTime(analysisTime);

        Map<String, String> analysisParameters = new HashMap<>();
        addAnalysisProperties(analysisParameters);
        analysisParameters.put("overallPassRate", Integer.toString(overallScore));
        analysisParameters.put("dimensions", Integer.toString(dimensions.size()));
        reportProperties.setAnalysisParameters(analysisParameters);

        NewElementOptions reportOptions = new NewElementOptions(reportClient.getMetadataSourceOptions());

        reportOptions.setAnchorGUID(elementGUID);
        reportOptions.setIsOwnAnchor(false);

        String reportGUID = reportClient.createAsset(reportOptions, null, reportProperties, null);

        if (originator)
        {
            reportClient.linkReportOriginator(elementGUID, reportGUID, new MakeAnchorOptions(reportClient.getMetadataSourceOptions()), null);
        }
        else
        {
            reportClient.linkReportSubject(elementGUID, reportGUID, new MakeAnchorOptions(reportClient.getMetadataSourceOptions()), null);
        }

        /*
         * One annotation per dimension plus the overall summary.
         */
        for (String dimension : dimensions.keySet())
        {
            DimensionSummary summary = dimensions.get(dimension);

            addAnnotation(annotationClient, reportGUID, reportProperties.getQualifiedName(), elementGUID, dimension, summary.score(), summary.passed, summary.failed, summary.skipped, summary.lastFailure, summary.lastResult, summary.severity);
        }

        addAnnotation(annotationClient, reportGUID, reportProperties.getQualifiedName(), elementGUID, "overall", overallScore, totalPassed, totalFailed, 0, null, null, null);

        logRecord(methodName, LovelaceInsightAuditCode.OPEN_LINEAGE_DATA_QUALITY_SUMMARISED.getMessageDefinition(governanceServiceName,
                                                                                                                elementKind,
                                                                                                                elementName,
                                                                                                                elementGUID,
                                                                                                                Integer.toString(dimensions.size()),
                                                                                                                Integer.toString(overallScore)));
    }


    /**
     * Add a quality annotation to the report and link it to the element it describes.
     *
     * @param annotationClient client
     * @param reportGUID report
     * @param reportQualifiedName qualified name of the report (prefix for the annotation's name)
     * @param elementGUID described element
     * @param dimension quality dimension
     * @param score pass rate
     * @param passed passed count
     * @param failed failed count
     * @param skipped skipped count
     * @param lastFailure last failure time (may be null)
     * @param lastResult last result (may be null)
     * @param severity severity (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addAnnotation(AnnotationClient annotationClient,
                               String           reportGUID,
                               String           reportQualifiedName,
                               String           elementGUID,
                               String           dimension,
                               int              score,
                               int              passed,
                               int              failed,
                               int              skipped,
                               Date             lastFailure,
                               String           lastResult,
                               String           severity) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        QualityAnnotationProperties properties = new QualityAnnotationProperties();

        properties.setTypeName(OpenMetadataType.QUALITY_ANNOTATION.typeName);
        properties.setQualifiedName(reportQualifiedName + "::" + dimension);
        properties.setAnnotationType(ANNOTATION_TYPE);
        properties.setAnalysisStep("SUMMARISE");
        properties.setQualityDimension(dimension);
        properties.setQualityScore(score);
        properties.setSummary(dimension + ": " + passed + " passed, " + failed + " failed" + ((skipped > 0) ? ", " + skipped + " skipped" : "") + " (" + score + "%)");

        StringBuilder description = new StringBuilder("Pass rate between " + formatDate(windowStart) + " and " + formatDate(windowEnd) + ".");

        if (lastFailure != null)
        {
            description.append("  Last failure at ").append(formatDate(lastFailure)).append(".");
        }
        if (lastResult != null)
        {
            description.append("  Latest result: ").append(lastResult).append(".");
        }
        if (severity != null)
        {
            description.append("  Severity: ").append(severity).append(".");
        }

        properties.setQualityDescription(description.toString());
        properties.setExplanation("Derived by " + governanceServiceName + " from the OpenLineage log store.");
        properties.setSampleSize(passed + failed + skipped);

        NewElementOptions annotationOptions = new NewElementOptions(annotationClient.getMetadataSourceOptions());

        annotationOptions.setAnchorGUID(reportGUID);
        annotationOptions.setIsOwnAnchor(false);
        annotationOptions.setParentGUID(reportGUID);
        annotationOptions.setParentRelationshipTypeName(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);
        annotationOptions.setParentAtEnd1(true);

        String annotationGUID = annotationClient.createAnnotation(annotationOptions, null, properties, null);

        annotationClient.linkAnnotationToDescribedElement(elementGUID, annotationGUID, new MakeAnchorOptions(annotationClient.getMetadataSourceOptions()), null);
    }
}
