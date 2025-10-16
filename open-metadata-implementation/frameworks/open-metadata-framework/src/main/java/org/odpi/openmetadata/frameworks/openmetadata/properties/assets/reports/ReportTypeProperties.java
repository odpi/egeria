/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReportTypeProperties is a class for representing a report type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportTypeProperties extends DataAssetProperties
{
    private String purpose           = null;
    private String author           = null;
    private Date   createdTime      = null;
    private Date   lastModifiedTime = null;
    private String lastModifier      = null;

    /**
     * Default constructor
     */
    public ReportTypeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.REPORT_TYPE.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ReportTypeProperties(ReportTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            purpose          = template.getPurpose();
            author           = template.getAuthor();
            createdTime      = template.getCreatedTime();
            lastModifiedTime = template.getLastModifiedTime();
            lastModifier     = template.getLastModifier();
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
     * Return the name of the author - or generation engine that created the report.
     *
     * @return string name
     */
    public String getAuthor()
    {
        return author;
    }


    /**
     * Set up the name of the author - or generation engine that created the report.
     *
     * @param author string name
     */
    public void setAuthor(String author)
    {
        this.author = author;
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
        return "ReportTypeProperties{" +
                "purpose='" + purpose + '\'' +
                ", author='" + author + '\'' +
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
        ReportTypeProperties that = (ReportTypeProperties) objectToCompare;
        return Objects.equals(purpose, that.purpose) &&
                Objects.equals(author, that.author) &&
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
        return Objects.hash(super.hashCode(), purpose, author,
                            createdTime, lastModifiedTime, lastModifier);
    }
}
