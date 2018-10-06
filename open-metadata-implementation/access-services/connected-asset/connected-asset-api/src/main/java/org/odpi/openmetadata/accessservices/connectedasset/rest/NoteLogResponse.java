/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.NoteLog;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * NoteLogResponse is the response structure used on the OMAS REST API calls that returns a
 * NoteLog object as a response.  It returns details of the note log and the count of the notes within it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteLogResponse implements java.io.Serializable
{
    private static final long    serialVersionUID = 1L;

    private NoteLog noteLog   = null;
    private int     noteCount = 0;


    /**
     * Default constructor
     */
    public NoteLogResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteLogResponse(NoteLogResponse template)
    {
        if (template != null)
        {
            this.noteLog = template.getNoteLog();
            this.noteCount = template.getNoteCount();
        }
    }


    /**
     * Return the note log properties.
     *
     * @return note log bean
     */
    public NoteLog getNoteLog()
    {
        return noteLog;
    }


    /**
     * Set up the not log properties.
     *
     * @param noteLog bean
     */
    public void setNoteLog(NoteLog noteLog)
    {
        this.noteLog = noteLog;
    }


    /**
     * Return the count of the notes within the note log.
     *
     * @return int
     */
    public int getNoteCount()
    {
        return noteCount;
    }


    /**
     * Set up the count of notes within the note log.
     *
     * @param noteCount int
     */
    public void setNoteCount(int noteCount)
    {
        this.noteCount = noteCount;
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
                ", noteCount=" + noteCount +
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
        NoteLogResponse that = (NoteLogResponse) objectToCompare;
        return getNoteCount() == that.getNoteCount() &&
                Objects.equals(getNoteLog(), that.getNoteLog());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getNoteLog(), getNoteCount());
    }
}
