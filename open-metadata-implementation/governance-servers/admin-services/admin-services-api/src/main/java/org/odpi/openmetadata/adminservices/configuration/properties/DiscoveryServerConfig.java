/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryServerConfig provides the properties to configure a discovery server.  The discovery service
 * runs one of more discovery engines.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryServerConfig extends AdminServicesConfigHeader
{
    /* Properties needed to call the access service REST APIs */
    private String        accessServiceRootURL     = null;
    private String        accessServiceServerName  = null;

    /* List of discovery engines that run in this server */
    private List<String>  discoveryEngineGUIDs = null;

    /**
     * Default constructor
     */
    public DiscoveryServerConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServerConfig(DiscoveryServerConfig template)
    {
        super(template);

        if (template != null)
        {
            accessServiceRootURL = template.getAccessServiceRootURL();
            accessServiceServerName = template.getAccessServiceServerName();
            discoveryEngineGUIDs = template.getDiscoveryEngineGUIDs();
        }
    }


    /**
     * Return the root URL of the access service.
     *
     * @return string root url
     */
    public String getAccessServiceRootURL()
    {
        return accessServiceRootURL;
    }


    /**
     * Set up the root URL of the access service.
     *
     * @param accessServiceRootURL string root url
     */
    public void setAccessServiceRootURL(String accessServiceRootURL)
    {
        this.accessServiceRootURL = accessServiceRootURL;
    }


    /**
     * Return the name of the server where the access service resides.
     *
     * @return string server name
     */
    public String getAccessServiceServerName()
    {
        return accessServiceServerName;
    }


    /**
     * Set up the name of the server where the access service resides.
     *
     * @param accessServiceServerName string server name
     */
    public void setAccessServiceServerName(String accessServiceServerName)
    {
        this.accessServiceServerName = accessServiceServerName;
    }


    /**
     * Return the connection used to access the topic that passes discovery requests to the discovery engine
     * services.
     *
     * @return connection object
     */
    public List<String> getDiscoveryEngineGUIDs()
    {
        return discoveryEngineGUIDs;
    }


    /**
     * Set up the list of unique identifiers (guids) for the discovery engines that will run in this discovery
     * server.
     *
     * @param discoveryEngineGUIDs connection object
     */
    public void setDiscoveryEngineGUIDs(List<String> discoveryEngineGUIDs)
    {
        this.discoveryEngineGUIDs = discoveryEngineGUIDs;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "DiscoveryServerConfig{" +
                "accessServiceRootURL='" + accessServiceRootURL + '\'' +
                ", accessServiceServerName='" + accessServiceServerName + '\'' +
                ", discoveryEngineGUIDs=" + discoveryEngineGUIDs +
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
        DiscoveryServerConfig that = (DiscoveryServerConfig) objectToCompare;
        return Objects.equals(getAccessServiceRootURL(), that.getAccessServiceRootURL()) &&
                Objects.equals(getAccessServiceServerName(), that.getAccessServiceServerName()) &&
                Objects.equals(getDiscoveryEngineGUIDs(), that.getDiscoveryEngineGUIDs());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAccessServiceRootURL(), getAccessServiceServerName(), getDiscoveryEngineGUIDs());
    }
}
