/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityCollectionMember describes a community that is a member of an individual's my-communities collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommunityCollectionMember extends CollectionMemberHeader
{
    private static final long    serialVersionUID = 1L;

    private String name        = null;
    private String description = null;
    private String mission     = null;

    /**
     * Default constructor
     */
    public CommunityCollectionMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommunityCollectionMember(CommunityCollectionMember template)
    {
        super(template);

        if (template != null)
        {
            name = template.getName();
            description = template.getDescription();
            mission = template.getMission();
        }
    }



    /**
     * Return the name of the community.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the community.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the community's aims and operations.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the community's aims and operations.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the mission for this project.
     *
     * @return string id
     */
    public String getMission()
    {
        return mission;
    }


    /**
     * Set up the mission for this project.
     *
     * @param mission string id
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
        return "CommunityCollectionMember{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", mission='" + mission + '\'' +
                       ", dateAddedToCollection=" + getDateAddedToCollection() +
                       ", membershipRationale='" + getMembershipRationale() + '\'' +
                       ", watchStatus=" + getWatchStatus() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CommunityProperties community = (CommunityProperties) objectToCompare;
        return Objects.equals(getMission(), community.getMission()) &&
                       Objects.equals(getName(), community.getName()) &&
                       Objects.equals(getDescription(), community.getDescription());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMission(), getName(), getDescription());
    }
}
