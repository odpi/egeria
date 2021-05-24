/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteEntryHeader covers the entries in a community forum and personal notes since they are based on a note log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommunityForumContribution.class, name = "CommunityForumContribution"),
        @JsonSubTypes.Type(value = PersonalNote.class, name = "PersonalNote")
})
public abstract class NoteEntryHeader extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private String user  = null;
    private String title = null;
    private String text  = null;


    /**
     * Default constructor
     */
    public NoteEntryHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteEntryHeader(NoteEntryHeader template)
    {
        super(template);

        if (template != null)
        {
            user = template.getUser();
            title = template.getTitle();
            text = template.getText();
        }
    }

    /**
     * Return the user id of the person who created the tag.  Null means the user id is not known.
     *
     * @return String tagging user
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the tag.  Null means the user id is not known.
     *
     * @param user String identifier of the creator of the tag.
     */
    public void setUser(String user)
    {
        this.user = user;
    }


    /**
     * Return the title of the entry.
     *
     * @return string title
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the title of the entry.
     *
     * @param title string title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return the main text of the entry.
     *
     * @return string text
     */
    public String getText()
    {
        return text;
    }


    /**
     * Set up the main text for this entry.
     *
     * @param text string text
     */
    public void setText(String text)
    {
        this.text = text;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NoteEntryHeader{" +
                       "user='" + user + '\'' +
                       ", title='" + title + '\'' +
                       ", text='" + text + '\'' +
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
        NoteEntryHeader that = (NoteEntryHeader) objectToCompare;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getText(), that.getText());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUser(), getTitle(), getText());
    }
}
