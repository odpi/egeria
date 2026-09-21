/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContextEventImpactProperties is a java bean used to describe a link between a context event and elements that
 * describe its impact.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContextEventImpactProperties extends LabeledRelationshipProperties
{
    private int severityLevel = 0;


    /**
     * Default constructor
     */
    public ContextEventImpactProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ContextEventImpactProperties(ContextEventImpactProperties template)
    {
        super (template);

        if (template != null)
        {
            severityLevel = template.getSeverityLevel();
        }
    }


    /**
     * Return the severity level identifier for the relationship.
     *
     * @return int
     */
    public int getSeverityLevel()
    {
        return severityLevel;
    }


    /**
     * Set up the severity level identifier for the relationship.
     *
     * @param severityLevel int
     */
    public void setSeverityLevel(int severityLevel)
    {
        this.severityLevel = severityLevel;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ContextEventImpactProperties{" +
                "severityLevel=" + severityLevel +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ContextEventImpactProperties that = (ContextEventImpactProperties) objectToCompare;
        return severityLevel == that.severityLevel;
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), severityLevel);
    }
}
