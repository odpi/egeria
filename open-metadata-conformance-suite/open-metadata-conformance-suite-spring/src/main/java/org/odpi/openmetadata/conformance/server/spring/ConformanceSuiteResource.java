/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.server.spring;

import org.odpi.openmetadata.conformance.rest.TestCaseListReportResponse;
import org.odpi.openmetadata.conformance.rest.TestCaseReportResponse;
import org.odpi.openmetadata.conformance.rest.TestLabReportResponse;
import org.odpi.openmetadata.conformance.rest.WorkbenchReportResponse;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteTestLabServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConformanceSuiteResource provides the REST API for running the Open Metadata Conformance Suite.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/conformance-suite-services/users/{userId}")

public class ConformanceSuiteResource
{
    private ConformanceSuiteTestLabServices restAPI = new ConformanceSuiteTestLabServices();

    /**
     * Requests detailed information on the execution of a specific test case.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param testCaseId technology under test server name.
     * @return TestCaseReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/report/test-cases/{testCaseId}")
    public TestCaseReportResponse getTestCaseReport(@PathVariable String   userId,
                                                    @PathVariable String   serverName,
                                                    @PathVariable String   testCaseId)
    {
        return restAPI.getTestCaseReport(userId, serverName, testCaseId);
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
    @RequestMapping(method = RequestMethod.GET, path = "/report/test-cases/failed")
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
    @RequestMapping(method = RequestMethod.GET, path = "/report/workbenches/{workbenchId}")
    public WorkbenchReportResponse getWorkbenchReport(@PathVariable String   userId,
                                                      @PathVariable String   serverName,
                                                      @PathVariable String   workbenchId)
    {
        return restAPI.getWorkbenchReport(userId, serverName, workbenchId);
    }


    /**
     * Request a full report on the conformance of the technology under test.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestLabReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/report")
    public TestLabReportResponse getConformanceReport(@PathVariable String   userId,
                                                      @PathVariable String   serverName)
    {
        return restAPI.getConformanceReport(userId, serverName);
    }
}
