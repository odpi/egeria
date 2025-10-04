/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
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
    private int    severityLevelIdentifier = 0;


    /**
     * Default constructor
     */
    public ContextEventImpactProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName);
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
            severityLevelIdentifier = template.getSeverityLevelIdentifier();
        }
    }


    /**
     * Return the severity level identifier for the relationship.
     *
     * @return int
     */
    public int getSeverityLevelIdentifier()
    {
        return severityLevelIdentifier;
    }


    /**
     * Set up the severity level identifier for the relationship.
     *
     * @param severityLevelIdentifier int
     */
    public void setSeverityLevelIdentifier(int severityLevelIdentifier)
    {
        this.severityLevelIdentifier = severityLevelIdentifier;
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
                "severityLevelIdentifier=" + severityLevelIdentifier +
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
        return severityLevelIdentifier == that.severityLevelIdentifier;
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), severityLevelIdentifier);
    }
}
