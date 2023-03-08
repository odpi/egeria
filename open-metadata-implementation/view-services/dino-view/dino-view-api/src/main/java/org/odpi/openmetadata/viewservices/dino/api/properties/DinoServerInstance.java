/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.adminservices.rest.ServerTypeClassificationSummary;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DinoServerInstance {

    private static final long    serialVersionUID = 1L;

    /*
     * A DinoServerInstance is used to include a brief summary of a server to be included into the serverList in response to a request
     * to the platform to list its servers [active or known]. A DinoServerInstance conveys just enough information for the UI to be able
     * to display the server's status and to identify the serverInstanceName (which is how server resource endpoints are configured into
     * the VS); and to identify the serverName (which is how the server itself is configured and how it is identified/referred to by the
     * platform hosting it).
     * The serverInstanceName will generally be used as the key to a map of DinoServerInstance objects but for ease of handling the
     * serverInstanceName is explicitly included in the DinoServerInstance object, alongside the serverName and active status.
     */

    /*
     * Include the server instance name - this is the name used in the VS configuration as opposed to the serverName used in the server configuration
     * and reported by the platform. THe serverInstanceName has no real meaning in terms of the configuration or deployment of OMAG resources, it is
     * a purely logical name used between the VS and the admin UI, to identify the instance of the server as being available to the user to query.
     */
    private String                          serverInstanceName;

    /*
     * Include the general server name - this is not the same as the serverInstanceName used in the VS configuration; it is the serverName as
     * used in the server configuration and the servername as reported by the platform.
     */
    private String                          serverName;

    /*
     * The boolean field 'active' indicates if the server is active or not.
     */
    private boolean                         isActive;

    /*
     * Include the platform name - this is the name of the platform (in the VS configuration) that was used in the query that retrieved this ServerStatus
     * object. It can be used by the UI to associate the server with the platform (via a graph relationship) and can be passed again by the client if it
     * needs to query further information about the server instance.
     */
    private String                          platformName;

    /**
     * Default Constructor sets the properties to nulls
     */
    public DinoServerInstance()
    {
        /*
         * Nothing to do.
         */
    }

    public DinoServerInstance(String serverInstanceName, String serverName, boolean active) {

        this.serverInstanceName = serverInstanceName;
        this.serverName         = serverName;
        this.isActive           = isActive;
        this.platformName       = platformName;
    }

    public String getServerInstanceName() {  return serverInstanceName;  }

    public void setServerInstanceName(String serverInstanceName) {
        this.serverInstanceName = serverInstanceName;
    }

    public String getServerName() {  return serverName;  }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean getIsActive() {  return isActive;  }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPlatformName() {  return platformName;  }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }



}
