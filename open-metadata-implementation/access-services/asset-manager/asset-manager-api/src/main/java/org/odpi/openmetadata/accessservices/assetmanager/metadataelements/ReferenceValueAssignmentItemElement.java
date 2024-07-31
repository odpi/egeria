/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceValueAssignmentProperties;

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
public class ReferenceValueAssignmentItemElement
{
    private ElementHeader                      relationshipHeader     = null;
    private ReferenceValueAssignmentProperties relationshipProperties = null;
    private ReferenceableElement               assignedItem = null;


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
        if (template != null)
        {
            this.relationshipHeader = template.getRelationshipHeader();
            this.relationshipProperties = template.getRelationshipProperties();
            this.assignedItem = template.getAssignedItem();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getRelationshipHeader()
    {
        return relationshipHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param relationshipHeader element header object
     */
    public void setRelationshipHeader(ElementHeader relationshipHeader)
    {
        this.relationshipHeader = relationshipHeader;
    }


    /**
     * Return the properties of the relationship.
     *
     * @return properties
     */
    public ReferenceValueAssignmentProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up the properties of the relationship.
     *
     * @param relationshipProperties  properties
     */
    public void setRelationshipProperties(ReferenceValueAssignmentProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
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
                       "relationshipHeader=" + relationshipHeader +
                       ", relationshipProperties=" + relationshipProperties +
                       ", assignedItem=" + assignedItem +
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
        if (! (objectToCompare instanceof ReferenceValueAssignmentItemElement that))
        {
            return false;
        }
        return Objects.equals(relationshipHeader, that.relationshipHeader) &&
                       Objects.equals(relationshipProperties, that.relationshipProperties) &&
                       Objects.equals(assignedItem, that.assignedItem);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipHeader, relationshipProperties, assignedItem);
    }
}
