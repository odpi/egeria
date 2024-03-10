/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActionTargetElement describes an element that a person owning a "To Do" action should process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionTargetElement
{
    private ElementHeader          relationshipHeader     = null;
    private ActionTargetProperties relationshipProperties = null;
    private OpenMetadataElement    targetElement          = null;


    /**
     * Typical Constructor
     */
    public ActionTargetElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public ActionTargetElement(ActionTargetElement template)
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
    public ActionTargetProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up the properties of the ActionTarget relationship.
     *
     * @param relationshipProperties properties
     */
    public void setRelationshipProperties(ActionTargetProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Return details of the target element that the governance action service is to process.
     *
     * @return metadata element properties
     */
    public OpenMetadataElement getTargetElement()
    {
        return targetElement;
    }


    /**
     * Set up details of the target element that the governance action service is to process.
     *
     * @param targetElement metadata element properties
     */
    public void setTargetElement(OpenMetadataElement targetElement)
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
        return "ActionTargetElement{" +
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
        ActionTargetElement that = (ActionTargetElement) objectToCompare;
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
