/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTConfigurationException;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestLabResults;
import org.odpi.openmetadata.conformance.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.conformance.ffdc.exception.PropertyServerException;
import org.odpi.openmetadata.conformance.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.conformance.rest.TestLabReportResponse;

import java.io.File;

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
    private OpenMetadataConformanceTestLabResults getConformanceReport() throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String   methodName = "getConformanceReport";
        final String   urlTemplate = "/servers/{0}/open-metadata/conformance-suite/users/{1}/report";

        try
        {
            TestLabReportResponse restResult = restClient.callReportGetRESTCall(methodName,
                                                                                serverURLRoot + urlTemplate,
                                                                                serverName,
                                                                                testClientUserId);

            exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            return restResult.getTestLabResults();
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
     * Main method handles the parameters passed and then calls the registered workbenches.
     *
     * @param args first parameter is the URL root of the OMAG server platform where the conformance suite is running,
     *             second parameter is the server name of the conformance suite OMAG server and the third parameter
     *             is the name of the server to test.
     */
    public static void main(String[] args)
    {
        final String  resultsFileName = "openmetadata.conformance.testlab.results";
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

        System.out.println("=======================================");
        System.out.println(" Open Metadata Conformance Test Report ");
        System.out.println("=======================================");
        System.out.println(" ... contacting conformance suite server: " + serverName + " (" + serverURLRoot + ")");
        System.out.println();

        OpenMetadataConformanceTestLabResults testLabResults = null;

        try
        {
            OpenMetadataConformanceTestReport testLab;

            if (password == null)
            {
                testLab = new OpenMetadataConformanceTestReport(serverName, serverURLRoot, userId);
            }
            else
            {
                testLab = new OpenMetadataConformanceTestReport(serverName, serverURLRoot, userId, password);
            }

            testLabResults = testLab.getConformanceReport();
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

        if (testLabResults == null)
        {
            System.out.println("No results are available");
            System.exit(-1);
        }

        try
        {
            File         resultsStoreFile = new File(resultsFileName);
            ObjectMapper objectMapper     = new ObjectMapper();
            String       jsonString       = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testLabResults);

            FileUtils.writeStringToFile(resultsStoreFile, jsonString, (String)null,false);
        }
        catch (Throwable  exc)
        {
            System.out.println("The OpenMetadataConformanceTestReport failed to process results " + exc.getMessage());
            System.exit(-1);
        }

        System.out.println("Conformance report from server:  " + serverName);
        System.out.println();
        System.out.println("Number of tests: " + testLabResults.getTestCaseCount());
        System.out.println("Number of tests passed: " + testLabResults.getTestPassCount());
        System.out.println("Number of tests failed: " + testLabResults.getTestFailedCount());
        System.out.println("Number of tests skipped: " + testLabResults.getTestSkippedCount());
        System.out.println();

        if (testLabResults.getTestCaseCount() == testLabResults.getTestPassCount())
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
