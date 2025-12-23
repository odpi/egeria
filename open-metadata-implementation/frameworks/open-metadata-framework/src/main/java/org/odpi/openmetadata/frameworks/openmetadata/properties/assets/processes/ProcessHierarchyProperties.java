/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ProcessContainmentType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProcessHierarchyProperties when linking processes in a parent-child hierarchy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessHierarchyProperties extends RelationshipBeanProperties
{
    private ProcessContainmentType containmentType = null;


    /**
     * Default constructor
     */
    public ProcessHierarchyProperties()
    {
        super();
        super.typeName = OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template to copy.
     */
    public ProcessHierarchyProperties(ProcessHierarchyProperties template)
    {
        super(template);

        if (template != null)
        {
            containmentType = template.getContainmentType();
        }
    }


    /**
     * Return the relationship between the parent and child process.
     *
     * @return process containment type enum
     */
    public ProcessContainmentType getContainmentType()
    {
        return containmentType;
    }


    /**
     * Set up the relationship between the parent and child process.
     *
     * @param containmentType process containment type enum
     */
    public void setContainmentType(ProcessContainmentType containmentType)
    {
        this.containmentType = containmentType;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProcessHierarchyProperties{" +
                "containmentType=" + containmentType +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ProcessHierarchyProperties that = (ProcessHierarchyProperties) objectToCompare;
        return containmentType == that.containmentType;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), containmentType);
    }
}