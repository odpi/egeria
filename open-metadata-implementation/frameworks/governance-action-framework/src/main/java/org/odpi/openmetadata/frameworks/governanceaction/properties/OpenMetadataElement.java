/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementControlHeader;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataElement describes an entity in an open metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataElement extends ElementControlHeader
{
    private String                       elementGUID       = null;
    private Date                         effectiveFromTime = null;
    private Date                         effectiveToTime   = null;
    private List<AttachedClassification> classifications   = null;
    private ElementProperties            elementProperties = null;


    /**
     * Typical Constructor
     */
    public OpenMetadataElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public OpenMetadataElement(OpenMetadataElement template)
    {
        super(template);

        if (template != null)
        {
            elementGUID       = template.getElementGUID();
            effectiveFromTime = template.getEffectiveFromTime();
            effectiveToTime   = template.getEffectiveToTime();
            classifications   = template.getClassifications();
            elementProperties = template.getElementProperties();
        }
    }


    /**
     * Return the unique id for the metadata element.
     *
     * @return String unique identifier
     */
    public String getElementGUID()
    {
        return elementGUID;
    }


    /**
     * Set up the unique id for the metadata element.
     *
     * @param guid String unique identifier
     */
    public void setElementGUID(String guid)
    {
        this.elementGUID = guid;
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
     * Return the list of classifications associated with the metadata element.
     *
     * @return Classifications  list of classifications
     */
    public List<AttachedClassification> getClassifications()
    {
        return classifications;
    }


    /**
     * Set up the list of classifications associated with the metadata element.
     *
     * @param classifications list of classifications
     */
    public void setClassifications(List<AttachedClassification> classifications)
    {
        this.classifications = classifications;
    }



    /**
     * Return the properties that are stored with the metadata element.  The ElementType
     * includes the list of valid property names.
     *
     * @return property map
     */
    public ElementProperties getElementProperties()
    {
        return elementProperties;
    }


    /**
     * Set up the properties that are stored with the metadata element.  The ElementType
     * includes the list of valid property names.
     *
     * @param elementProperties property map
     */
    public void setElementProperties(ElementProperties elementProperties)
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
        return "OpenMetadataElement{" +
                       "elementGUID='" + elementGUID + '\'' +
                       ", effectiveFromTime=" + effectiveFromTime +
                       ", effectiveToTime=" + effectiveToTime +
                       ", classifications=" + classifications +
                       ", elementProperties=" + elementProperties +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataElement that = (OpenMetadataElement) objectToCompare;
        return Objects.equals(elementGUID, that.elementGUID) &&
                       Objects.equals(effectiveFromTime, that.effectiveFromTime) &&
                       Objects.equals(effectiveToTime, that.effectiveToTime) &&
                       Objects.equals(classifications, that.classifications) &&
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
        return Objects.hash(super.hashCode(), elementGUID, effectiveFromTime, effectiveToTime, classifications, elementProperties);
    }
}