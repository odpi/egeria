/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Note;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.NoteLogHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * NoteLogResponse is the response structure used on the OMAS REST API calls that returns a
 * NoteLogHeader object as a response.  It returns details of the note log, the first few notes and the total count
 * of the notes within it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteLogResponse extends OCFOMASAPIResponse
{
    private NoteLogHeader noteLog = null;
    private List<Note>    notes   = null;

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
    public NoteLogResponse(NoteLogResponse template)
    {
        super(template);

        if (template != null)
        {
            this.noteLog = template.getNoteLog();
            this.notes = template.getNotes();
        }
    }


    /**
     * Return the note log properties.
     *
     * @return note log bean
     */
    public NoteLogHeader getNoteLog()
    {
        return noteLog;
    }


    /**
     * Set up the not log properties.
     *
     * @param noteLog bean
     */
    public void setNoteLog(NoteLogHeader noteLog)
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
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
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
        return Objects.equals(getNoteLog(), that.getNoteLog()) &&
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
        return Objects.hash(super.hashCode(), getNoteLog(), getNotes());
    }
}
