/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.api.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TexTypesRequestBody {


    /*
     * The TexTypesRequestBody class provides a body for REST requests to perform a load of type information
     */

    private String                    serverName;                    // must be non-null
    private String                    platformName;                  // must be non-null
    private Boolean                   enterpriseOption;              // if not set will default to false
    private Boolean                   deprecationOption;             // if not set will default to false


    public TexTypesRequestBody() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getServerName() { return serverName; }

    public String getPlatformName() { return platformName; }

    public Boolean getEnterpriseOption()
    {
        if (enterpriseOption == null)
            return false;
        else
            return enterpriseOption;
    }

    public Boolean getDeprecationOption() {
        if (deprecationOption == null)
            return false;
        else
            return deprecationOption;
    }


    public void setServerName(String serverName) { this.serverName = serverName; }

    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public void setEnterpriseOption(Boolean enterpriseOption) { this.enterpriseOption = enterpriseOption; }

    public void setDeprecationOption(Boolean deprecationOption) { this.deprecationOption = deprecationOption; }


    @Override
    public String toString()
    {
        return "TexTypesRequestBody{" +
                ", serverName=" + serverName +
                ", platformName=" + platformName +
                ", enterpriseOption=" + enterpriseOption +
                ", deprecationOption=" + deprecationOption +
                '}';
    }



}
