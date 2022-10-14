/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectProperties describes a project.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectProperties extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private String identifier     = null;
    private String name           = null;
    private String description    = null;
    private String status         = null;
    private Date   startDate      = null;
    private Date   plannedEndDate = null;


    /**
     * Default constructor
     */
    public ProjectProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectProperties(ProjectProperties template)
    {
        super(template);

        if (template != null)
        {
            this.identifier = template.getIdentifier();
            this.name = template.getName();
            this.description = template.getDescription();
            this.status = template.getStatus();
            this.startDate = template.getStartDate();
            this.plannedEndDate = template.getPlannedEndDate();
        }
    }


    /**
     * Return the code value or symbol used to identify the project - typically unique.
     *
     * @return string identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the code value or symbol used to identify the project - typically unique.
     *
     * @param identifier string identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }



    /**
     * Return the name of the project.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the project.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the project.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the project.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * Return the status for this project.
     *
     * @return string id
     */
    public String getStatus()
    {
        return status;
    }


    /**
     * Set up the status for this project.
     *
     * @param status string id
     */
    public void setStatus(String status)
    {
        this.status = status;
    }


    /**
     * Return the date that the project was created.
     *
     * @return date
     */
    public Date getStartDate()
    {
        if (startDate == null)
        {
            return null;
        }
        else
        {
            return new Date(startDate.getTime());
        }
    }


    /**
     * Set up the date that the project was created.
     *
     * @param startDate date
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the date that the project is expected to complete.
     *
     * @return date
     */
    public Date getPlannedEndDate()
    {
        if (plannedEndDate == null)
        {
            return null;
        }
        else
        {
            return new Date(plannedEndDate.getTime());
        }
    }


    /**
     * Set up the date that the project is expected to complete.
     *
     * @param plannedEndDate date
     */
    public void setPlannedEndDate(Date plannedEndDate)
    {
        this.plannedEndDate = plannedEndDate;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProjectProperties{" +
                       "identifier='" + identifier + '\'' +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", status='" + status + '\'' +
                       ", startDate=" + startDate +
                       ", plannedEndDate=" + plannedEndDate +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ProjectProperties that = (ProjectProperties) objectToCompare;
        return Objects.equals(identifier, that.identifier) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(status, that.status) &&
                       Objects.equals(startDate, that.startDate) &&
                       Objects.equals(plannedEndDate, that.plannedEndDate);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), identifier, name, description, status, startDate, plannedEndDate);
    }
}
