/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Structure for returning a relationship between two metadata elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRelationship extends ElementControlHeader
{
    private String                  relationshipGUID       = null;
    private ElementType             relationshipType       = null;
    private Date                    effectiveFromTime      = null;
    private Date                    effectiveToTime        = null;
    private ElementProperties       relationshipProperties = null;
    private String                  labelAtEnd1            = null;
    private String                  elementGUIDAtEnd1      = null;
    private OpenMetadataElementStub elementAtEnd1          = null;
    private String                  labelAtEnd2            = null;
    private String                  elementGUIDAtEnd2      = null;
    private OpenMetadataElementStub elementAtEnd2          = null;


    /**
     * Typical Constructor
     */
    public OpenMetadataRelationship()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public OpenMetadataRelationship(OpenMetadataRelationship template)
    {
        super(template);

        if (template != null)
        {
            relationshipType       = template.getRelationshipType();
            relationshipGUID       = template.getRelationshipGUID();
            effectiveFromTime      = template.getEffectiveFromTime();
            effectiveToTime        = template.getEffectiveToTime();
            relationshipProperties = template.getRelationshipProperties();
            elementGUIDAtEnd1      = template.getElementGUIDAtEnd1();
            elementGUIDAtEnd2      = template.getElementGUIDAtEnd2();
            labelAtEnd1            = template.getLabelAtEnd1();
            labelAtEnd2            = template.getLabelAtEnd2();
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
     * Return the unique identifier of the element at end 1 of the relationship.
     *
     * @return related element
     */
    public String getElementGUIDAtEnd1() { return elementGUIDAtEnd1; }


    /**
     * Set up the unique identifier of the element at end 1 of the relationship.
     *
     * @param elementGUIDAtEnd1 related element
     */
    public void setElementGUIDAtEnd1(String elementGUIDAtEnd1)
    {
        this.elementGUIDAtEnd1 = elementGUIDAtEnd1;
    }


    /**
     * Return the unique identifier of the element at end 2 of the relationship.
     *
     * @return related element
     */
    public String getElementGUIDAtEnd2() { return elementGUIDAtEnd2; }


    /**
     * Set up the unique identifier of the element at end 2 of the relationship.
     *
     * @param elementGUIDAtEnd2 related element
     */
    public void setElementGUIDAtEnd2(String elementGUIDAtEnd2)
    {
        this.elementGUIDAtEnd2 = elementGUIDAtEnd2;
    }


    /**
     * Return the descriptive label for end 1 of the relationship.
     *
     * @return string name
     */
    public String getLabelAtEnd1()
    {
        return labelAtEnd1;
    }


    /**
     * Set up the descriptive label for end 1 of the relationship.
     *
     * @param labelAtEnd1 string name
     */
    public void setLabelAtEnd1(String labelAtEnd1)
    {
        this.labelAtEnd1 = labelAtEnd1;
    }


    /**
     * Return the descriptive label for end 2 of the relationship.
     *
     * @return string name
     */
    public String getLabelAtEnd2()
    {
        return labelAtEnd2;
    }


    /**
     * Set up the descriptive label for end 2 of the relationship.
     *
     * @param labelAtEnd2 string name
     */
    public void setLabelAtEnd2(String labelAtEnd2)
    {
        this.labelAtEnd2 = labelAtEnd2;
    }


    /**
     * Return details of the element at end 1 of this relationship.
     *
     * @return element stub
     */
    public OpenMetadataElementStub getElementAtEnd1()
    {
        return elementAtEnd1;
    }


    /**
     * Set up details of the element at end 1 of this relationship.
     *
     * @param elementAtEnd1 element stub
     */
    public void setElementAtEnd1(OpenMetadataElementStub elementAtEnd1)
    {
        this.elementAtEnd1 = elementAtEnd1;
    }


    /**
     * Return details of the element at end 2 of this relationship.
     *
     * @return element stub
     */
    public OpenMetadataElementStub getElementAtEnd2()
    {
        return elementAtEnd2;
    }


    /**
     * Set up details of the element at end 2 of this relationship.
     *
     * @param elementAtEnd2 element stub
     */
    public void setElementAtEnd2(OpenMetadataElementStub elementAtEnd2)
    {
        this.elementAtEnd2 = elementAtEnd2;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRelationship{" +
                       "relationshipGUID='" + relationshipGUID + '\'' +
                       ", relationshipType=" + relationshipType +
                       ", effectiveFromTime=" + effectiveFromTime +
                       ", effectiveToTime=" + effectiveToTime +
                       ", relationshipProperties=" + relationshipProperties +
                       ", labelAtEnd1='" + labelAtEnd1 + '\'' +
                       ", elementGUIDAtEnd1='" + elementGUIDAtEnd1 + '\'' +
                       ", elementAtEnd1='" + elementAtEnd1 + '\'' +
                       ", labelAtEnd2='" + labelAtEnd2 + '\'' +
                       ", elementGUIDAtEnd2='" + elementGUIDAtEnd2 + '\'' +
                       ", elementAtEnd2='" + elementAtEnd2 + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", headerVersion=" + getHeaderVersion() +
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
        OpenMetadataRelationship that = (OpenMetadataRelationship) objectToCompare;
        return Objects.equals(relationshipGUID, that.relationshipGUID) &&
                       Objects.equals(relationshipType, that.relationshipType) &&
                       Objects.equals(effectiveFromTime, that.effectiveFromTime) &&
                       Objects.equals(effectiveToTime, that.effectiveToTime) &&
                       Objects.equals(relationshipProperties, that.relationshipProperties) &&
                       Objects.equals(elementGUIDAtEnd1, that.elementGUIDAtEnd1) &&
                       Objects.equals(elementAtEnd1, that.elementAtEnd1) &&
                       Objects.equals(labelAtEnd1, that.labelAtEnd1) &&
                       Objects.equals(elementGUIDAtEnd2, that.elementGUIDAtEnd2) &&
                       Objects.equals(elementAtEnd2, that.elementAtEnd2) &&
                       Objects.equals(labelAtEnd2, that.labelAtEnd2);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipGUID, relationshipType, effectiveFromTime, effectiveToTime,
                            relationshipProperties, elementGUIDAtEnd1, elementGUIDAtEnd1, elementAtEnd1, elementAtEnd1);
    }
}