/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineSummary is a summary of the properties known about a specific governance engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceEngineSummary
{
    private String                 governanceEngineName        = null;
    private String                 governanceEngineTypeName    = null;
    private String                 governanceEngineService     = null;
    private String                 governanceEngineGUID        = null;
    private String                 governanceEngineDescription = null;
    private GovernanceEngineStatus governanceEngineStatus      = null;
    private List<String>           governanceRequestTypes      = null;


    /**
     * Default constructor
     */
    public GovernanceEngineSummary()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceEngineSummary(GovernanceEngineSummary template)
    {
        if (template != null)
        {
            governanceEngineName = template.getGovernanceEngineName();
            governanceEngineTypeName = template.getGovernanceEngineTypeName();
            governanceEngineService = template.getGovernanceEngineService();
            governanceEngineGUID = template.getGovernanceEngineGUID();
            governanceEngineDescription = template.getGovernanceEngineDescription();
            governanceEngineStatus = template.getGovernanceEngineStatus();
            governanceRequestTypes = template.getGovernanceRequestTypes();
        }
    }

    /**
     * Return the name of this governance engine.
     *
     * @return string name
     */
    public String getGovernanceEngineName()
    {
        return governanceEngineName;
    }


    /**
     * Set up the name of this governance engine.
     *
     * @param governanceEngineName string name
     */
    public void setGovernanceEngineName(String governanceEngineName)
    {
        this.governanceEngineName = governanceEngineName;
    }


    /**
     * Return the type of the governance engine.
     *
     * @return string name
     */
    public String getGovernanceEngineTypeName()
    {
        return governanceEngineTypeName;
    }


    /**
     * Set up the type of the governance engine.
     *
     * @param governanceEngineTypeName string name
     */
    public void setGovernanceEngineTypeName(String governanceEngineTypeName)
    {
        this.governanceEngineTypeName = governanceEngineTypeName;
    }


    /**
     * Return the name of the Open Metadata Engine Service (OMES) that is supporting this governance engine.
     *
     * @return service name
     */
    public String getGovernanceEngineService()
    {
        return governanceEngineService;
    }


    /**
     * Set up the name of the Open Metadata Engine Service (OMES) that is supporting this governance engine.
     *
     * @param governanceEngineService service name
     */
    public void setGovernanceEngineService(String governanceEngineService)
    {
        this.governanceEngineService = governanceEngineService;
    }


    /**
     * Return the governance engine's unique identifier.  This is only available if the
     * governance engine has managed to retrieve its configuration from the metadata server.
     *
     * @return string identifier
     */
    public String getGovernanceEngineGUID()
    {
        return governanceEngineGUID;
    }


    /**
     * Set up the governance engine's unique identifier.
     *
     * @param governanceEngineGUID string identifier
     */
    public void setGovernanceEngineGUID(String governanceEngineGUID)
    {
        this.governanceEngineGUID = governanceEngineGUID;
    }


    /**
     * Return the description of the governance engine. This is only available if the
     * governance engine has managed to retrieve its configuration from the metadata server.
     *
     * @return string description
     */
    public String getGovernanceEngineDescription()
    {
        return governanceEngineDescription;
    }

    /**
     * Set up the description of the governance engine.
     *
     * @param governanceEngineDescription string description
     */
    public void setGovernanceEngineDescription(String governanceEngineDescription)
    {
        this.governanceEngineDescription = governanceEngineDescription;
    }


    /**
     * Return the status of the governance engine.
     *
     * @return status enum
     */
    public GovernanceEngineStatus getGovernanceEngineStatus()
    {
        return governanceEngineStatus;
    }


    /**
     * Set up the status of the governance engine.
     *
     * @param governanceEngineStatus status enum
     */
    public void setGovernanceEngineStatus(GovernanceEngineStatus governanceEngineStatus)
    {
        this.governanceEngineStatus = governanceEngineStatus;
    }


    /**
     * Return the list of request types that this governance engine supports.
     *
     * @return list of strings (governance request types)
     */
    public List<String> getGovernanceRequestTypes()
    {
        if (governanceRequestTypes == null)
        {
            return null;
        }
        else if (governanceRequestTypes.isEmpty())
        {
            return null;
        }

        return governanceRequestTypes;
    }


    /**
     * Set up the governance request types
     *
     * @param governanceRequestTypes list of strings (governance request types)
     */
    public void setGovernanceRequestTypes(List<String> governanceRequestTypes)
    {
        this.governanceRequestTypes = governanceRequestTypes;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineSummary{" +
                       "governanceEngineName='" + governanceEngineName + '\'' +
                       ", governanceEngineTypeName='" + governanceEngineTypeName + '\'' +
                       ", governanceEngineService='" + governanceEngineService + '\'' +
                       ", governanceEngineGUID='" + governanceEngineGUID + '\'' +
                       ", governanceEngineDescription='" + governanceEngineDescription + '\'' +
                       ", governanceEngineStatus=" + governanceEngineStatus +
                       ", governanceRequestTypes=" + governanceRequestTypes +
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
        GovernanceEngineSummary that = (GovernanceEngineSummary) objectToCompare;
        return Objects.equals(governanceEngineName, that.governanceEngineName) &&
                       Objects.equals(governanceEngineTypeName, that.governanceEngineTypeName) &&
                       Objects.equals(governanceEngineService, that.governanceEngineService) &&
                       Objects.equals(governanceEngineGUID, that.governanceEngineGUID) &&
                Objects.equals(governanceEngineDescription, that.governanceEngineDescription) &&
                governanceEngineStatus == that.governanceEngineStatus &&
                Objects.equals(governanceRequestTypes, that.governanceRequestTypes);
    }


   /**
     * Simple hash for the object
     *
     * @return int
     */
   @Override
   public int hashCode()
   {
       return Objects.hash(governanceEngineName, governanceEngineTypeName, governanceEngineService,
                           governanceEngineGUID, governanceEngineDescription, governanceEngineStatus, governanceRequestTypes);
   }
}
