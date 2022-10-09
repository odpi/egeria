/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class ReportProperties extends DataSetProperties
{
    private static final long    serialVersionUID = 1L;

    private String id               = null;
    private String author           = null;
    private String url              = null;
    private Date   createTime       = null;
    private Date   lastModifiedTime = null;
    private String lastModifier     = null;

    /**
     * Default constructor
     */
    public ReportProperties()
    {
        super();
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
            id = template.getId();
            author = template.getAuthor();
            url = template.getUrl();
            createTime = template.getCreateTime();
            lastModifiedTime = template.getLastModifiedTime();
            lastModifier = template.getLastModifier();
        }
    }


    /**
     * Return the business identifier for the report.
     *
     * @return string id
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the business identifier for the report.
     *
     * @param id string id
     */
    public void setId(String id)
    {
        this.id = id;
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
     * Return the URL to retrieve the report.
     *
     * @return link to report
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Set up the URL to retrieve the report.
     *
     * @param url link to the report
     */
    public void setUrl(String url)
    {
        this.url = url;
    }


    /**
     * Return the date/time that the report was created.
     *
     * @return data object
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    /**
     * Set up the date/time that the report was created.
     *
     * @param createTime date object
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
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
                       "name='" + getName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", id='" + id + '\'' +
                       ", author='" + author + '\'' +
                       ", url='" + url + '\'' +
                       ", createTime=" + createTime +
                       ", lastModifiedTime=" + lastModifiedTime +
                       ", lastModifier='" + lastModifier + '\'' +
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
        if (! (objectToCompare instanceof ReportProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        ReportProperties that = (ReportProperties) objectToCompare;

        if (id != null ? ! id.equals(that.id) : that.id != null)
        {
            return false;
        }
        if (author != null ? ! author.equals(that.author) : that.author != null)
        {
            return false;
        }
        if (url != null ? ! url.equals(that.url) : that.url != null)
        {
            return false;
        }
        if (createTime != null ? ! createTime.equals(that.createTime) : that.createTime != null)
        {
            return false;
        }
        if (lastModifiedTime != null ? ! lastModifiedTime.equals(that.lastModifiedTime) : that.lastModifiedTime != null)
        {
            return false;
        }
        return lastModifier != null ? lastModifier.equals(that.lastModifier) : that.lastModifier == null;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), id, author, url, createTime, lastModifiedTime, lastModifier);
    }
}
