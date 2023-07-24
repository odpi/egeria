/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasEntity describes an entity instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasEntity extends AtlasStruct
{
    public static final String KEY_GUID            = "guid";
    public static final String KEY_HOME_ID         = "homeId";
    public static final String KEY_IS_PROXY        = "isProxy";
    public static final String KEY_IS_INCOMPLETE   = "isIncomplete";
    public static final String KEY_PROVENANCE_TYPE = "provenanceType";
    public static final String KEY_STATUS          = "status";
    public static final String KEY_CREATED_BY      = "createdBy";
    public static final String KEY_UPDATED_BY      = "updatedBy";
    public static final String KEY_CREATE_TIME     = "createTime";
    public static final String KEY_UPDATE_TIME     = "updateTime";
    public static final String KEY_VERSION         = "version";


    private String                           guid                   = null;
    private String                           homeId                 = null;
    private boolean                          isProxy                = false;
    private boolean                          isIncomplete           = false;
    private Integer                          provenanceType         = 0;
    private AtlasInstanceStatus              status                 = AtlasInstanceStatus.ACTIVE;
    private String                           createdBy              = null;
    private String                           updatedBy              = null;
    private Date                             createTime             = null;
    private Date                             updateTime             = null;
    private Long                             version                = 0L;
    private Map<String, Object>              relationshipAttributes = null;
    private List<AtlasClassification>        classifications        = null;
    private List<AtlasTermAssignmentHeader>  meanings               = null;
    private Map<String, String>              customAttributes       = null;
    private Map<String, Map<String, Object>> businessAttributes     = null;
    private Set<String>                      labels                 = null;
    private Set<String>                      pendingTasks           = null; // read-only field i.e. value provided is ignored during entity create/update


    public AtlasEntity()
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


    public boolean getProxy()
    {
        return isProxy;
    }


    public void setProxy(boolean proxy)
    {
        isProxy = proxy;
    }


    public boolean getIncomplete()
    {
        return isIncomplete;
    }


    public void setIncomplete(boolean incomplete)
    {
        isIncomplete = incomplete;
    }


    public Integer getProvenanceType()
    {
        return provenanceType;
    }


    public void setProvenanceType(Integer provenanceType)
    {
        this.provenanceType = provenanceType;
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


    public Long getVersion()
    {
        return version;
    }


    public void setVersion(Long version)
    {
        this.version = version;
    }


    public Map<String, Object> getRelationshipAttributes()
    {
        return relationshipAttributes;
    }


    public void setRelationshipAttributes(Map<String, Object> relationshipAttributes)
    {
        this.relationshipAttributes = relationshipAttributes;
    }


    public List<AtlasClassification> getClassifications()
    {
        return classifications;
    }


    public void setClassifications(List<AtlasClassification> classifications)
    {
        this.classifications = classifications;
    }


    public List<AtlasTermAssignmentHeader> getMeanings()
    {
        return meanings;
    }


    public void setMeanings(List<AtlasTermAssignmentHeader> meanings)
    {
        this.meanings = meanings;
    }


    public Map<String, String> getCustomAttributes()
    {
        return customAttributes;
    }


    public void setCustomAttributes(Map<String, String> customAttributes)
    {
        this.customAttributes = customAttributes;
    }


    public Map<String, Map<String, Object>> getBusinessAttributes()
    {
        return businessAttributes;
    }


    public void setBusinessAttributes(Map<String, Map<String, Object>> businessAttributes)
    {
        this.businessAttributes = businessAttributes;
    }


    public Set<String> getLabels()
    {
        return labels;
    }


    public void setLabels(Set<String> labels)
    {
        this.labels = labels;
    }


    public Set<String> getPendingTasks()
    {
        return pendingTasks;
    }


    public void setPendingTasks(Set<String> pendingTasks)
    {
        this.pendingTasks = pendingTasks;
    }


    @Override
    public String toString()
    {
        return "AtlasEntity{" +
                       "guid='" + guid + '\'' +
                       ", homeId='" + homeId + '\'' +
                       ", isProxy=" + isProxy +
                       ", isIncomplete=" + isIncomplete +
                       ", provenanceType=" + provenanceType +
                       ", status=" + status +
                       ", createdBy='" + createdBy + '\'' +
                       ", updatedBy='" + updatedBy + '\'' +
                       ", createTime=" + createTime +
                       ", updateTime=" + updateTime +
                       ", version=" + version +
                       ", relationshipAttributes=" + relationshipAttributes +
                       ", classifications=" + classifications +
                       ", meanings=" + meanings +
                       ", customAttributes=" + customAttributes +
                       ", businessAttributes=" + businessAttributes +
                       ", labels=" + labels +
                       ", pendingTasks=" + pendingTasks +
                       ", typeName='" + getTypeName() + '\'' +
                       ", attributes=" + getAttributes() +
                       '}';
    }
}
