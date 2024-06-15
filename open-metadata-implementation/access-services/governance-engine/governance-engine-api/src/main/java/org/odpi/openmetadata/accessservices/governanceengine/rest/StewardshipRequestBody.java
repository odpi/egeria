/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * StewardshipRequestBody provides a structure for passing the properties associated with stewardship actions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StewardshipRequestBody
{
    private int     statusIdentifier     = 0;
    private String  steward              = null;
    private String  stewardTypeName      = null;
    private String  stewardPropertyName  = null;
    private String  source               = null;
    private String  notes                = null;


    /**
     * Default constructor
     */
    public StewardshipRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StewardshipRequestBody(StewardshipRequestBody template)
    {
        if (template != null)
        {
            statusIdentifier = template.getStatusIdentifier();
            steward = template.getSteward();
            stewardTypeName = template.getStewardPropertyName();
            stewardPropertyName = template.getStewardPropertyName();
            source = template.getSource();
            notes = template.getNotes();
        }
    }


    /**
     * Return the status of this duplicate - usable if greater than 0, 0=proposed, negative means invalid
     *
     * @return int
     */
    public int getStatusIdentifier()
    {
        return statusIdentifier;
    }


    /**
     * Set up the status of this duplicate - usable if greater than 0, 0=proposed, negative means invalid
     *
     * @param statusIdentifier int
     */
    public void setStatusIdentifier(int statusIdentifier)
    {
        this.statusIdentifier = statusIdentifier;
    }


    /**
     * Return the identifier of the steward that added this duplicate.
     *
     * @return identifier
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the identifier of the steward that added this duplicate.
     *
     * @param steward identifier
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return the type name of the element used to identify the steward.
     *
     * @return type name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type name of the element used to identify the steward.
     *
     * @param stewardTypeName type name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property used to identify the steward.
     *
     * @return property name
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Return the name of the property used to identify the steward.
     *
     * @param stewardPropertyName property name
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }


    /**
     * Return the details of the source that detected the duplicate.
     *
     * @return name
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the details of the source that detected the duplicate.
     *
     * @param source name
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return any notes for the steward.
     *
     * @return text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up any notes for the steward.
     *
     * @param notes text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "StewardshipRequestBody{" +
                       "statusIdentifier=" + statusIdentifier +
                       ", steward='" + steward + '\'' +
                       ", stewardTypeName='" + stewardTypeName + '\'' +
                       ", stewardPropertyName='" + stewardPropertyName + '\'' +
                       ", source='" + source + '\'' +
                       ", notes='" + notes + '\'' +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        StewardshipRequestBody that = (StewardshipRequestBody) objectToCompare;
        return statusIdentifier == that.statusIdentifier && Objects.equals(steward, that.steward) && Objects.equals(stewardTypeName,
                                                                                                                    that.stewardTypeName) && Objects.equals(
                stewardPropertyName, that.stewardPropertyName) && Objects.equals(source, that.source) && Objects.equals(notes,
                                                                                                                        that.notes);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(statusIdentifier, steward, stewardTypeName, stewardPropertyName, source, notes);
    }
}
