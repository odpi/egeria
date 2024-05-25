/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ReferenceValueAssignmentProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceValueAssignmentItemElement describes a Referenceable that is using a valid values set/definition
 * as a tag/classifier.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceValueAssignmentItemElement extends ReferenceValueAssignmentProperties
{
    private ReferenceableElement assignedItem = null;


    /**
     * Default constructor
     */
    public ReferenceValueAssignmentItemElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceValueAssignmentItemElement(ReferenceValueAssignmentItemElement template)
    {
        super(template);

        if (template != null)
        {
            assignedItem = template.getAssignedItem();
        }
    }


    /**
     * Return the referenceable bean
     *
     * @return bean
     */
    public ReferenceableElement getAssignedItem()
    {
        return assignedItem;
    }


    /**
     * Set up the referenceable bean
     *
     * @param assignedItem bean
     */
    public void setAssignedItem(ReferenceableElement assignedItem)
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
        return "ReferenceValueAssignmentItemElement{" +
                "consumer=" + assignedItem +
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
        ReferenceValueAssignmentItemElement that = (ReferenceValueAssignmentItemElement) objectToCompare;
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
