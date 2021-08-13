/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * A summary of the platform for the Server Author View. It contains a name a description of the platform, a status and
 * the associated servers if we can get them. It is a minimal representation of the platforms and associated stored servers, that could be
 * displayed in a UI table. The platform url root and technical ids are hidden from the user interface.
 */
public class Platform {

    private static final long serialVersionUID = 1L;


    private String platformName;
    private String platformDescription;
    private PlatformStatus platformStatus;
    private Set<StoredServer> storedServers = new HashSet<>();

    private List<RegisteredOMAGService> accessServices;
    private List<RegisteredOMAGService> engineServices;
    private List<RegisteredOMAGService> integrationServices;
    private List<RegisteredOMAGService> viewServices;

    /**
     * Default Constructor sets the properties to nulls
     */
    public Platform() {
        /*
         * Nothing to do.
         */
    }

    /**
     * Constructor
     *
     * @param platformName        platform name
     * @param platformDescription platform description
     */
    public Platform(String platformName, String platformDescription) {

        this.platformName = platformName;
        this.platformDescription = platformDescription;
    }


    /**
     * Get the platform name
     *
     * @return platform name
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * Set a meaningful name for the platform
     *
     * @param platformName set platform name
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * Get the platform description
     *
     * @return platform description
     */
    public String getPlatformDescription() {
        return platformDescription;
    }

    /**
     * Set the description for the platform
     *
     * @param platformDescription set platform description
     */
    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }

    /**
     * Set the stored servers on this platform
     *
     * @return the stored servers on this platform
     */
    public Set<StoredServer> getStoredServers() {
        return storedServers;
    }

    /**
     * Get the stored servers on this platform
     *
     * @param storedServers servers
     */
    public void setStoredServers(Set<StoredServer> storedServers) {
        this.storedServers = storedServers;
    }

    /**
     * Set the access services that this platform supports.
     * @param accessServices
     */
    public void setAccessServices(List<RegisteredOMAGService> accessServices) {
        this.accessServices = accessServices;
    }
    /**
     * Get the engine services that this platform supports.
     * @return engine services supported by this platform
     */
    public List<RegisteredOMAGService> getEngineServices() {
        return engineServices;
    }
    /**
     * Set the engine services that this platform supports.
     * @param engineServices
     */
    public void setEngineServices(List<RegisteredOMAGService> engineServices) {
        this.engineServices = engineServices;
    }
    /**
     * Get the governance services that this platform supports.
     * @return governance services supported by this platform
     */
    public List<RegisteredOMAGService> getIntegrationServices() {
        return integrationServices;
    }
    /**
     * Set the governance services that this platform supports.
     * @param integrationServices
     */
    public void setIntegrationServices(List<RegisteredOMAGService> integrationServices) { this.integrationServices = integrationServices; }
    /**
     * Get the view services that this platform supports.
     * @return view services supported by this platform
     */
    public List<RegisteredOMAGService> getViewServices() {
        return viewServices;
    }
    /**
     * Set the view services that this platform supports.
     * @param viewServices
     */
    public void setViewServices(List<RegisteredOMAGService> viewServices) {
        this.viewServices = viewServices;
    }

    /**
     * Add a stored server to the platform
     *
     * @param storedServer stored server to add
     */
    public void addStoredServer(StoredServer storedServer) {
        storedServers.add(storedServer);
    }

    /**
     * return the status of the platform
     *
     * @return the returned platform status
     */
    public PlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    /**
     * Set the platform status
     *
     * @param platformStatus platform status to set
     */
    public void setPlatformStatus(PlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("platform=");
        sb.append("{");
        sb.append("platformName=").append(this.platformName).append(",");
        sb.append("platformDescription=").append(this.platformDescription).append(",");
        sb.append("platformStatus=").append(this.platformStatus).append(",");
        sb.append("storedServers={");
        for (StoredServer storedServer:storedServers) {
             sb.append(storedServer).append(',');
        }
        sb.append('}');
        sb.append("accessServices={");
        for (RegisteredOMAGService registeredOMAGService:accessServices) {
            sb.append(registeredOMAGService).append(',');
        }
        sb.append('}');
        sb.append("viewServices={");
        for (RegisteredOMAGService registeredOMAGService:viewServices) {
            sb.append(registeredOMAGService).append(',');
        }
        sb.append('}');
        sb.append("governanceServices={");
        for (RegisteredOMAGService registeredOMAGService: integrationServices) {
            sb.append(registeredOMAGService).append(',');
        }
        sb.append('}');
        sb.append("commonServices={");
        for (RegisteredOMAGService registeredOMAGService: engineServices) {
            sb.append(registeredOMAGService).append(',');
        }
        sb.append('}');
        sb.append('}');
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Platform platform = (Platform) o;
        for (StoredServer storedServer:storedServers) {
            boolean foundIt = false;
           for (StoredServer platformStoredServer: platform.storedServers) {
               if (Objects.equals(storedServer, platformStoredServer)) {
                   foundIt = true;
               }
               if (!foundIt) {
                   return false;
               }
           }
        }
        for (RegisteredOMAGService registeredOMAGService:accessServices) {
            boolean foundIt = false;
            for (RegisteredOMAGService platformRegisteredOMAGService: platform.accessServices) {
                if (Objects.equals(registeredOMAGService, platformRegisteredOMAGService)) {
                    foundIt = true;
                }
                if (!foundIt) {
                    return false;
                }
            }
        }
        for (RegisteredOMAGService registeredOMAGService: engineServices) {
            boolean foundIt = false;
            for (RegisteredOMAGService platformRegisteredOMAGService: platform.engineServices) {
                if (Objects.equals(registeredOMAGService, platformRegisteredOMAGService)) {
                    foundIt = true;
                }
                if (!foundIt) {
                    return false;
                }
            }
        }
        for (RegisteredOMAGService registeredOMAGService: integrationServices) {
            boolean foundIt = false;
            for (RegisteredOMAGService platformRegisteredOMAGService: platform.integrationServices) {
                if (Objects.equals(registeredOMAGService, platformRegisteredOMAGService)) {
                    foundIt = true;
                }
                if (!foundIt) {
                    return false;
                }
            }
        }
        for (RegisteredOMAGService registeredOMAGService:viewServices) {
            boolean foundIt = false;
            for (RegisteredOMAGService platformRegisteredOMAGService: platform.viewServices) {
                if (Objects.equals(registeredOMAGService, platformRegisteredOMAGService)) {
                    foundIt = true;
                }
                if (!foundIt) {
                    return false;
                }
            }
        }

        return Objects.equals(platformName, platform.platformName) &&
                Objects.equals(platformDescription, platform.platformDescription) &&
                Objects.equals(platformStatus, platform.platformStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), platformName, platformDescription, platformStatus, storedServers, accessServices, viewServices, integrationServices, engineServices);
    }
}
