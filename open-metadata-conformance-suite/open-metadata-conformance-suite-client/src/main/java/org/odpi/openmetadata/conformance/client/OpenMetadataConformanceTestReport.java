/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTConfigurationException;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.conformance.beans.*;
import org.odpi.openmetadata.conformance.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.conformance.ffdc.exception.PropertyServerException;
import org.odpi.openmetadata.conformance.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.conformance.rest.*;
import org.odpi.openmetadata.http.HttpHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * OpenMetadataConformanceTestReport provides the client to call an open metadata conformance suite server to
 * retrieve the results of its test.
 */
public class OpenMetadataConformanceTestReport
{
    private String     serverName;                        /* Initialized in constructor */
    private String     serverURLRoot;                     /* Initialized in constructor */
    private String     testClientUserId;                  /* Initialized in constructor */
    private RESTClient restClient;                        /* Initialized in constructor */

    private final ObjectMapper objectMapper;
    private RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    /**
     * Constructor to create a test lab object that is initialized with the server to test.
     *
     * @param serverName name of server to test.
     * @param serverURLRoot location of server to test.
     * @param testClientUserId userId for test
     * @throws RESTConfigurationException unable to configure the REST client
     */
    private OpenMetadataConformanceTestReport(String    serverName,
                                              String    serverURLRoot,
                                              String    testClientUserId) throws RESTConfigurationException
    {
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
        this.testClientUserId = testClientUserId;

        this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.restClient = new RESTClient(serverName, serverURLRoot);
    }


    /**
     * Constructor to create a test lab object that is initialized with the server to test.
     *
     * @param serverName name of server to test.
     * @param serverURLRoot location of server to test.
     * @param testClientUserId userId for test
     * @param testClientPassword password for userId
     * @throws RESTConfigurationException unable to configure the REST client
     */
    private OpenMetadataConformanceTestReport(String    serverName,
                                              String    serverURLRoot,
                                              String    testClientUserId,
                                              String    testClientPassword) throws RESTConfigurationException
    {
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
        this.testClientUserId = testClientUserId;

        this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.restClient = new RESTClient(serverName, serverURLRoot, testClientUserId, testClientPassword);
    }


    /**
     * Request each registered workbench runs its tests.
     *
     * @return combined results from all of the workbenches.
     * @throws InvalidParameterException the server to test is not a member of the cohort.
     * @throws PropertyServerException the conformance test server is not reachable.
     * @throws UserNotAuthorizedException the test user is not authorized to run the tests.
     */
    private OpenMetadataConformanceTestLabSummary getConformanceSummary() throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        final String   methodName = "getConformanceSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/conformance-suite/users/{1}/report/summary";

        try
        {
            TestLabSummaryResponse restResult = restClient.callSummaryGetRESTCall(methodName,
                    serverURLRoot + urlTemplate,
                    serverName,
                    testClientUserId);

            exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            return restResult.getTestLabSummary();
        }
        catch (RESTServerException   error)
        {
            throw new PropertyServerException(error.getReportedHTTPCode(),
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction(),
                    error);
        }
    }


    /**
     * Request list of executed profiles.
     *
     * @return list of profile names
     * @throws InvalidParameterException the server to test is not a member of the cohort.
     * @throws PropertyServerException the conformance test server is not reachable.
     * @throws UserNotAuthorizedException the test user is not authorized to run the tests.
     */
    private List<String> getProfileNames() throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        final String   methodName = "getProfileNames";
        final String   urlTemplate = "/servers/{0}/open-metadata/conformance-suite/users/{1}/report/profiles";

        try
        {
            ProfileNameListResponse restResult = restClient.callProfilesListREST(methodName,
                    serverURLRoot + urlTemplate,
                    serverName,
                    testClientUserId);

            exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            return restResult.getProfileNames();
        }
        catch (RESTServerException   error)
        {
            throw new PropertyServerException(error.getReportedHTTPCode(),
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction(),
                    error);
        }
    }


    /**
     * Request details of a profile's evidence.
     *
     * @param name of the profile
     * @return details of the profile's evidence
     * @throws InvalidParameterException the server to test is not a member of the cohort.
     * @throws PropertyServerException the conformance test server is not reachable.
     * @throws UserNotAuthorizedException the test user is not authorized to run the tests.
     */
    private OpenMetadataConformanceProfileResults getProfile(String name) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        final String   methodName = "getProfile";
        final String   urlTemplate = "/servers/{0}/open-metadata/conformance-suite/users/{1}/report/profiles/{2}";

        try
        {
            ProfileReportResponse restResult = restClient.callProfileGetREST(methodName,
                    serverURLRoot + urlTemplate,
                    serverName,
                    testClientUserId,
                    name);

            exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            return restResult.getProfileResult();
        }
        catch (RESTServerException   error)
        {
            throw new PropertyServerException(error.getReportedHTTPCode(),
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction(),
                    error);
        }
    }


    /**
     * Request list of executed test cases.
     *
     * @return list of test case IDs
     * @throws InvalidParameterException the server to test is not a member of the cohort.
     * @throws PropertyServerException the conformance test server is not reachable.
     * @throws UserNotAuthorizedException the test user is not authorized to run the tests.
     */
    private List<String> getTestCaseIds() throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        final String   methodName = "getTestCaseIds";
        final String   urlTemplate = "/servers/{0}/open-metadata/conformance-suite/users/{1}/report/test-cases";

        try
        {
            TestCaseListResponse restResult = restClient.callTestCasesListREST(methodName,
                    serverURLRoot + urlTemplate,
                    serverName,
                    testClientUserId);

            exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            return restResult.getTestCaseIds();
        }
        catch (RESTServerException   error)
        {
            throw new PropertyServerException(error.getReportedHTTPCode(),
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction(),
                    error);
        }
    }


    /**
     * Request details of a test case's evidence.
     *
     * @param id of the test case
     * @return details of the test case's evidence
     * @throws InvalidParameterException the server to test is not a member of the cohort.
     * @throws PropertyServerException the conformance test server is not reachable.
     * @throws UserNotAuthorizedException the test user is not authorized to run the tests.
     */
    private OpenMetadataTestCaseResult getTestCase(String id) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        final String   methodName = "getTestCase";
        final String   urlTemplate = "/servers/{0}/open-metadata/conformance-suite/users/{1}/report/test-cases/{2}";

        try
        {
            TestCaseReportResponse restResult = restClient.callTestCaseGetREST(methodName,
                    serverURLRoot + urlTemplate,
                    serverName,
                    testClientUserId,
                    id);

            exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            return restResult.getTestCaseResult();
        }
        catch (RESTServerException   error)
        {
            throw new PropertyServerException(error.getReportedHTTPCode(),
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction(),
                    error);
        }
    }


    private void writeJsonToFile(String path, Object value) throws IOException
    {
        String noSpaces = path.replaceAll("\\s+", "_");
        File file = new File(noSpaces);
        objectMapper.writeValue(file, value);
    }


    /**
     * Main method handles the parameters passed and then calls the registered workbenches.
     *
     * @param args first parameter is the URL root of the OMAG server platform where the conformance suite is running,
     *             second parameter is the server name of the conformance suite OMAG server and the third parameter
     *             is the name of the server to test.
     */
    public static void main(String[] args)
    {
        final String  resultsSummaryFileName = "openmetadata_cts_summary.json";
        final String  profileDetailsDirectory = "profile-details";
        final String  testCaseDetailsDirectory = "test-case-details";
        final String  defaultUserId = "ConformanceSuiteUser";

        if ((args == null) || (args.length < 2))
        {
            System.out.println("Please specify the conformance suite OMAG server's name in the first parameter and " +
                                       "its OMAG server platform's URL root in the second parameter");
            System.out.println("Optionally, the userId and password can be specified in the third and fourth parameter.");
            System.exit(-1);
        }

        String serverName = args[0];
        String serverURLRoot = args[1];
        String userId = defaultUserId;
        String password = null;

        if (args.length > 2)
        {
            userId = args[2];
        }

        if (args.length > 3)
        {
            password = args[3];
        }

        HttpHelper.noStrictSSLIfConfigured();

        System.out.println("=======================================");
        System.out.println(" Open Metadata Conformance Test Report ");
        System.out.println("=======================================");
        System.out.println("Contacting conformance suite server: " + serverName + " (" + serverURLRoot + ")");
        System.out.println();

        OpenMetadataConformanceTestLabSummary testLabSummary = null;
        List<String> profileNames = null;
        List<String> testCaseIds  = null;

        OpenMetadataConformanceTestReport testLab = null;
        try
        {

            if (password == null)
            {
                testLab = new OpenMetadataConformanceTestReport(serverName, serverURLRoot, userId);
            }
            else
            {
                testLab = new OpenMetadataConformanceTestReport(serverName, serverURLRoot, userId, password);
            }

            testLabSummary = testLab.getConformanceSummary();
            profileNames = testLab.getProfileNames();
            testCaseIds  = testLab.getTestCaseIds();
        }
        catch (RESTConfigurationException error)
        {
            System.out.println("Unable to issue calls to the conformance suite server " + serverName + " at " + serverURLRoot);
            System.out.println("Returned error message is " + error.getErrorMessage() + ".");
            System.out.println();
        }
        catch (InvalidParameterException  error)
        {
            System.out.println("The server " + serverName + " is not a conformance suite server");
            System.out.println("Returned error message is " + error.getErrorMessage() + ".");
            System.out.println();
        }
        catch (PropertyServerException  error)
        {
            System.out.println("The OMAG server platform at " + serverURLRoot + " is not contactable or the conformance suite server " + serverName + " has not been started.");
            System.out.println("Returned error message is " + error.getErrorMessage() + ".");
            System.out.println();
        }
        catch (UserNotAuthorizedException  error)
        {
            System.out.println("The userId " + userId + " is not authorized to call the conformance suite tests.");
            System.out.println("Returned error message is " + error.getErrorMessage() + ".");
            System.out.println();
        }

        if (testLabSummary == null)
        {
            System.out.println("No results are available");
            System.exit(-1);
        }

        // Write out the summary information
        try
        {
            testLab.writeJsonToFile(resultsSummaryFileName, testLabSummary);
        }
        catch (Exception  exc)
        {
            System.out.println("The OpenMetadataConformanceTestReport failed to process results " + exc.getMessage());
            System.exit(-1);
        }

        // Write out the profile details
        if (profileNames != null)
        {
            try
            {
                System.out.println("Saving full profile details into '" + profileDetailsDirectory + "' directory...");
                Files.createDirectories(Paths.get(profileDetailsDirectory));
                System.out.println("Summary of profile results:");
                for (String profileName : profileNames)
                {
                    OpenMetadataConformanceProfileResults details = testLab.getProfile(profileName);
                    if (details != null)
                    {
                        OpenMetadataConformanceStatus status = details.getConformanceStatus();
                        System.out.println("... " + profileName + ": " + (status == null ? "<null>" : status.name()));
                        String path = profileDetailsDirectory + File.separator + profileName + ".json";
                        testLab.writeJsonToFile(path, details);
                    }
                    else
                    {
                        System.out.println("... " + profileName + ": <unknown>");
                    }
                }
            }
            catch (Exception exc)
            {
                System.out.println("The OpenMetadataConformanceTestReport failed to process the profile details " + exc.getMessage());
                System.exit(-1);
            }
        }

        int totalTestCases   = 0;
        int passedTestCases  = 0;
        int failedTestCases  = 0;
        int skippedTestCases = 0;
        // Write out the test case details
        if (testCaseIds != null)
        {
            try
            {
                System.out.println("Saving full test case details into '" + testCaseDetailsDirectory + "' directory (can take 1-2 minutes)...");
                Files.createDirectories(Paths.get(testCaseDetailsDirectory));
                for (String testCaseId : testCaseIds)
                {
                    // for each test case retrieved, increment the total number of tests
                    totalTestCases++;
                    OpenMetadataTestCaseResult details = testLab.getTestCase(testCaseId);
                    if (details != null)
                    {
                        if (!details.getUnsuccessfulAssertions().isEmpty())
                        {
                            // if 'unsuccessfulAssertions' is not empty, increment failed test count
                            failedTestCases++;
                        }
                        else if (!details.getSuccessfulAssertions().isEmpty() || !details.getNotSupportAssertions().isEmpty())
                        {
                            // else if 'successfulAssertions' or 'notSupportedAssertions' are not empty, increment passed test count
                            passedTestCases++;
                        }
                        String path = testCaseDetailsDirectory + File.separator + testCaseId + ".json";
                        testLab.writeJsonToFile(path, details);
                    }
                }
            }
            catch (Exception exc)
            {
                System.out.println("The OpenMetadataConformanceTestReport failed to process the test case details " + exc.getMessage());
                System.exit(-1);
            }
        }

        System.out.println("Summary:");
        System.out.println("... number of tests: " + totalTestCases);
        System.out.println("... number of tests passed: " + passedTestCases);
        System.out.println("... number of tests failed: " + failedTestCases);
        System.out.println("... number of tests skipped: " + skippedTestCases);
        System.out.println();

        if (totalTestCases == passedTestCases)
        {
            System.out.println("Congratulations, technology under test is conformant");
            System.exit(0);
        }
        else
        {
            System.out.println("Technology under test is not yet conformant");
            System.exit(1);
        }
    }
}
