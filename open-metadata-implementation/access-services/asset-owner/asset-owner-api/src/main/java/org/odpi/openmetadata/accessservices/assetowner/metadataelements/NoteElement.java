/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.NoteProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serial;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteElement contains the properties and header for a Note entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteElement implements MetadataElement
{
    private ElementHeader                   elementHeader         = null;
    private NoteProperties                  properties            = null;
    private Date                            lastUpdate            = null;
    private String                          user                  = null;
    private FeedbackTargetElement           feedbackTargetElement = null;


    /**
     * Default constructor
     */
    public NoteElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NoteElement(NoteElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            feedbackTargetElement = template.getFeedbackTargetElement();
            user = template.getUser();

            Date templateLastUpdate = template.getLastUpdate();
            if (templateLastUpdate != null)
            {
                lastUpdate = new Date(templateLastUpdate.getTime());
            }
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the note.
     *
     * @return properties bean
     */
    public NoteProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the note.
     *
     * @param properties properties bean
     */
    public void setProperties(NoteProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return details of the relationship from the element in the request to the note.  This value is null if the note was retrieved independently
     * of any noted element.
     *
     * @return associated relationship
     */
    public FeedbackTargetElement getFeedbackTargetElement()
    {
        return feedbackTargetElement;
    }


    /**
     * Set up details of the relationship from the element in the request to the note.  This value is null if the note was retrieved independently
     * of any noted element.
     *
     * @param feedbackTargetElement associated relationship
     */
    public void setFeedbackTargetElement(FeedbackTargetElement feedbackTargetElement)
    {
        this.feedbackTargetElement = feedbackTargetElement;
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
            return null;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NoteElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", lastUpdate=" + lastUpdate +
                       ", user='" + user + '\'' +
                       ", feedbackTargetElement=" + feedbackTargetElement +
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
        if (! (objectToCompare instanceof NoteElement that))
        {
            return false;
        }
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(user, that.user) &&
                       Objects.equals(feedbackTargetElement, that.feedbackTargetElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, feedbackTargetElement, user, lastUpdate);
    }
}
