/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceValueAssignedItem contains the properties for a referenceable that is linked to a requested valid value via the ReferenceValueAssignment.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceValueAssignedItem extends ReferenceValue
{
    private Referenceable       assignedItem        = null;


    /**
     * Default constructor
     */
    public ReferenceValueAssignedItem()
    {
        super();
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ReferenceValueAssignedItem(ReferenceValueAssignedItem template)
    {
        super(template);

        if (template != null)
        {
            assignedItem = template.getAssignedItem();
        }
    }


    /**
     * Returns the linked referenceable.
     *
     * @return properties of the referenceable
     */
    public Referenceable getAssignedItem()
    {
        return assignedItem;
    }


    /**
     * Set up the linked referenceable.
     *
     * @param assignedItem properties of the referenceable
     */
    public void setAssignedItem(Referenceable assignedItem)
    {
        this.assignedItem = assignedItem;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceValueAssignedItem{" +
                "assignedItem=" + assignedItem +
                ", confidence=" + getConfidence() +
                ", steward='" + getSteward() + '\'' +
                ", notes='" + getNotes() + '\'' +
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
        ReferenceValueAssignedItem that = (ReferenceValueAssignedItem) objectToCompare;
        return Objects.equals(assignedItem, that.assignedItem);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assignedItem);
    }
}
