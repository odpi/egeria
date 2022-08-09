/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProcessContainmentProperties when linking processes in a parent-child hierarchy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessContainmentProperties extends RelationshipProperties
{
    private static final long     serialVersionUID = 1L;

    private ProcessContainmentType processContainmentType = null;




    /**
     * Default constructor
     */
    public ProcessContainmentProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template to copy.
     */
    public ProcessContainmentProperties(ProcessContainmentProperties template)
    {
        super(template);

        if (template != null)
        {
            processContainmentType = template.getProcessContainmentType();
        }
    }


    /**
     * Return the relationship between the parent and child process.
     *
     * @return process containment type enum
     */
    public ProcessContainmentType getProcessContainmentType()
    {
        return processContainmentType;
    }


    /**
     * Set up the relationship between the parent and child process.
     *
     * @param processContainmentType process containment type enum
     */
    public void setProcessContainmentType(ProcessContainmentType processContainmentType)
    {
        this.processContainmentType = processContainmentType;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProcessContainmentProperties{" +
                       "processContainmentType=" + processContainmentType +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
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
        ProcessContainmentProperties that = (ProcessContainmentProperties) objectToCompare;
        return processContainmentType == that.processContainmentType;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), processContainmentType);
    }
}