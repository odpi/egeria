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
 * ProjectCollectionMember describes a project that is a member of an individual's my-projects collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectCollectionMember extends CollectionMemberHeader
{
    private static final long    serialVersionUID = 1L;

    private String              status               = null;
    private Date                startDate            = null;
    private Date                plannedEndDate       = null;
    private Map<String, Object> extendedProperties   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public ProjectCollectionMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectCollectionMember(ProjectCollectionMember template)
    {
        super(template);

        if (template != null)
        {
            this.status = template.getStatus();
            this.startDate = template.getStartDate();
            this.plannedEndDate = template.getPlannedEndDate();
            this.extendedProperties = template.getExtendedProperties();
            this.additionalProperties = template.getAdditionalProperties();
        }
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
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> additionalProperties)
    {
        this.extendedProperties = additionalProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProjectCollectionMember{" +
                "status='" + status + '\'' +
                ", startDate=" + startDate +
                ", plannedEndDate=" + plannedEndDate +
                ", dateAddedToCollection=" + getDateAddedToCollection() +
                ", membershipRationale='" + getMembershipRationale() + '\'' +
                ", watchStatus=" + getWatchStatus() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ProjectCollectionMember that = (ProjectCollectionMember) objectToCompare;
        return Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getStartDate(), that.getStartDate()) &&
                Objects.equals(getPlannedEndDate(), that.getPlannedEndDate());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getStatus(), getStartDate(), getPlannedEndDate());
    }
}
