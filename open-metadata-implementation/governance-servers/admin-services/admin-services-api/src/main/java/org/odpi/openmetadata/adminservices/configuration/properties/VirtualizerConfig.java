/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * VirtualizerConfig caches the properties that are used to setup up the connector to the virtualisation
 * solutions in the server. The configurations contain the name of the connector provider and the corresponding
 * additional properties.
 *
 * This configuration class should support various types of the virtualisation connectors
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VirtualizerConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private String                 connectorProvider = null;
    private Map<String, Object> additionalProperties = null;
    private Connection      virtualizationConnection = null;
    private Connection            eventbusConnection = null;

    /**
     * Default constuctor
     */
    public VirtualizerConfig(){

    }

    /**
     * Copy data from template
     *
     * @param template
     */
    public VirtualizerConfig(VirtualizerConfig template){
        if (template != null){
            connectorProvider = template.getConnectorProvider();
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
}
