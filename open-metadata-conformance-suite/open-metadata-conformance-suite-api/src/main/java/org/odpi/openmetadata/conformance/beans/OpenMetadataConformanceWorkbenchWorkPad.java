/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteErrorCode;
import org.odpi.openmetadata.conformance.ffdc.exception.ConformanceSuiteRuntimeException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.*;

/**
 * OpenMetadataConformanceWorkbenchWorkPad provides the super type for the work pad used by each of the conformance workbenches.
 */
public abstract class OpenMetadataConformanceWorkbenchWorkPad
{
    protected String              workbenchId;
    protected String              workbenchName;
    protected String              workbenchVersionNumber;
    protected String              workbenchDocURL;
    protected String              localServerUserId;
    protected String              localServerPassword;
    protected String              tutName;
    protected String              tutType;
    protected int                 maxPageSize;

    protected boolean             workbenchComplete;

    protected List<OpenMetadataConformanceTestEvidence>  testEvidenceList = new ArrayList<>();

    protected Map<String, OpenMetadataTestCase>    testCaseMap = new HashMap<>();

    /*
     * Add time of last received event - this is to help with determining whether workbench has completed
     */
    protected Date lastActive = new Date();
    private static final long DELAY_TIME = 10000L;  // 10 seconds - time to wait after activity has quiesced before reporting completion

    /**
     * Constructor takes properties that are common to all work pads.
     *
     * @param workbenchId unique identifier of the workbench.
     * @param workbenchName display name for the workbench.
     * @param workbenchVersionNumber version number of the workbench.
     * @param workbenchDocURL link to documentation for the workbench.
     * @param localServerUserId local server's userId
     * @param localServerPassword local server's password
     * @param tutType type of technology to test
     * @param maxPageSize maximum number of results to return on any one call.
     */
    public OpenMetadataConformanceWorkbenchWorkPad(String       workbenchId,
                                                   String       workbenchName,
                                                   String       workbenchVersionNumber,
                                                   String       workbenchDocURL,
                                                   String       localServerUserId,
                                                   String       localServerPassword,
                                                   String       tutType,
                                                   int          maxPageSize)
    {
        this.workbenchId = workbenchId;
        this.workbenchName = workbenchName;
        this.workbenchVersionNumber = workbenchVersionNumber;
        this.workbenchDocURL = workbenchDocURL;
        this.localServerUserId = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.tutType = tutType;
        this.maxPageSize = maxPageSize;
        this.workbenchComplete = false;
    }


    /**
     * Return the unique identifier of the workbench.
     *
     * @return string id
     */
    public String getWorkbenchId()
    {
        return workbenchId;
    }


    /**
     * Return the name of this workbench.
     *
     * @return name
     */
    public String getWorkbenchName()
    {
        return workbenchName;
    }


    /**
     * Return the version number that determines the conformance level.
     *
     * @return string version number
     */
    public String getWorkbenchVersionNumber()
    {
        return workbenchVersionNumber;
    }


    /**
     * Return the URL to the documentation for this workbench.
     *
     * @return url
     */
    public String getWorkbenchDocURL()
    {
        return workbenchDocURL;
    }


    /**
     * Return the userId for this server.
     *
     * @return userId
     */
    public String getLocalServerUserId()
    {
        return localServerUserId;
    }

    /**
     * Return the completion state of the workbench.
     *
     * @return boolean - whether workbench is complete
     */
    public OpenMetadataConformanceWorkbenchStatus getWorkbenchStatus()
    {
        OpenMetadataConformanceWorkbenchStatus status = new OpenMetadataConformanceWorkbenchStatus();
        status.setWorkbenchId(workbenchId);
        if (workbenchComplete) {
            /*
             * The synchronous tests are complete, but check whether the workbench has quiesced.
             */
            Date now = new Date();
            long millis = now.getTime() - lastActive.getTime();
            if (millis >= DELAY_TIME)
            {
                status.setWorkbenchComplete();
            }

        }
        return status;
    }

    /**
     * Set the completion state of the workbench to true.
     * This signifies that the synchronous portion of the tests have completed.
     * The workbench may still be processing asynchronous events which trigger further tests.
     */
    public void setWorkbenchComplete()
    {
        this.workbenchComplete = true;
    }


    /**
     * Return the password for this server.
     *
     * @return password
     */
    public String getLocalServerPassword()
    {
        return localServerPassword;
    }


    /**
     * Return the maximum number of records that should be returned on a REST call.
     *
     * @return int
     */
    public int getMaxPageSize()
    {
        return maxPageSize;
    }


    /**
     * Register this test case with the work pad so that its results can be harvested.
     *
     * @param testCase test case object.
     */
    synchronized void  registerTestCase(OpenMetadataTestCase     testCase)
    {
        final String methodName = "registerTestCase";
        OpenMetadataTestCase   duplicateTestCase = testCaseMap.put(testCase.getTestCaseId(), testCase);

        if (duplicateTestCase != null)
        {
            Map<String, Object> relatedProperties = new HashMap<>();
            relatedProperties.put("Existing TestCase", duplicateTestCase);
            relatedProperties.put("New TestCase", testCase);

            throw new ConformanceSuiteRuntimeException(ConformanceSuiteErrorCode.DUPLICATE_TEST_CASE.getMessageDefinition(tutName,
                                                                                                                          tutType,
                                                                                                                          testCase.getTestCaseId()),
                                                       this.getClass().getName(),
                                                       methodName,
                                                       relatedProperties);
        }
    }


    /**
     * Retrieve the set of test case IDs registered with this work pad.
     *
     * @return the test case IDs
     */
    synchronized Set<String> getTestCaseIds()
    {
        return new TreeSet<>(testCaseMap.keySet());
    }


    /**
     * Accumulate the set of profile names registered with this work pad.
     *
     * @return the profile names
     */
    public abstract List<String> getProfileNames();


    /**
     * Accumulate the evidences for a given profile.
     *
     * @param profileName for which to obtain the detailed results
     * @return the test evidence organized by profile and requirement within profile
     */
    public abstract OpenMetadataConformanceProfileResults getProfileResults(String profileName);


    /**
     * Accumulate the evidences for each profile
     *
     * @return the test evidence organized by profile and requirement within profile
     */
    public abstract List<OpenMetadataConformanceProfileResults> getProfileResults();


    /**
     * Accumulate the summarized evidences for each profile
     *
     * @return the summarized test evidence organized by profile and requirement within profile
     */
    public abstract List<OpenMetadataConformanceProfileSummary> getProfileSummaries();


    /**
     * Calculate the status of a profile or requirement based on the test evidence.
     *
     * @param positiveTestEvidence positive test evidence
     * @param negativeTestEvidence negative test evidence
     * @param unsupportedTestEvidence evidence where technology correctly reported it does not support a function.
     *
     * @return OpenMetadataConformanceStatus enum
     */
    private   OpenMetadataConformanceStatus getStatusFromEvidence(List<OpenMetadataConformanceTestEvidence> positiveTestEvidence,
                                                                  List<OpenMetadataConformanceTestEvidence> negativeTestEvidence,
                                                                  List<OpenMetadataConformanceTestEvidence> unsupportedTestEvidence)
    {
        OpenMetadataConformanceStatus status;

        if ((positiveTestEvidence == null || positiveTestEvidence.isEmpty()) &&
            (negativeTestEvidence == null || negativeTestEvidence.isEmpty()) &&
            (unsupportedTestEvidence == null || unsupportedTestEvidence.isEmpty()))
        {
            /*
             * No test evidence available.
             */
            status = OpenMetadataConformanceStatus.UNKNOWN_STATUS;
        }
        else
        {
            if (! (negativeTestEvidence == null || negativeTestEvidence.isEmpty()))
            {
                /*
                 * Any negative evidence means not conformant
                 */
                status = OpenMetadataConformanceStatus.NOT_CONFORMANT;
            }
            else if (! (positiveTestEvidence == null || positiveTestEvidence.isEmpty()))
            {
                /*
                 * Some positive evidence means something is working.
                 */
                if (! (unsupportedTestEvidence == null || unsupportedTestEvidence.isEmpty()))
                {
                    /*
                     * The technology under test is correctly reporting it does not support something.
                     */
                    status = OpenMetadataConformanceStatus.CONFORMANT_PARTIAL_SUPPORT;
                }
                else
                {
                    /*
                     * Looks like there is no evidence to say that something is missing, failing or not supported.
                     */
                    status = OpenMetadataConformanceStatus.CONFORMANT_FULL_SUPPORT;
                }
            }
            else
            {
                /*
                 * The only evidence is correct self-reporting of unsupported function.
                 */
                status = OpenMetadataConformanceStatus.CONFORMANT_NO_SUPPORT;
            }
        }

        return status;
    }


    /**
     * From the test evidence list, set up the positive and negative evidence lists and return the conformance status
     * based on the evidence.
     *
     * @param testEvidenceList list of evidence
     * @param positiveTestEvidence extracted positive evidence
     * @param negativeTestEvidence extracted negative evidence
     * @return conformance status
     */
    protected OpenMetadataConformanceStatus processEvidence(List<OpenMetadataConformanceTestEvidence> testEvidenceList,
                                                            List<OpenMetadataConformanceTestEvidence> positiveTestEvidence,
                                                            List<OpenMetadataConformanceTestEvidence> negativeTestEvidence)
    {
        List<OpenMetadataConformanceTestEvidence> unsupportedTestEvidence = new ArrayList<>();
        OpenMetadataConformanceStatus             status;

        if (testEvidenceList != null)
        {
            for (OpenMetadataConformanceTestEvidence testEvidenceItem : testEvidenceList)
            {
                if (testEvidenceItem != null)
                {
                    OpenMetadataConformanceTestEvidenceType testEvidenceType = testEvidenceItem.getTestEvidenceType();

                    if (testEvidenceType != null)
                    {
                        switch (testEvidenceType)
                        {
                            case NO_DATA_AVAILABLE:
                                /* ignore */
                                break;

                            case SUCCESSFUL_ASSERTION:
                            case DISCOVERED_PROPERTY:
                                positiveTestEvidence.add(testEvidenceItem);
                                break;

                            case UNEXPECTED_EXCEPTION:
                            case UNSUCCESSFUL_ASSERTION:
                                negativeTestEvidence.add(testEvidenceItem);
                                break;

                            case NOT_SUPPORTED_FUNCTION:
                                unsupportedTestEvidence.add(testEvidenceItem);
                                break;

                        }
                    }
                }
            }
        }

        status = getStatusFromEvidence(positiveTestEvidence,
                                       negativeTestEvidence,
                                       unsupportedTestEvidence);

        if (positiveTestEvidence != null)
        {
            positiveTestEvidence.addAll(unsupportedTestEvidence);
        }

        return status;
    }


    /**
     * Log that a test case has reported that a condition has been met for a specific requirement.
     *
     * @param profileId profile for the requirement
     * @param requirementId identifier of the requirement
     * @param testCaseId identifier of the reporting test case
     * @param testCaseName name of the reporting test case
     * @param testCaseDocumentationURL link to the test case documentation.
     * @param assertionId details of the assertion
     * @param methodName the method that the condition tests
     * @param elapsedTime of the test executing (in milliseconds)
     */
    public synchronized void addSuccessfulCondition(Integer  profileId,
                                                    Integer  requirementId,
                                                    String   testCaseId,
                                                    String   testCaseName,
                                                    String   testCaseDocumentationURL,
                                                    String   assertionId,
                                                    String   methodName,
                                                    Long     elapsedTime)
    {
        OpenMetadataConformanceTestEvidence  testEvidence = new OpenMetadataConformanceTestEvidence();

        testEvidence.setProfileId(profileId);
        testEvidence.setRequirementId(requirementId);
        testEvidence.setTestCaseId(testCaseId);
        testEvidence.setTestCaseName(testCaseName);
        testEvidence.setTestCaseDescriptionURL(testCaseDocumentationURL);
        testEvidence.setAssertionId(assertionId);
        testEvidence.setTestEvidenceType(OpenMetadataConformanceTestEvidenceType.SUCCESSFUL_ASSERTION);
        if (methodName != null)
        {
            testEvidence.setMethodName(methodName);
        }
        if (elapsedTime != null)
        {
            testEvidence.setElapsedTime(elapsedTime);
        }

        testEvidenceList.add(testEvidence);
    }


    /**
     * Log that a test case has reported that a condition has not been met for a specific requirement.
     *
     * @param profileId profile for the requirement
     * @param requirementId identifier of the requirement
     * @param testCaseId identifier of the reporting test case
     * @param testCaseName name of the reporting test case
     * @param testCaseDocumentationURL link to the test case documentation.
     * @param assertionId details of the assertion
     * @param methodName the method that the condition tests
     * @param elapsedTime of the test executing (in milliseconds)
     */
    public synchronized void addUnsuccessfulCondition(Integer  profileId,
                                                      Integer  requirementId,
                                                      String   testCaseId,
                                                      String   testCaseName,
                                                      String   testCaseDocumentationURL,
                                                      String   assertionId,
                                                      String   methodName,
                                                      Long     elapsedTime)
    {
        OpenMetadataConformanceTestEvidence  testEvidence = new OpenMetadataConformanceTestEvidence();

        testEvidence.setProfileId(profileId);
        testEvidence.setRequirementId(requirementId);
        testEvidence.setTestCaseId(testCaseId);
        testEvidence.setTestCaseName(testCaseName);
        testEvidence.setTestCaseDescriptionURL(testCaseDocumentationURL);
        testEvidence.setAssertionId(assertionId);
        testEvidence.setTestEvidenceType(OpenMetadataConformanceTestEvidenceType.UNSUCCESSFUL_ASSERTION);
        if (methodName != null)
        {
            testEvidence.setMethodName(methodName);
        }
        if (elapsedTime != null)
        {
            testEvidence.setElapsedTime(elapsedTime);
        }

        testEvidenceList.add(testEvidence);
    }


    /**
     * Log that a test case has reported the correct response to a non-supported function.
     *
     * @param profileId profile for the requirement
     * @param requirementId identifier of the requirement
     * @param testCaseId identifier of the reporting test case
     * @param testCaseName name of the reporting test case
     * @param testCaseDocumentationURL link to the test case documentation.
     * @param assertionId details of the assertion
     */
    public synchronized void addNotSupportedCondition(Integer  profileId,
                                                      Integer  requirementId,
                                                      String   testCaseId,
                                                      String   testCaseName,
                                                      String   testCaseDocumentationURL,
                                                      String   assertionId)
    {
        OpenMetadataConformanceTestEvidence  testEvidence = new OpenMetadataConformanceTestEvidence();

        testEvidence.setProfileId(profileId);
        testEvidence.setRequirementId(requirementId);
        testEvidence.setTestCaseId(testCaseId);
        testEvidence.setTestCaseName(testCaseName);
        testEvidence.setTestCaseDescriptionURL(testCaseDocumentationURL);
        testEvidence.setAssertionId(assertionId);
        testEvidence.setTestEvidenceType(OpenMetadataConformanceTestEvidenceType.NOT_SUPPORTED_FUNCTION);

        testEvidenceList.add(testEvidence);
    }


    /**
     * Log a property discovered by a test case.
     *
     * @param profileId profile for the requirement
     * @param requirementId identifier of the requirement
     * @param testCaseId identifier of the reporting test case
     * @param testCaseName name of the reporting test case
     * @param testCaseDocumentationURL link to the test case documentation.
     * @param propertyName name of the property
     * @param propertyValue value of the property
     */
    public synchronized void  addDiscoveredProperty(Integer  profileId,
                                                    Integer  requirementId,
                                                    String   testCaseId,
                                                    String   testCaseName,
                                                    String   testCaseDocumentationURL,
                                                    String   propertyName,
                                                    Object   propertyValue)
    {
        OpenMetadataConformanceTestEvidence  testEvidence = new OpenMetadataConformanceTestEvidence();

        testEvidence.setProfileId(profileId);
        testEvidence.setRequirementId(requirementId);
        testEvidence.setTestCaseId(testCaseId);
        testEvidence.setTestCaseName(testCaseName);
        testEvidence.setTestCaseDescriptionURL(testCaseDocumentationURL);
        testEvidence.setPropertyName(propertyName);
        testEvidence.setPropertyValue(propertyValue);
        testEvidence.setTestEvidenceType(OpenMetadataConformanceTestEvidenceType.DISCOVERED_PROPERTY);

        testEvidenceList.add(testEvidence);
    }


    /**
     * Log that an unexpected exception occurred during the test run.  This will halt the test.
     *
     * @param profileId profile for the requirement
     * @param requirementId identifier of the requirement
     * @param testCaseId identifier of the reporting test case
     * @param testCaseName name of the reporting test case
     * @param testCaseDocumentationURL link to the test case documentation.
     * @param assertionId message associated with the exception.
     * @param exception exception
     */
    synchronized void  addUnexpectedException(Integer              profileId,
                                              Integer              requirementId,
                                              String               testCaseId,
                                              String               testCaseName,
                                              String               testCaseDocumentationURL,
                                              String               assertionId,
                                              ExceptionBean        exception)
    {
        OpenMetadataConformanceTestEvidence  testEvidence = new OpenMetadataConformanceTestEvidence();

        testEvidence.setProfileId(profileId);
        testEvidence.setRequirementId(requirementId);
        testEvidence.setTestCaseId(testCaseId);
        testEvidence.setTestCaseName(testCaseName);
        testEvidence.setTestCaseDescriptionURL(testCaseDocumentationURL);
        testEvidence.setAssertionId(assertionId);
        testEvidence.setConformanceException(exception);
        testEvidence.setTestEvidenceType(OpenMetadataConformanceTestEvidenceType.UNEXPECTED_EXCEPTION);

        testEvidenceList.add(testEvidence);
    }


    /**
     * Return the results from a single test case.
     *
     * @param testCaseId identifier of the test case of interest
     * @return test case results
     * @throws InvalidParameterException unknown test case id
     */
    synchronized  OpenMetadataTestCaseResult   getTestCaseResult(String  testCaseId) throws InvalidParameterException
    {
        final String   methodName    = "getTestCaseReport";
        final String   parameterName = "testCaseId";

        OpenMetadataTestCase  testCase = testCaseMap.get(testCaseId);

        if (testCase != null)
        {
            return testCase.getResult();
        }

        throw new InvalidParameterException(ConformanceSuiteErrorCode.UNKNOWN_TEST_CASE_ID.getMessageDefinition(testCaseId),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }


    /**
     * Return a list of failed test cases detected by this workbench.
     *
     * @return failed test cases
     */
    synchronized List<OpenMetadataTestCaseResult>  getFailedTestCases()
    {
        List<OpenMetadataTestCaseResult> failedTestCases = new ArrayList<>();

        Collection<OpenMetadataTestCase> allTestCases = testCaseMap.values();

        for (OpenMetadataTestCase   testCase : allTestCases)
        {
            if (testCase != null)
            {
                if (testCase.isTestRan() && ! testCase.isTestPassed())
                {
                    failedTestCases.add(testCase.getResult());
                }
            }
        }

        if (failedTestCases.isEmpty())
        {
            return null;
        }
        else
        {
            return failedTestCases;
        }
    }

    /**
     * Return the results determined so far by the workbench.
     *
     * @return workbench results object
     */
    synchronized OpenMetadataConformanceWorkbenchResults getWorkbenchResults()
    {
        OpenMetadataConformanceWorkbenchResults workbenchResults = new OpenMetadataConformanceWorkbenchResults();

        workbenchResults.setWorkbenchId(workbenchId);
        workbenchResults.setWorkbenchName(workbenchName);
        workbenchResults.setVersionNumber(workbenchVersionNumber);
        workbenchResults.setTutName(tutName);
        workbenchResults.setTutType(tutType);

        workbenchResults.setProfileResults(getProfileResults());

        /*
         * Work through the test cases and extract the results
         */
        List<OpenMetadataTestCaseResult>  passedTestCases  = new ArrayList<>();
        List<OpenMetadataTestCaseResult>  failedTestCases  = new ArrayList<>();
        List<OpenMetadataTestCaseSummary> skippedTestCases = new ArrayList<>();

        /*
         * Executing tests only if there is a repository connector
         */
        for (OpenMetadataTestCase testCase : testCaseMap.values())
        {
            if (testCase.isTestRan())
            {
                if (testCase.isTestPassed())
                {
                    passedTestCases.add(testCase.getResult());
                }
                else
                {
                    failedTestCases.add(testCase.getResult());
                }
            }
            else
            {
                skippedTestCases.add(testCase.getSummary());
            }
        }

        if (! passedTestCases.isEmpty())
        {
            workbenchResults.setPassedTestCases(passedTestCases);
        }
        if (! failedTestCases.isEmpty())
        {
            workbenchResults.setFailedTestCases(failedTestCases);
        }
        if (! skippedTestCases.isEmpty())
        {
            workbenchResults.setSkippedTestCases(skippedTestCases);
        }

        return workbenchResults;
    }

    /**
     * Return the summarized results determined so far by the workbench.
     *
     * @return workbench summary results object
     */
    synchronized OpenMetadataConformanceWorkbenchSummary getWorkbenchSummary()
    {
        OpenMetadataConformanceWorkbenchSummary workbenchSummary = new OpenMetadataConformanceWorkbenchSummary();

        workbenchSummary.setWorkbenchId(workbenchId);
        workbenchSummary.setWorkbenchName(workbenchName);
        workbenchSummary.setVersionNumber(workbenchVersionNumber);
        workbenchSummary.setTutName(tutName);
        workbenchSummary.setTutType(tutType);

        workbenchSummary.setProfileSummaries(getProfileSummaries());

        return workbenchSummary;
    }


    /**
     *  Register any activity to help with working out when the workbench has quiesced.
     *  Any kind of activity is significant, especially if beyond the end of the synchronous tests.
     */
    public void registerActivity()
    {
        /*
         * Record the current date and time...
         */
        lastActive = new Date();
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceWorkbenchWorkPad{" +
                "workbenchId='" + workbenchId + '\'' +
                ", workbenchName='" + workbenchName + '\'' +
                ", workbenchVersionNumber='" + workbenchVersionNumber + '\'' +
                ", workbenchDocURL='" + workbenchDocURL + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", localServerPassword='" + localServerPassword + '\'' +
                ", tutName='" + tutName + '\'' +
                ", tutType='" + tutType + '\'' +
                ", maxPageSize=" + maxPageSize +
                ", testEvidenceList=" + testEvidenceList +
                ", testCaseMap=" + testCaseMap +
                ", failedTestCases=" + getFailedTestCases() +
                ", workbenchResults=" + getWorkbenchResults() +
                '}';
    }
}
