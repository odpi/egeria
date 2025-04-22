/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The GovernanceMetricImplementation defines the query and data set that supports the measurements
 * for a GovernanceMetricProperties.  The list of connections to the data set are also provided to enable
 * the client to query the values in the data set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceMetricImplementation extends GovernanceMetricElement
{
    private List<RelatedElementStub> implementations = null;

    /**
     * Default constructor
     */
    public GovernanceMetricImplementation()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceMetricImplementation(GovernanceMetricImplementation  template)
    {
        super(template);

        if (template != null)
        {
            this.implementations = template.getImplementations();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceMetricImplementation(GovernanceMetricElement  template)
    {
        super(template);
    }


    /**
     * Return the list of relationships that links the governance metric to the measurement data set.
     *
     * @return related elements
     */
    public List<RelatedElementStub> getImplementations()
    {
        return implementations;
    }


    /**
     * Set up the list of relationships that links the governance metric to the measurement data set.
     *
     * @param implementations related elements
     */
    public void setImplementations(List<RelatedElementStub> implementations)
    {
        this.implementations = implementations;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceMetricImplementation{" +
                "implementations=" + implementations +
                "} " + super.toString();
    }



    /**
     * Test the properties of the GovernanceMetricImplementation to determine if the supplied object is equal to this one.
     *
     * @param objectToCompare object
     * @return boolean evaluation
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof GovernanceMetricImplementation that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(implementations, that.implementations);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), implementations);
    }
}
