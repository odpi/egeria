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
 * OpenMetadataConformanceRequirementResults documents the conformance assessment of the technology under test
 * for one of the requirements specified for a workbench's profile.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataConformanceRequirementResults extends OpenMetadataConformanceRequirementSummary
{
    private static final long   serialVersionUID = 1L;

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
        super(id, name, description, documentationURL);
    }


    /**
     * Copy/clone constructor used in JSON exchange
     *
     * @param template object to copy
     */
    public OpenMetadataConformanceRequirementResults(OpenMetadataConformanceRequirementResults  template)
    {
        super(template);
        if (template != null)
        {
            this.positiveTestEvidence = template.getPositiveTestEvidence();
            this.negativeTestEvidence = template.getNegativeTestEvidence();
        }
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
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", documentationURL='" + getDocumentationURL() + '\'' +
                ", conformanceStatus=" + getConformanceStatus() +
                ", positiveTestEvidence=" + positiveTestEvidence +
                ", negativeTestEvidence=" + negativeTestEvidence +
                '}';
    }
}
