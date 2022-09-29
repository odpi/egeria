/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ArchiveProperties defined the properties that are stored when a data source is archived or deleted.  This
 * allows the Asset to remain in the metadata repository after the real-world artifact has gone.  This is important
 * to prevent lineage graphs from becoming fragmented.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArchiveProperties implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private Date                archiveDate       = null;
    private String              archiveProcess    = null;
    private Map<String, String> archiveProperties = null;


    /**
     * Default constructor
     */
    public ArchiveProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor for the template properties.
     *
     * @param template template object to copy.
     */
    public ArchiveProperties(ArchiveProperties template)
    {
        if (template != null)
        {
            archiveDate = template.getArchiveDate();
            archiveProcess = template.getArchiveProcess();
            archiveProperties = template.getArchiveProperties();
        }
    }


    /**
     * Returns the date when the data source was archived (or discovered missing).  Null means "now".
     *
     * @return date of archive
     */
    public Date getArchiveDate()
    {
        return archiveDate;
    }


    /**
     * Set up the date when the data source was archived (or discovered missing).  Null means "now".
     *
     * @param archiveDate date of archive
     */
    public void setArchiveDate(Date archiveDate)
    {
        this.archiveDate = archiveDate;
    }


    /**
     * Returns the name of the process that either performed the archive or detected the missing data source.
     *
     * @return String name
     */
    public String getArchiveProcess()
    {
        return archiveProcess;
    }


    /**
     * Set up the name of the process that either performed the archive or detected the missing data source.
     *
     * @param archiveProcess String name
     */
    public void setArchiveProcess(String archiveProcess)
    {
        this.archiveProcess = archiveProcess;
    }


    /**
     * Return the properties that characterize where the data source was archived to.
     *
     * @return map of name value pairs, all strings
     */
    public Map<String, String> getArchiveProperties()
    {
        if (archiveProperties == null)
        {
            return null;
        }
        else if (archiveProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(archiveProperties);
        }
    }


    /**
     * Set up the properties that characterize where the data source was archived to.
     *
     * @param archiveProperties map of name value pairs, all strings
     */
    public void setArchiveProperties(Map<String, String> archiveProperties)
    {
        this.archiveProperties = archiveProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ArchiveProperties{" +
                       "archiveDate=" + archiveDate +
                       ", archiveProcess='" + archiveProcess + '\'' +
                       ", archiveProperties='" + archiveProperties +
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
        ArchiveProperties that = (ArchiveProperties) objectToCompare;
        return Objects.equals(archiveDate, that.archiveDate) &&
                Objects.equals(archiveProcess, that.archiveProcess) &&
                Objects.equals(archiveProperties, that.archiveProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(archiveDate, archiveProcess, archiveProperties);
    }
}
