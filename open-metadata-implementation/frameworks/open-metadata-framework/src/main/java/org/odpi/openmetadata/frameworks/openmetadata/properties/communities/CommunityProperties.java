/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.communities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityProperties describes the core properties of a community.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommunityProperties extends ReferenceableProperties
{
    private String mission = null;


    /**
     * Default constructor
     */
    public CommunityProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.COMMUNITY.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityProperties(CommunityProperties template)
    {
        super(template);

        if (template != null)
        {
            mission = template.getMission();
        }
    }


    /**
     * Return the mission of the community.
     *
     * @return text
     */
    public String getMission()
    {
        return mission;
    }


    /**
     * Set up the mission of the community.
     *
     * @param mission text
     */
    public void setMission(String mission)
    {
        this.mission = mission;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityProperties{" +
                "mission='" + mission + '\'' +
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
        CommunityProperties community = (CommunityProperties) objectToCompare;
        return Objects.equals(mission, community.mission);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mission);
    }
}
