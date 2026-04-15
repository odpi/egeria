/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Identifies a collection of log analysis results.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogAnalysisProperties extends ClassificationBeanProperties
{
    private String               process                 = null;
    private String               source                  = null;
    private String               notes                   = null;
    private Date                 dataCollectionStartTime = null;
    private Date                 dataCollectionEndTime   = null;
    private Map<String, Integer> counts                  = null;
    private Map<String, String>  values                  = null;
    private Map<String, Boolean> flags                   = null;
    private Map<String, Date>    dates                   = null;

    /**
     * Default constructor
     */
    public LogAnalysisProperties()
    {
        super();
        super.typeName = OpenMetadataType.LOG_ANALYSIS_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LogAnalysisProperties(LogAnalysisProperties template)
    {
        super(template);

        if (template != null)
        {
            process = template.getProcess();
            source  = template.getSource();
            notes   = template.getNotes();

            dataCollectionStartTime = template.getDataCollectionStartTime();
            dataCollectionEndTime   = template.getDataCollectionEndTime();
            counts                  = template.getCounts();
            values                  = template.getValues();
            flags                   = template.getFlags();
            dates                   = template.getDates();
        }
    }


    /**
     * Return the process that is responsible for the analysis.
     *
     * @return string
     */
    public String getProcess()
    {
        return process;
    }


    /**
     * Set up the process that is responsible for the analysis.
     *
     * @param process string
     */
    public void setProcess(String process)
    {
        this.process = process;
    }


    /**
     * Return the source of information that determined the analysis.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of information that determined the analysis.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Return the additional notes associated with the analysis.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the additional notes associated with the analysis.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * Return the start time of the analysis.
     *
     * @return date
     */
    public Date getDataCollectionStartTime()
    {
        return dataCollectionStartTime;
    }


    /**
     * Set up the start time of the analysis.
     *
     * @param dataCollectionStartTime date
     */
    public void setDataCollectionStartTime(Date dataCollectionStartTime)
    {
        this.dataCollectionStartTime = dataCollectionStartTime;
    }


    /**
     * Return the end time of the analysis.
     *
     * @return date
     */
    public Date getDataCollectionEndTime()
    {
        return dataCollectionEndTime;
    }


    /**
     * Set up the end time of the analysis.
     *
     * @param dataCollectionEndTime date
     */
    public void setDataCollectionEndTime(Date dataCollectionEndTime)
    {
        this.dataCollectionEndTime = dataCollectionEndTime;
    }


    /**
     * Return the counts found by the analysis.
     *
     * @return map of count name to count value
     */
    public Map<String, Integer> getCounts()
    {
        return counts;
    }


    /**
     * Set up the counts found by the analysis.
     *
     * @param counts map of count name to count value
     */
    public void setCounts(Map<String, Integer> counts)
    {
        this.counts = counts;
    }


    /**
     * Return the string values found by the analysis.
     *
     * @return map of value name to value
     */
    public Map<String, String> getValues()
    {
        return values;
    }


    /**
     * Set up the string values found by the analysis.
     *
     * @param values map of value name to value
     */
    public void setValues(Map<String, String> values)
    {
        this.values = values;
    }


    /**
     * Return the flags found by the analysis.
     *
     * @return map of flag name to boolean value
     */
    public Map<String, Boolean> getFlags()
    {
        return flags;
    }


    /**
     * Set up the flags found by the analysis.
     *
     * @param flags map of flag name to boolean value
     */
    public void setFlags(Map<String, Boolean> flags)
    {
        this.flags = flags;
    }


    /**
     * Return the dates found by the analysis.
     *
     * @return map of date name to date value
     */
    public Map<String, Date> getDates()
    {
        return dates;
    }


    /**
     * Set up the dates found by the analysis.
     *
     * @param dates map of date name to date value
     */
    public void setDates(Map<String, Date> dates)
    {
        this.dates = dates;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LogAnalysisProperties{" +
                "process='" + process + '\'' +
                ", source='" + source + '\'' +
                ", notes='" + notes + '\'' +
                ", dataCollectionStartTime=" + dataCollectionStartTime +
                ", dataCollectionEndTime=" + dataCollectionEndTime +
                ", counts=" + counts +
                ", values=" + values +
                ", flags=" + flags +
                ", dates=" + dates +
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
        LogAnalysisProperties that = (LogAnalysisProperties) objectToCompare;
        return Objects.equals(process, that.process) &&
                Objects.equals(source, that.source) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(dataCollectionStartTime, that.dataCollectionStartTime) &&
                Objects.equals(dataCollectionEndTime, that.dataCollectionEndTime) &&
                Objects.equals(counts, that.counts) &&
                Objects.equals(values, that.values) &&
                Objects.equals(flags, that.flags) &&
                Objects.equals(dates, that.dates);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), process, source, notes, dataCollectionStartTime, dataCollectionEndTime, counts, values, flags, dates);
    }
}
