/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ValidValueAssignmentProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueAssignmentConsumerElement describes a Referenceable that is using a valid values set/definition to
 * define the values that may/must be assigned to it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueAssignmentConsumerElement extends ValidValueAssignmentProperties
{
    private static final long    serialVersionUID = 1L;

    private ReferenceableElement consumer = null;


    /**
     * Default constructor
     */
    public ValidValueAssignmentConsumerElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueAssignmentConsumerElement(ValidValueAssignmentConsumerElement template)
    {
        super(template);

        if (template != null)
        {
            consumer = template.getConsumer();
        }
    }


    /**
     * Return the referenceable bean
     *
     * @return bean
     */
    public ReferenceableElement getConsumer()
    {
        return consumer;
    }


    /**
     * Set up the referenceable bean
     *
     * @param consumer bean
     */
    public void setConsumer(ReferenceableElement consumer)
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
        return "ValidValueAssignmentConsumerElement{" + "consumer=" + consumer +
                ", strictRequirement=" + getStrictRequirement() + '}';
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
        ValidValueAssignmentConsumerElement that = (ValidValueAssignmentConsumerElement) objectToCompare;
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
