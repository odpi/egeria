/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueElement contains the properties and header for a valid value definition or set entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueElement implements MetadataElement
{
    private ElementHeader        elementHeader = null;
    private ValidValueProperties validValueProperties = null;
    private String               setGUID = null;
    private boolean   isDefaultValue = false;
    private RelatedBy relatedBy      = null;


    /**
     * Default constructor
     */
    public ValidValueElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ValidValueElement(ValidValueElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            validValueProperties = template.getValidValueProperties();
            setGUID = template.setGUID;
            isDefaultValue = getIsDefaultValue();
            relatedBy      = getRelatedBy();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties for the valid value definition or set.
     *
     * @return properties bean
     */
    public ValidValueProperties getValidValueProperties()
    {
        return validValueProperties;
    }


    /**
     * Set up the properties for the valid value definition or set.
     *
     * @param validValueProperties properties bean
     */
    public void setValidValueProperties(ValidValueProperties validValueProperties)
    {
        this.validValueProperties = validValueProperties;
    }


    /**
     * Return the set unique identifier if the valid value is retrieved via a set.
     *
     * @return string identifier
     */
    public String getSetGUID()
    {
        return setGUID;
    }


    /**
     * Set up the set unique identifier if the valid value is retrieved via a set.
     *
     * @param setGUID string identifier
     */
    public void setSetGUID(String setGUID)
    {
        this.setGUID = setGUID;
    }


    /**
     * Return whether this valid value is the default of the setGUID (if supplied)
     * @return flag
     */
    public boolean getIsDefaultValue()
    {
        return isDefaultValue;
    }


    /**
     * Set up whether this valid value is the default of the setGUID (if supplied).
     *
     * @param defaultValue flag
     */
    public void setIsDefaultValue(boolean defaultValue)
    {
        isDefaultValue = defaultValue;
    }



    /**
     * Return information about the related element and relationship (if any) that was
     * used to retrieve the valid value.
     *
     * @return related element
     */
    public RelatedBy getRelatedBy()
    {
        return relatedBy;
    }


    /**
     * Set up information about the related element and relationship (if any) that was
     * used to retrieve the valid value.
     *
     * @param relatedBy related element
     */
    public void setRelatedBy(RelatedBy relatedBy)
    {
        this.relatedBy = relatedBy;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ValidValueElement{" +
                "elementHeader=" + elementHeader +
                ", validValueProperties=" + validValueProperties +
                ", setGUID='" + setGUID + '\'' +
                ", isDefaultValue=" + isDefaultValue +
                ", relatedBy=" + relatedBy +
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
        ValidValueElement that = (ValidValueElement) objectToCompare;
        return isDefaultValue == that.isDefaultValue &&
                Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(validValueProperties, that.validValueProperties) &&
                Objects.equals(setGUID, that.setGUID) &&
                Objects.equals(relatedBy, that.relatedBy);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, validValueProperties, setGUID, isDefaultValue);
    }
}
