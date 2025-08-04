/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateOptions provides a structure for the additional options when updating an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditOptions extends MetadataSourceOptions
{
    private String journalCategory = "journal";
    private String journalEntry    = null;



    /**
     * Default constructor
     */
    public AuditOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AuditOptions(AuditOptions template)
    {
        super(template);

        if (template != null)
        {
            journalCategory = template.getJournalCategory();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AuditOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return category of note log where the journal entry should go.  The default is "journal".
     *
     * @return string
     */
    public String getJournalCategory()
    {
        return journalCategory;
    }


    /**
     * Set up category of note log where the journal entry should go.  The default is "journal".
     *
     * @param journalCategory string
     */
    public void setJournalCategory(String journalCategory)
    {
        this.journalCategory = journalCategory;
    }


    /**
     * Return the text that should be added the journal of the anchor element.
     *
     * @return string
     */
    public String getJournalEntry()
    {
        return journalEntry;
    }


    /**
     * Set up the text that should be added the journal of the anchor element.
     *
     * @param journalEntry string
     */
    public void setJournalEntry(String journalEntry)
    {
        this.journalEntry = journalEntry;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UpdateOptions{" +
                ", replaceAllProperties=" + journalCategory +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof AuditOptions that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return journalCategory == that.journalCategory;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), journalCategory);
    }
}
