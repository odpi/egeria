/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineHostServicesResponse provides a response object for returning information about a
 * list of engine services that are configured for an engine host OMAG Server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineHostServicesResponse extends FFDCResponseBase
{
    private List<EngineConfig> governanceEngines;


    /**
     * Default constructor
     */
    public EngineHostServicesResponse()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public EngineHostServicesResponse(EngineHostServicesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.governanceEngines = template.getGovernanceEngines();
        }
    }


    /**
     * Return the list of governance engines
     *
     * @return service descriptions
     */
    public List<EngineConfig> getGovernanceEngines()
    {
        return governanceEngines;
    }


    /**
     * Set up the list of governance engines
     *
     * @param governanceEngines service
     */
    public void setGovernanceEngines(List<EngineConfig> governanceEngines)
    {
        this.governanceEngines = governanceEngines;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EngineHostServicesResponse{" +
                "governanceEngines=" + governanceEngines +
                "} " + super.toString();
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
        EngineHostServicesResponse that = (EngineHostServicesResponse) objectToCompare;
        return Objects.equals(getGovernanceEngines(), that.getGovernanceEngines());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getGovernanceEngines());
    }
}
