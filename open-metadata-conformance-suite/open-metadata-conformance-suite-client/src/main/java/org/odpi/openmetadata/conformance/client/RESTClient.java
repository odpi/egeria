/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.client;


import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTConfigurationException;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientFactory;
import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteErrorCode;
import org.odpi.openmetadata.conformance.rest.*;


/**
 * RESTClient is responsible for issuing calls to either the conformance suite or the technology under test.
 */
class RESTClient
{
    private RESTClientConnector clientConnector;        /* Initialized in constructor */


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws RESTConfigurationException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    RESTClient(String serverName,
               String serverPlatformURLRoot) throws RESTConfigurationException
    {
        final String  methodName = "RESTClient(no authentication)";

        RESTClientFactory  factory = new RESTClientFactory(serverName, serverPlatformURLRoot);

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception     error)
        {
            ConformanceSuiteErrorCode errorCode    = ConformanceSuiteErrorCode.NULL_LOCAL_SERVER_NAME;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, error.getMessage());


            throw new RESTConfigurationException(errorCode.getHTTPErrorCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 errorMessage,
                                                 errorCode.getSystemAction(),
                                                 errorCode.getUserAction(),
                                                 error);
        }
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws RESTConfigurationException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    RESTClient(String serverName,
               String serverPlatformURLRoot,
               String userId,
               String password) throws RESTConfigurationException
    {
        final String  methodName = "RESTClient(userId and password)";

        RESTClientFactory  factory = new RESTClientFactory(serverName,
                                                           serverPlatformURLRoot,
                                                           userId,
                                                           password);

        try
        {
            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception     error)
        {
            ConformanceSuiteErrorCode errorCode    = ConformanceSuiteErrorCode.NULL_LOCAL_SERVER_NAME;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, error.getMessage());


            throw new RESTConfigurationException(errorCode.getHTTPErrorCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 errorMessage,
                                                 errorCode.getSystemAction(),
                                                 errorCode.getUserAction(),
                                                 error);
        }
    }


    /**
     * Issue a GET REST call that returns the test results object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TestLabReportResponse
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    TestLabReportResponse callReportGetRESTCall(String    methodName,
                                                String    urlTemplate,
                                                Object... params) throws RESTServerException
    {
        return clientConnector.callGetRESTCall(methodName, TestLabReportResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns the test summary object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TestLabSummaryResponse
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    TestLabSummaryResponse callSummaryGetRESTCall(String    methodName,
                                                  String    urlTemplate,
                                                  Object... params) throws RESTServerException
    {
        return clientConnector.callGetRESTCall(methodName, TestLabSummaryResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns the list of profile names.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ProfileNameListResponse
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    ProfileNameListResponse callProfilesListREST(String    methodName,
                                                 String    urlTemplate,
                                                 Object... params) throws RESTServerException
    {
        return clientConnector.callGetRESTCall(methodName, ProfileNameListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns the details of a profile.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return ProfileReportResponse
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    ProfileReportResponse callProfileGetREST(String    methodName,
                                             String    urlTemplate,
                                             Object... params) throws RESTServerException
    {
        return clientConnector.callGetRESTCall(methodName, ProfileReportResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns the list of test cases.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TestCaseListResponse
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    TestCaseListResponse callTestCasesListREST(String    methodName,
                                               String    urlTemplate,
                                               Object... params) throws RESTServerException
    {
        return clientConnector.callGetRESTCall(methodName, TestCaseListResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns the details of a test case.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return TestCaseReportResponse
     * @throws RESTServerException something went wrong with the REST call stack.
     */
    TestCaseReportResponse callTestCaseGetREST(String    methodName,
                                               String    urlTemplate,
                                               Object... params) throws RESTServerException
    {
        return clientConnector.callGetRESTCall(methodName, TestCaseReportResponse.class, urlTemplate, params);
    }
}
