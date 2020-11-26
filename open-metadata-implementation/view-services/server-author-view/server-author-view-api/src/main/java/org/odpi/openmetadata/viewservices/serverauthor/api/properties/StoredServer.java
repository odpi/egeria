/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * The is the Server Author View Service's concept of a stored server, it is a minimal representation of the stored server, including
 * its name, description and type; so a UI could use htis information to show a meaningful summary of the stored server.
 */
public class StoredServer {

    private static final long serialVersionUID = 1L;


    private String storedServerName;
    private String storedServerDescription;
    private String serverType;

    /**
     * Default Constructor sets the properties to nulls
     */
    public StoredServer() {
        /*
         * Nothing to do.
         */
    }

    /**
     * Constructor
     *
     * @param storedServerName        storedServer name
     * @param storedServerDescription storedServer description
     * @param serverType              meaningful name of the type of the server
     */
    public StoredServer(String storedServerName, String storedServerDescription, String serverType) {

        this.storedServerName = storedServerName;
        this.storedServerDescription = storedServerDescription;
        this.serverType = serverType;
    }


    /**
     * Get the storedServer name
     *
     * @return storedServer name
     */
    public String getStoredServerName() {
        return storedServerName;
    }

    /**
     * Set a meaningful name for the storedServer
     *
     * @param storedServerName set storedServer name
     */
    public void setStoredServerName(String storedServerName) {
        this.storedServerName = storedServerName;
    }

    /**
     * Get the storedServer description
     *
     * @return storedServer description
     */
    public String getStoredServerDescription() {
        return storedServerDescription;
    }

    /**
     * Set the description for the storedServer
     *
     * @param storedServerDescription set storedServer description
     */
    public void setStoredServerDescription(String storedServerDescription) {
        this.storedServerDescription = storedServerDescription;
    }

    /**
     * Get the type of the stored Server
     *
     * @return server type
     */
    public String getServerType() {
        return serverType;
    }

    /**
     * Set the type for the storedServer
     *
     * @param serverType set storedServer type
     */
    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("storedServer=");
        sb.append("{");
        sb.append("storedServerName=").append(this.storedServerName).append(",");
        sb.append("storedServerDescription=").append(this.storedServerDescription).append(",");
        sb.append('}');
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StoredServer storedServer = (StoredServer) o;

        return Objects.equals(storedServerName, storedServer.storedServerName) &&
                Objects.equals(storedServerDescription, storedServer.storedServerDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), storedServerName, storedServerDescription);
    }
}
