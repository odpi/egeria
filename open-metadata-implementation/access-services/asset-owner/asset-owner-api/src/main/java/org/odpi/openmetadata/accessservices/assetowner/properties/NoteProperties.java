/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Note defines the properties of a single note in a note log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteProperties extends ReferenceableProperties
{
    private String title = null;
    private String text  = null;



    /**
     * Default constructor
     */
    public NoteProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template note to copy
     */
    public NoteProperties(NoteProperties template)
    {
        super(template);

        if (template != null)
        {
            title = template.getTitle();
            text = template.getText();
        }
    }


    /**
     * Return the title.
     *
     * @return string
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the title.
     *
     * @param title string
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return the text of the note.
     *
     * @return String text
     */
    public String getText() { return text; }


    /**
     * Set up the text of the note.
     *
     * @param text String text
     */
    public void setText(String text)
    {
        this.text = text;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NoteProperties{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NoteProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(title, that.title) && Objects.equals(text, that.text);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), title, text);
    }
}