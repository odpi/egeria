/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryServiceConfigurationEvent is used to inform a discovery server that the configuration of one of the
 * discovery services within one of its discovery engines has changed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryServiceConfigurationEvent extends DiscoveryEngineConfigurationEvent
{
    private static final long serialVersionUID = 1L;

    private String              registeredDiscoveryServiceGUID = null;
    private List<String>        discoveryRequestTypes          = null;
    private Map<String, String> defaultAnalysisParameters      = null;


    /**
     * Default constructor
     */
    public DiscoveryServiceConfigurationEvent()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServiceConfigurationEvent(DiscoveryServiceConfigurationEvent template)
    {
        super(template);

        if (template != null)
        {
            registeredDiscoveryServiceGUID = template.getRegisteredDiscoveryServiceGUID();
            discoveryRequestTypes          = template.getDiscoveryRequestTypes();
            defaultAnalysisParameters      = template.getDefaultAnalysisParameters();
        }
    }


    /**
     * Return the unique identifier of the registered discovery service that has changed.
     *
     * @return string guid
     */
    public String getRegisteredDiscoveryServiceGUID()
    {
        return registeredDiscoveryServiceGUID;
    }


    /**
     * Set up the unique identifier of the registered discovery service that has changed.
     *
     * @param registeredDiscoveryServiceGUID string guid
     */
    public void setRegisteredDiscoveryServiceGUID(String registeredDiscoveryServiceGUID)
    {
        this.registeredDiscoveryServiceGUID = registeredDiscoveryServiceGUID;
    }


    /**
     * Return the list of discovery request types for the discovery service affected by the change.
     *
     * @return list of discovery request types
     */
    public List<String> getDiscoveryRequestTypes()
    {
        if (discoveryRequestTypes == null)
        {
            return null;
        }
        else if (discoveryRequestTypes.isEmpty())
        {
            return  null;
        }

        return new ArrayList<>(discoveryRequestTypes);
    }


    /**
     * Set up the list of discovery request types for the discovery service affected by the change.
     *
     * @param discoveryRequestTypes list of discovery request types
     */
    public void setDiscoveryRequestTypes(List<String> discoveryRequestTypes)
    {
        this.discoveryRequestTypes = discoveryRequestTypes;
    }


    /**
     * Return the list of default analysis parameters for the discovery service affected by the change.
     *
     * @return map of parameters
     */
    public Map<String, String> getDefaultAnalysisParameters()
    {
        if (defaultAnalysisParameters == null)
        {
            return null;
        }
        else if (defaultAnalysisParameters.isEmpty())
        {
            return  null;
        }

        return new HashMap<>(defaultAnalysisParameters);
    }


    /**
     * Set up the list of default analysis parameters for the discovery service affected by the change.
     *
     * @param defaultAnalysisParameters map of parameters
     */
    public void setDefaultAnalysisParameters(Map<String, String> defaultAnalysisParameters)
    {
        this.defaultAnalysisParameters = defaultAnalysisParameters;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryServiceConfigurationEvent{" +
                "registeredDiscoveryServiceGUID='" + registeredDiscoveryServiceGUID + '\'' +
                ", discoveryRequestTypes=" + discoveryRequestTypes +
                ", defaultAnalysisParameters=" + defaultAnalysisParameters +
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
        DiscoveryServiceConfigurationEvent that = (DiscoveryServiceConfigurationEvent) objectToCompare;
        return Objects.equals(registeredDiscoveryServiceGUID, that.registeredDiscoveryServiceGUID) &&
                Objects.equals(discoveryRequestTypes, that.discoveryRequestTypes) &&
                Objects.equals(defaultAnalysisParameters, that.defaultAnalysisParameters);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), registeredDiscoveryServiceGUID, discoveryRequestTypes, defaultAnalysisParameters);
    }
}
