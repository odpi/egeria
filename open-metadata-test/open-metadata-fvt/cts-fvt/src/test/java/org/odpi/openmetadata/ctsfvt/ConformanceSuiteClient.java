/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.ctsfvt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestLabResults;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestLabSummary;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceWorkbenchStatus;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCaseResult;
import org.odpi.openmetadata.conformance.rest.TestCaseListReportResponse;
import org.odpi.openmetadata.conformance.rest.TestCaseListResponse;
import org.odpi.openmetadata.conformance.rest.TestLabReportResponse;
import org.odpi.openmetadata.conformance.rest.TestLabSummaryResponse;
import org.odpi.openmetadata.conformance.rest.WorkbenchStatusResponse;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * ConformanceSuiteClient reads the conformance test server's results over its REST API.
 * <br>
 * The conformance suite ships a client of its own - {@code OpenMetadataConformanceTestReport} - but every
 * one of its methods is private: it is a command line program that writes the report to files, not a
 * library.  Rather than run it as a program and read its output back, this calls the same REST endpoints
 * directly and deserializes the responses into the suite's own beans, which are public API.
 */
class ConformanceSuiteClient
{
    /**
     * The repository workbench's identifier, as defined by {@code RepositoryConformanceWorkPad}.
     */
    static final String REPOSITORY_WORKBENCH_ID = "repository-workbench";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
                                                              .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                                                              .enable(SerializationFeature.INDENT_OUTPUT);

    private final HttpClient httpClient = HttpClient.newBuilder()
                                                    .connectTimeout(Duration.ofSeconds(30))
                                                    .build();

    private final String serviceURLRoot;


    /**
     * Create a client for the conformance test server running on the harness's platform.
     */
    ConformanceSuiteClient()
    {
        this.serviceURLRoot = OMAGPlatformExtension.getPlatformURLRoot()
                                      + "/servers/" + OMAGPlatformExtension.CTS_SERVER_NAME
                                      + "/open-metadata/conformance-suite/users/" + OMAGPlatformExtension.USER_ID;
    }


    /**
     * Return the current status of the repository workbench - most importantly, whether it has finished.
     *
     * @return workbench status, or null if the server has not reported one yet
     * @throws Exception problem calling the conformance test server
     */
    OpenMetadataConformanceWorkbenchStatus getRepositoryWorkbenchStatus() throws Exception
    {
        WorkbenchStatusResponse response = call("/status/workbenches/" + REPOSITORY_WORKBENCH_ID,
                                                WorkbenchStatusResponse.class);

        return response.getWorkbenchStatus();
    }


    /**
     * Return how many test cases the workbench has recorded so far.
     * <br>
     * This is the harness's measure of progress.  It matters most when a run does not finish: a workbench that
     * has recorded test cases is working and simply needs longer, whereas one that has recorded none never
     * started, which is a different problem with a different cause.
     *
     * @return number of test cases recorded
     * @throws Exception problem calling the conformance test server
     */
    int getTestCaseCount() throws Exception
    {
        TestCaseListResponse response = call("/report/test-cases", TestCaseListResponse.class);

        List<String> testCaseIds = response.getTestCaseIds();

        return (testCaseIds == null) ? 0 : testCaseIds.size();
    }


    /**
     * Return the summary of the whole conformance run - one entry per workbench, each with the status of
     * every profile the workbench evaluated.
     *
     * @return test lab summary
     * @throws Exception problem calling the conformance test server
     */
    OpenMetadataConformanceTestLabSummary getSummary() throws Exception
    {
        TestLabSummaryResponse response = call("/report/summary", TestLabSummaryResponse.class);

        return response.getTestLabSummary();
    }


    /**
     * Return the full result of every test case that failed.
     *
     * @return failed test case results - empty if nothing failed
     * @throws Exception problem calling the conformance test server
     */
    List<OpenMetadataTestCaseResult> getFailedTestCases() throws Exception
    {
        TestCaseListReportResponse response = call("/report/test-cases/failed", TestCaseListReportResponse.class);

        List<OpenMetadataTestCaseResult> results = response.getTestCaseResults();

        return (results == null) ? List.of() : results;
    }


    /**
     * Return the full conformance report - every workbench, profile and requirement, with the evidence
     * recorded for each.
     * <br>
     * The evidence is where the timings live.  Each assertion records how long the repository call it made
     * took, and the method it called, so the report already answers "where did the run spend its time"
     * without anything extra having to be measured.
     *
     * @return full test lab results
     * @throws Exception problem calling the conformance test server
     */
    OpenMetadataConformanceTestLabResults getConformanceReport() throws Exception
    {
        TestLabReportResponse response = call("/report", TestLabReportResponse.class);

        return response.getTestLabResults();
    }


    /**
     * Write a value out as indented JSON, so that a run leaves behind something a person can read through
     * afterwards rather than only what the assertion messages had room for.
     *
     * @param file file to write
     * @param value value to write
     * @throws Exception problem writing the file
     */
    static void writeJson(File file, Object value) throws Exception
    {
        File parent = file.getParentFile();

        if ((parent != null) && (! parent.isDirectory()) && (! parent.mkdirs()))
        {
            throw new IllegalStateException("Unable to create directory " + parent.getPath());
        }

        OBJECT_MAPPER.writeValue(file, value);
    }


    /**
     * Issue one GET and deserialize the response.
     *
     * @param path path below this server's conformance suite root
     * @param responseClass response bean to deserialize into
     * @param <T> response type
     * @return deserialized response
     * @throws Exception the call failed, or the response could not be understood
     */
    private <T> T call(String path, Class<T> responseClass) throws Exception
    {
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(serviceURLRoot + path))
                                         .timeout(Duration.ofMinutes(5))
                                         .GET()
                                         .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200)
        {
            throw new IllegalStateException("Conformance test server returned HTTP " + response.statusCode()
                                                    + " for " + path + ": " + response.body());
        }

        return OBJECT_MAPPER.readValue(response.body(), responseClass);
    }
}
