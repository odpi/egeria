/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;


import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceHeader manages the attributes that are common to entities and relationship instances.  This includes
 * its unique identifier and URL along with information about its type, provenance and change history.
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
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * Entities and relationships have unique identifiers.
     */
    private String                    guid                   = null;

    /*
     * Some metadata repositories offer a direct URL to access the instance.
     */
    private String                    instanceURL            = null;

    /*
     * If this instance has been re-identified (its GUID changed), then this refers to the previous GUID by
     * which it was known (for full auditability).
     */
    private String                    reIdentifiedFromGUID   = null;

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
            this.guid = template.getGUID();
            this.instanceURL = template.getInstanceURL();
            this.reIdentifiedFromGUID = template.getReIdentifiedFromGUID();
        }
    }

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
     * Return the unique identifier by which this instance was previously known.
     *
     * @return guid String unique identifier
     */
    public String getReIdentifiedFromGUID()
    {
        return reIdentifiedFromGUID;
    }


    /**
     * Set up the unique identifier by which t his instance was previously known.
     *
     * @param reIdentifiedFromGUID String unique identifier
     */
    public void setReIdentifiedFromGUID(String reIdentifiedFromGUID)
    {
        this.reIdentifiedFromGUID = reIdentifiedFromGUID;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString() {
        return "InstanceHeader{" +
                "headerVersion=" + getHeaderVersion() +
                ", type=" + getType() +
                ", instanceProvenanceType=" + getInstanceProvenanceType() +
                ", metadataCollectionId='" + getMetadataCollectionId() + '\'' +
                ", metadataCollectionName='" + getMetadataCollectionName() + '\'' +
                ", replicatedBy='" + getReplicatedBy() + '\'' +
                ", instanceLicense='" + getInstanceLicense() + '\'' +
                ", status=" + getStatus() +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", maintainedBy=" + getMaintainedBy() +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", version=" + getVersion() +
                ", statusOnDelete=" + getStatusOnDelete() +
                ", mappingProperties=" + getMappingProperties() +
                ", guid='" + guid + '\'' +
                ", instanceURL='" + instanceURL + '\'' +
                ", reIdentifiedFromGUID='" + reIdentifiedFromGUID + '\'' +
                ", GUID='" + getGUID() + '\'' +
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
        InstanceHeader that = (InstanceHeader) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(getInstanceURL(), that.getInstanceURL()) &&
                Objects.equals(getReIdentifiedFromGUID(), that.getReIdentifiedFromGUID());
    }



    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), guid, getInstanceURL(), reIdentifiedFromGUID);
    }
}
