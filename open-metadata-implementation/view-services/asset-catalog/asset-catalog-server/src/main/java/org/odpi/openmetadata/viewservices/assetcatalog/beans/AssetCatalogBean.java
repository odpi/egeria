/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCatalogBean object holds properties that are used for displaying details of an asset bean from the metadata catalog,
 * plus the properties and classifications and relationships.
 * Also, the connection to asset is available in this object.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetCatalogBean extends Element
{
    /**
     * The list of relationships.
     */
    private List<Relationship> relationships;


    /**
     * Default constructor.
     */
    public AssetCatalogBean()
    {
    }


    /**
     * Returns the list of available relationships.
     *
     * @return the list of relationships
     */
    public List<Relationship> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the list of relationships.
     *
     * @param relationships of the element
     */
    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetCatalogBean{" +
                       "relationships=" + relationships +
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
        if (! (objectToCompare instanceof AssetCatalogBean that))
        {
            return false;
        }
        return Objects.equals(relationships, that.relationships);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationships);
    }
}
