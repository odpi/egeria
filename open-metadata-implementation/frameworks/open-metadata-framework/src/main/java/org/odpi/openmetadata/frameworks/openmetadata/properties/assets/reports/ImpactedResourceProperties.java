/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ImpactedResourceProperties describes the element that is impacted by the incident reported by the incident report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImpactedResourceProperties extends RelationshipBeanProperties
{
    private int    severityLevelIdentifier = 0;

    /**
     * Typical Constructor
     */
    public ImpactedResourceProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.IMPACTED_RESOURCE_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public ImpactedResourceProperties(ImpactedResourceProperties template)
    {
        super(template);

        if (template != null)
        {
            severityLevelIdentifier = template.getSeverityLevelIdentifier();
        }
    }


    /**
     * Return the severity of the impact - the definition of the severity level is set up as part of the
     * governance program.
     *
     * @return integer
     */
    public int getSeverityLevelIdentifier()
    {
        return severityLevelIdentifier;
    }


    /**
     * Set up the severity of the impact - the definition of the severity level is set up as part of the
     * governance program.
     *
     * @param severityLevelIdentifier integer
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
        return "ImpactedResourceProperties{" +
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
        ImpactedResourceProperties that = (ImpactedResourceProperties) objectToCompare;
        return severityLevelIdentifier == that.severityLevelIdentifier;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), severityLevelIdentifier);
    }
}
