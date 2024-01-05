/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.conformance.rest.*;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteTestLabServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * ConformanceSuiteResource provides the REST API for running the Open Metadata Conformance Suite.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/conformance-suite/users/{userId}")

@Tag(name="Conformance Suite",
     description="The open metadata conformance suite provides a testing framework to help the developers integrate a specific technology into the open metadata ecosystem.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/guides/cts/overview/"))


public class ConformanceSuiteResource
{
    private final ConformanceSuiteTestLabServices restAPI = new ConformanceSuiteTestLabServices();


    /**
     * Requests the list of test case IDs that are available.
     * (Response is likely to weigh in at around 250KB.)
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestCaseListResponse or
     * InvalidParameterException the serverName is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report/test-cases")
    public TestCaseListResponse getTestCaseIds(@PathVariable String   userId,
                                               @PathVariable String   serverName)
    {
        return restAPI.getTestCaseIds(userId, serverName);
    }


    /**
     * Requests detailed information on the execution of a specific test case.
     * (Response size will vary, but could be 50-100KB each for the larger test cases.)
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param testCaseId of the test case for which to retrieve a detailed report.
     * @return TestCaseReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report/test-cases/{testCaseId}")
    public TestCaseReportResponse getTestCaseReport(@PathVariable String   userId,
                                                    @PathVariable String   serverName,
                                                    @PathVariable String   testCaseId)
    {
        return restAPI.getTestCaseReport(userId, serverName, testCaseId);
    }


    /**
     * Requests the list of profile (names) that are available.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestCaseListResponse or
     * InvalidParameterException the serverName is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report/profiles")
    public ProfileNameListResponse getProfileNames(@PathVariable String   userId,
                                                   @PathVariable String   serverName)
    {
        return restAPI.getProfileNames(userId, serverName);
    }


    /**
     * Requests detailed information on the execution of a specific profile.
     * (Response size will vary, but could be ~25MB for the largest profile ("Metadata sharing").)
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param profileName name of the profile for which to obtain a detailed report.
     * @return ProfileReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report/profiles/{profileName}")
    public ProfileReportResponse getProfileReport(@PathVariable String   userId,
                                                  @PathVariable String   serverName,
                                                  @PathVariable String   profileName)
    {
        return restAPI.getProfileReport(userId, serverName, profileName);
    }


    /**
     * Requests detailed information on the execution of all of the failed test cases.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestCaseListReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report/test-cases/failed")
    public TestCaseListReportResponse getFailedTestCaseReport(@PathVariable String   userId,
                                                              @PathVariable String   serverName)
    {
        return restAPI.getFailedTestCaseReport(userId, serverName);
    }


    /**
     * Requests information on the level of conformance discovered by a specific workbench
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param workbenchId identifier of the workbench.
     * @return WorkbenchReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report/workbenches/{workbenchId}")
    public WorkbenchReportResponse getWorkbenchReport(@PathVariable String   userId,
                                                      @PathVariable String   serverName,
                                                      @PathVariable String   workbenchId)
    {
        return restAPI.getWorkbenchReport(userId, serverName, workbenchId);
    }


    /**
     * Request a full report on the conformance of the technology under test.
     * Note: this can be 100's of MB, and aside from being un-loadable by just about any JSON editor also runs the risk
     * of over-running the server-side JVM's heap: just serializing the response can use around 1GB of heap. It remains
     * for backwards compatibility reasons, but it is suggested to instead use a combination of the other (more granular)
     * endpoints to retrieve the information of interest, and make iterative calls if still necessary to retrieve all
     * details of every profile and test case that was executed.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestLabReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report")
    public TestLabReportResponse getConformanceReport(@PathVariable String   userId,
                                                      @PathVariable String   serverName)
    {
        return restAPI.getConformanceReport(userId, serverName);
    }


    /**
     * Request a summary report on the conformance of the technology under test.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestLabSummaryResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/report/summary")
    public TestLabSummaryResponse getConformanceSummaryReport(@PathVariable String   userId,
                                                              @PathVariable String   serverName)
    {
        return restAPI.getConformanceSummaryReport(userId, serverName);
    }


    /**
     * Interrogate a workbench to find out if it has completed (its synchronous tests)
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param workbenchId   which workbench?
     * @return TestLabReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @GetMapping(path = "/status/workbenches/{workbenchId}")
    public WorkbenchStatusResponse getWorkbenchStatus(@PathVariable String   userId,
                                                      @PathVariable String   serverName,
                                                      @PathVariable String   workbenchId)
    {
        return restAPI.getWorkbenchStatus(userId, serverName, workbenchId);
    }
}
