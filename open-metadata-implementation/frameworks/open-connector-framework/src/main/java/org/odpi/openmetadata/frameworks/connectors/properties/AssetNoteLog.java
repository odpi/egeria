/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.NoteLog;

import java.util.Objects;

/**
 * AssetNoteLog manages a collection of notes for an asset
 */
public class AssetNoteLog extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected NoteLog    noteLogBean;
    protected AssetNotes notes = null;


    /**
     * Bean constructor
     *
     * @param noteLogBean bean describing the note log
     * @param notes iterator for the notes within the note log
     */
    public AssetNoteLog(NoteLog noteLogBean,
                        AssetNotes notes)
    {
        super(noteLogBean);

        if (noteLogBean == null)
        {
            this.noteLogBean = new NoteLog();
        }
        else
        {
            this.noteLogBean = noteLogBean;
        }

        this.notes = notes;
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset   descriptor for parent asset
     * @param noteLogBean bean describing the note log
     * @param notes iterator for the notes within the note log
     */
    public AssetNoteLog(AssetDescriptor parentAsset,
                        NoteLog noteLogBean,
                        AssetNotes notes)
    {
        super(parentAsset, noteLogBean);

        if (noteLogBean == null)
        {
            this.noteLogBean = new NoteLog();
        }
        else
        {
            this.noteLogBean = noteLogBean;
        }

        this.notes = notes;
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset   descriptor for parent asset
     * @param templateNoteLog   note log to copy
     */
    public AssetNoteLog(AssetDescriptor parentAsset, AssetNoteLog templateNoteLog)
    {
        super(parentAsset, templateNoteLog);

        if (templateNoteLog == null)
        {
            this.noteLogBean = new NoteLog();
        }
        else
        {
            NoteLog noteLogBean = templateNoteLog.getNoteLogBean();

            this.noteLogBean = noteLogBean;

            AssetNotes templateNotes = templateNoteLog.getNotes();
            if (templateNotes != null)
            {
                notes = templateNotes.cloneIterator(super.getParentAsset());
            }
        }
    }


    /**
     * Return the bean containing the properties for the note log
     *
     * @return note log bean
     */
    protected NoteLog getNoteLogBean()
    {
        return noteLogBean;
    }


    /**
     * Returns the stored display name property for the note log.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName() { return noteLogBean.getDisplayName(); }


    /**
     * Returns the stored description property for the note log.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription() { return noteLogBean.getDescription(); }


    /**
     * Return the list of notes defined for this note log.
     *
     * @return Notes   list of notes
     */
    public AssetNotes getNotes()
    {
        if (notes == null)
        {
            return null;
        }
        else
        {
            return notes.cloneIterator(super.getParentAsset());
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
        return noteLogBean.toString();
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
        AssetNoteLog that = (AssetNoteLog) objectToCompare;
        return Objects.equals(noteLogBean, that.noteLogBean) &&
                       Objects.equals(notes, that.notes);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), noteLogBean, notes);
    }
}