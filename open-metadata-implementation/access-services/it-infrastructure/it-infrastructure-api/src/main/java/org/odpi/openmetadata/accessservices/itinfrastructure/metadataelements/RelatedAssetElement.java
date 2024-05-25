/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * RelatedAssetElement contains the properties and header for a relationship to an asset retrieved from the metadata repository.
 * The related asset is returned as well.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedAssetElement implements MetadataElement
{
    private ElementHeader       elementHeader = null;
    private Map<String, Object> properties    = null;
    private Date                effectiveFrom = null;
    private Date                effectiveTo   = null;
    private AssetElement        relatedAsset  = null;


    /**
     * Default constructor
     */
    public RelatedAssetElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedAssetElement(RelatedAssetElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            relatedAsset = template.getRelatedAsset();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties for the relationship.
     *
     * @return relationship properties (using appropriate subclass)
     */
    public Map<String, Object> getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the relationship.
     *
     * @param properties asset properties
     */
    public void setProperties(Map<String, Object> properties)
    {
        this.properties = properties;
    }



    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return the properties for the related asset.
     *
     * @return asset properties (using appropriate subclass)
     */
    public AssetElement getRelatedAsset()
    {
        return relatedAsset;
    }


    /**
     * Set up the properties for the related asset.
     *
     * @param relatedAsset asset properties
     */
    public void setRelatedAsset(AssetElement relatedAsset)
    {
        this.relatedAsset = relatedAsset;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RelatedAssetElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", relatedAsset=" + relatedAsset +
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
        RelatedAssetElement that = (RelatedAssetElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
                       Objects.equals(relatedAsset, that.relatedAsset);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, effectiveFrom, effectiveTo, relatedAsset);
    }
}
