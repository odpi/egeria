/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dino.api.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DinoEngineRequestBody {


    /*
     * The DinoEngineRequestBody class provides a body for REST requests to the platform-oriented Dino REST APIs
     */

    private String                    serverName;             // mandatory
    private String                    platformName;           // mandatory
    private String                    requestContextCorrelator;
    private String                    engineQualifiedName;    // mandatory


    public DinoEngineRequestBody() {
       // No initialization yet
    }

    /*
     * Getters for Jackson
     */

    public String getServerName() { return serverName; }

    public void setServerName(String serverName) { this.serverName = serverName; }

    public String getPlatformName() { return platformName; }

    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public String getRequestContextCorrelator() { return requestContextCorrelator; }

    public void setRequestContextCorrelator(String requestContextCorrelator) { this.requestContextCorrelator = requestContextCorrelator; }

    public String getEngineQualifiedName() { return engineQualifiedName; }

    public void setEngineQualifiedName(String engineQualifiedName) { this.engineQualifiedName = engineQualifiedName; }

    @Override
    public String toString()
    {
        return "DinoEngineRequestBody{" +
                ", serverName=" + serverName +
                ", platformName=" + platformName +
                ", requestContextCorrelator=" + requestContextCorrelator +
                ", engineQualifiedName=" + engineQualifiedName +
                '}';
    }



}
