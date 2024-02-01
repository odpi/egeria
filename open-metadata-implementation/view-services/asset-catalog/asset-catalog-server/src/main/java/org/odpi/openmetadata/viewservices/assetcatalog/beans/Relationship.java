/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Relationship object holds properties that are used for displaying a relationship between two assets
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Relationship extends Element
{
    /**
     * The start element for the relationship
     */
    private Element fromEntity;

    /**
     * The destination element for the relationship
     */
    private Element toEntity;


    /**
     * Default constructor.
     */
    public Relationship()
    {
    }


    /**
     * Returns the start element for the relationship.
     *
     * @return the start element for the relationship
     */
    public Element getFromEntity()
    {
        return fromEntity;
    }


    /**
     * Set up the start element for the relationship.
     *
     * @param fromEntity the start element for the relationship
     */
    public void setFromEntity(Element fromEntity)
    {
        this.fromEntity = fromEntity;
    }


    /**
     * Returns the destination element for the relationship.
     *
     * @return the destination element for the relationship
     */
    public Element getToEntity()
    {
        return toEntity;
    }


    /**
     * Set up the destination element for the relationship.
     *
     * @param toEntity the destination element for the relationship
     */
    public void setToEntity(Element toEntity)
    {
        this.toEntity = toEntity;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Relationship{" +
                       "fromEntity=" + fromEntity +
                       ", toEntity=" + toEntity +
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
        if (! (objectToCompare instanceof Relationship that))
        {
            return false;
        }
        return Objects.equals(fromEntity, that.fromEntity) && Objects.equals(toEntity, that.toEntity);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(fromEntity, toEntity);
    }
}
