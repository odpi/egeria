/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceAuditHeader manages the attributes that are common to classifications and "proper" instances, ie
 * as entities and relationships.  We need to be able to audit when these fundamental elements change and
 * by whom.  Thus they share this header.  The fields in this header are managed as follows:
 * <ul>
 *     <li>
 *         Type defines which TypeDef defines the type of concept/thing that this instance represents and
 *         controls the properties that can be stored in the instance.
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
    /*
     * Summary information about this element's type
     */
    protected InstanceType   type = null;

    /*
     * Standard header information for a classification, entity and relationship.
     */
    protected String         createdBy         = null;
    protected String         updatedBy         = null;
    protected Date           createTime        = null;
    protected Date           updateTime        = null;
    protected long           version           = 0L;
    protected InstanceStatus currentStatus     = null;

    /*
     * Used only if the status is DELETED.  It defines the status to use if the instance is restored.
     */
    protected InstanceStatus statusOnDelete  = null;


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
            this.createdBy = template.getCreatedBy();
            this.updatedBy = template.getUpdatedBy();
            this.createTime = template.getCreateTime();
            this.updateTime = template.getUpdateTime();
            this.version = template.getVersion();
            this.currentStatus = template.getStatus();
            this.statusOnDelete = template.getStatusOnDelete();
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
     * Return the status of this instance (UNKNOWN, PROPOSED, DRAFT, ACTIVE, DELETED).
     *
     * @return InstanceStatus
     */
    public InstanceStatus getStatus() { return currentStatus; }


    /**
     * Set up the status of this instance (UNKNOWN, PROPOSED, DRAFT, ACTIVE, DELETED).
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
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "InstanceAuditHeader{" +
                "type=" + type +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                ", currentStatus=" + currentStatus +
                ", statusOnDelete=" + statusOnDelete +
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
        if (!(objectToCompare instanceof InstanceAuditHeader))
        {
            return false;
        }
        InstanceAuditHeader that = (InstanceAuditHeader) objectToCompare;
        return getVersion() == that.getVersion() &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getCreatedBy(), that.getCreatedBy()) &&
                Objects.equals(getUpdatedBy(), that.getUpdatedBy()) &&
                Objects.equals(getCreateTime(), that.getCreateTime()) &&
                Objects.equals(getUpdateTime(), that.getUpdateTime()) &&
                currentStatus == that.currentStatus &&
                getStatusOnDelete() == that.getStatusOnDelete();
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getType(),
                            getCreatedBy(),
                            getUpdatedBy(),
                            getCreateTime(),
                            getUpdateTime(),
                            getVersion(),
                            currentStatus,
                            getStatusOnDelete());
    }
}
