/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceHeader manages the attributes that are common to entities and relationship instances.  This includes
 * information abut its type, provenance and change history.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntitySummary.class, name = "EntitySummary"),
        @JsonSubTypes.Type(value = Relationship.class, name = "Relationship")
})
public abstract class InstanceHeader extends InstanceAuditHeader
{
    /*
     * Provenance information defining where the instance came from and whether this is a master or reference copy.
     */
    private InstanceProvenanceType    instanceProvenanceType = null;
    private String                    metadataCollectionId   = null;

    /*
     * Entities and relationships have unique identifiers.
     */
    private String                    guid                   = null;

    /*
     * Some metadata repositories offer a direct URL to access the instance.
     */
    private String                    instanceURL            = null;

    /**
     * Default Constructor sets the instance to nulls.
     */
    public InstanceHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor set the value to those supplied in the template.
     *
     * @param template Instance header
     */
    public InstanceHeader(InstanceHeader    template)
    {
        super(template);

        if (template != null)
        {
            this.metadataCollectionId = template.getMetadataCollectionId();
            this.instanceProvenanceType = template.getInstanceProvenanceType();
            this.guid = template.getGUID();
            this.instanceURL = template.getInstanceURL();
        }
    }


    /**
     * Return the type of the provenance for this instance (UNKNOWN, LOCAL_COHORT, EXPORT_ARCHIVE, CONTENT_PACK,
     * DEREGISTERED_REPOSITORY, CONFIGURATION).
     *
     * @return InstanceProvenanceType enum
     */
    public InstanceProvenanceType getInstanceProvenanceType() { return instanceProvenanceType; }


    /**
     * Set up the type of the provenance for this instance (UNKNOWN, LOCAL_COHORT, EXPORT_ARCHIVE, CONTENT_PACK,
     * DEREGISTERED_REPOSITORY, CONFIGURATION).
     *
     * @param instanceProvenanceType InstanceProvenanceType enum
     */
    public void setInstanceProvenanceType(InstanceProvenanceType instanceProvenanceType)
    {
        this.instanceProvenanceType = instanceProvenanceType;
    }


    /**
     * Return the unique identifier for the metadata collection that is the home for this instance.
     * If the metadataCollectionId is null it means this instance belongs to the local metadata collection.
     *
     * @return metadataCollectionId String unique identifier for the repository
     */
    public String getMetadataCollectionId() { return metadataCollectionId; }


    /**
     * Set up the unique identifier for the home metadata collection for this instance.
     * If the metadataCollectionId is null it means this instance belongs to the local metadata collection.
     *
     * @param metadataCollectionId String unique identifier for the repository
     */
    public void setMetadataCollectionId(String metadataCollectionId) { this.metadataCollectionId = metadataCollectionId; }


    /**
     * Return the URL for this instance (or null if the metadata repository does not support instance URLs).
     *
     * @return String URL
     */
    public String getInstanceURL()
    {
        return instanceURL;
    }


    /**
     * Set up the URL for this instance (or null if the metadata repository does not support instance URLs).
     *
     * @param instanceURL String URL
     */
    public void setInstanceURL(String instanceURL)
    {
        this.instanceURL = instanceURL;
    }


    /**
     * Return the unique identifier for this instance.
     *
     * @return guid String unique identifier
     */
    public String getGUID() { return guid; }


    /**
     * Set up the unique identifier for this instance.
     *
     * @param guid String unique identifier
     */
    public void setGUID(String guid) { this.guid = guid; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "InstanceHeader{" +
                "GUID='" + getGUID() + '\'' +
                ", type=" + getType() +
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
        if (!(objectToCompare instanceof InstanceHeader))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        InstanceHeader that = (InstanceHeader) objectToCompare;
        return getInstanceProvenanceType() == that.getInstanceProvenanceType() &&
                Objects.equals(getMetadataCollectionId(), that.getMetadataCollectionId()) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(getInstanceURL(), that.getInstanceURL());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(),
                            getInstanceProvenanceType(),
                            getMetadataCollectionId(),
                            guid,
                            getInstanceURL());
    }
}
