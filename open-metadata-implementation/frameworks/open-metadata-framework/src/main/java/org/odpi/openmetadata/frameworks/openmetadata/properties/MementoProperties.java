/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the properties for the Anchors classification
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MementoProperties extends ClassificationBeanProperties
{
    private Date                archiveDate       = null;
    private String              archiveUser       = null;
    private String              archiveService    = null;
    private String              archiveMethod     = null;
    private String              archiveProcess    = null;
    private Map<String, String> archiveProperties = null;


    /**
     * Default constructor
     */
    public MementoProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.MEMENTO_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MementoProperties(MementoProperties template)
    {
        super(template);

        if (template != null)
        {
            this.archiveDate       = template.getArchiveDate();
            this.archiveUser       = template.getArchiveUser();
            this.archiveService    = template.getArchiveService();
            this.archiveMethod     = template.getArchiveMethod();
            this.archiveProcess    = template.getArchiveProcess();
            this.archiveProperties = template.getArchiveProperties();
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
        return archiveProperties;
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
     * Return the userId requesting the archive.
     *
     * @return string name
     */
    public String getArchiveUser()
    {
        return archiveUser;
    }


    /**
     * Set up the userId requesting the archive.
     *
     * @param archiveUser string name
     */
    public void setArchiveUser(String archiveUser)
    {
        this.archiveUser = archiveUser;
    }


    /**
     * Return the service used to archive.
     *
     * @return string name
     */
    public String getArchiveService()
    {
        return archiveService;
    }


    /**
     * Set up the service used to archive.
     *
     * @param archiveService string name
     */
    public void setArchiveService(String archiveService)
    {
        this.archiveService = archiveService;
    }


    /**
     * Return the archive method.
     *
     * @return string guid
     */
    public String getArchiveMethod()
    {
        return archiveMethod;
    }


    /**
     * Set up the archive method.
     *
     * @param archiveMethod string guid
     */
    public void setArchiveMethod(String archiveMethod)
    {
        this.archiveMethod = archiveMethod;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MementoProperties{" +
                "archiveDate=" + archiveDate +
                ", archiveUser='" + archiveUser + '\'' +
                ", archiveService='" + archiveService + '\'' +
                ", archiveMethod='" + archiveMethod + '\'' +
                ", archiveProcess='" + archiveProcess + '\'' +
                ", archiveProperties=" + archiveProperties +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        MementoProperties that = (MementoProperties) objectToCompare;
        return Objects.equals(archiveDate, that.archiveDate) &&
                Objects.equals(archiveUser, that.archiveUser) &&
                Objects.equals(archiveService, that.archiveService) &&
                Objects.equals(archiveProcess, that.archiveProcess) &&
                Objects.equals(archiveProperties, that.archiveProperties) &&
                Objects.equals(archiveMethod, that.archiveMethod);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), archiveDate, archiveUser,
                            archiveService, archiveMethod,
                            archiveProcess, archiveProperties);
    }
}
