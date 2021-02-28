/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
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
public class OpenMetadataConformanceWorkbenchSummary implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String workbenchId               = null;
    private String workbenchName             = null;
    private String versionNumber             = null;
    private String workbenchDocumentationURL = null;

    private String tutType                   = null;
    private String tutName                   = null;

    private List<OpenMetadataConformanceProfileSummary> profileSummaries = null;


    /**
     * Default constructor - used when constructing from JSON.
     */
    public OpenMetadataConformanceWorkbenchSummary()
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
     * Return the version number of the workbench.
     *
     * @return string version
     */
    public String getVersionNumber()
    {
        return versionNumber;
    }


    /**
     * Set up the version number of the workbench running the tests.
     *
     * @param versionNumber string version number
     */
    public void setVersionNumber(String versionNumber)
    {
        this.versionNumber = versionNumber;
    }


    /**
     * Return the url to the workbench documentation.
     *
     * @return string url
     */
    public String getWorkbenchDocumentationURL()
    {
        return workbenchDocumentationURL;
    }


    /**
     * Set up the url to the workbench documentation.
     *
     * @param workbenchDocumentationURL url
     */
    public void setWorkbenchDocumentationURL(String workbenchDocumentationURL)
    {
        this.workbenchDocumentationURL = workbenchDocumentationURL;
    }


    /**
     * Return the technology under test type description.
     *
     * @return name
     */
    public String getTutType()
    {
        return tutType;
    }


    /**
     * Set up the technology under test type description.
     *
     * @param tutType name
     */
    public void setTutType(String tutType)
    {
        this.tutType = tutType;
    }


    /**
     * Return the technology under test name (eg server name).
     *
     * @return name
     */
    public String getTutName()
    {
        return tutName;
    }


    /**
     * Set up the technology under test name (eg server name).
     *
     * @param tutName name
     */
    public void setTutName(String tutName)
    {
        this.tutName = tutName;
    }


    /**
     * Return the indexed list of profile summaries.  There is an entry for each profile indexed
     * by the profileId.
     *
     * @return map of profile id to profile summaries
     */
    public List<OpenMetadataConformanceProfileSummary> getProfileSummaries()
    {
        return profileSummaries;
    }

    /**
     * Set up the indexed list of profile summaries.  There is an entry for each profile indexed
     * by the profileId.
     *
     * @param profileSummaries map of profile id to profile results
     */
    public void setProfileSummaries(List<OpenMetadataConformanceProfileSummary> profileSummaries)
    {
        this.profileSummaries = profileSummaries;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceWorkbenchSummary{" +
                "workbenchId='" + workbenchId + '\'' +
                ", workbenchName='" + workbenchName + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", workbenchDocumentationURL='" + workbenchDocumentationURL + '\'' +
                ", tutType='" + tutType + '\'' +
                ", tutName='" + tutName + '\'' +
                ", profileSummaries=" + profileSummaries +
                '}';
    }
}
