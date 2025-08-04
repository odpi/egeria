/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UpdateOptions provides a structure for the additional options when updating an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeleteOptions extends MetadataSourceOptions
{
    private boolean             cascadedDelete    = false;
    private DeleteMethod        deleteMethod      = DeleteMethod.LOOK_FOR_LINEAGE;
    private Date                archiveDate       = null;
    private String              archiveProcess    = null;
    private Map<String, String> archiveProperties = null;


    /**
     * Default constructor
     */
    public DeleteOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeleteOptions(DeleteOptions template)
    {
        super(template);

        if (template != null)
        {
            cascadedDelete = template.getCascadedDelete();
            deleteMethod = template.getDeleteMethod();
            archiveDate = template.getArchiveDate();
            archiveProcess = template.getArchiveProcess();
            archiveProperties = template.getArchiveProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeleteOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return flag to indicate whether to completely replace the existing properties with the new properties, or just update
     * the individual properties specified on the request.  The default is false.
     *
     * @return boolean
     */
    public boolean getCascadedDelete()
    {
        return cascadedDelete;
    }


    /**
     * Set up flag to indicate whether to completely replace the existing properties with the new properties, or just update
     * the individual properties specified on the request.  The default is false.
     *
     * @param cascadedDelete boolean
     */
    public void setCascadedDelete(boolean cascadedDelete)
    {
        this.cascadedDelete = cascadedDelete;
    }


    /**
     * Return the approach to use to delete the element.
     *
     * @return enum
     */
    public DeleteMethod getDeleteMethod()
    {
        return deleteMethod;
    }


    /**
     * Set up the approach to use to delete the element.
     *
     * @param deleteMethod enum
     */
    public void setDeleteMethod(DeleteMethod deleteMethod)
    {
        this.deleteMethod = deleteMethod;
    }


    /**
     * Returns the date when the element was archived (or discovered missing).  Null means "now".
     * This field is used if the element is archived.  It is stored in the Memento classification.
     *
     * @return date of archive
     */
    public Date getArchiveDate()
    {
        return archiveDate;
    }


    /**
     * Set up the date when the element was archived (or a related item was discovered missing).  Null means "now".
     * This field is used if the element is archived.  It is stored in the Memento classification.
     *
     * @param archiveDate date of archive
     */
    public void setArchiveDate(Date archiveDate)
    {
        this.archiveDate = archiveDate;
    }


    /**
     * Returns the name of the process that requested delete.
     * This field is used if the element is archived.  It is stored in the Memento classification.
     *
     * @return String name
     */
    public String getArchiveProcess()
    {
        return archiveProcess;
    }


    /**
     * Set up the name of the process that requested delete.
     * This field is used if the element is archived.  It is stored in the Memento classification.
     *
     * @param archiveProcess String name
     */
    public void setArchiveProcess(String archiveProcess)
    {
        this.archiveProcess = archiveProcess;
    }


    /**
     * Return the properties that provide additional information related to any associated data - eg
     * details of where the data source was archived to.
     * This field is used if the element is archived.  It is stored in the Memento classification.
     *
     * @return map of name value pairs, all strings
     */
    public Map<String, String> getArchiveProperties()
    {
        return archiveProperties;
    }


    /**
     * Set up the properties that provide additional information related to any associated data - eg
     * details of where the data source was archived to.
     * This field is used if the element is archived.  It is stored in the Memento classification.
     *
     * @param archiveProperties map of name value pairs, all strings
     */
    public void setArchiveProperties(Map<String, String> archiveProperties)
    {
        this.archiveProperties = archiveProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DeleteOptions{" +
                "cascadedDelete=" + cascadedDelete +
                ", deleteMethod=" + deleteMethod +
                ", archiveDate=" + archiveDate +
                ", archiveProcess='" + archiveProcess + '\'' +
                ", archiveProperties=" + archiveProperties +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DeleteOptions that = (DeleteOptions) objectToCompare;
        return cascadedDelete == that.cascadedDelete &&
                deleteMethod == that.deleteMethod &&
                Objects.equals(archiveDate, that.archiveDate) &&
                Objects.equals(archiveProcess, that.archiveProcess) &&
                Objects.equals(archiveProperties, that.archiveProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), cascadedDelete, deleteMethod, archiveDate, archiveProcess, archiveProperties);
    }
}
