/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * StewardshipServicesConfig provides the configuration properties for
 * the stewardship services
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StewardshipServicesConfig extends AdminServicesConfigHeader
{
    /* Properties needed to call the access service REST APIs */
    private String        accessServiceRootURL     = null;
    private String        accessServiceServerName  = null;

    /* Connection for topic that receives inbound requests */
    private Connection inboundRequestConnection = null;

    /**
     * Default constructor
     */
    public StewardshipServicesConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StewardshipServicesConfig(StewardshipServicesConfig  template)
    {
        super(template);

        if (template != null)
        {
            accessServiceRootURL = template.getAccessServiceRootURL();
            accessServiceServerName = template.getAccessServiceServerName();
            inboundRequestConnection = template.getInboundRequestConnection();
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
     * Return the connection used to access the topic that passes stewardship requests to the stewardship
     * services.
     *
     * @return connection object
     */
    public Connection getInboundRequestConnection()
    {
        return inboundRequestConnection;
    }


    /**
     * Set up the connection used to access the topic that passes discovery requests to the discovery engine
     * services.
     *
     * @param inboundRequestConnection connection object
     */
    public void setInboundRequestConnection(Connection inboundRequestConnection)
    {
        this.inboundRequestConnection = inboundRequestConnection;
    }



    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "StewardshipServicesConfig{" +
                "accessServiceRootURL='" + accessServiceRootURL + '\'' +
                ", accessServiceServerName='" + accessServiceServerName + '\'' +
                ", inboundRequestConnection=" + inboundRequestConnection +
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
        StewardshipServicesConfig that = (StewardshipServicesConfig) objectToCompare;
        return Objects.equals(getAccessServiceRootURL(), that.getAccessServiceRootURL()) &&
                Objects.equals(getAccessServiceServerName(), that.getAccessServiceServerName()) &&
                Objects.equals(getInboundRequestConnection(), that.getInboundRequestConnection());
    }



    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAccessServiceRootURL(), getAccessServiceServerName(), getInboundRequestConnection());
    }
}
