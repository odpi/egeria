/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelationship describes a relationship instance in Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasRelationship extends AtlasStruct
{
    public static final String KEY_GUID             = "guid";
    public static final String KEY_HOME_ID          = "homeId";
    public static final String KEY_PROVENANCE_TYPE = "provenanceType";
    public static final String KEY_STATUS           = "status";
    public static final String KEY_CREATED_BY       = "createdBy";
    public static final String KEY_UPDATED_BY       = "updatedBy";
    public static final String KEY_CREATE_TIME      = "createTime";
    public static final String KEY_UPDATE_TIME      = "updateTime";
    public static final String KEY_VERSION          = "version";
    public static final String KEY_END1             = "end1";
    public static final String KEY_END2             = "end2";
    public static final String KEY_LABEL            = "label";
    public static final String KEY_PROPAGATE_TAGS   = "propagateTags";

    public static final String KEY_BLOCKED_PROPAGATED_CLASSIFICATIONS = "blockedPropagatedClassifications";
    public static final String KEY_PROPAGATED_CLASSIFICATIONS         = "propagatedClassifications";

    private String                   guid                             = null;
    private String                   homeId                           = null;
    private Integer                  provenanceType                   = null;
    private AtlasObjectId            end1                             = null;
    private AtlasObjectId            end2                             = null;
    private String                   label                            = null;
    private AtlasPropagateTags       propagateTags                    = AtlasPropagateTags.NONE;
    private AtlasInstanceStatus      status                           = AtlasInstanceStatus.ACTIVE;
    private String                   createdBy                        = null;
    private String                   updatedBy                        = null;
    private Date                     createTime                       = null;
    private Date                     updateTime                       = null;
    private long                     version                          = 0L;
    private Set<AtlasClassification> propagatedClassifications        = null;
    private Set<AtlasClassification> blockedPropagatedClassifications = null;


    public AtlasRelationship()
    {
    }


    public String getGuid()
    {
        return guid;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public String getHomeId()
    {
        return homeId;
    }


    public void setHomeId(String homeId)
    {
        this.homeId = homeId;
    }


    public Integer getProvenanceType()
    {
        return provenanceType;
    }


    public void setProvenanceType(Integer provenanceType)
    {
        this.provenanceType = provenanceType;
    }


    public AtlasObjectId getEnd1()
    {
        return end1;
    }


    public void setEnd1(AtlasObjectId end1)
    {
        this.end1 = end1;
    }


    public AtlasObjectId getEnd2()
    {
        return end2;
    }


    public void setEnd2(AtlasObjectId end2)
    {
        this.end2 = end2;
    }


    public String getLabel()
    {
        return label;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public AtlasPropagateTags getPropagateTags()
    {
        return propagateTags;
    }


    public void setPropagateTags(AtlasPropagateTags propagateTags)
    {
        this.propagateTags = propagateTags;
    }


    public AtlasInstanceStatus getStatus()
    {
        return status;
    }


    public void setStatus(AtlasInstanceStatus status)
    {
        this.status = status;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }


    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    public String getUpdatedBy()
    {
        return updatedBy;
    }


    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }


    public Date getCreateTime()
    {
        return createTime;
    }


    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    public Date getUpdateTime()
    {
        return updateTime;
    }


    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    public long getVersion()
    {
        return version;
    }


    public void setVersion(long version)
    {
        this.version = version;
    }


    public Set<AtlasClassification> getPropagatedClassifications()
    {
        return propagatedClassifications;
    }


    public void setPropagatedClassifications(Set<AtlasClassification> propagatedClassifications)
    {
        this.propagatedClassifications = propagatedClassifications;
    }


    public Set<AtlasClassification> getBlockedPropagatedClassifications()
    {
        return blockedPropagatedClassifications;
    }


    public void setBlockedPropagatedClassifications(Set<AtlasClassification> blockedPropagatedClassifications)
    {
        this.blockedPropagatedClassifications = blockedPropagatedClassifications;
    }


    @Override
    public String toString()
    {
        return "AtlasRelationship{" +
                       "guid='" + guid + '\'' +
                       ", homeId='" + homeId + '\'' +
                       ", provenanceType=" + provenanceType +
                       ", end1=" + end1 +
                       ", end2=" + end2 +
                       ", label='" + label + '\'' +
                       ", propagateTags=" + propagateTags +
                       ", status=" + status +
                       ", createdBy='" + createdBy + '\'' +
                       ", updatedBy='" + updatedBy + '\'' +
                       ", createTime=" + createTime +
                       ", updateTime=" + updateTime +
                       ", version=" + version +
                       ", propagatedClassifications=" + propagatedClassifications +
                       ", blockedPropagatedClassifications=" + blockedPropagatedClassifications +
                       '}';
    }
}
