/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableHeader provides the common properties found in objects that inherit from Referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActorHeader.class, name = "ActorHeader"),
        @JsonSubTypes.Type(value = Collection.class, name = "Collection"),
        @JsonSubTypes.Type(value = CollectionMemberHeader.class, name = "CollectionMemberHeader"),
        @JsonSubTypes.Type(value = Community.class, name = "Community"),
        @JsonSubTypes.Type(value = NoteLogHeader.class, name = "NoteLogHeader"),
        @JsonSubTypes.Type(value = NoteEntryHeader.class, name = "NoteEntryHeader"),
        @JsonSubTypes.Type(value = PersonalRole.class, name = "PersonalRole"),
        @JsonSubTypes.Type(value = ToDo.class, name = "ToDo")
})
public abstract class ReferenceableHeader extends CommonHeader
{
    private static final long    serialVersionUID = 1L;

    private List<Classification> classifications = null;
    private String               qualifiedName   = null;
    private String               name            = null;
    private String               description     = null;


    /**
     * Default constructor
     */
    public ReferenceableHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceableHeader(ReferenceableHeader   template)
    {
        super(template);

        if (template != null)
        {
            this.classifications = template.getClassifications();
            this.qualifiedName = template.getQualifiedName();
            this.name = template.getName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the unique name for this element.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name for this element.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the display name for this element (normally a shortened form of the qualified name).
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the display name for this element (normally a shortened form of the qualified name).
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description for this element.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this element.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * Return the list of active classifications for this element.
     *
     * @return list of classification objects
     */
    public List<Classification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return classifications;
        }
    }


    /**
     * Set up the list of active classifications for this element.
     *
     * @param classifications list of classification objects
     */
    public void setClassifications(List<Classification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceableHeader{" +
                "classifications=" + classifications +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
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
        ReferenceableHeader that = (ReferenceableHeader) objectToCompare;
        return Objects.equals(getClassifications(), that.getClassifications()) &&
                Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDescription(), that.getDescription());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getClassifications(), getQualifiedName(), getName(), getDescription());
    }
}
