/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.events;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryEngineConfigurationEvent is used to inform the discovery server that the configuration of
 * one of its discovery engines has changed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DiscoveryServiceConfigurationEvent.class, name = "DiscoveryServiceConfigurationEvent")
})
public class DiscoveryEngineConfigurationEvent extends DiscoveryEngineEvent
{
    private static final long serialVersionUID = 1L;

    private String   discoveryEngineGUID = null;
    private String   discoveryEngineName = null;

    /**
     * Default constructor
     */
    public DiscoveryEngineConfigurationEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DiscoveryEngineConfigurationEvent(DiscoveryEngineConfigurationEvent template)
    {
        super(template);

        if (template != null)
        {
            discoveryEngineGUID = template.getDiscoveryEngineGUID();
            discoveryEngineName = template.getDiscoveryEngineName();
        }
    }


    /**
     * Return the unique identifier of the discovery engine that has a configuration change.
     *
     * @return string guid
     */
    public String getDiscoveryEngineGUID()
    {
        return discoveryEngineGUID;
    }


    /**
     * Set up the unique identifier of the discovery engine that has a configuration change.
     *
     * @param discoveryEngineGUID string guid
     */
    public void setDiscoveryEngineGUID(String discoveryEngineGUID)
    {
        this.discoveryEngineGUID = discoveryEngineGUID;
    }


    /**
     * Return the unique name of the discovery engine that has a configuration change.
     *
     * @return string name
     */
    public String getDiscoveryEngineName()
    {
        return discoveryEngineName;
    }


    /**
     * Set up the unique name of the discovery engine that has a configuration change.
     *
     * @param discoveryEngineName string name
     */
    public void setDiscoveryEngineName(String discoveryEngineName)
    {
        this.discoveryEngineName = discoveryEngineName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryEngineConfigurationEvent{" +
                "discoveryEngineGUID='" + discoveryEngineGUID + '\'' +
                ", discoveryEngineName='" + discoveryEngineName + '\'' +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DiscoveryEngineConfigurationEvent that = (DiscoveryEngineConfigurationEvent) objectToCompare;
        return Objects.equals(discoveryEngineGUID, that.discoveryEngineGUID) &&
                Objects.equals(discoveryEngineName, that.discoveryEngineName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), discoveryEngineGUID, discoveryEngineName);
    }
}
