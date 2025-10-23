/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ImpactedResourceProperties describes the element that is impacted by the incident reported by the incident report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IncidentImpactedElement
{
    private int    impactSeverityLevel = 0;
    private String impactedElementGUID = null;

    /**
     * Typical Constructor
     */
    public IncidentImpactedElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public IncidentImpactedElement(IncidentImpactedElement template)
    {
        if (template != null)
        {
            impactSeverityLevel = template.getImpactSeverityLevel();
            impactedElementGUID = template.getImpactedElementGUID();
        }
    }


    /**
     * Return the severity of the impact - the definition of the severity level is set up as part of the
     * governance program.
     *
     * @return integer
     */
    public int getImpactSeverityLevel()
    {
        return impactSeverityLevel;
    }


    /**
     * Set up the severity of the impact - the definition of the severity level is set up as part of the
     * governance program.
     *
     * @param impactSeverityLevel integer
     */
    public void setImpactSeverityLevel(int impactSeverityLevel)
    {
        this.impactSeverityLevel = impactSeverityLevel;
    }


    /**
     * Return the unique identifier of the impacted element.
     *
     * @return string identifier
     */
    public String getImpactedElementGUID()
    {
        return impactedElementGUID;
    }


    /**
     * Set up the unique identifier of the impacted element.
     *
     * @param impactedElementGUID string identifier
     */
    public void setImpactedElementGUID(String impactedElementGUID)
    {
        this.impactedElementGUID = impactedElementGUID;
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
                       "impactSeverityLevel=" + impactSeverityLevel +
                       ", impactedElementGUID='" + impactedElementGUID + '\'' +
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
        IncidentImpactedElement that = (IncidentImpactedElement) objectToCompare;
        return Objects.equals(impactSeverityLevel, that.impactSeverityLevel) &&
                       Objects.equals(impactedElementGUID, that.impactedElementGUID);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(impactSeverityLevel, impactedElementGUID);
    }
}
