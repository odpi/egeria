/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteLogHeader covers the properties that are common between a CommunityForum and a PersonalNoteLog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommunityForum.class, name = "CommunityForum"),
        @JsonSubTypes.Type(value = PersonalNoteLog.class, name = "PersonalNoteLog")
})
public abstract class NoteLogHeader extends ReferenceableProperties
{
    private static final long serialVersionUID = 1L;

    private boolean isPublic = false;


    /**
     * Default constructor
     */
    public NoteLogHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteLogHeader(NoteLogHeader template)
    {
        super(template);

        if (template != null)
        {
            isPublic = template.getIsPublic();
        }
    }


    /**
     * Return if the link to the note log is private to the creating user.
     *
     * @return boolean
     */
    public boolean getIsPublic()
    {
        return isPublic;
    }


    /**
     * Set up whether the link to the note log is private to the creating user or not.
     *
     * @param aPublic boolean
     */
    public void setIsPublic(boolean aPublic)
    {
        isPublic = aPublic;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NoteLogHeader{" +
                       "isPublic=" + isPublic +
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
        NoteLogHeader that = (NoteLogHeader) objectToCompare;
        return isPublic == that.isPublic;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getIsPublic());
    }
}
