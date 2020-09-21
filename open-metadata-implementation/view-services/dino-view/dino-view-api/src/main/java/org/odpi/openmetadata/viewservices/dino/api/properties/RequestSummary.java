/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestSummary {

    private static final long    serialVersionUID = 1L;

    /*
     * A RequestSummary object captures the essence of a request and is passed back in response.
     * It is stored in the gen into which the response is stored.
     */

    private String                          platformName;
    private String                          operation;        // TODO - this could be an Enum - but initially it is a String {"loadPlatform" | "loadServer" | ... }
    private String                          serverName;


    /**
     * Default Constructor sets the properties to nulls
     */
    public RequestSummary()
    {
        /*
         * Nothing to do.
         */
    }

    public RequestSummary(String platformName, String serverName, String operation) {

        this.platformName   = platformName;
        this.serverName     = serverName;
        this.operation      = operation;

    }

    public String getPlatformName() {  return platformName;  }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getServerName() {  return serverName;  }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getOperation() {  return operation;  }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RequestSummary{" +
                "platformName='" + getPlatformName() + '\'' +
                ", serverName='" + getServerName() + '\'' +
                ", operation='" + getOperation() + '\'' +
                '}';
    }

}
