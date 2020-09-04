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
public abstract class NoteEntryHeader extends CommonHeader
{
    private static final long    serialVersionUID = 1L;

    private     String                  qualifiedName      = null;
    private     String                  title              = null;
    private     String                  text               = null;


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
            qualifiedName = template.getQualifiedName();
            title = template.getTitle();
            text = template.getText();
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
                "qualifiedName='" + qualifiedName + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", originId='" + getOriginId() + '\'' +
                ", originName='" + getOriginName() + '\'' +
                ", originType='" + getOriginType() + '\'' +
                ", originLicense='" + getOriginLicense() + '\'' +
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
        return Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
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
        return Objects.hash(super.hashCode(), getQualifiedName(), getTitle(), getText());
    }
}
