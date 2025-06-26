/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ResultsRequestBody carries the date/time for a query along with other common search parameters.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResultsRequestBody
{
    private boolean             forLineage             = false;
    private boolean             forDuplicateProcessing = false;
    private Date                effectiveTime          = null;
    private List<ElementStatus> limitResultsByStatus   = null;
    private Date                asOfTime               = null;
    private SequencingOrder     sequencingOrder        = null;
    private String              sequencingProperty     = null;
    private TemplateFilter      templateFilter         = null;


    /**
     * Default constructor
     */
    public ResultsRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ResultsRequestBody(ResultsRequestBody template)
    {
        if (template != null)
        {
            forLineage             = template.getForLineage();
            forDuplicateProcessing = template.getForDuplicateProcessing();
            effectiveTime          = template.getEffectiveTime();
            limitResultsByStatus   = template.getLimitResultsByStatus();
            asOfTime               = template.getAsOfTime();
            sequencingOrder        = template.getSequencingOrder();
            sequencingProperty     = template.getSequencingProperty();
            templateFilter         = template.getTemplateFilter();
        }
    }


    /**
     * Return whether this request is to update lineage memento elements.
     *
     * @return flag
     */
    public boolean getForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether this request is to update lineage memento elements.
     *
     * @param forLineage flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /**
     * Return whether this request is updating an element as part of a deduplication exercise.
     *
     * @return flag
     */
    public boolean getForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether this request is updating an element as part of a deduplication exercise.
     *
     * @param forDuplicateProcessing flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /**
     * Return the date/time to use for the query.
     *
     * @return date object
     */
    public Date getEffectiveTime()
    {
        return effectiveTime;
    }


    /**
     * Set up the date/time to use for the query.
     *
     * @param effectiveTime date object
     */
    public void setEffectiveTime(Date effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }


    /**
     * Return the optional list of statuses that should be used to restrict the elements returned by their status.
     * By default, the elements in all non-DELETED statuses are returned.  However, it is possible
     * to specify a list of statuses (eg ACTIVE, DELETED) to restrict the results to.  Null means all
     * status values except DELETED.
     *
     * @return list of statuses or null
     */
    public List<ElementStatus> getLimitResultsByStatus()
    {
        return limitResultsByStatus;
    }


    /**
     * Set up the optional list of statuses that should be used to restrict the elements returned by their status.
     * By default, the elements in all non-DELETED statuses are returned.  However, it is possible
     * to specify a list of statuses (eg ACTIVE, DELETED) to restrict the results to.  Null means all
     * status values except DELETED.
     *
     * @param limitResultsByStatus list of statuses or null
     */
    public void setLimitResultsByStatus(List<ElementStatus> limitResultsByStatus)
    {
        this.limitResultsByStatus = limitResultsByStatus;
    }


    /**
     * Return the repository time that should be used when retrieving metadata.
     *
     * @return date/time (default null which means now)
     */
    public Date getAsOfTime()
    {
        return asOfTime;
    }


    /**
     * Set up the repository time that should be used when retrieving metadata.
     *
     * @param asOfTime date/time (default null which means now)
     */
    public void setAsOfTime(Date asOfTime)
    {
        this.asOfTime = asOfTime;
    }


    /**
     * Return the property that should be used to order the results.
     *
     * @return property name
     */
    public String getSequencingProperty()
    {
        return sequencingProperty;
    }


    /**
     * Set up the property that should be used to order the results.
     *
     * @param sequencingProperty property name
     */
    public void setSequencingProperty(String sequencingProperty)
    {
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the order that results should be returned in.
     *
     * @return enum
     */
    public SequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the order that results should be returned in.
     *
     * @param sequencingOrder enum
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * Return the setting of the template filter.
     *
     * @return TemplateFilter enum
     */
    public TemplateFilter getTemplateFilter()
    {
        return templateFilter;
    }


    /**
     *  Set up the setting of the template filter.
     *
     * @param templateFilter enum
     */
    public void setTemplateFilter(TemplateFilter templateFilter)
    {
        this.templateFilter = templateFilter;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ResultsRequestBody{" +
                "forLineage=" + forLineage +
                ", forDuplicateProcessing=" + forDuplicateProcessing +
                ", effectiveTime=" + effectiveTime +
                ", limitResultsByStatus=" + limitResultsByStatus +
                ", asOfTime=" + asOfTime +
                ", sequencingOrder=" + sequencingOrder +
                ", sequencingProperty='" + sequencingProperty + '\'' +
                ", templateFilter=" + templateFilter +
                '}';
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
        ResultsRequestBody that = (ResultsRequestBody) objectToCompare;
        return forLineage == that.forLineage &&
                forDuplicateProcessing == that.forDuplicateProcessing &&
                Objects.equals(effectiveTime, that.effectiveTime) &&
                Objects.equals(limitResultsByStatus, that.limitResultsByStatus) &&
                Objects.equals(asOfTime, that.asOfTime) &&
                sequencingOrder == that.sequencingOrder &&
                Objects.equals(sequencingProperty, that.sequencingProperty) &&
                templateFilter == that.templateFilter;
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(forLineage, forDuplicateProcessing, effectiveTime,
                            limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, templateFilter);
    }
}
