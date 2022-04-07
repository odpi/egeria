/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class RexRelationshipRequestBody {


    /*
     * The RexRelationshipRequestBody class provides a body for REST requests to retrieve relationships by GUID
     */

    private String                    serverName;                    // must be non-null
    private String                    platformName;                  // must be non-null
    private String                    relationshipGUID;              // must be non-null, GUID of root of traversal
    private Boolean                   enterpriseOption;
    private Long                      asOfTime;                      // as of time to issue the query. null means now.


    public RexRelationshipRequestBody() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getServerName() { return serverName; }

    public String getPlatformName() { return platformName; }

    public String getRelationshipGUID() { return relationshipGUID; }

    public Boolean getEnterpriseOption()
    {
        if (enterpriseOption == null)
            return false;
        else
            return enterpriseOption;
    }

    public Long getAsOfTime() {
        return asOfTime;
    }



    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public void setRelationshipGUID(String relationshipGUID) { this.relationshipGUID = relationshipGUID; }

    public void setEnterpriseOption(Boolean enterpriseOption) { this.enterpriseOption = enterpriseOption; }

    public void setAsOfTime(Long asOfTime) {
        this.asOfTime = asOfTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RexRelationshipRequestBody)) return false;
        RexRelationshipRequestBody that = (RexRelationshipRequestBody) o;
        return getAsOfTime() == that.getAsOfTime() && Objects.equals(getServerName(), that.getServerName()) && Objects.equals(getPlatformName(), that.getPlatformName()) && Objects.equals(getRelationshipGUID(), that.getRelationshipGUID()) && Objects.equals(getEnterpriseOption(), that.getEnterpriseOption());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerName(), getPlatformName(), getRelationshipGUID(), getEnterpriseOption(), getAsOfTime());
    }

    @Override
    public String toString()
    {
        return "RexRelationshipRequestBody{" +
                ", serverName=" + serverName +
                ", platformName=" + platformName +
                ", relationshipGUID=" + relationshipGUID +
                ", enterpriseOption=" + enterpriseOption +
                ", asOfTime=" + asOfTime +
                '}';
    }



}
