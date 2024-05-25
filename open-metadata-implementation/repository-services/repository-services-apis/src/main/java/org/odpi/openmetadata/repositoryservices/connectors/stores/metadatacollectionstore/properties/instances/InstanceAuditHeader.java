/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceAuditHeader manages the attributes that are common to classifications,
 * entities and relationships.  We need to be able to audit when these fundamental elements change and
 * by whom.  Thus, they share this header.  The fields in this header are managed as follows:
 * <ul>
 *     <li>
 *         Type identifies which TypeDef defines the type of concept/thing that this instance represents and
 *         controls the properties that can be stored in the instance.
 *     </li>
 *     <li>
 *         InstanceProvenanceType defines the type of metadata collection that this instance originated from.
 *     </li>
 *     <li>
 *         MetadataCollectionId defines which metadata collection that this instance came from - that is its home.
 *     </li>
 *     <li>
 *         MetadataCollectionName defines the display name for the home metadata collection.
 *     </li>
 *     <li>
 *         InstanceLicense defines a specific license that applies to this instance - null means no restrictions.
 *     </li>
 *     <li>
 *         CreatedBy contains the userId of the person/engine that created the instance.  This field is set
 *         up when the instance is created and not changed.  It typically indicates who is the owner of the
 *         element and hence who has update rights.
 *     </li>
 *     <li>
 *         UpdatedBy contains the userId of the person/engine that last updated the instance.  This field is
 *         set automatically by the open metadata repository when the update happens.
 *     </li>
 *     <li>
 *         MaintainedBy contains the list of userIds of the person/engine that are responsible for maintained this
 *         instance.  Null means that the maintainer is the creator.
 *     </li>
 *     <li>
 *         CreateTime contains the Date/Time when the instance was created.  It is set automatically by the
 *         open metadata repository when the instance is created.
 *     </li>
 *     <li>
 *         UpdateTime contains the Data/Time when the instance was last updated.  It is also set automatically
 *         by the open metadata repository when the update happens.
 *     </li>
 *     <li>
 *         Version is a numeric count of the updates to the instance.  It is used by the open metadata repositories
 *         to ensure updates to reference copies of the instance are applied in the right sequence.  The home open
 *         metadata repository (where the create an all subsequent updates happen) maintains the version number.
 *     </li>
 *     <li>
 *         CurrentStatus indicates the status of the instance.  It is initialized by the open metadata repository
 *         to the first status defined in the TypeDef.  After that, it is the actions of the consumers of the
 *         metadata (typically the Open Metadata Access Services (OMASs)
 *     </li>
 *     <li>
 *         StatusOnDelete is populated when the instance is deleted and is se to the status when the deleted was
 *         called - it is used set the status if the instance is restored.
 *     </li>
 *     <li>
 *         MappingProperties is used by connector implementations that are mapping between an existing repository
 *         and open metadata. It provides space for the connector to stash identifiers and other values that help
 *         them to map instances stored with the open metadata equivalent.  These values should be maintained by the
 *         master repository and stored by any repository that is saving the reference copy.
 *     </li>
 * </ul>
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Classification.class, name = "Classification"),
        @JsonSubTypes.Type(value = InstanceHeader.class, name = "InstanceHeader")
})
public abstract class InstanceAuditHeader extends InstanceElementHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Version of the header used in this release
     */
    public static final long CURRENT_AUDIT_HEADER_VERSION = 1;

    /*
     * Summary information about this element's type
     */
    private InstanceType   type = null;

    /*
     * Provenance information defining where the instance came from and whether this is a master or reference copy.
     */
    private InstanceProvenanceType    instanceProvenanceType = null;
    private String                    metadataCollectionId   = null;
    private String                    metadataCollectionName = null;
    private String                    replicatedBy           = null;
    private String                    instanceLicense        = null;


    /*
     * Standard header information for a classification, entity and relationship.
     */
    private String         createdBy         = null;
    private String         updatedBy         = null;
    private List<String>   maintainedBy      = null;
    private Date           createTime        = null;
    private Date           updateTime        = null;
    private long           version           = 0L;
    private InstanceStatus currentStatus     = null;

    /*
     * Used only if the status is DELETED.  It defines the status to use if the instance is restored.
     */
    private InstanceStatus statusOnDelete  = null;

    /*
     * Used by connector implementations that are mapping between an existing repository and open metadata.
     * It provides space for the connector to stash identifiers and other values that help them to map
     * instances stored with the open metadata equivalent.  These values should be maintained by the
     * master repository and stored by any repository that is saving the reference copy.
     */
    private Map<String, Serializable>  mappingProperties = null;

    /**
     * Default Constructor sets the instance to nulls.
     */
    public InstanceAuditHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor set the value to those supplied in the template.
     *
     * @param template Instance header
     */
    public InstanceAuditHeader(InstanceAuditHeader template)
    {
        super(template);

        if (template != null)
        {
            this.type = template.getType();
            this.instanceProvenanceType = template.getInstanceProvenanceType();
            this.metadataCollectionId = template.getMetadataCollectionId();
            this.metadataCollectionName = template.getMetadataCollectionName();
            this.replicatedBy = template.getReplicatedBy();
            this.instanceLicense = template.getInstanceLicense();
            this.createdBy = template.getCreatedBy();
            this.updatedBy = template.getUpdatedBy();
            this.maintainedBy = template.getMaintainedBy();
            this.createTime = template.getCreateTime();
            this.updateTime = template.getUpdateTime();
            this.version = template.getVersion();
            this.currentStatus = template.getStatus();
            this.statusOnDelete = template.getStatusOnDelete();
            this.mappingProperties = template.getMappingProperties();
        }
    }


    /**
     * Return the type of this instance.  This identifies the type definition (TypeDef) that determines its properties.
     *
     * @return InstanceType object
     */
    public InstanceType getType()
    {
        if (type == null)
        {
            return null;
        }
        else
        {
            return new InstanceType(type);
        }
    }


    /**
     * Set up the type of this instance.  This identifies the type definition (TypeDef) that determines its properties.
     *
     * @param type InstanceType object
     */
    public void setType(InstanceType type)
    {
        this.type = type;
    }


    /**
     * Return the type of the provenance for this instance.
     *
     * @return InstanceProvenanceType enum
     */
    public InstanceProvenanceType getInstanceProvenanceType() { return instanceProvenanceType; }


    /**
     * Set up the type of the provenance for this instance.
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
     * Return a display name for the metadata collection that this instance belongs to.  The source of this name is
     * dependent on the type of origin it has.  For example, this may be the
     * name of the server where the metadata is hosted, the archive that the instance came from or the name
     * of the tool, platform or engine that originated the metadata.
     *
     * @return display name or null
     */
    public String getMetadataCollectionName()
    {
        return metadataCollectionName;
    }


    /**
     * Set up a display name for the metadata collection that this instance belongs to.  The source of this name is
     * dependent on the type of origin it has.  For example, this may be the
     * name of the server where the metadata is hosted, the archive that the instance came from or the name
     * of the tool, platform or engine that originated the metadata.
     *
     * @param metadataCollectionName display name or null
     */
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        this.metadataCollectionName = metadataCollectionName;
    }


    /**
     * Return the metadata collection id of the cohort member responsible for replicating metadata
     * owned by repositories (eg data tools/engines/platforms) from outside the
     * cohort or from an open metadata archive.  Null means the metadata is owned
     * by a cohort member, or is not to be replicated.
     *
     * @return string metadata collection id
     */
    public String getReplicatedBy()
    {
        return replicatedBy;
    }


    /**
     * Set up the metadata collection id of the cohort member responsible for replicating metadata
     * owned by repositories (eg data tools/engines/platforms) from outside the
     * cohort or from an open metadata archive.  Null means the metadata is owned
     * by a cohort member, or is not to be replicated.
     *
     * @param replicatedBy string metadata collection id
     */
    public void setReplicatedBy(String replicatedBy)
    {
        this.replicatedBy = replicatedBy;
    }


    /**
     * Return the license string for this instance - null means no restrictions.
     *
     * @return license string or null
     */
    public String getInstanceLicense()
    {
        return instanceLicense;
    }


    /**
     * Set up the license string for this instance - null means no restrictions.
     *
     * @param instanceLicense license string or null
     */
    public void setInstanceLicense(String instanceLicense)
    {
        this.instanceLicense = instanceLicense;
    }


    /**
     * Return the status of this instance.
     *
     * @return InstanceStatus
     */
    public InstanceStatus getStatus() { return currentStatus; }


    /**
     * Set up the status of this instance.
     *
     * @param newStatus InstanceStatus
     */
    public void setStatus(InstanceStatus newStatus) { this.currentStatus = newStatus; }


    /**
     * Return the name of the user that created this instance.
     *
     * @return String user name
     */
    public String getCreatedBy() { return createdBy; }


    /**
     * Set up the name of the user that created this instance.
     *
     * @param createdBy String user name
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }


    /**
     * Return the name of the user that last updated this instance.
     *
     * @return String user name
     */
    public String getUpdatedBy() { return updatedBy; }


    /**
     * Set up the name of the user that last updated this instance.
     *
     * @param updatedBy String user name
     */
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }


    /**
     * Return the list of users responsible for maintaining this instance.
     *
     * @return list of user identifiers
     */
    public List<String> getMaintainedBy()
    {
        if (maintainedBy == null)
        {
            return null;
        }
        else if (maintainedBy.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(maintainedBy);
        }
    }


    /**
     * Set up the list of users responsible for maintaining this instance.
     *
     * @param maintainedBy list of user identifiers
     */
    public void setMaintainedBy(List<String> maintainedBy)
    {
        this.maintainedBy = maintainedBy;
    }


    /**
     * Return the date/time that this instance was created.
     *
     * @return Date/Time of creation
     */
    public Date getCreateTime()
    {
        if (createTime == null)
        {
            return null;
        }
        else
        {
            return new Date(createTime.getTime());
        }
    }


    /**
     * Set up the time that this instance was created.
     *
     * @param createTime Date/Time of creation
     */
    public void setCreateTime(Date createTime) { this.createTime = createTime; }


    /**
     * Return what was the late time this instance was updated.
     *
     * @return Date/Time last updated
     */
    public Date getUpdateTime()
    {
        if (updateTime == null)
        {
            return null;
        }
        else
        {
            return new Date(updateTime.getTime());
        }
    }


    /**
     * Set up the last update time for this instance.
     *
     * @param updateTime Date/Time last updated
     */
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }


    /**
     * Return the version number for this instance.
     *
     * @return Long version number
     */
    public long getVersion() { return version; }


    /**
     * Set up the version number for this instance.
     *
     * @param version Long version number
     */
    public void setVersion(long version) { this.version = version; }



    /**
     * Return the status to use when a deleted instance is restored.  UNKNOWN is used whenever the instance is
     * not in DELETED status.
     *
     * @return InstanceStatus
     */
    public InstanceStatus getStatusOnDelete() { return statusOnDelete; }


    /**
     * Set up the status to use when a deleted instance is restored.  UNKNOWN is used whenever the instance is
     * not in DELETED status.
     *
     * @param statusOnDelete InstanceStatus Enum
     */
    public void setStatusOnDelete(InstanceStatus statusOnDelete) { this.statusOnDelete = statusOnDelete; }


    /**
     * Return the additional properties used by the master repository to map to stored instances.
     *
     * @return property map
     */
    public Map<String, Serializable> getMappingProperties()
    {
        if (mappingProperties == null)
        {
            return null;
        }
        else if (mappingProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(mappingProperties);
        }
    }


    /**
     * Set up the additional properties used by the master repository to map to stored instances.
     *
     * @param mappingProperties property map
     */
    public void setMappingProperties(Map<String, Serializable> mappingProperties)
    {
        this.mappingProperties = mappingProperties;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString() {
        return "InstanceAuditHeader{" +
                "headerVersion=" + getHeaderVersion() +
                ", type=" + type +
                ", instanceProvenanceType=" + instanceProvenanceType +
                ", metadataCollectionId='" + metadataCollectionId + '\'' +
                ", metadataCollectionName='" + metadataCollectionName + '\'' +
                ", replicatedBy='" + replicatedBy + '\'' +
                ", instanceLicense='" + instanceLicense + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", maintainedBy=" + maintainedBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", currentStatus=" + currentStatus +
                ", statusOnDelete=" + statusOnDelete +
                ", mappingProperties=" + mappingProperties +
                ", status=" + getStatus() +
                '}';
    }

    /**
     * Validate if the supplied object equals this object.
     *
     * @param objectToCompare test object
     * @return boolean evaluation
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof InstanceAuditHeader))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        InstanceAuditHeader that = (InstanceAuditHeader) objectToCompare;

        if (version != that.version)
        {
            return false;
        }
        if (type != null ? ! type.equals(that.type) : that.type != null)
        {
            return false;
        }
        if (instanceProvenanceType != that.instanceProvenanceType)
        {
            return false;
        }
        if (metadataCollectionId != null ? ! metadataCollectionId.equals(that.metadataCollectionId) : that.metadataCollectionId != null)
        {
            return false;
        }
        if (metadataCollectionName != null ? ! metadataCollectionName.equals(that.metadataCollectionName) : that.metadataCollectionName != null)
        {
            return false;
        }
        if (replicatedBy != null ? ! replicatedBy.equals(that.replicatedBy) : that.replicatedBy != null)
        {
            return false;
        }
        if (instanceLicense != null ? ! instanceLicense.equals(that.instanceLicense) : that.instanceLicense != null)
        {
            return false;
        }
        if (createdBy != null ? ! createdBy.equals(that.createdBy) : that.createdBy != null)
        {
            return false;
        }
        if (updatedBy != null ? ! updatedBy.equals(that.updatedBy) : that.updatedBy != null)
        {
            return false;
        }
        if (maintainedBy != null ? ! maintainedBy.equals(that.maintainedBy) : that.maintainedBy != null)
        {
            return false;
        }
        if (createTime != null ? ! createTime.equals(that.createTime) : that.createTime != null)
        {
            return false;
        }
        if (updateTime != null ? ! updateTime.equals(that.updateTime) : that.updateTime != null)
        {
            return false;
        }
        if (currentStatus != that.currentStatus)
        {
            return false;
        }
        if (statusOnDelete != that.statusOnDelete)
        {
            return false;
        }
        return mappingProperties != null ? mappingProperties.equals(that.mappingProperties) : that.mappingProperties == null;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (instanceProvenanceType != null ? instanceProvenanceType.hashCode() : 0);
        result = 31 * result + (metadataCollectionId != null ? metadataCollectionId.hashCode() : 0);
        result = 31 * result + (metadataCollectionName != null ? metadataCollectionName.hashCode() : 0);
        result = 31 * result + (replicatedBy != null ? replicatedBy.hashCode() : 0);
        result = 31 * result + (instanceLicense != null ? instanceLicense.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + (maintainedBy != null ? maintainedBy.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (currentStatus != null ? currentStatus.hashCode() : 0);
        result = 31 * result + (statusOnDelete != null ? statusOnDelete.hashCode() : 0);
        result = 31 * result + (mappingProperties != null ? mappingProperties.hashCode() : 0);
        return result;
    }
}
