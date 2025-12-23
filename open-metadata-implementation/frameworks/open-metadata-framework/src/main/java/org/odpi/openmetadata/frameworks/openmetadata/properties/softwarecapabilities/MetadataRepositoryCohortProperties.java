/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataRepositoryCohortProperties describes the properties of an open metadata repository cohort.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataRepositoryCohortProperties extends ReferenceableProperties
{
    private List<String> cohortTopics = null;



    /**
     * Default constructor
     */
    public MetadataRepositoryCohortProperties()
    {
        super();
        super.typeName = OpenMetadataType.METADATA_REPOSITORY_COHORT.typeName;
    }


    /**
     * Copy/clone constructor for an Endpoint.
     *
     * @param template template object to copy.
     */
    public MetadataRepositoryCohortProperties(MetadataRepositoryCohortProperties template)
    {
        super(template);

        if (template != null)
        {
            cohortTopics = template.getCohortTopics();
        }
    }


    /**
     * Set up the list of topics used by this cohort.
     *
     * @param cohortTopics String resource name
     */
    public void setCohortTopics(List<String> cohortTopics)
    {
        this.cohortTopics = cohortTopics;
    }


    /**
     * Returns the list of topics used by this cohort.
     *
     * @return address
     */
    public List<String> getCohortTopics()
    {
        return cohortTopics;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataRepositoryCohortProperties{" +
                "cohortTopics=" + cohortTopics +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MetadataRepositoryCohortProperties that = (MetadataRepositoryCohortProperties) objectToCompare;
        return Objects.equals(cohortTopics, that.cohortTopics);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), cohortTopics);
    }
}
