/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

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
public class HistoryRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private Date           asOfTime   = null;


    /**
     * Default constructor
     */
    public HistoryRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public HistoryRequest(HistoryRequest template)
    {
        super(template);

        if (template != null)
        {
            this.asOfTime = template.getAsOfTime();
        }
    }


    /**
     * Return the current type.
     *
     * @return date/time
     */
    public Date getAsOfTime()
    {
        return asOfTime;
    }


    /**
     * Set up the current type
     *
     * @param asOfTime date time
     */
    public void setAsOfTime(Date asOfTime)
    {
        this.asOfTime = asOfTime;
    }


        /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "HistoryRequest{" +
                "asOfTime=" + asOfTime +
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
        if (!(objectToCompare instanceof HistoryRequest))
        {
            return false;
        }
        HistoryRequest
                that = (HistoryRequest) objectToCompare;
        return Objects.equals(getAsOfTime(), that.getAsOfTime());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAsOfTime());
    }
}
