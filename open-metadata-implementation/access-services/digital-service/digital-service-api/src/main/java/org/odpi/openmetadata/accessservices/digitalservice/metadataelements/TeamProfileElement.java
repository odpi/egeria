/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.TeamProfileProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

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
    private TeamProfileProperties properties = null;
    private ElementStub           superTeam  = null;
    private List<ElementStub>     subTeams   = null;

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
     * @return description of linked profile
     */
    public ElementStub getSuperTeam()
    {
        return superTeam;
    }


    /**
     * Set up the unique identifier (guid) of the team that this team reports to - null means top level team.
     *
     * @param superTeam description of linked profile
     */
    public void setSuperTeam(ElementStub superTeam)
    {
        this.superTeam = superTeam;
    }


    /**
     * Return the list of unique identifiers (guids) for the teams that report to this team.
     *
     * @return list of descriptions of linked profiles
     */
    public List<ElementStub> getSubTeams()
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
     * @param subTeams list of descriptions of linked profiles
     */
    public void setSubTeams(List<ElementStub> subTeams)
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
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(superTeam, that.superTeam) &&
                       Objects.equals(subTeams, that.subTeams);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, superTeam, subTeams);
    }
}
