/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance;

import org.odpi.openmetadata.compliance.beans.ExceptionBean;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseResult;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseSummary;
import org.odpi.openmetadata.compliance.ffdc.AssertionFailureException;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataTestCase is the superclass for an open metadata compliance test.  It manages the
 * test environment and reporting.
 */
public abstract class OpenMetadataTestCase
{
    private static final String  documentationRootURL = "https://odpi.github.io/egeria/open-metadata-compliance-suite/docs/";

    protected String  testCaseId;
    protected String  testCaseName;
    protected String  testCaseDescriptionURL;


    protected List<String>  successfulAssertions = new ArrayList<>();
    protected List<String>  unsuccessfulAssertions = new ArrayList<>();

    protected OpenMetadataTestCaseResult result = null;

    /**
     * Default constructor
     */
    public OpenMetadataTestCase()
    {
    }


    /**
     * Typical constructor
     *
     * @param workbenchId identifier of calling workbench
     * @param testCaseId identifier of test case
     * @param testCaseName name of test case
     */
    public OpenMetadataTestCase(String workbenchId,
                                String testCaseId,
                                String testCaseName)
    {
        this.testCaseId = testCaseId;
        this.testCaseName = testCaseName;
        this.testCaseDescriptionURL = documentationRootURL + workbenchId + "/" + testCaseId + "-test-case.md";
    }


    /**
     * Set up identifiers for test case.
     *
     * @param workbenchId identifier of calling workbench
     * @param testCaseId identifier of test case
     * @param testCaseName name of test case
     */
    protected void setTestCaseIds(String workbenchId,
                                  String testCaseId,
                                  String testCaseName)
    {
        this.testCaseId = testCaseId;
        this.testCaseName = testCaseName;
        this.testCaseDescriptionURL = documentationRootURL + workbenchId + "/" + testCaseId + "-test-case.md";
    }


    /**
     * Return the unique identifier of the test case.
     *
     * @return string id
     */
    public String getTestCaseId()
    {
        return testCaseId;
    }


    /**
     * Return the display name of the test case.
     *
     * @return string name
     */
    public String getTestCaseName()
    {
        return testCaseName;
    }


    /**
     * Return the URL of the description of the test case.
     *
     * @return string url
     */
    public String getTestCaseDescriptionURL()
    {
        return testCaseDescriptionURL;
    }


    /**
     * Has the test case been ran yet?
     *
     * @return boolean flag
     */
    public boolean isTestRan()
    {
        return ((! successfulAssertions.isEmpty()) || (! unsuccessfulAssertions.isEmpty()));
    }


    /**
     * Has the test case passed? If it has not run then false is returned.
     *
     * @return boolean flag
     */
    public boolean isTestPassed()
    {
        return (this.isTestRan() && (unsuccessfulAssertions.isEmpty()));
    }


    /**
     * Return the result object for this test case.  It will be null if the test case has not run.
     *
     * @return result bean (or null if the test has not yet been run).
     */
    public OpenMetadataTestCaseResult getResult()
    {
        return result;
    }


    /**
     * Return a summary bean for this test case.
     *
     * @return summary bean
     */
    public OpenMetadataTestCaseSummary getSummary()
    {
        return new OpenMetadataTestCaseSummary(this);
    }


    /**
     * Throw an exception if the condition is not true; else return
     *
     * @param condition condition to test
     * @param assertionId identifier for the assertion
     * @param assertionMessage descriptive message of the assertion
     * @throws AssertionFailureException condition was false
     */
    protected void assertCondition(boolean   condition,
                                   String    assertionId,
                                   String    assertionMessage) throws AssertionFailureException
    {
        if (condition)
        {
            successfulAssertions.add(assertionMessage);
            return;
        }

        unsuccessfulAssertions.add(assertionId + ": " + assertionMessage);
        throw new AssertionFailureException(assertionId, assertionMessage);
    }


    /**
     * Request from the workbench to execute this test and generate a result object.
     */
    public void executeTest()
    {
        result = new OpenMetadataTestCaseResult(this);

        try
        {
            /*
             * Delegate to subclass
             */
            this.run();
        }
        catch (AssertionFailureException   exception)
        {
        }
        catch (Throwable   exception)
        {
            this.unsuccessfulAssertions.add("test-case-base-01: Unexpected Exception " + exception.getClass().getSimpleName());

            ExceptionBean              exceptionBean = new ExceptionBean();

            exceptionBean.setErrorMessage(exception.getMessage());
            exceptionBean.setExceptionClassName(exception.getClass().getName());

            result.setComplianceException(exceptionBean);
        }

        result.setSuccessfulAssertions(successfulAssertions);
        result.setUnsuccessfulAssertions(unsuccessfulAssertions);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected abstract void run() throws Exception;

}
