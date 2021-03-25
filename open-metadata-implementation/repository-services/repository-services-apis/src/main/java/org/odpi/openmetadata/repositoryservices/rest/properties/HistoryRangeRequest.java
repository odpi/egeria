/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * HistoryRequest carries the date/time for a historical query.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class HistoryRangeRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private Date                   fromTime        = null;
    private Date                   toTime          = null;
    private int                    offset          = 0;
    private int                    pageSize        = 0;
    private HistorySequencingOrder sequencingOrder = HistorySequencingOrder.BACKWARDS;


    /**
     * Default constructor
     */
    public HistoryRangeRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public HistoryRangeRequest(HistoryRangeRequest template)
    {
        super(template);

        if (template != null)
        {
            this.fromTime = template.getFromTime();
            this.toTime = template.getToTime();
            this.offset = template.getOffset();
            this.pageSize = template.getPageSize();
            this.sequencingOrder = template.getSequencingOrder();
        }
    }


    /**
     * Return the earliest point of history to retrieve (inclusive).
     *
     * @return date/time
     */
    public Date getFromTime()
    {
        return fromTime;
    }


    /**
     * Set up the earliest point of history to retrieve (inclusive).
     *
     * @param fromTime date/time
     */
    public void setFromTime(Date fromTime)
    {
        this.fromTime = fromTime;
    }


    /**
     * Return the latest point of history up to which to return (exclusive).
     *
     * @return date/time
     */
    public Date getToTime()
    {
        return toTime;
    }


    /**
     * Set up the latest point of history up to which to return (exclusive).
     *
     * @param toTime date/time
     */
    public void setToTime(Date toTime)
    {
        this.toTime = toTime;
    }


    /**
     * Return the starting element number for this set of results.  This is used when retrieving elements
     * beyond the first page of results. Zero means the results start from the first element.
     *
     * @return offset number
     */
    public int getOffset()
    {
        return offset;
    }


    /**
     * Set up the starting element number for this set of results.  This is used when retrieving elements
     * beyond the first page of results. Zero means the results start from the first element.
     *
     * @param offset offset number
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }


    /**
     * Return the maximum number of elements that can be returned on this request.
     *
     * @return page size
     */
    public int getPageSize()
    {
        return pageSize;
    }


    /**
     * Set up the maximum number of elements that can be returned on this request.
     *
     * @param pageSize integer number
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    /**
     * Return the sequencing order for the results.
     *
     * @return sequencing order enum
     */
    public HistorySequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the sequencing order for the results.
     *
     * @param sequencingOrder sequencing order enum
     */
    public void setSequencingOrder(HistorySequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "HistoryRangeRequest{" +
                "fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
                ", sequencingOrder=" + sequencingOrder +
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
        if (!(objectToCompare instanceof HistoryRangeRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        HistoryRangeRequest that = (HistoryRangeRequest) objectToCompare;
        return getOffset() == that.getOffset() &&
                getPageSize() == that.getPageSize() &&
                Objects.equals(getFromTime(), that.getFromTime()) &&
                Objects.equals(getToTime(), that.getToTime()) &&
                getSequencingOrder() == that.getSequencingOrder();
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(),
                            getFromTime(),
                            getToTime(),
                            getOffset(),
                            getPageSize(),
                            getSequencingOrder());
    }
}
