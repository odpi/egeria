/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueConsumer describes a Referenceable that is using a valid values set/definition to
 * define the values that may/must be assigned to it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueConsumer implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private Referenceable consumer          = null;
    private boolean       strictRequirement = true;


    /**
     * Default constructor
     */
    public ValidValueConsumer()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueConsumer(ValidValueConsumer template)
    {
        if (template != null)
        {
            consumer          = template.getConsumer();
            strictRequirement = template.isStrictRequirement();
        }
    }


    /**
     * Return the referenceable bean
     *
     * @return bean
     */
    public Referenceable getConsumer()
    {
        return consumer;
    }


    /**
     * Set up the referenceable bean
     *
     * @param consumer bean
     */
    public void setConsumer(Referenceable consumer)
    {
        this.consumer = consumer;
    }


    /**
     * Return the strict requirement flag.
     *
     * @return boolean
     */
    public boolean isStrictRequirement()
    {
        return strictRequirement;
    }


    /**
     * Set up the type name of the referenceable.
     *
     * @param strictRequirement string type name
     */
    public void setStrictRequirement(boolean strictRequirement)
    {
        this.strictRequirement = strictRequirement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueConsumer{" + "consumer=" + consumer + ", strictRequirement=" + strictRequirement + '}';
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
        ValidValueConsumer that = (ValidValueConsumer) objectToCompare;
        return strictRequirement == that.strictRequirement && Objects.equals(consumer, that.consumer);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(consumer, strictRequirement);
    }
}
