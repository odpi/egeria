/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataConformanceWorkbenchResults provides a bean for storing the results of an
 * Open Metadata Conformance Suite Workbench.  This includes the accumulated
 * results from each of the tests it runs within each profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataConformanceWorkbenchStatus
{
    private String workbenchId               = null;
    private String workbenchName             = null;

    private Boolean workbenchComplete        = false;

    //private String versionNumber             = null;
    //private String workbenchDocumentationURL = null;

    //private String tutType                   = null;
    //private String tutName                   = null;

    //private List<OpenMetadataConformanceProfileResults> profileResults = null;

    //private List<OpenMetadataTestCaseResult>  passedTestCases  = null;
    //private List<OpenMetadataTestCaseResult>  failedTestCases  = null;
    //private List<OpenMetadataTestCaseSummary> skippedTestCases = null;


    /**
     * Default constructor - used when constructing from JSON.
     */
    public OpenMetadataConformanceWorkbenchStatus()
    {
        super();
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
     * Set up the unique identifier of the workbench.
     *
     * @param workbenchId string id
     */
    public void setWorkbenchId(String workbenchId)
    {
        this.workbenchId = workbenchId;
    }


    /**
     * Return the name of the workbench
     *
     * @return string name
     */
    public String getWorkbenchName()
    {
        return workbenchName;
    }


    /**
     * Set up the name of the workbench
     *
     * @param workbenchName name
     */
    public void setWorkbenchName(String workbenchName)
    {
        this.workbenchName = workbenchName;
    }

    /**
     * Return the unique identifier of the workbench.
     *
     * @return boolean true/false - whether the workbench is complete
     */
    public Boolean getWorkbenchComplete()
    {
        return workbenchComplete;
    }


    /**
     * Set up the unique identifier of the workbench.
     *
     */
    public void setWorkbenchComplete()
    {
        this.workbenchComplete = true;
    }

    /**
     * Return the version number of the workbench.
     *
     * @return string version
     */
    //public String getVersionNumber()
    //{
    //    return versionNumber;
    //}


    /**
     * Set up the version number of the workbench running the tests.
     *
     * @param versionNumber string version number
     */
    //public void setVersionNumber(String versionNumber)
    //{
    //    this.versionNumber = versionNumber;
    //}


    /**
     * Return the url to the workbench documentation.
     *
     * @return string url
     */
    //public String getWorkbenchDocumentationURL()
    //{
    //    return workbenchDocumentationURL;
    //}


    /**
     * Set up the url to the workbench documentation.
     *
     * @param workbenchDocumentationURL url
     */
    //public void setWorkbenchDocumentationURL(String workbenchDocumentationURL)
    //{
    //    this.workbenchDocumentationURL = workbenchDocumentationURL;
    //}


    /**
     * Return the technology under test type description.
     *
     * @return name
     */
    //public String getTutType()
    //{
    //    return tutType;
    //}


    /**
     * Set up the technology under test type description.
     *
     * @param tutType name
     */
    //public void setTutType(String tutType)
    //{
    //    this.tutType = tutType;
    //}


    /**
     * Return the technology under test name (eg server name).
     *
     * @return name
     */
    //public String getTutName()
    //{
    //    return tutName;
    //}


    /**
     * Set up the technology under test name (eg server name).
     *
     * @param tutName name
     */
    //public void setTutName(String tutName)
    //{
    //    this.tutName = tutName;
    //}


    /**
     * Return the indexed list of profile results.  There is an entry for each profile indexed
     * by the profileId.
     *
     * @return map of profile id to profile results
     */
    //public List<OpenMetadataConformanceProfileResults> getProfileResults()
    //{
    //    return profileResults;
    //}


    /**
     * Set up the indexed list of profile results.  There is an entry for each profile indexed
     * by the profileId.
     *
     * @param profileResults map of profile id to profile results
     */
    //public void setProfileResults(List<OpenMetadataConformanceProfileResults> profileResults)
    //{
    //    this.profileResults = profileResults;
    //}


    /**
     * Return the number of test cases run.
     *
     * @return int count
     */
    //public int getTestCaseCount()
    //{
    //    return (this.countTestCaseResults(passedTestCases) +
    //           this.countTestCaseResults(failedTestCases) +
    //            this.countTestCaseSummaries(skippedTestCases));
    //}


    /**
     * Return the number of test cases passed.
     *
     * @return int count
     */
    //public int getTestPassCount()
    //{
    //    return super.countTestCaseResults(passedTestCases);
    //}


    /**
     * Return the number of test cases failed.
     *
     * @return int count
     */
    //public int getTestFailedCount()
    //{
    //    return super.countTestCaseResults(failedTestCases);
    //}


    /**
     * Return the number of test cases skipped.
     *
     * @return int count
     */
    //public int getTestSkippedCount()
    //{
    //    return super.countTestCaseSummaries(skippedTestCases);
    //}


    /**
     * Return the list of test cases that passed.
     *
     * @return list of test cases
     */
    //public List<OpenMetadataTestCaseResult> getPassedTestCases()
    //{
    //    return passedTestCases;
    //}


    /**
     * Set up the list of test cases that passed.
     *
     * @param passedTestCases list of test cases
     */
    //public void setPassedTestCases(List<OpenMetadataTestCaseResult> passedTestCases)
    //{
    //    this.passedTestCases = passedTestCases;
    //}


    /**
     * Return the list of test cases that failed.
     *
     * @return list of test cases
     */
    //public List<OpenMetadataTestCaseResult> getFailedTestCases()
    //{
    //    return failedTestCases;
    //}


    /**
     * Set up the list of test cases that failed.
     *
     * @param failedTestCases list of test cases
     */
    //public void setFailedTestCases(List<OpenMetadataTestCaseResult> failedTestCases)
    //{
    //    this.failedTestCases = failedTestCases;
    //}


    /**
     * Return the list of test cases that were not run due to an earlier failure.
     *
     * @return list of test cases
     */
    //public List<OpenMetadataTestCaseSummary> getSkippedTestCases()
    //{
    //    return skippedTestCases;
    //}


    /**
     * Set up the list of test cases that were not run sue to an earlier failure.
     *
     * @param skippedTestCases list of test cases
     */
    //public void setSkippedTestCases(List<OpenMetadataTestCaseSummary> skippedTestCases)
    //{
    //    this.skippedTestCases = skippedTestCases;
    //}


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceWorkbenchStatus{" +
                "workbenchId='" + workbenchId + '\'' +
                ", workbenchName='" + workbenchName + '\'' +
                ", workbenchComplete='" + workbenchComplete +
                '}';
    }
}
