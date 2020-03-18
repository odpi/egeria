/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexSearchBody {


    /*
     * The RexTraversalRequestBody class provides a body for REST requests to perform a rex-traversal
     */

    private String                    serverName;                    // must be non-null
    private String                    serverURLRoot;                 // must be non-null
    private String                    searchText;                    // must be non-null
    private Boolean                   enterpriseOption;
    private String                    typeName;                      // filter by type, or null
    private Integer                   gen;                           // indicator of the current gen of the traversal


    public RexSearchBody() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getServerName() { return serverName; }

    public String getServerURLRoot() { return serverURLRoot; }

    public String getSearchText() { return searchText; }

    public String getTypeName() { return typeName; }

    public Boolean getEnterpriseOption() { return enterpriseOption; }

    public Integer getGen() { return gen; }


    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setServerURLRoot(String serverURLRoot) { this.serverURLRoot = serverURLRoot; }

    public void setSearchText(String searchText) { this.searchText = searchText; }

    public void setTypeName(String typeName) { this.typeName = typeName; }

    public void setEnterpriseOption(Boolean enterpriseOption) { this.enterpriseOption = enterpriseOption; }

    public void setGen(Integer gen) { this.gen = gen; }





    @Override
    public String toString()
    {
        return "RexSearchBody{" +
                ", serverName=" + serverName +
                ", serverURLRoot=" + serverURLRoot +
                ", searchText=" + searchText +
                ", enterpriseOption=" + enterpriseOption +
                ", typeName=" + typeName +
                ", gen=" + gen +
                '}';
    }



}
