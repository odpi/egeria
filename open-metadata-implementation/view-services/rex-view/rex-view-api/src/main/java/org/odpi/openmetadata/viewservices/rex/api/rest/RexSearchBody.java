/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class RexSearchBody {



    /*
     * The RexTraversalRequestBody class provides a body for REST requests to perform a rex-traversal
     */

    private String                    serverName;                    // must be non-null
    private String                    platformName;                  // must be non-null
    private String                    searchText;                    // must be non-null
    private Boolean                   enterpriseOption;
    private String                    typeName;                      // filter by type, or null
    private List<String>              classificationNames;           // Limit results of entity searches to instances with at least one of these classifications
    private Long                      asOfTime;                      // as of time to issue the query. null means now.

    public RexSearchBody() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getServerName() { return serverName; }

    public String getPlatformName() { return platformName; }

    public String getSearchText() { return searchText; }

    public String getTypeName() { return typeName; }

    public List<String> getClassificationNames() { return classificationNames; }

    public Long getAsOfTime() {
        return asOfTime;
    }

    public Boolean getEnterpriseOption() {
        if (enterpriseOption == null)
            return false;
        else
            return enterpriseOption;
    }


    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public void setSearchText(String searchText) { this.searchText = searchText; }

    public void setTypeName(String typeName) { this.typeName = typeName; }

    public void setClassificationNames(List<String> classificationNames ) { this.classificationNames = classificationNames; }

    public void setEnterpriseOption(Boolean enterpriseOption) { this.enterpriseOption = enterpriseOption; }

    public void setAsOfTime(Long asOfTime) {
        this.asOfTime = asOfTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RexSearchBody)) return false;
        RexSearchBody that = (RexSearchBody) o;
        Long myAsOfTime = getAsOfTime();
        Long theirAsOfTime = that.getAsOfTime();
        if (myAsOfTime == null && theirAsOfTime !=null) {
            return false;
        } else  if (myAsOfTime != null && theirAsOfTime ==null) {
            return false;
        } else  if (myAsOfTime != null && theirAsOfTime !=null) {
            if (Long.valueOf(myAsOfTime) != Long.valueOf(theirAsOfTime)) {
                return false;
            }
        }
        return Objects.equals(getServerName(), that.getServerName()) && Objects.equals(getPlatformName(), that.getPlatformName()) && Objects.equals(getSearchText(), that.getSearchText()) && Objects.equals(getEnterpriseOption(), that.getEnterpriseOption()) && Objects.equals(getTypeName(), that.getTypeName()) && Objects.equals(getClassificationNames(), that.getClassificationNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerName(), getPlatformName(), getSearchText(), getEnterpriseOption(), getTypeName(), getClassificationNames(), getAsOfTime());
    }

    @Override
    public String toString() {
        return "RexSearchBody{" +
                "serverName='" + serverName + '\'' +
                ", platformName='" + platformName + '\'' +
                ", searchText='" + searchText + '\'' +
                ", enterpriseOption=" + enterpriseOption +
                ", typeName='" + typeName + '\'' +
                ", classificationNames=" + classificationNames +
                ", asOfTime=" + asOfTime +
                '}';
    }

}
