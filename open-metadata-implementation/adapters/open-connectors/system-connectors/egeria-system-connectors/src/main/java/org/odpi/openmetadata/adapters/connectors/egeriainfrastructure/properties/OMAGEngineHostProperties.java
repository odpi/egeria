/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGEngineHostProperties extends OMAGServerProperties
{
    private List<GovernanceEngineSummary> governanceEngineSummaries = null;

    public OMAGEngineHostProperties()
    {
    }


    public List<GovernanceEngineSummary> getGovernanceEngineSummaries()
    {
        return governanceEngineSummaries;
    }

    public void setGovernanceEngineSummaries(List<GovernanceEngineSummary> governanceEngineSummaries)
    {
        this.governanceEngineSummaries = governanceEngineSummaries;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGEngineHostProperties{" +
                "governanceEngineSummaries=" + governanceEngineSummaries +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        OMAGEngineHostProperties that = (OMAGEngineHostProperties) objectToCompare;
        return Objects.equals(governanceEngineSummaries, that.governanceEngineSummaries);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceEngineSummaries);
    }
}
