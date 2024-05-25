/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NoteLogHeader manages a list of notes for an asset
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NoteLogHeader extends Referenceable
{
    /*
     * Attributes of a note log
     */
    protected String     displayName = null;
    protected String     description = null;


    /**
     * Default constructor
     */
    public NoteLogHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateNoteLog   note log to copy
     */
    public NoteLogHeader(NoteLogHeader templateNoteLog)
    {
        super(templateNoteLog);

        if (templateNoteLog != null)
        {
            displayName = templateNoteLog.getDisplayName();
            description = templateNoteLog.getDescription();
        }
    }


    /**
     * Return the stored display name property for the note log.
     * If no display name is available then null is returned.
     *
     * @return String Name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the note log.
     *
     * @param displayName - String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the stored description property for the note log.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the note log.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NoteLogHeader{" +
                       "URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
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
        NoteLogHeader noteLog = (NoteLogHeader) objectToCompare;
        return Objects.equals(getDisplayName(), noteLog.getDisplayName()) &&
                       Objects.equals(getDescription(), noteLog.getDescription());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription());
    }
}