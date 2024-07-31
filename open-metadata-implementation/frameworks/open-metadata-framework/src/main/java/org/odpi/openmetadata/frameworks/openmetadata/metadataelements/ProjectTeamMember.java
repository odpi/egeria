/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectTeamProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectTeamMember describes a team member of a project.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectTeamMember implements MetadataElement
{
    private ElementHeader         elementHeader         = null;
    private ProjectTeamProperties projectTeamProperties = null;
    private ElementStub           member                = null;


    /**
     * Default constructor
     */
    public ProjectTeamMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectTeamMember(ProjectTeamMember template)
    {
        if (template != null)
        {
            this.elementHeader         = template.getElementHeader();
            this.projectTeamProperties = template.getProjectTeamProperties();
            this.member                = template.getMember();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties from the membership relationship.
     *
     * @return membership properties
     */
    public ProjectTeamProperties getProjectTeamProperties()
    {
        return projectTeamProperties;
    }


    /**
     * Set up the properties from the membership relationship.
     *
     * @param projectTeamProperties membership properties
     */
    public void setProjectTeamProperties(ProjectTeamProperties projectTeamProperties)
    {
        this.projectTeamProperties = projectTeamProperties;
    }


    /**
     * Return the properties of the member.
     *
     * @return member properties
     */
    public ElementStub getMember()
    {
        return member;
    }


    /**
     * Set up the properties of the member.
     *
     * @param member  properties
     */
    public void setMember(ElementStub member)
    {
        this.member = member;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProjectTeamMember{" +
                       "elementHeader=" + elementHeader +
                       ", membershipProperties=" + projectTeamProperties +
                       ", member=" + member +
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
        if (! (objectToCompare instanceof ProjectTeamMember that))
        {
            return false;
        }
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(projectTeamProperties, that.projectTeamProperties) &&
                       Objects.equals(member, that.member);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, projectTeamProperties, member);
    }
}
