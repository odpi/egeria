/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.List;
import java.util.Objects;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class RexTraversalRequestBody {


    /*
     * The RexTraversalRequestBody class provides a body for REST requests to perform a rex-traversal
     */

    private String                    serverName;                    // must be non-null
    private String                    platformName;                  // must be non-null
    private String                    entityGUID;                    // must be non-null, GUID of root of traversal
    private Boolean                   enterpriseOption;
    private List<String>              entityTypeGUIDs;               // a list of type guids or null
    private List<String>              relationshipTypeGUIDs;         // a list of type guids or null
    private List<String>              classificationNames;           // a list of names or null
    private Integer                   depth;                         // the depth of traversal
    private long                      asOfTime = 0;                  // as of time to issue the query. 0 means now.


    public RexTraversalRequestBody() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getServerName() { return serverName; }

    public String getPlatformName() { return platformName; }

    public String getEntityGUID() { return entityGUID; }

    public Boolean getEnterpriseOption() {
        if (enterpriseOption == null)
            return false;
        else
            return enterpriseOption;
    }

    public List<String> getEntityTypeGUIDs() { return entityTypeGUIDs; }

    public List<String> getRelationshipTypeGUIDs() {
        return relationshipTypeGUIDs;
    }

    public List<String> getClassificationNames() {
        return classificationNames;
    }

    public Integer getDepth() { return depth; }

    public long getAsOfTime() {
        return asOfTime;
    }

    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public void setEntityGUID(String entityGUID) { this.entityGUID = entityGUID; }

    public void setEnterpriseOption(Boolean enterpriseOption) { this.enterpriseOption = enterpriseOption; }

    public void setEntityTypeGUIDs(List<String> entityTypeGUIDs) { this.entityTypeGUIDs = entityTypeGUIDs; }

    public void setRelationshipTypeGUIDs(List<String> relationshipTypeGUIDs) {
        this.relationshipTypeGUIDs = relationshipTypeGUIDs;
    }

    public void setClassificationNames(List<String> classificationNames) {
        this.classificationNames = classificationNames;
    }

    public void setDepth(Integer depth) { this.depth = depth; }

    public void setAsOfTime(long asOfTime) {
        this.asOfTime = asOfTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RexTraversalRequestBody)) return false;
        RexTraversalRequestBody that = (RexTraversalRequestBody) o;

        return getAsOfTime() == that.getAsOfTime() && Objects.equals(getServerName(), that.getServerName())  && Objects.equals(getPlatformName(), that.getPlatformName()) && Objects.equals(getEntityGUID(), that.getEntityGUID()) && Objects.equals(getEnterpriseOption(), that.getEnterpriseOption()) && Objects.equals(getEntityTypeGUIDs(), that.getEntityTypeGUIDs()) && Objects.equals(getRelationshipTypeGUIDs(), that.getRelationshipTypeGUIDs()) && Objects.equals(getClassificationNames(), that.getClassificationNames()) && Objects.equals(getDepth(), that.getDepth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerName(), getPlatformName(), getEntityGUID(), getEnterpriseOption(), getEntityTypeGUIDs(), getRelationshipTypeGUIDs(), getClassificationNames(), getDepth(), getAsOfTime());
    }


    @Override
    public String toString()
    {
        return "RexTraversalRequestBody{" +
                ", serverName=" + serverName +
                ", platformName=" + platformName +
                ", entityGUID=" + entityGUID +
                ", depth=" + depth +
                ", enterpriseOption=" + enterpriseOption +
                ", entityTypeGUIDs=" + entityTypeGUIDs +
                ", relationshipTypeGUIDs=" + relationshipTypeGUIDs +
                ", classificationNames=" + classificationNames +
                '}';
    }



}
