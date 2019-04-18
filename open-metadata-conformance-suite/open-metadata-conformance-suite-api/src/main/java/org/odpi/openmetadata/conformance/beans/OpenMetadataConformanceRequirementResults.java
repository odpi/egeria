/* SPDX-License-Identifier: Apache 2.0 */
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
 * OpenMetadataConformanceRequirementResults documents the conformance assessment of the technology under test
 * for one of the requirements specified for a workbench's profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataConformanceRequirementResults implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private Integer                                   id                   = null;
    private String                                    name                 = null;
    private String                                    description          = null;
    private String                                    documentationURL     = null;
    private OpenMetadataConformanceStatus             conformanceStatus    = null;
    private List<OpenMetadataConformanceTestEvidence> positiveTestEvidence = null;
    private List<OpenMetadataConformanceTestEvidence> negativeTestEvidence = null;


    /**
     * Default constructor used in JSON exchange
     */
    public OpenMetadataConformanceRequirementResults()
    {
    }


    /**
     * Standard constructor used during test run.
     *
     * @param id ui
     * @param name name of the requirement.
     * @param description description of the requirement.
     * @param documentationURL URL link to more information.
     */
    public OpenMetadataConformanceRequirementResults(Integer    id,
                                                     String     name,
                                                     String     description,
                                                     String     documentationURL)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.documentationURL = documentationURL;
    }


    /**
     * Copy/clone constructor used in JSON exchange
     */
    public OpenMetadataConformanceRequirementResults(OpenMetadataConformanceRequirementResults  template)
    {
        if (template != null)
        {
            this.id = template.getId();
            this.name = template.getName();
            this.description = template.getDescription();
            this.documentationURL = template.getDocumentationURL();
            this.conformanceStatus = template.getConformanceStatus();
            this.positiveTestEvidence = template.getPositiveTestEvidence();
            this.negativeTestEvidence = template.getNegativeTestEvidence();
        }
    }


    /**
     * Return the identifier of the requirement.
     *
     * @return integer used to index the requirement
     */
    public Integer getId()
    {
        return id;
    }


    /**
     * Set up the identifier of the requirement.
     *
     * @param id integer used to index the requirement
     */
    public void setId(Integer id)
    {
        this.id = id;
    }


    /**
     * Return the display name of the requirement.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the display name of the requirement.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the short description of the requirement.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the short description of the requirement.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the URL link to the requirement.
     *
     * @return url
     */
    public String getDocumentationURL()
    {
        return documentationURL;
    }


    /**
     * Set up the URL link to the requirement.
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
     * Return the list of test evidence that demonstrates the technology under test is
     * conformant.
     *
     * @return list of test evidence
     */
    public List<OpenMetadataConformanceTestEvidence> getPositiveTestEvidence()
    {
        return positiveTestEvidence;
    }


    /**
     * Set up the list of test evidence that demonstrates the technology under test is
     * conformant.
     *
     * @param positiveTestEvidence list of test evidence
     */
    public void setPositiveTestEvidence(List<OpenMetadataConformanceTestEvidence> positiveTestEvidence)
    {
        this.positiveTestEvidence = positiveTestEvidence;
    }


    /**
     * Return the list of test evidence that demonstrates the technology under test is not
     * conformant.
     *
     * @return list of test evidence
     */
    public List<OpenMetadataConformanceTestEvidence> getNegativeTestEvidence()
    {
        return negativeTestEvidence;
    }


    /**
     * Set up the list of test evidence that demonstrates the technology under test is not
     * conformant.
     * @param negativeTestEvidence list of test evidence
     */
    public void setNegativeTestEvidence(List<OpenMetadataConformanceTestEvidence> negativeTestEvidence)
    {
        this.negativeTestEvidence = negativeTestEvidence;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OpenMetadataConformanceRequirementResults{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", documentationURL='" + documentationURL + '\'' +
                ", conformanceStatus=" + conformanceStatus +
                ", positiveTestEvidence=" + positiveTestEvidence +
                ", negativeTestEvidence=" + negativeTestEvidence +
                '}';
    }
}
