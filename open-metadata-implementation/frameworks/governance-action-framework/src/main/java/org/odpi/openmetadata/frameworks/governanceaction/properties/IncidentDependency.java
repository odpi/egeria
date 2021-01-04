/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IncidentDependency describes a link to an IncidentReport previous raised that covers a similar or
 * the same issue on these resources.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IncidentDependency implements Serializable
{
    private static final long      serialVersionUID = 1L;

    private String previouslyReportedIncidentGUID = null;
    private String description                    = null;

    /**
     * Typical Constructor
     */
    public IncidentDependency()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public IncidentDependency(IncidentDependency template)
    {
        if (template != null)
        {
            previouslyReportedIncidentGUID = template.getPreviouslyReportedIncidentGUID();
            description = template.getDescription();
        }
    }


    /**
     * Return the identifier of an incident that this incident is dependent on.
     *
     * @return string identifier
     */
    public String getPreviouslyReportedIncidentGUID()
    {
        return previouslyReportedIncidentGUID;
    }


    /**
     * Set up the identifier of an incident that this incident is dependent on.
     *
     * @param previouslyReportedIncidentGUID string identifier
     */
    public void setPreviouslyReportedIncidentGUID(String previouslyReportedIncidentGUID)
    {
        this.previouslyReportedIncidentGUID = previouslyReportedIncidentGUID;
    }


    /**
     * Return the description of the dependency.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the dependency.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "IncidentDependency{" +
                       "previouslyReportedIncidentGUID='" + previouslyReportedIncidentGUID + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        IncidentDependency that = (IncidentDependency) objectToCompare;
        return Objects.equals(previouslyReportedIncidentGUID, that.previouslyReportedIncidentGUID) &&
                       Objects.equals(description, that.description);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(previouslyReportedIncidentGUID, description);
    }
}
