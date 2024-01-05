/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Elements object is used to describe the elements returned by the search method
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Elements extends Element
{
    /**
     * The list of item elements returned by the search method.
     */
    private List<AssetCatalogItemElement> assetCatalogItemElements;


    /**
     * Default constructor.
     */
    public Elements()
    {
    }


    /**
     * Returns the list of item elements returned by the search method.
     *
     * @return the list of item elements
     */
    public List<AssetCatalogItemElement> getAssetCatalogItemElements()
    {
        return assetCatalogItemElements;
    }


    /**
     * Set up the list of item elements returned by the search method
     * @param assetCatalogItemElements the list of item elements
     */
    public void setAssetCatalogItemElements(List<AssetCatalogItemElement> assetCatalogItemElements)
    {
        this.assetCatalogItemElements = assetCatalogItemElements;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Elements{" +
                       "assetCatalogItemElements=" + assetCatalogItemElements +
                       ", guid='" + getGuid() + '\'' +
                       ", type=" + getType() +
                       ", name='" + getName() + '\'' +
                       ", createdBy='" + getCreatedBy() + '\'' +
                       ", createTime=" + getCreateTime() +
                       ", updatedBy='" + getUpdatedBy() + '\'' +
                       ", updateTime=" + getUpdateTime() +
                       ", version=" + getVersion() +
                       ", status='" + getStatus() + '\'' +
                       ", url='" + getUrl() + '\'' +
                       ", properties=" + getProperties() +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", classifications=" + getClassifications() +
                       ", parentElement=" + getAnchorElement() +
                       ", origin=" + getOrigin() +
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
        if (! (objectToCompare instanceof Elements elements))
        {
            return false;
        }
        return Objects.equals(assetCatalogItemElements, elements.assetCatalogItemElements);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(assetCatalogItemElements);
    }
}
