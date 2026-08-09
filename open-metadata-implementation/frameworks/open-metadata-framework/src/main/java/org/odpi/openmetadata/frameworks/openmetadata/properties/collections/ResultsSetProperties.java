/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ResultsSetProperties is a java bean used to represent a collection whose membership is maintained by a connector,
 * typically as the results of running a query.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResultsSetProperties extends CollectionProperties
{
    private Date   createdTime      = null;
    private Date   startTime        = null;
    private Date   completionTime   = null;
    private String completionMessage = null;


    /**
     * Default constructor
     */
    public ResultsSetProperties()
    {
        super();
        super.typeName = OpenMetadataType.RESULTS_SET.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ResultsSetProperties(ResultsSetProperties template)
    {
        super(template);

        if (template != null)
        {
            createdTime       = template.getCreatedTime();
            startTime         = template.getStartTime();
            completionTime    = template.getCompletionTime();
            completionMessage = template.getCompletionMessage();
        }
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ResultsSetProperties(CollectionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.RESULTS_SET.typeName;
    }


    /**
     * Return the time that the results set's membership was first maintained.
     *
     * @return date/time
     */
    public Date getCreatedTime()
    {
        return createdTime;
    }


    /**
     * Set up the time that the results set's membership was first maintained.
     *
     * @param createdTime date/time
     */
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }


    /**
     * Return the time that the connector started the most recent refresh of the results set's membership.
     *
     * @return date/time
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the time that the connector started the most recent refresh of the results set's membership.
     *
     * @param startTime date/time
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the time that the connector completed the most recent refresh of the results set's membership.
     *
     * @return date/time
     */
    public Date getCompletionTime()
    {
        return completionTime;
    }


    /**
     * Set up the time that the connector completed the most recent refresh of the results set's membership.
     *
     * @param completionTime date/time
     */
    public void setCompletionTime(Date completionTime)
    {
        this.completionTime = completionTime;
    }


    /**
     * Return the message describing the outcome of the most recent refresh of the results set's membership, or
     * null if it completed without error.
     *
     * @return string message
     */
    public String getCompletionMessage()
    {
        return completionMessage;
    }


    /**
     * Set up the message describing the outcome of the most recent refresh of the results set's membership, or
     * null if it completed without error.
     *
     * @param completionMessage string message
     */
    public void setCompletionMessage(String completionMessage)
    {
        this.completionMessage = completionMessage;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ResultsSetProperties{" +
                "createdTime=" + createdTime +
                ", startTime=" + startTime +
                ", completionTime=" + completionTime +
                ", completionMessage='" + completionMessage + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ResultsSetProperties that = (ResultsSetProperties) objectToCompare;
        return Objects.equals(createdTime, that.createdTime) &&
                       Objects.equals(startTime, that.startTime) &&
                       Objects.equals(completionTime, that.completionTime) &&
                       Objects.equals(completionMessage, that.completionMessage);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), createdTime, startTime, completionTime, completionMessage);
    }
}
