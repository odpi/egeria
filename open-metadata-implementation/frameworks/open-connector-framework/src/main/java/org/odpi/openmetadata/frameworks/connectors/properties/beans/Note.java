/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Note defines the properties of a single note in a note log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Note extends Referenceable
{
    private static final long     serialVersionUID = 1L;

    /*
     * Attributes of a Note
     */
    protected String text = null;
    protected Date   lastUpdate = null;
    protected String user = null;


    /**
     * Default constructor
     */
    public Note()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateNote note to copy
     */
    public Note(Note templateNote)
    {
        super(templateNote);

        if (templateNote != null)
        {
            text = templateNote.getText();
            user = templateNote.getUser();

            Date templateLastUpdate = templateNote.getLastUpdate();
            if (templateLastUpdate != null)
            {
                lastUpdate = new Date(templateLastUpdate.getTime());
            }
        }
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
     * Return the last time a change was made to this note.
     *
     * @return Date last updated
     */
    public Date getLastUpdate()
    {
        if (lastUpdate == null)
        {
            return lastUpdate;
        }
        else
        {
            return new Date(lastUpdate.getTime());
        }
    }


    /**
     * Set up the last time a change was made to this note.
     *
     * @param lastUpdate Date last updated
     */
    public void setLastUpdate(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }


    /**
     * Return the user id of the person who created the note.  Null means the user id is not known.
     *
     * @return String user making notes
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the note.  Null means the user id is not known.
     *
     * @param user String user making notes
     */
    public void setUser(String user)
    {
        this.user = user;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Note{" +
                       "URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", text='" + text + '\'' +
                       ", lastUpdate=" + lastUpdate +
                       ", user='" + user + '\'' +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Note note = (Note) objectToCompare;
        return Objects.equals(getText(), note.getText()) &&
                       Objects.equals(getLastUpdate(), note.getLastUpdate()) &&
                       Objects.equals(getUser(), note.getUser());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getText(), getLastUpdate(), getUser());
    }
}