/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerClientConfig provides the properties to configure a server that connects to an
 * OMAG Server.  This is typically used by a Governance or a View Server to retrieve metadata from
 * a metadata server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscoveryEngineServicesConfig.class,   name = "DiscoveryEngineServicesConfig"),
        @JsonSubTypes.Type(value = EngineServiceConfig.class,             name = "EngineServiceConfig"),
        @JsonSubTypes.Type(value = IntegrationServiceConfig.class,        name = "IntegrationServiceConfig"),
        @JsonSubTypes.Type(value = StewardshipEngineServicesConfig.class, name = "StewardshipEngineServicesConfig"),
        @JsonSubTypes.Type(value = ViewServiceConfig.class,               name = "ViewServiceConfig")
})
public class OMAGServerClientConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    /* Properties needed to call the OMAG Server REST APIs */
    private String omagServerPlatformRootURL = null;
    private String omagServerName            = null;

    /**
     * Default constructor
     */
    public OMAGServerClientConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMAGServerClientConfig(OMAGServerClientConfig template)
    {
        super(template);

        if (template != null)
        {
            omagServerPlatformRootURL = template.getOMAGServerPlatformRootURL();
            omagServerName            = template.getOMAGServerName();
        }
    }


    /**
     * Return the root URL of the OMAG ServerPlatform.
     *
     * @return string root url
     */
    public String getOMAGServerPlatformRootURL()
    {
        return omagServerPlatformRootURL;
    }


    /**
     * Set up the root URL of the OMAG Server Platform.
     *
     * @param omagServerPlatformRootURL string root url
     */
    public void setOMAGServerPlatformRootURL(String omagServerPlatformRootURL)
    {
        this.omagServerPlatformRootURL = omagServerPlatformRootURL;
    }


    /**
     * Return the name of the OMAG server.
     *
     * @return string server name
     */
    public String getOMAGServerName()
    {
        return omagServerName;
    }


    /**
     * Set up the name of the OMAG server.
     *
     * @param omagServerName string server name
     */
    public void setOMAGServerName(String omagServerName)
    {
        this.omagServerName = omagServerName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMAGServerClientConfig{" +
                "omagServerPlatformRootURL='" + omagServerPlatformRootURL + '\'' +
                ", omagServerName='" + omagServerName + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        OMAGServerClientConfig that = (OMAGServerClientConfig) objectToCompare;
        return Objects.equals(getOMAGServerPlatformRootURL(), that.getOMAGServerPlatformRootURL()) &&
                Objects.equals(getOMAGServerName(), that.getOMAGServerName());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getOMAGServerPlatformRootURL(), getOMAGServerName());
    }
}
