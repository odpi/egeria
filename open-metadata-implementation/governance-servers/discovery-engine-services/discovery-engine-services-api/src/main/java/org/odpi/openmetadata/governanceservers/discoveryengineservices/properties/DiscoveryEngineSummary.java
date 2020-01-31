/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.discoveryengineservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryEngineSummary is a summary of the properties known about a specific discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryEngineSummary implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                discoveryEngineName        = null;
    private String                discoveryEngineGUID        = null;
    private String                discoveryEngineDescription = null;
    private DiscoveryEngineStatus discoveryEngineStatus      = null;
    private List<String>          discoveryRequestTypes      = null;


    /**
     * Default constructor
     */
    public DiscoveryEngineSummary()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryEngineSummary(DiscoveryEngineSummary template)
    {
        if (template != null)
        {
            discoveryEngineName = template.getDiscoveryEngineName();
            discoveryEngineGUID = template.getDiscoveryEngineGUID();
            discoveryEngineDescription = template.getDiscoveryEngineDescription();
            discoveryEngineStatus = template.getDiscoveryEngineStatus();
            discoveryRequestTypes = template.getDiscoveryRequestTypes();
        }
    }

    /**
     * Return the name of this discovery engine.
     *
     * @return string name
     */
    public String getDiscoveryEngineName()
    {
        return discoveryEngineName;
    }


    /**
     * Set up the name of this discovery engine.
     *
     * @param discoveryEngineName string name
     */
    public void setDiscoveryEngineName(String discoveryEngineName)
    {
        this.discoveryEngineName = discoveryEngineName;
    }


    /**
     * Return the discovery engine's unique identifier.  This is only available if the
     * discovery engine has managed to retrieve its configuration from the metadata server.
     *
     * @return string identifier
     */
    public String getDiscoveryEngineGUID()
    {
        return discoveryEngineGUID;
    }


    /**
     * Set up the discovery engine's unique identifier.
     *
     * @param discoveryEngineGUID string identifier
     */
    public void setDiscoveryEngineGUID(String discoveryEngineGUID)
    {
        this.discoveryEngineGUID = discoveryEngineGUID;
    }


    /**
     * Return the description of the discovery engine. This is only available if the
     * discovery engine has managed to retrieve its configuration from the metadata server.
     *
     * @return string description
     */
    public String getDiscoveryEngineDescription()
    {
        return discoveryEngineDescription;
    }

    /**
     * Set up the description of the discovery engine.
     *
     * @param discoveryEngineDescription string description
     */
    public void setDiscoveryEngineDescription(String discoveryEngineDescription)
    {
        this.discoveryEngineDescription = discoveryEngineDescription;
    }


    /**
     * Return the status of the discovery engine.
     *
     * @return status enum
     */
    public DiscoveryEngineStatus getDiscoveryEngineStatus()
    {
        return discoveryEngineStatus;
    }


    /**
     * Set up the status of the discovery engine.
     *
     * @param discoveryEngineStatus status enum
     */
    public void setDiscoveryEngineStatus(DiscoveryEngineStatus discoveryEngineStatus)
    {
        this.discoveryEngineStatus = discoveryEngineStatus;
    }


    /**
     * Return the list of request types that this discovery engine supports.
     *
     * @return list of strings (discovery request types)
     */
    public List<String> getDiscoveryRequestTypes()
    {
        if (discoveryRequestTypes == null)
        {
            return null;
        }
        else if (discoveryRequestTypes.isEmpty())
        {
            return null;
        }

        return discoveryRequestTypes;
    }


    /**
     * Set up the discovery request types
     *
     * @param discoveryRequestTypes list of strings (discovery request types)
     */
    public void setDiscoveryRequestTypes(List<String> discoveryRequestTypes)
    {
        this.discoveryRequestTypes = discoveryRequestTypes;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "DiscoveryEngineSummary{" +
                "discoveryEngineName='" + discoveryEngineName + '\'' +
                ", discoveryEngineGUID='" + discoveryEngineGUID + '\'' +
                ", discoveryEngineDescription='" + discoveryEngineDescription + '\'' +
                ", discoveryEngineStatus=" + discoveryEngineStatus +
                ", discoveryRequestTypes=" + discoveryRequestTypes +
                '}';
    }


    /**
     * Compare objects
     *
     * @param objectToCompare object
     * @return boolean
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
        DiscoveryEngineSummary that = (DiscoveryEngineSummary) objectToCompare;
        return Objects.equals(discoveryEngineName, that.discoveryEngineName) &&
                Objects.equals(discoveryEngineGUID, that.discoveryEngineGUID) &&
                Objects.equals(discoveryEngineDescription, that.discoveryEngineDescription) &&
                discoveryEngineStatus == that.discoveryEngineStatus &&
                Objects.equals(discoveryRequestTypes, that.discoveryRequestTypes);
    }


   /**
     * Simple hash for the object
     *
     * @return int
     */
   @Override
   public int hashCode()
   {
       return Objects.hash(discoveryEngineName, discoveryEngineGUID, discoveryEngineDescription, discoveryEngineStatus, discoveryRequestTypes);
   }
}
