/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Identifies an asset and containing an exception backlog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExceptionBacklogProperties extends ClassificationBeanProperties
{
    private String process             = null;
    private String steward             = null;
    private String stewardTypeName     = null;
    private String stewardPropertyName = null;
    private String source              = null;
    private String notes               = null;



    /**
     * Default constructor
     */
    public ExceptionBacklogProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXCEPTION_BACKLOG_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExceptionBacklogProperties(ExceptionBacklogProperties template)
    {
        super(template);

        if (template != null)
        {
            process             = template.getProcess();
            source              = template.getSource();
            steward             = template.getSteward();
            stewardTypeName     = template.getStewardTypeName();
            stewardPropertyName = template.getStewardPropertyName();
            notes               = template.getNotes();
        }
    }


    /**
     * Return the process that is responsible for the exception backlog.
     *
     * @return string
     */
    public String getProcess()
    {
        return process;
    }


    /**
     * Set up the process that is responsible for the exception backlog.
     *
     * @param process string
     */
    public void setProcess(String process)
    {
        this.process = process;
    }


    /**
     * Return the source of information that determined the exception.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of information that determined the exception.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Returns the id of the steward responsible for the exception backlog.
     *
     * @return String id
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the id of the steward responsible for the exception backlog.
     *
     * @param steward String id
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return the type of element that describes the steward.
     *
     * @return type name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type of element that describes the steward.
     *
     * @param stewardTypeName type name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property that holds the steward's identifier.
     *
     * @return property name
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the name of the property that holds the steward's identifier.
     *
     * @param stewardPropertyName property name
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }


    /**
     * Return the additional notes associated with the exception backlog.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the additional notes associated with the exception backlog.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
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
        return "ExceptionBacklogProperties{" +
                "process='" + process + '\'' +
                ", steward='" + steward + '\'' +
                ", stewardTypeName='" + stewardTypeName + '\'' +
                ", stewardPropertyName='" + stewardPropertyName + '\'' +
                ", source='" + source + '\'' +
                ", notes='" + notes + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare)) return false;
        ExceptionBacklogProperties that = (ExceptionBacklogProperties) objectToCompare;
        return Objects.equals(process, that.process) &&
                Objects.equals(steward, that.steward) &&
                Objects.equals(stewardTypeName, that.stewardTypeName) &&
                Objects.equals(stewardPropertyName, that.stewardPropertyName) &&
                Objects.equals(source, that.source) &&
                Objects.equals(notes, that.notes);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), process, steward, stewardTypeName, stewardPropertyName, source, notes);
    }
}
