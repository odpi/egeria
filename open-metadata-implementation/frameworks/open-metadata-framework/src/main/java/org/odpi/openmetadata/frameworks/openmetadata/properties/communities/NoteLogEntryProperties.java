/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.communities;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteLogEntryProperties covers the entries in a community forum and personal notes since they are based on a note log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteLogEntryProperties extends ReferenceableProperties
{
    private List<String> contributors = null;
    private String       title        = null;
    private String       text         = null;


    /**
     * Default constructor
     */
    public NoteLogEntryProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteLogEntryProperties(NoteLogEntryProperties template)
    {
        super(template);

        if (template != null)
        {
            contributors = template.getContributors();
            title = template.getTitle();
            text = template.getText();
        }
    }

    /**
     * Return the list of user ids of the people who created the note log entry.
     *
     * @return  authoring users
     */
    public List<String> getContributors()
    {
        if (contributors == null)
        {
            return null;
        }
        else if (contributors.isEmpty())
        {
            return null;
        }

        return contributors;
    }


    /**
     * Set up the user id of the person who created the tag.  Null means the user id is not known.
     *
     * @param contributors  authoring users
     */
    public void setContributors(List<String> contributors)
    {
        this.contributors = contributors;
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
        return "NoteLogEntryProperties{" +
                       "contributors=" + contributors +
                       ", title='" + title + '\'' +
                       ", text='" + text + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        NoteLogEntryProperties that = (NoteLogEntryProperties) objectToCompare;
        return Objects.equals(getContributors(), that.getContributors()) &&
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
        return Objects.hash(super.hashCode(), getContributors(), getTitle(), getText());
    }
}
