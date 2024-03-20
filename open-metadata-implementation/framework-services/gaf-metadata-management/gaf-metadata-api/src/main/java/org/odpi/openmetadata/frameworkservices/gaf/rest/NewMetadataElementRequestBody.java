/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewMetadataElementRequestBody provides a structure for passing the properties for a new metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewMetadataElementRequestBody extends MetadataSourceRequestBody
{
    private String                         typeName                     = null;
    private ElementStatus                  initialStatus                = null;
    private Map<String, ElementProperties> initialClassifications       = null;
    private String                         anchorGUID                   = null;
    private boolean                        isOwnAnchor                  = false;
    private Date                           effectiveFrom                = null;
    private Date                           effectiveTo                  = null;
    private ElementProperties              properties                   = null;
    private String                         parentGUID                   = null;
    private String                         parentRelationshipTypeName   = null;
    private ElementProperties              parentRelationshipProperties = null;
    private boolean                        parentAtEnd1                 = true;
    private Date                           effectiveTime                = null;


    /**
     * Default constructor
     */
    public NewMetadataElementRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewMetadataElementRequestBody(NewMetadataElementRequestBody template)
    {
        super(template);

        if (template != null)
        {
            typeName = template.getTypeName();
            initialStatus = template.getInitialStatus();
            initialClassifications = template.getInitialClassifications();
            anchorGUID = template.getAnchorGUID();
            isOwnAnchor = template.getIsOwnAnchor();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
            properties = template.getProperties();
            parentGUID = template.getParentGUID();
            parentRelationshipTypeName = template.getParentRelationshipTypeName();
            parentRelationshipProperties = template.getParentRelationshipProperties();
            parentAtEnd1 = template.getParentAtEnd1();
            effectiveTime = template.getEffectiveTime();
        }
    }


    /**
     * Return the open metadata type name for the new metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the open metadata type name for the new metadata element.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the initial status of the metadata element (typically ACTIVE).
     *
     * @return element status enum value
     */
    public ElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Return the map of classification name to properties describing the initial classification for the new metadata element.
     *
     * @return map of classification name to classification properties (or null for none)
     */
    public Map<String, ElementProperties> getInitialClassifications()
    {
        return initialClassifications;
    }


    /**
     * Set up the map of classification name to properties describing the initial classification for the new metadata element.
     *
     * @param initialClassifications map of classification name to classification properties (or null for none)
     */
    public void setInitialClassifications(Map<String, ElementProperties> initialClassifications)
    {
        this.initialClassifications = initialClassifications;
    }


    /**
     * Return the unique identifier of the element that should be the anchor for the new element. It is set to null if no anchor,
     * or the Anchors classification is included in the initial classifications.
     *
     * @return string guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     * or the Anchors classification is included in the initial classifications.
     *
     * @param anchorGUID string guid
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return whether this element should be classified as its own anchor or not.  The default is false.
     *
     * @return boolean
     */
    public boolean getIsOwnAnchor()
    {
        return isOwnAnchor;
    }


    /**
     * Set up whether this element should be classified as its own anchor or not.  The default is false.
     *
     * @param ownAnchor boolean
     */
    public void setIsOwnAnchor(boolean ownAnchor)
    {
        isOwnAnchor = ownAnchor;
    }


    /**
     * Set up the initial status of the metadata element (typically ACTIVE).
     *
     * @param initialStatus element status enum value
     */
    public void setInitialStatus(ElementStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * Return the date/time that this new element becomes effective in the governance program (null means immediately).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this new element becomes effective in the governance program (null means immediately).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time when the new element is no longer effective in the  governance program (null means until deleted).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set up the date/time when the new element is no longer effective in the  governance program (null means until deleted).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return the properties for the new metadata element.
     *
     * @return list of properties
     */
    public ElementProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the new metadata element.
     *
     * @param properties list of properties
     */
    public void setProperties(ElementProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the optional unique identifier for an element that should be connected to the newly created element.
     * If this property is specified, parentRelationshipTypeName must also be specified.
     *
     * @return string guid
     */
    public String getParentGUID()
    {
        return parentGUID;
    }


    /**
     * Set up the optional unique identifier for an element that should be connected to the newly created element.
     * If this property is specified, parentRelationshipTypeName must also be specified.
     *
     * @param parentGUID string guid
     */
    public void setParentGUID(String parentGUID)
    {
        this.parentGUID = parentGUID;
    }


    /**
     * Return the name of the relationship, if any, that should be established between the new element and the parent element.
     *
     * @return string type name
     */
    public String getParentRelationshipTypeName()
    {
        return parentRelationshipTypeName;
    }


    /**
     * Set up the name of the optional relationship from the newly created element to a parent element.
     *
     * @param parentRelationshipTypeName string type name
     */
    public void setParentRelationshipTypeName(String parentRelationshipTypeName)
    {
        this.parentRelationshipTypeName = parentRelationshipTypeName;
    }


    /**
     * Return any properties that should be included in the parent relationship.
     *
     * @return element properties
     */
    public ElementProperties getParentRelationshipProperties()
    {
        return parentRelationshipProperties;
    }


    /**
     * Set up any properties that should be included in the parent relationship.
     *
     * @param parentRelationshipProperties element properties
     */
    public void setParentRelationshipProperties(ElementProperties parentRelationshipProperties)
    {
        this.parentRelationshipProperties = parentRelationshipProperties;
    }


    /**
     * Return which end any parent entity sits on the relationship.
     *
     * @return boolean
     */
    public boolean getParentAtEnd1()
    {
        return parentAtEnd1;
    }


    /**
     * Set up  which end any parent entity sits on the relationship.
     *
     * @param parentAtEnd1 boolean
     */
    public void setParentAtEnd1(boolean parentAtEnd1)
    {
        this.parentAtEnd1 = parentAtEnd1;
    }


    /**
     * Return the effective time use on any queries for related elements.
     *
     * @return date object
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }


    /**
     * Set up the effective time use on any queries for related elements.
     *
     * @param effectiveTime date object
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewMetadataElementRequestBody{" +
                       "typeName='" + typeName + '\'' +
                       ", initialStatus=" + initialStatus +
                       ", initialClassifications=" + initialClassifications +
                       ", anchorGUID='" + anchorGUID + '\'' +
                       ", isOwnAnchor='" + isOwnAnchor + '\'' +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", properties=" + properties +
                       ", parentGUID='" + parentGUID + '\'' +
                       ", parentRelationshipTypeName='" + parentRelationshipTypeName + '\'' +
                       ", parentRelationshipProperties=" + parentRelationshipProperties +
                       ", parentAtEnd1=" + parentAtEnd1 +
                       ", effectiveTime=" + effectiveTime +
                       ", externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NewMetadataElementRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return parentAtEnd1 == that.parentAtEnd1 &&
                       isOwnAnchor == that.isOwnAnchor &&
                       Objects.equals(typeName, that.typeName) &&
                       initialStatus == that.initialStatus &&
                       Objects.equals(initialClassifications, that.initialClassifications) &&
                       Objects.equals(anchorGUID, that.anchorGUID) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(parentGUID, that.parentGUID) &&
                       Objects.equals(parentRelationshipTypeName, that.parentRelationshipTypeName) &&
                       Objects.equals(parentRelationshipProperties, that.parentRelationshipProperties) &&
                       Objects.equals(effectiveTime, that.effectiveTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), typeName, initialStatus, initialClassifications, anchorGUID, isOwnAnchor, effectiveFrom, effectiveTo, properties,
                            parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, effectiveTime);
    }
}
