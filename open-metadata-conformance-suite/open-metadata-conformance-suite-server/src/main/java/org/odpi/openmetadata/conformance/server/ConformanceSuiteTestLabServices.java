/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.server;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteErrorCode;
import org.odpi.openmetadata.conformance.ffdc.exception.ConformanceSuiteCheckedExceptionBase;
import org.odpi.openmetadata.conformance.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.conformance.ffdc.exception.PropertyServerException;
import org.odpi.openmetadata.conformance.rest.*;
import org.odpi.openmetadata.conformance.beans.TechnologyUnderTestWorkPad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class ConformanceSuiteTestLabServices
{
    private static ConformanceServicesInstanceMap instanceMap   = new ConformanceServicesInstanceMap();

    private static final Logger log = LoggerFactory.getLogger(ConformanceSuiteTestLabServices.class);


    /**
     * Requests the list of profile (names) that are available.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestCaseListResponse or
     * InvalidParameterException the serverName is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    public ProfileNameListResponse getProfileNames(String  userId,
                                                   String  serverName)
    {
        final String   methodName = "getProfileNames";

        log.debug("Calling method: " + methodName);

        ProfileNameListResponse response = new ProfileNameListResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setProfileNames(workPad.getProfileNames());
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Requests detailed information on the execution of a specific test case.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param profileName of the profile for which to obtain a detailed report.
     * @return TestCaseReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    public ProfileReportResponse getProfileReport(String   userId,
                                                  String   serverName,
                                                  String   profileName)
    {
        final String   methodName = "getProfileReport";

        log.debug("Calling method: " + methodName);

        ProfileReportResponse response = new ProfileReportResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setProfileResult(workPad.getProfileReport(profileName));
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Requests the list of test case IDs that are available.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @return TestCaseListResponse or
     * InvalidParameterException the serverName is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    public TestCaseListResponse getTestCaseIds(String  userId,
                                               String  serverName)
    {
        final String   methodName = "getTestCaseIds";

        log.debug("Calling method: " + methodName);

        TestCaseListResponse response = new TestCaseListResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setTestCaseIds(workPad.getTestCaseIds());
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Requests detailed information on the execution of a specific test case.
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param testCaseId of the test case for which to obtain a detailed report.
     * @return TestCaseReportResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    public TestCaseReportResponse getTestCaseReport(String   userId,
                                                    String   serverName,
                                                    String   testCaseId)
    {
        final String   methodName = "getTestCaseReport";

        log.debug("Calling method: " + methodName);

        TestCaseReportResponse response = new TestCaseReportResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setTestCaseResult(workPad.getTestCaseReport(testCaseId));
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
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
    public TestCaseListReportResponse getFailedTestCaseReport(String   userId,
                                                              String   serverName)
    {
        final String   methodName = "getFailedTestCaseReport";

        log.debug("Calling method: " + methodName);

        TestCaseListReportResponse response = new TestCaseListReportResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setTestCaseResults(workPad.getFailedTestCaseReport());
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
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
    public WorkbenchReportResponse getWorkbenchReport(String   userId,
                                                      String   serverName,
                                                      String   workbenchId)
    {
        final String   methodName = "getWorkbenchReport";

        log.debug("Calling method: " + methodName);

        WorkbenchReportResponse response = new WorkbenchReportResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setWorkbenchResults(workPad.getWorkbenchReport(workbenchId));
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
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
    public TestLabReportResponse getConformanceReport(String   userId,
                                                      String   serverName)
    {
        final String   methodName = "getConformanceReport";

        log.debug("Calling method: " + methodName);

        TestLabReportResponse response = new TestLabReportResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setTestLabResults(workPad.getTestLabResults());
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
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
    public TestLabSummaryResponse getConformanceSummaryReport(String   userId,
                                                              String   serverName)
    {
        final String   methodName = "getConformanceSummaryReport";

        log.debug("Calling method: " + methodName);

        TestLabSummaryResponse response = new TestLabSummaryResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setTestLabSummary(workPad.getTestLabSummary());
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request the status of a conformance workbench
     *
     * @param userId calling user.
     * @param serverName the name of the conformance service.
     * @param workbenchId the id of the workbench.
     * @return WorkbenchStatusResponse or
     * InvalidParameterException the serverName or workbenchId is not known or
     * UserNotAuthorizedException the supplied userId is not known.
     */
    public WorkbenchStatusResponse getWorkbenchStatus(String   userId,
                                                      String   serverName,
                                                      String   workbenchId)
    {
        final String   methodName = "getWorkbenchStatus";

        log.debug("Calling method: " + methodName);

        WorkbenchStatusResponse response = new WorkbenchStatusResponse();

        try
        {
            validateUserId(userId, methodName);
            TechnologyUnderTestWorkPad workPad = getWorkPad(serverName, methodName);
            response.setWorkbenchStatus(workPad.getWorkbenchStatus(workbenchId));
        }
        catch (PropertyServerException   error)
        {
            capturePropertyServerException(response, error);
        }
        catch (InvalidParameterException   error)
        {
            captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }

    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    private void validateUserId(String userId, String methodName) throws InvalidParameterException
    {
        if (userId == null)
        {
            ConformanceSuiteErrorCode errorCode = ConformanceSuiteErrorCode.NULL_USER_ID;
            String                 errorMessage = errorCode.getErrorMessageId()
                                                + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                "userId");
        }
    }


    /**
     * Retrieve the work pad for the requested server from the instance map.  If the requested server is not running
     * the conformance suite then an exception is thrown.
     *
     * @param serverName name of the conformance suite server
     * @param methodName name of the calling method
     * @return work pad for server
     * @throws InvalidParameterException null server name
     * @throws PropertyServerException problem with the service
     */
    private TechnologyUnderTestWorkPad   getWorkPad(String serverName,
                                                    String methodName) throws InvalidParameterException,
                                                                              PropertyServerException
    {
        ConformanceServicesInstance instance = validateServerName(serverName, methodName);

        if (instance != null)
        {
            return instance.getWorkPad();
        }
        else
        {
            ConformanceSuiteErrorCode errorCode    = ConformanceSuiteErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Validate that the server name is not null and retrieve an operational services instance for the server.
     * If no operational services instance is stored in the instance map it means this server is not running and
     * a new operational services instance is created.  The resulting operational services instance is returned.
     *
     * @param serverName  serverName passed on a request
     * @param methodName  method being called
     * @return OMAGOperationalServicesInstance object
     * @throws InvalidParameterException null server name
     */
    private ConformanceServicesInstance validateServerName(String serverName,
                                                           String methodName) throws InvalidParameterException
    {
        final String  serverNameParameter = "serverName";

        /*
         * If the local server name is still null then save the server name in the configuration.
         */
        if (serverName == null)
        {
            ConformanceSuiteErrorCode errorCode    = ConformanceSuiteErrorCode.NULL_LOCAL_SERVER_NAME;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                serverNameParameter);
        }
        else
        {
            return instanceMap.getInstance(serverName);
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    private void captureCheckedException(ConformanceServicesAPIResponse       response,
                                         ConformanceSuiteCheckedExceptionBase error,
                                         String                               exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(ConformanceServicesAPIResponse       response,
                                         ConformanceSuiteCheckedExceptionBase error,
                                         String                               exceptionClassName,
                                         Map<String, Object>                  exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(ConformanceServicesAPIResponse response,
                                                  InvalidParameterException      error)
    {
        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            Map<String, Object> exceptionProperties = new HashMap<>();

            exceptionProperties.put("parameterName", parameterName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void capturePropertyServerException(ConformanceServicesAPIResponse     response,
                                                PropertyServerException          error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }
}
