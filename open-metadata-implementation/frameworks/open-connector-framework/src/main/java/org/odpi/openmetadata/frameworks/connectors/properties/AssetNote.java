/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Note;

import java.util.Date;
import java.util.Objects;


/**
 * Note defines the properties of a single note in a note log.
 */
public class AssetNote extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected Note  noteBean;


    /**
     * Bean constructor
     *
     * @param noteBean bean containing the properties
     */
    public AssetNote(Note  noteBean)
    {
        super(noteBean);

        if (noteBean == null)
        {
            this.noteBean = new Note();
        }
        else
        {
            this.noteBean = new Note(noteBean);
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param noteBean bean containing the properties
     */
    public AssetNote(AssetDescriptor parentAsset,
                     Note            noteBean)
    {
        super(parentAsset, noteBean);

        if (noteBean == null)
        {
            this.noteBean = new Note();
        }
        else
        {
            this.noteBean = new Note(noteBean);
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateNote note to copy
     */
    public AssetNote(AssetDescriptor parentAsset, AssetNote templateNote)
    {
        /*
         * Save the parent asset description.
         */
        super(parentAsset, templateNote);

        if (templateNote == null)
        {
            this.noteBean = new Note();
        }
        else
        {
            this.noteBean = templateNote.getNoteBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return note bean
     */
    protected  Note getNoteBean()
    {
        return noteBean;
    }

    /**
     * Return the text of the note.
     *
     * @return String text
     */
    public String getText() { return noteBean.getText(); }


    /**
     * Return the last time a change was made to this note.
     *
     * @return Date last update
     */
    public Date getLastUpdate()
    {
        Date lastUpdate = noteBean.getLastUpdate();

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
     * Return the user id of the person who created the note.  Null means the user id is not known.
     *
     * @return String liking user
     */
    public String getUser() { return noteBean.getUser(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return noteBean.toString();
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
        AssetNote assetNote = (AssetNote) objectToCompare;
        return Objects.equals(noteBean, assetNote.noteBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), noteBean);
    }
}