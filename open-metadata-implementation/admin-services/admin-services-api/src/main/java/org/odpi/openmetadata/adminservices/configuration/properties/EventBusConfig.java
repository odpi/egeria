/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EventBusConfig caches the default properties that are used to set up event-based connectors in the server.  If it
 * is set up then the admin services will ensure that all connectors created after the event bus is configured,
 * that embed an event bus will use the same event bus connector with the core additional properties.
 * (These additional properties can be overridden when a specific connector is set up).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EventBusConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private static  String DEFAULT_TOPIC_ROOT = "egeria.omag";

    private String              connectorProvider       = null;
    private String              topicURLRoot            = null;
    private Map<String, Object> configurationProperties = null;

    @Deprecated private Map<String, Object> additionalProperties    = null;


    /**
     * Default constructor
     */
    public EventBusConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EventBusConfig(EventBusConfig   template)
    {
        super(template);

        if (template != null)
        {
            connectorProvider = template.getConnectorProvider();
            topicURLRoot = template.getTopicURLRoot();
            configurationProperties = template.getConfigurationProperties();
        }
    }


    /**
     * Return the class name of the connector provider for the event bus.
     *
     * @return class name
     */
    public String getConnectorProvider()
    {
        return connectorProvider;
    }


    /**
     * Set up the class name of the connector provider for the event bus.
     *
     * @param connectorProvider class name
     */
    public void setConnectorProvider(String connectorProvider)
    {
        this.connectorProvider = connectorProvider;
    }


    /**
     * Return the root of the topic URL.  The open metadata modules will add specific names to the root URL.
     *
     * @return string URL
     */
    public String getTopicURLRoot()
    {
        if (topicURLRoot == null)
        {
            return DEFAULT_TOPIC_ROOT;
        }

        return topicURLRoot;
    }


    /**
     * Set up the root of the topic URL.  The open metadata modules will add specific names to the root URL.
     *
     * @param topicURLRoot string URL
     */
    public void setTopicURLRoot(String topicURLRoot)
    {
        this.topicURLRoot = topicURLRoot;
    }


    /**
     * Return the additional properties for the event bus connection.
     *
     * @return map of name value pairs
     */
    @Deprecated public Map<String, Object> getAdditionalProperties()
    {
        return getConfigurationProperties();
    }


    /**
     * Set up the additional properties for the event bus connection.
     *
     * @param properties map of name value pairs
     */
    @Deprecated public void setAdditionalProperties(Map<String, Object> properties)
    {
        this.configurationProperties = properties;
    }


    /**
     * Return the configuration properties for the event bus connection.
     *
     * @return map of name value pairs
     */
    public Map<String, Object> getConfigurationProperties()
    {

        if (configurationProperties == null)
        {
            return null;
        }
        else if (configurationProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(configurationProperties);
        }
    }


    /**
     * Set up the configuration properties for the event bus connection.
     *
     * @param configurationProperties map of name value pairs
     */
    public void setConfigurationProperties(Map<String, Object> configurationProperties)
    {
        this.configurationProperties = configurationProperties;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EventBusConfig{" +
                "connectorProvider='" + connectorProvider + '\'' +
                ", topicURLRoot='" + topicURLRoot + '\'' +
                ", configurationProperties=" + configurationProperties +
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
        EventBusConfig that = (EventBusConfig) objectToCompare;
        return Objects.equals(getConnectorProvider(), that.getConnectorProvider()) &&
                Objects.equals(getTopicURLRoot(), that.getTopicURLRoot()) &&
                Objects.equals(getConfigurationProperties(), that.getConfigurationProperties());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getConnectorProvider(), getTopicURLRoot(), getConfigurationProperties());
    }
}
