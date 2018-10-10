/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Note;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.NoteLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteLogResponse extends AssetConsumerOMASAPIResponse
{
    private NoteLog    noteLog             = null;
    private List<Note> notes               = null;
    private int        startingFromElement = 0;
    private int        totalListSize       = 0;


    /**
     * Default constructor
     */
    public NoteLogResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteLogResponse(NoteLogResponse  template)
    {
        super(template);

        if (template != null)
        {
            this.noteLog = template.getNoteLog();
            this.notes = template.getNotes();
            this.startingFromElement = template.getStartingFromElement();
            this.totalListSize = template.getTotalListSize();
        }
    }


    /**
     * Return the note log header.
     *
     * @return note log header
     */
    public NoteLog getNoteLog()
    {
        if (noteLog == null)
        {
            return null;
        }
        else
        {
            return new NoteLog(noteLog);
        }
    }


    /**
     * Set up the note log header
     *
     * @param noteLog note log header
     */
    public void setNoteLog(NoteLog noteLog)
    {
        this.noteLog = noteLog;
    }


    /**
     * Return the notes in the note log.
     *
     * @return list of notes
     */
    public List<Note> getNotes()
    {
        if (notes == null)
        {
            return null;
        }
        else if (notes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(notes);
        }
    }


    /**
     * Set up the list of notes from the note log.  If there are too many notes to return on a single call,
     * the starting element number for the notes returned in this response are
     *
     * @param notes list of notes
     */
    public void setNotes(List<Note> notes)
    {
        this.notes = notes;
    }


    /**
     * Return the starting element number from the server side list that this response contains.
     *
     * @return int
     */
    public int getStartingFromElement()
    {
        return startingFromElement;
    }


    /**
     * Set up the starting element number from the server side list that this response contains.
     *
     * @param startingFromElement int
     */
    public void setStartingFromElement(int startingFromElement)
    {
        this.startingFromElement = startingFromElement;
    }


    /**
     * Return the size of the list at the server side.
     *
     * @return int
     */
    public int getTotalListSize()
    {
        return totalListSize;
    }


    /**
     * Set up the size of the list at the server side.
     *
     * @param totalListSize int
     */
    public void setTotalListSize(int totalListSize)
    {
        this.totalListSize = totalListSize;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NoteLogResponse{" +
                "noteLog=" + noteLog +
                ", notes=" + notes +
                ", startingFromElement=" + startingFromElement +
                ", totalListSize=" + totalListSize +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        NoteLogResponse that = (NoteLogResponse) objectToCompare;
        return getStartingFromElement() == that.getStartingFromElement() &&
                getTotalListSize() == that.getTotalListSize() &&
                Objects.equals(getNoteLog(), that.getNoteLog()) &&
                Objects.equals(getNotes(), that.getNotes());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getNoteLog(), getNotes(), getStartingFromElement(), getTotalListSize());
    }
}
