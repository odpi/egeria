/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectElement contains the properties and header for a community.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectElement implements MetadataElement
{
    private ElementHeader                       elementHeader   = null;
    private ProjectProperties                   properties      = null;
    private RelatedElement                      startingElement = null;
    private List<RelatedMetadataElementSummary> resourceList    = null;
    private List<RelatedMetadataElementSummary> projectManagers = null;
    private List<RelatedMetadataElementSummary> projectTeam     = null;


    /**
     * Default constructor
     */
    public ProjectElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectElement(ProjectElement template)
    {
        if (template != null)
        {
            elementHeader   = template.getElementHeader();
            properties      = template.getProperties();
            startingElement = template.getStartingElement();
            resourceList    = template.getResourceList();
            projectManagers = template.getProjectManagers();
            projectTeam     = template.getProjectTeam();
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
     * Return the properties of the community.
     *
     * @return properties
     */
    public ProjectProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the community properties.
     *
     * @param properties  properties
     */
    public void setProperties(ProjectProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @return list of element stubs
     */
    public RelatedElement getStartingElement()
    {
        return startingElement;
    }


    /**
     * Set up details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @param startingElement relationship details
     */
    public void setStartingElement(RelatedElement startingElement)
    {
        this.startingElement = startingElement;
    }


    /**
     * Return other related elements (not including any starting element if supplied.
     *
     * @return list of related element summaries
     */
    public List<RelatedMetadataElementSummary> getResourceList()
    {
        return resourceList;
    }


    /**
     * Set up  elements linked via the resource list relationship.
     *
     * @param resourceList list of related element summaries
     */
    public void setResourceList(List<RelatedMetadataElementSummary> resourceList)
    {
        this.resourceList = resourceList;
    }


    /**
     * Return the list of project managers.
     *
     * @return  list of related element summaries
     */
    public List<RelatedMetadataElementSummary> getProjectManagers()
    {
        return projectManagers;
    }


    /**
     * Set up the list of project managers.
     *
     * @param projectManagers list of related element summaries
     */
    public void setProjectManagers(List<RelatedMetadataElementSummary> projectManagers)
    {
        this.projectManagers = projectManagers;
    }


    /**
     * Return the list of project team members.
     *
     * @return  list of related element summaries
     */
    public List<RelatedMetadataElementSummary> getProjectTeam()
    {
        return projectTeam;
    }


    /**
     * Set up the list of project team members.
     *
     * @param projectTeam  list of related element summaries
     */
    public void setProjectTeam(List<RelatedMetadataElementSummary> projectTeam)
    {
        this.projectTeam = projectTeam;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProjectElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", startingElement=" + startingElement +
                ", resourceList=" + resourceList +
                ", projectManagers=" + projectManagers +
                ", projectTeam=" + projectTeam +
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
        ProjectElement that = (ProjectElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(resourceList, that.resourceList) &&
                Objects.equals(projectManagers, that.projectManagers) &&
                Objects.equals(projectTeam, that.projectTeam) &&
                Objects.equals(startingElement, that.startingElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, startingElement, resourceList, projectManagers, projectTeam);
    }
}
