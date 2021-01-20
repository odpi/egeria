/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.*;

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
    private static final long serialVersionUID = 1L;

    private String              relationshipGUID       = null;
    private ElementType         relationshipType       = null;
    private Date                effectiveFromTime      = null;
    private Date                effectiveToTime        = null;
    private ElementProperties   relationshipProperties = null;
    private OpenMetadataElement elementProperties      = null;


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
            relationshipType = template.getRelationshipType();
            relationshipGUID = template.getRelationshipGUID();
            effectiveFromTime = template.getEffectiveFromTime();
            effectiveToTime   = template.getEffectiveToTime();
            relationshipProperties = template.getRelationshipProperties();
            elementProperties = template.getElementProperties();
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
     * Return the element type properties for this relationship object.  These values are set up by the metadata repository
     * and define details to the metadata relationship used to represent this element.
     *
     * @return element type properties
     */
    public ElementType getRelationshipType() { return relationshipType; }


    /**
     * Set up the element type properties for this relationship object.  These values are set up by the metadata repository
     * and define details to the metadata relationship used to represent this element.
     *
     * @param relationshipType element type properties
     */
    public void setRelationshipType(ElementType relationshipType)
    {
        this.relationshipType = relationshipType;
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
    public OpenMetadataElement getElementProperties() { return elementProperties; }


    /**
     * Set up the properties of the related element.
     *
     * @param elementProperties related element
     */
    public void setElementProperties(OpenMetadataElement elementProperties)
    {
        this.elementProperties = elementProperties;
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
                       ", relationshipType=" + relationshipType +
                       ", effectiveFromTime=" + effectiveFromTime +
                       ", effectiveToTime=" + effectiveToTime +
                       ", relationshipProperties=" + relationshipProperties +
                       ", elementProperties=" + elementProperties +
                       ", headerVersion=" + getHeaderVersion() +
                       ", elementSourceServer='" + getElementSourceServer() + '\'' +
                       ", elementOriginCategory=" + getElementOriginCategory() +
                       ", elementMetadataCollectionId='" + getElementMetadataCollectionId() + '\'' +
                       ", elementMetadataCollectionName='" + getElementMetadataCollectionName() + '\'' +
                       ", elementLicense='" + getElementLicense() + '\'' +
                       ", status=" + getStatus() +
                       ", elementCreatedBy='" + getElementCreatedBy() + '\'' +
                       ", elementUpdatedBy='" + getElementUpdatedBy() + '\'' +
                       ", elementMaintainedBy=" + getElementMaintainedBy() +
                       ", elementCreateTime=" + getElementCreateTime() +
                       ", elementUpdateTime=" + getElementUpdateTime() +
                       ", elementVersion=" + getElementVersion() +
                       ", mappingProperties=" + getMappingProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelatedMetadataElement that = (RelatedMetadataElement) objectToCompare;
        return Objects.equals(relationshipGUID, that.relationshipGUID) &&
                       Objects.equals(relationshipType, that.relationshipType) &&
                       Objects.equals(effectiveFromTime, that.effectiveFromTime) &&
                       Objects.equals(effectiveToTime, that.effectiveToTime) &&
                       Objects.equals(relationshipProperties, that.relationshipProperties) &&
                       Objects.equals(elementProperties, that.elementProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipGUID, relationshipType, effectiveFromTime,
                            effectiveToTime, relationshipProperties, elementProperties);
    }
}