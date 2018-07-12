/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance;

import org.odpi.openmetadata.compliance.beans.ExceptionBean;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseResult;
import org.odpi.openmetadata.compliance.beans.OpenMetadataTestCaseSummary;
import org.odpi.openmetadata.compliance.ffdc.AssertionFailureException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * OpenMetadataTestCase is the superclass for an open metadata compliance test.  It manages the
 * test environment and reporting.
 */
public abstract class OpenMetadataTestCase
{
    protected String  testCaseId = null;
    protected String  testCaseName = null;
    protected String  testCaseDescription = null;

    protected boolean testRan = false;
    protected boolean testPassed = false;

    protected OpenMetadataTestCaseResult result = null;

    protected OMRSRepositoryConnector connector = null;


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
    public String getTestCaseDescription()
    {
        return testCaseDescription;
    }


    /**
     * Has the test case been ran yet?
     *
     * @return boolean flag
     */
    public boolean isTestRan()
    {
        return testRan;
    }


    /**
     * Has the test case passed? If it has not run then false is returned.
     *
     * @return boolean flag
     */
    public boolean isTestPassed()
    {
        return testPassed;
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
     * Set up the connector to the repository that is being tested.
     *
     * @param connector initialized and started OMRSRepositoryConnector object
     */
    public void setConnector(OMRSRepositoryConnector connector)
    {
        this.connector = connector;
    }


    /**
     * Request from the workbench to execute this test and generate a result object.
     */
    public void executeTest()
    {
        try
        {
            /*
             * Delegate to subclass
             */
            this.run();
        }
        catch (Throwable   exception)
        {
            this.testRan = true;
            this.testPassed = false;

            this.result = new OpenMetadataTestCaseResult(this);

            ExceptionBean              exceptionBean = new ExceptionBean();

            exceptionBean.setErrorMessage(exception.getMessage());
            exceptionBean.setExceptionClassName(exception.getClass().getName());

            this.result.setComplianceException(exceptionBean);
        }
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws AssertionFailureException something went wrong with the test.
     */
    protected abstract void run() throws AssertionFailureException;

}
