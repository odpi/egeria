/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReportProperties is a class for representing a report for an application or reporting engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SurveyReportProperties.class, name = "SurveyReportProperties"),
                @JsonSubTypes.Type(value = ConnectorActivityReportProperties.class, name = "ConnectorActivityReportProperties"),
                @JsonSubTypes.Type(value = IncidentReportProperties.class, name = "IncidentReportProperties"),
        })
public class ReportProperties extends DataSetProperties
{
    private String purpose           = null;
    private Date   startTime         = null;
    private Date   completionTime    = null;
    private String completionMessage = null;
    private Date   createdTime       = null;
    private Date   lastModifiedTime  = null;
    private String lastModifier      = null;

    /**
     * Default constructor
     */
    public ReportProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.REPORT.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ReportProperties(ReportProperties template)
    {
        super(template);

        if (template != null)
        {
            purpose           = template.getPurpose();
            startTime         = template.getStartTime();
            completionTime    = template.getCompletionTime();
            completionMessage = template.getCompletionMessage();
            createdTime       = template.getCreatedTime();
            lastModifiedTime  = template.getLastModifiedTime();
            lastModifier      = template.getLastModifier();
        }
    }


    /**
     * Return the purpose of the report.
     *
     * @return string
     */
    public String getPurpose()
    {
        return purpose;
    }


    /**
     * Set up the purpose for the report.
     *
     * @param purpose string
     */
    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }


    /**
     * Return the start time period that this report covers.
     *
     * @return date/time
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the start time period that this report covers.
     *
     * @param date date/time
     */
    public void setStartTime(Date date)
    {
        this.startTime = date;
    }




    /**
     * Return the completion time for the report.
     *
     * @return Date that the report was completed.
     */
    public Date getCompletionTime()
    {
        return completionTime;
    }


    /**
     * Set up the completion date for the report.
     *
     * @param date Date that the report was completed.
     */
    public void setCompletionTime(Date date)
    {
        this.completionTime = date;
    }



    /**
     * Return the completion message - it may be null which means it completed ok.
     *
     * @return string text
     */
    public String getCompletionMessage()
    {
        return completionMessage;
    }


    /**
     * Set up the completion message - it may be null which means it completed ok.
     *
     * @param message text of completion message
     */
    public void setCompletionMessage(String message)
    {
        this.completionMessage = message;
    }


    /**
     * Return the date/time that the report was created.
     *
     * @return data object
     */
    public Date getCreatedTime()
    {
        return createdTime;
    }


    /**
     * Set up the date/time that the report was created.
     *
     * @param createdTime date object
     */
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }


    /**
     * Return the date/time that the report was last updated.
     *
     * @return date object
     */
    public Date getLastModifiedTime()
    {
        return lastModifiedTime;
    }


    /**
     * Set up date/time that the report was last updated.
     *
     * @param lastModifiedTime date object
     */
    public void setLastModifiedTime(Date lastModifiedTime)
    {
        this.lastModifiedTime = lastModifiedTime;
    }


    /**
     * Return the name of the person or engine that last modified the report.
     *
     * @return string name
     */
    public String getLastModifier()
    {
        return lastModifier;
    }


    /**
     * Set up the name of the person or engine that last modified the report.
     *
     * @param lastModifier string name
     */
    public void setLastModifier(String lastModifier)
    {
        this.lastModifier = lastModifier;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReportProperties{" +
                "purpose='" + purpose + '\'' +
                ", startTime=" + startTime +
                ", completionTime=" + completionTime +
                ", completionMessage='" + completionMessage + '\'' +
                ", createTime=" + createdTime +
                ", lastModifiedTime=" + lastModifiedTime +
                ", lastModifier='" + lastModifier + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ReportProperties that = (ReportProperties) objectToCompare;
        return Objects.equals(purpose, that.purpose) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(completionTime, that.completionTime) &&
                Objects.equals(completionMessage, that.completionMessage) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(lastModifiedTime, that.lastModifiedTime) &&
                Objects.equals(lastModifier, that.lastModifier);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), purpose, startTime, completionTime, completionMessage, createdTime, lastModifiedTime, lastModifier);
    }
}
