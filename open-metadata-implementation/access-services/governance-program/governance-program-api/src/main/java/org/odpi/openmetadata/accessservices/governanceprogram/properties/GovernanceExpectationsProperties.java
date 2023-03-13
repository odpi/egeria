/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceExpectationsProperties defines the values expected in the operation of the linked referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceExpectationsProperties.class, name = "GovernanceExpectationsProperties"),
                @JsonSubTypes.Type(value = GovernanceMeasurementsDataSetProperties.class, name = "GovernanceMeasurementsDataSetProperties"),
                @JsonSubTypes.Type(value = GovernanceMeasurementsProperties.class, name = "GovernanceMeasurementsProperties"),
        })
public class GovernanceExpectationsProperties extends ClassificationProperties
{
    private static final long    serialVersionUID = 1L;

    private Map<String, Integer> counts = null;
    private Map<String, String>  values = null;
    private Map<String, Boolean> flags  = null;



    /**
     * Default constructor
     */
    public GovernanceExpectationsProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceExpectationsProperties(GovernanceExpectationsProperties template)
    {
        super(template);

        if (template != null)
        {
            counts = template.getCounts();
            values = template.getValues();
            flags = template.getFlags();
        }
    }


    /**
     * Return the set of name-value counts.
     *
     * @return string name-value counts
     */
    public Map<String, Integer> getCounts()
    {
        return counts;
    }


    /**
     * Set up the set of name-value counts.
     *
     * @param counts name-value counts
     */
    public void setCounts(Map<String, Integer> counts)
    {
        this.counts = counts;
    }


    /**
     * Return the set of name-value pairs.
     *
     * @return name-value pairs
     */
    public Map<String, String> getValues()
    {
        return values;
    }


    /**
     * Set up the set of name-value pairs.
     *
     * @param values name-value pairs
     */
    public void setValues(Map<String, String> values)
    {
        this.values = values;
    }


    /**
     * Return the set of name-value flags.
     *
     * @return name-value flags
     */
    public Map<String, Boolean> getFlags()
    {
        return flags;
    }


    /**
     * Set up the set of name-value flags.
     *
     * @param flags name-value flags
     */
    public void setFlags(Map<String, Boolean> flags)
    {
        this.flags = flags;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceExpectationsProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", counts=" + counts +
                       ", values=" + values +
                       ", flags=" + flags +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof GovernanceExpectationsProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceExpectationsProperties that = (GovernanceExpectationsProperties) objectToCompare;
        return Objects.equals(counts, that.counts) &&
                       Objects.equals(values, that.values) &&
                       Objects.equals(flags, that.flags);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), counts, values, flags);
    }
}
