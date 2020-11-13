/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValuesAssignmentConsumer contains the properties for a referenceable that is linked to a requested valid value via the ValidValuesAssignment.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValuesAssignmentConsumer extends ValidValuesAssignment
{
    private static final long serialVersionUID = 1L;

    private Referenceable consumer = null;


    /**
     * Default constructor
     */
    public ValidValuesAssignmentConsumer()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ValidValuesAssignmentConsumer(ValidValuesAssignmentConsumer template)
    {
        super(template);

        if (template != null)
        {
            consumer = template.getConsumer();
        }
    }


    /**
     * Returns the linked referenceable.
     *
     * @return properties of the referenceable
     */
    public Referenceable getConsumer()
    {
        return consumer;
    }


    /**
     * Set up the linked referenceable.
     *
     * @param consumer properties of the referenceable
     */
    public void setConsumer(Referenceable consumer)
    {
        this.consumer = consumer;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValuesAssignmentConsumer{" +
                "consumer=" + consumer +
                ", strictRequirement=" + getStrictRequirement() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ValidValuesAssignmentConsumer that = (ValidValuesAssignmentConsumer) objectToCompare;
        return Objects.equals(consumer, that.consumer);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), consumer);
    }
}
