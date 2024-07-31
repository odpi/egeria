/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoActionTargetProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ToDoActionTargetElement describes an element that a person owning a "To Do" action should process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToDoActionTargetElement
{
    private ElementHeader              relationshipHeader     = null;
    private ToDoActionTargetProperties relationshipProperties = null;
    private MetadataElementSummary     targetElement          = null;


    /**
     * Typical Constructor
     */
    public ToDoActionTargetElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public ToDoActionTargetElement(ToDoActionTargetElement template)
    {
        if (template != null)
        {
            relationshipHeader = template.getRelationshipHeader();
            relationshipProperties = template.getRelationshipProperties();
            targetElement = template.getTargetElement();
        }
    }


    /**
     * Return the header of the ActionTarget relationship.
     *
     * @return header
     */
    public ElementHeader getRelationshipHeader()
    {
        return relationshipHeader;
    }


    /**
     * Set up the header of the ActionTarget relationship.
     *
     * @param relationshipHeader header
     */
    public void setRelationshipHeader(ElementHeader relationshipHeader)
    {
        this.relationshipHeader = relationshipHeader;
    }


    /**
     * Return the properties of the ActionTarget relationship.
     *
     * @return properties
     */
    public ToDoActionTargetProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up the properties of the ActionTarget relationship.
     *
     * @param relationshipProperties properties
     */
    public void setRelationshipProperties(ToDoActionTargetProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Return details of the target element that the governance action service is to process.
     *
     * @return metadata element properties
     */
    public MetadataElementSummary getTargetElement()
    {
        return targetElement;
    }


    /**
     * Set up details of the target element that the governance action service is to process.
     *
     * @param targetElement metadata element properties
     */
    public void setTargetElement(MetadataElementSummary targetElement)
    {
        this.targetElement = targetElement;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ToDoActionTargetElement{" +
                       "relationshipGUID=" + relationshipHeader +
                       ", relationshipProperties=" + relationshipProperties +
                       ", targetElement=" + targetElement +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ToDoActionTargetElement that = (ToDoActionTargetElement) objectToCompare;
        return Objects.equals(relationshipHeader, that.relationshipHeader) &&
                Objects.equals(relationshipProperties, that.relationshipProperties) &&
                Objects.equals(targetElement, that.targetElement);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipHeader, relationshipProperties, targetElement);
    }
}
