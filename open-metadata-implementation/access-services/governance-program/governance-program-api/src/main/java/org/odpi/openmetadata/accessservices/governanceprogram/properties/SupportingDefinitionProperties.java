/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportingDefinitionProperties provides a details of why a governance definition is supporting other governance definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportingDefinitionProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    String rationale = null;

    /**
     * Default constructor
     */
    public SupportingDefinitionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportingDefinitionProperties(SupportingDefinitionProperties template)
    {
        super(template);

        if (template != null)
        {
            this.rationale = template.getRationale();
        }
    }


    /**
     * Return the reason why the new definition supports the original definition.
     *
     * @return rationale
     */
    public String getRationale()
    {
        return rationale;
    }


    /**
     * Set up the reason why the new definition supports the original definition.
     *
     * @param rationale rationale
     */
    public void setRationale(String rationale)
    {
        this.rationale = rationale;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SupportingDefinitionProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", rationale='" + rationale + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        SupportingDefinitionProperties that = (SupportingDefinitionProperties) objectToCompare;
        return Objects.equals(rationale, that.rationale);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), rationale);
    }
}
