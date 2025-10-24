/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This property header implements any common mechanisms that all property objects need.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMetadataElement extends ElementControlHeader
{
    private String              relationshipGUID       = null;
    private Date                effectiveFromTime      = null;
    private Date                effectiveToTime        = null;
    private ElementProperties   relationshipProperties = null;
    private OpenMetadataElement element                = null;
    private boolean             elementAtEnd1          = false;


    /**
     * Typical Constructor
     */
    public RelatedMetadataElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public RelatedMetadataElement(RelatedMetadataElement template)
    {
        super(template);

        if (template != null)
        {
            relationshipGUID = template.getRelationshipGUID();
            effectiveFromTime = template.getEffectiveFromTime();
            effectiveToTime   = template.getEffectiveToTime();
            relationshipProperties = template.getRelationshipProperties();
            element = template.getElement();
            elementAtEnd1 = template.getElementAtEnd1();
        }
    }


    /**
     * Return the unique id for the relationship .
     *
     * @return String unique id
     */
    public String getRelationshipGUID()
    {
        return relationshipGUID;
    }


    /**
     * Set up the unique id for the relationship.
     *
     * @param guid String unique identifier
     */
    public void setRelationshipGUID(String guid)
    {
        this.relationshipGUID = guid;
    }


    /**
     * Return the date/time that this instance should start to be used (null means it can be used from creationTime).
     *
     * @return Date object
     */
    public Date getEffectiveFromTime() { return effectiveFromTime; }


    /**
     * Set up the date/time that this instance should start to be used (null means it can be used from creationTime).
     *
     * @param effectiveFromTime Date object
     */
    public void setEffectiveFromTime(Date effectiveFromTime)
    {
        this.effectiveFromTime = effectiveFromTime;
    }


    /**
     * Return the date/time that this instance should no longer be used.
     *
     * @return Date object
     */
    public Date getEffectiveToTime()
    {
        return effectiveToTime;
    }


    /**
     * Set up the date/time that this instance should no longer be used.
     *
     * @param effectiveToTime Date object
     */
    public void setEffectiveToTime(Date effectiveToTime)
    {
        this.effectiveToTime = effectiveToTime;
    }


    /**
     * Return the properties associated with the relationship.
     *
     * @return property names and values
     */
    public ElementProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up the properties associated with the relationship.
     *
     * @param relationshipProperties property names and values
     */
    public void setRelationshipProperties(ElementProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Return the properties of the related element.
     *
     * @return related element
     */
    public OpenMetadataElement getElement() { return element; }


    /**
     * Set up the properties of the related element.
     *
     * @param element related element
     */
    public void setElement(OpenMetadataElement element)
    {
        this.element = element;
    }


    /**
     * Return whether the related element is at end 1 of the relationship.
     *
     * @return boolean
     */
    public boolean getElementAtEnd1()
    {
        return elementAtEnd1;
    }


    /**
     * Set up whether the related element is at end 1 of the relationship.
     *
     * @param elementAtEnd1 boolean
     */
    public void setElementAtEnd1(boolean elementAtEnd1)
    {
        this.elementAtEnd1 = elementAtEnd1;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedMetadataElement{" +
                "relationshipGUID='" + relationshipGUID + '\'' +
                ", effectiveFromTime=" + effectiveFromTime +
                ", effectiveToTime=" + effectiveToTime +
                ", relationshipProperties=" + relationshipProperties +
                ", element=" + element +
                ", elementAtEnd1=" + elementAtEnd1 +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelatedMetadataElement that = (RelatedMetadataElement) objectToCompare;
        return Objects.equals(relationshipGUID, that.relationshipGUID) &&
                       Objects.equals(effectiveFromTime, that.effectiveFromTime) &&
                       Objects.equals(effectiveToTime, that.effectiveToTime) &&
                       Objects.equals(relationshipProperties, that.relationshipProperties) &&
                       Objects.equals(element, that.element) &&
                       elementAtEnd1 == that.elementAtEnd1;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipGUID, effectiveFromTime,
                            effectiveToTime, relationshipProperties, element, elementAtEnd1);
    }
}