/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetElement contains the properties and header for an  asset retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetElement implements MetadataElement, Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private AssetProperties                 assetProperties    = null;
    private List<MetadataCorrelationHeader> correlationHeaders = null;
    private ElementHeader                   elementHeader       = null;


    /**
     * Default constructor
     */
    public AssetElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetElement(AssetElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            correlationHeaders = template.getCorrelationHeaders();
            assetProperties = template.getAssetProperties();
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
     * Return the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @return list of correlation properties objects
     */
    @Override
    public List<MetadataCorrelationHeader> getCorrelationHeaders()
    {
        if (correlationHeaders == null)
        {
            return null;
        }
        else if (correlationHeaders.isEmpty())
        {
            return null;
        }

        return correlationHeaders;
    }


    /**
     * Set up the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @param correlationHeaders list of correlation properties objects
     */
    @Override
    public void setCorrelationHeaders(List<MetadataCorrelationHeader> correlationHeaders)
    {
        this.correlationHeaders = correlationHeaders;
    }


    /**
     * Return the properties for the asset.
     *
     * @return asset properties (using appropriate subclass)
     */
    public AssetProperties getAssetProperties()
    {
        return assetProperties;
    }


    /**
     * Set up the properties for the asset.
     *
     * @param assetProperties asset properties
     */
    public void setAssetProperties(AssetProperties assetProperties)
    {
        this.assetProperties = assetProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetElement{" +
                       "assetProperties=" + assetProperties +
                       ", correlationHeaders=" + correlationHeaders +
                       ", elementHeader=" + elementHeader +
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
        AssetElement that = (AssetElement) objectToCompare;
        return Objects.equals(getAssetProperties(), that.getAssetProperties()) &&
                       Objects.equals(getCorrelationHeaders(), that.getCorrelationHeaders()) &&
                       Objects.equals(getElementHeader(), that.getElementHeader());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, correlationHeaders, assetProperties);
    }
}
