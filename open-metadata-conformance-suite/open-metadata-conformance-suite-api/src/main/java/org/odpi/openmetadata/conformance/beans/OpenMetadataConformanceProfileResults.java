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
 * OpenMetadataConformanceProfileResults provides a detailed assessment of a technology's
 * conformance to an open metadata conformance profile, including all evidence collected.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataConformanceProfileResults
{
    private Integer                                                 id                 = 0;
    private String                                                  name               = null;
    private String                                                  description        = null;
    private String                                                  documentationURL   = null;
    private OpenMetadataConformanceProfilePriority                  profilePriority    = null;
    private OpenMetadataConformanceStatus                           conformanceStatus  = null;
    private List<OpenMetadataConformanceRequirementResults>         requirementResults = null;


    /**
     * Default constructor used in JSON exchange
     */
    public OpenMetadataConformanceProfileResults()
    {
    }


    /**
     * Standard constructor used during test run.
     *
     * @param id unique identifier use in indexing the results
     * @param name name of the profile
     * @param description description of the profile
     * @param documentationURL URL to the documentation of the profile
     * @param profilePriority mandatory or optional
     */
    public OpenMetadataConformanceProfileResults(Integer                                id,
                                                 String                                 name,
                                                 String                                 description,
                                                 String                                 documentationURL,
                                                 OpenMetadataConformanceProfilePriority profilePriority)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.documentationURL = documentationURL;
        this.profilePriority = profilePriority;
    }

    /**
     * Copy/clone constructor used in JSON exchange
     *
     * @param template object to copy
     */
    public OpenMetadataConformanceProfileResults(OpenMetadataConformanceProfileResults  template)
    {
        if (template != null)
        {
            this.id = template.getId();
            this.name = template.getName();
            this.description = template.getDescription();
            this.documentationURL = template.getDocumentationURL();
            this.profilePriority = template.getProfilePriority();
            this.conformanceStatus = template.getConformanceStatus();
            this.requirementResults = template.getRequirementResults();
        }
    }


    /**
     * Return the identifier of the profile.
     *
     * @return integer used to index the profile
     */
    public Integer getId()
    {
        return id;
    }


    /**
     * Set up the identifier of the profile.
     *
     * @param id integer used to index the profile
     */
    public void setId(Integer id)
    {
        this.id = id;
    }


    /**
     * Return the display name of the profile.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the display name of the profile.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the short description of the profile.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the short description of the profile.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the URL link to the profile.
     *
     * @return url
     */
    public String getDocumentationURL()
    {
        return documentationURL;
    }


    /**
     * Set up the URL link to the profile.
     *
     * @param documentationURL url
     */
    public void setDocumentationURL(String documentationURL)
    {
        this.documentationURL = documentationURL;
    }


    /**
     * Return the conformance status of the technology under test (TUT).
     *
     * @return status enum
     */
    public OpenMetadataConformanceStatus getConformanceStatus()
    {
        return conformanceStatus;
    }


    /**
     * Set up the conformance status of the technology under test (TUT).
     *
     * @param conformanceStatus status enum
     */
    public void setConformanceStatus(OpenMetadataConformanceStatus conformanceStatus)
    {
        this.conformanceStatus = conformanceStatus;
    }


    /**
     * Return the priority of the profile.
     *
     * @return priority enum
     */
    public OpenMetadataConformanceProfilePriority getProfilePriority()
    {
        return profilePriority;
    }


    /**
     * Set up the priority of the profile.
     *
     * @param profilePriority priority enum
     */
    public void setProfilePriority(OpenMetadataConformanceProfilePriority profilePriority)
    {
        this.profilePriority = profilePriority;
    }


    /**
     * Return detailed results, requirement by requirement.
     *
     * @return list of requirement results
     *
     */
    public List<OpenMetadataConformanceRequirementResults> getRequirementResults()
    {
        return requirementResults;
    }


    /**
     * Set up detailed results, requirement by requirement.
     *
     * @param requirementResults list of requirement results
     */
    public void setRequirementResults(List<OpenMetadataConformanceRequirementResults> requirementResults)
    {
        this.requirementResults = requirementResults;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceProfileResults{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", documentationURL='" + documentationURL + '\'' +
                ", profilePriority=" + profilePriority +
                ", conformanceStatus=" + conformanceStatus +
                ", requirementResults=" + requirementResults +
                '}';
    }
}
