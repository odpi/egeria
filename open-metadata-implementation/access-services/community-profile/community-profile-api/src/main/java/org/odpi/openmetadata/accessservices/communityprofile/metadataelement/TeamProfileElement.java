/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.metadataelement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TeamProfileProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TeamProfileElement contains the properties and header for a team profile retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TeamProfileElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader         elementHeader = null;
    private TeamProfileProperties properties    = null;
    private String                superTeam     = null;
    private List<String>          subTeams      = null;

    /**
     * Default constructor
     */
    public TeamProfileElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TeamProfileElement(TeamProfileElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            superTeam = template.getSuperTeam();
            subTeams = template.getSubTeams();
        }
    }

    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties from the team profile.
     *
     * @return properties
     */
    public TeamProfileProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties from the team profile.
     *
     * @param properties  properties
     */
    public void setProperties(TeamProfileProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the unique identifier (guid) of the team that this team reports to - null means top level team.
     *
     * @return guid
     */
    public String getSuperTeam()
    {
        return superTeam;
    }


    /**
     * Set up the unique identifier (guid) of the team that this team reports to - null means top level team.
     *
     * @param superTeam guid
     */
    public void setSuperTeam(String superTeam)
    {
        this.superTeam = superTeam;
    }


    /**
     * Return the list of unique identifiers (guids) for the teams that report to this team.
     *
     * @return list of guids
     */
    public List<String> getSubTeams()
    {
        if (subTeams == null)
        {
            return null;
        }
        else if (subTeams.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(subTeams);
        }
    }


    /**
     * Set up the list of unique identifiers (guids) for the teams that report to this team.
     *
     * @param subTeams list of guids
     */
    public void setSubTeams(List<String> subTeams)
    {
        this.subTeams = subTeams;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TeamProfileElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", superTeam='" + superTeam + '\'' +
                       ", subTeams=" + subTeams +
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
        TeamProfileElement that = (TeamProfileElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties);
    }
}
