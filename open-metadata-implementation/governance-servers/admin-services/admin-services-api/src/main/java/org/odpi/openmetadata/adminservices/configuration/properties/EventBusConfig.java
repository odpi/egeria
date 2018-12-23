/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    private String              connectorProvider    = null;
    private String              topicURLRoot         = null;
    private Map<String, Object> additionalProperties = null;


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
            additionalProperties = template.getAdditionalProperties();
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
    public Map<String, Object> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up the additional properties for the event bus connection.
     *
     * @param additionalProperties map of name value pairs
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */



    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */




    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
}
