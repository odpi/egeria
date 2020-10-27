/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.stewardshipengineservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * StewardshipEngineSummary is a summary of the properties known about a specific stewardship engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StewardshipEngineSummary implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                stewardshipEngineName        = null;
    private String                stewardshipEngineGUID        = null;
    private String                stewardshipEngineDescription = null;
    private StewardshipEngineStatus stewardshipEngineStatus      = null;
    private List<String>          stewardshipRequestTypes      = null;


    /**
     * Default constructor
     */
    public StewardshipEngineSummary()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StewardshipEngineSummary(StewardshipEngineSummary template)
    {
        if (template != null)
        {
            stewardshipEngineName = template.getStewardshipEngineName();
            stewardshipEngineGUID = template.getStewardshipEngineGUID();
            stewardshipEngineDescription = template.getStewardshipEngineDescription();
            stewardshipEngineStatus = template.getStewardshipEngineStatus();
            stewardshipRequestTypes = template.getStewardshipRequestTypes();
        }
    }

    /**
     * Return the name of this stewardship engine.
     *
     * @return string name
     */
    public String getStewardshipEngineName()
    {
        return stewardshipEngineName;
    }


    /**
     * Set up the name of this stewardship engine.
     *
     * @param stewardshipEngineName string name
     */
    public void setStewardshipEngineName(String stewardshipEngineName)
    {
        this.stewardshipEngineName = stewardshipEngineName;
    }


    /**
     * Return the stewardship engine's unique identifier.  This is only available if the
     * stewardship engine has managed to retrieve its configuration from the metadata server.
     *
     * @return string identifier
     */
    public String getStewardshipEngineGUID()
    {
        return stewardshipEngineGUID;
    }


    /**
     * Set up the stewardship engine's unique identifier.
     *
     * @param stewardshipEngineGUID string identifier
     */
    public void setStewardshipEngineGUID(String stewardshipEngineGUID)
    {
        this.stewardshipEngineGUID = stewardshipEngineGUID;
    }


    /**
     * Return the description of the stewardship engine. This is only available if the
     * stewardship engine has managed to retrieve its configuration from the metadata server.
     *
     * @return string description
     */
    public String getStewardshipEngineDescription()
    {
        return stewardshipEngineDescription;
    }

    /**
     * Set up the description of the stewardship engine.
     *
     * @param stewardshipEngineDescription string description
     */
    public void setStewardshipEngineDescription(String stewardshipEngineDescription)
    {
        this.stewardshipEngineDescription = stewardshipEngineDescription;
    }


    /**
     * Return the status of the stewardship engine.
     *
     * @return status enum
     */
    public StewardshipEngineStatus getStewardshipEngineStatus()
    {
        return stewardshipEngineStatus;
    }


    /**
     * Set up the status of the stewardship engine.
     *
     * @param stewardshipEngineStatus status enum
     */
    public void setStewardshipEngineStatus(StewardshipEngineStatus stewardshipEngineStatus)
    {
        this.stewardshipEngineStatus = stewardshipEngineStatus;
    }


    /**
     * Return the list of request types that this stewardship engine supports.
     *
     * @return list of strings (stewardship request types)
     */
    public List<String> getStewardshipRequestTypes()
    {
        if (stewardshipRequestTypes == null)
        {
            return null;
        }
        else if (stewardshipRequestTypes.isEmpty())
        {
            return null;
        }

        return stewardshipRequestTypes;
    }


    /**
     * Set up the stewardship request types
     *
     * @param stewardshipRequestTypes list of strings (stewardship request types)
     */
    public void setStewardshipRequestTypes(List<String> stewardshipRequestTypes)
    {
        this.stewardshipRequestTypes = stewardshipRequestTypes;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "StewardshipEngineSummary{" +
                "stewardshipEngineName='" + stewardshipEngineName + '\'' +
                ", stewardshipEngineGUID='" + stewardshipEngineGUID + '\'' +
                ", stewardshipEngineDescription='" + stewardshipEngineDescription + '\'' +
                ", stewardshipEngineStatus=" + stewardshipEngineStatus +
                ", stewardshipRequestTypes=" + stewardshipRequestTypes +
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
        StewardshipEngineSummary that = (StewardshipEngineSummary) objectToCompare;
        return Objects.equals(stewardshipEngineName, that.stewardshipEngineName) &&
                Objects.equals(stewardshipEngineGUID, that.stewardshipEngineGUID) &&
                Objects.equals(stewardshipEngineDescription, that.stewardshipEngineDescription) &&
                stewardshipEngineStatus == that.stewardshipEngineStatus &&
                Objects.equals(stewardshipRequestTypes, that.stewardshipRequestTypes);
    }


   /**
     * Simple hash for the object
     *
     * @return int
     */
   @Override
   public int hashCode()
   {
       return Objects.hash(stewardshipEngineName, stewardshipEngineGUID, stewardshipEngineDescription, stewardshipEngineStatus, stewardshipRequestTypes);
   }
}
