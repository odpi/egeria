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
 * Identifies an asset containing an audit log.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LineageLogProperties extends ClassificationBeanProperties
{
    private String process = null;
    private String source  = null;
    private String notes   = null;

    /**
     * Default constructor
     */
    public LineageLogProperties()
    {
        super();
        super.typeName = OpenMetadataType.LINEAGE_LOG_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LineageLogProperties(LineageLogProperties template)
    {
        super(template);

        if (template != null)
        {
            process = template.getProcess();
            source  = template.getSource();
            notes   = template.getNotes();
        }
    }


    /**
     * Return the process that is responsible for the lineage log.
     *
     * @return string
     */
    public String getProcess()
    {
        return process;
    }


    /**
     * Set up the process that is responsible for the lineage log.
     *
     * @param process string
     */
    public void setProcess(String process)
    {
        this.process = process;
    }


    /**
     * Return the source of information that determined the log record.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of information that determined the log record.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the additional notes associated with the lineage log.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the additional notes associated with the audit log.
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
        return "LineageLogProperties{" +
                "process='" + process + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        LineageLogProperties that = (LineageLogProperties) objectToCompare;
        return Objects.equals(process, that.process) &&
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
        return Objects.hash(super.hashCode(), process, source, notes);
    }
}
