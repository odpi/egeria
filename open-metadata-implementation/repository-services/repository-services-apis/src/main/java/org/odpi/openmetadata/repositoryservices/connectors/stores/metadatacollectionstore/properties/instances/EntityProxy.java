/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityProxy summarizes an entity instance.  It is used to describe one of the entities connected together by a
 * relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityProxy extends EntitySummary
{
    private static final long    serialVersionUID = 1L;

    private InstanceProperties uniqueProperties = null;


    /**
     * Default constructor sets up an empty entity proxy.
     */
    public  EntityProxy()
    {
        super();
    }


    /**
     * Copy/clone constructor for the entity proxy.
     *
     * @param template entity proxy to copy
     */
    public EntityProxy(EntityProxy   template)
    {
        super(template);

        if (template != null)
        {
            this.uniqueProperties = template.getUniqueProperties();
        }
    }


    /**
     * Copy/clone constructor for the entity proxy.
     *
     * @param template entity summary to copy
     */
    public EntityProxy(EntitySummary   template)
    {
        super(template);
    }


    /**
     * Return a copy of the unique attributes for the entity.
     *
     * @return InstanceProperties iterator
     */
    public InstanceProperties getUniqueProperties()
    {
        if (uniqueProperties == null)
        {
            return null;
        }
        else if ((uniqueProperties.getInstanceProperties() == null) &&
                 (uniqueProperties.getEffectiveFromTime() == null) &&
                 (uniqueProperties.getEffectiveToTime() == null))
        {
            return null;
        }
        else
        {
            return new InstanceProperties(uniqueProperties);
        }
    }


    /**
     * Set up the list of unique properties for this entity proxy. These attributes provide properties such
     * as unique names etc that are useful to display.
     *
     * @param uniqueAttributes InstanceProperties iterator
     */
    public void setUniqueProperties(InstanceProperties uniqueAttributes) { this.uniqueProperties = uniqueAttributes; }



    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EntityProxy{" +
                "type=" + getType() +
                ", uniqueProperties=" + uniqueProperties +
                ", classifications=" + getClassifications() +
                ", instanceURL='" + getInstanceURL() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", instanceProvenanceType=" + getInstanceProvenanceType() +
                ", metadataCollectionId='" + getMetadataCollectionId() + '\'' +
                ", metadataCollectionName='" + getMetadataCollectionName() + '\'' +
                ", instanceLicense='" + getInstanceLicense() + '\'' +
                ", status=" + getStatus() +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", version=" + getVersion() +
                ", statusOnDelete=" + getStatusOnDelete() +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        EntityProxy that = (EntityProxy) objectToCompare;
        return Objects.equals(getUniqueProperties(), that.getUniqueProperties());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUniqueProperties());
    }
}
