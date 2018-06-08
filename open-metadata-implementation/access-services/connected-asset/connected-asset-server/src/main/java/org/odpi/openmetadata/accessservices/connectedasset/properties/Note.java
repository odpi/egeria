/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

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
    /*
     * Attributes of a Note
     */
    private String text = null;
    private Date   lastUpdate = null;
    private String user = null;


    /**
     * Default Constructor
     */
    public Note()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateNote - note to copy
     */
    public Note(Note templateNote)
    {
        super(templateNote);

        if (templateNote != null)
        {
            /*
             * Copy the properties from the supplied note.
             */
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
     * @param text - String
     */
    public void setText(String text) { this.text = text; }


    /**
     * Return the last time a change was made to this note.
     *
     * @return Date last update
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
     * Set up the last update data for the note.
     *
     * @param lastUpdate - Date
     */
    public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }


    /**
     * Return the user id of the person who created the like.  Null means the user id is not known.
     *
     * @return String - liking user
     */
    public String getUser() {
        return user;
    }


    /**
     * Set up the user id of the person who created the like. Null means the user id is not known.
     *
     * @param user - String - liking user
     */
    public void setUser(String user) {
        this.user = user;
    }
}