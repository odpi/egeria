/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.NoteLogHeader;

import java.util.Objects;


/**
 * Stores information about a comment connected to an asset.  Comments provide informal feedback to assets
 * and can be added at any time.
 *
 * Comments have the userId of the person who added the feedback, along with their comment text.
 *
 * Comments can have other comments attached.
 *
 * The content of the comment is a personal statement (which is why the user's id is in the comment)
 * and there is no formal review of the content.
 */
public class NoteLog extends NoteLogHeader
{
    protected Notes notes = null;

    /**
     * Bean constructor
     *
     * @param noteLogHeader bean that contains all the properties
     * @param notes iterator
     */
    public NoteLog(NoteLogHeader noteLogHeader,
                   Notes         notes)
    {
        super(noteLogHeader);
        this.notes = notes;
    }



    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public NoteLog(NoteLog template)
    {
        super(template);

        if (template != null)
        {
            notes = template.getNotes();
        }
    }


    /**
     * Return an iterator of the replies to this comment - null means no replies are available.
     *
     * @return comment replies iterator
     */
    public Notes getNotes()
    {
        if (notes == null)
        {
            return null;
        }
        else
        {
            return notes.cloneIterator();
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NoteLog{" +
                       "notes=" + notes +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
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
        NoteLog that = (NoteLog) objectToCompare;
        return Objects.equals(notes, that.notes);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), notes);
    }
}