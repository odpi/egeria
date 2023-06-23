/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataContentForDataSetProperties defines a query on an asset that returns all or part of the values for a dataset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContentForDataSetProperties extends RelationshipProperties
{
    private String queryId         = null;
    private String query           = null;



    /**
     * Default constructor
     */
    public DataContentForDataSetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template to copy.
     */
    public DataContentForDataSetProperties(DataContentForDataSetProperties template)
    {
        super(template);

        if (template != null)
        {
            queryId = template.getQueryId();
            query = template.getQuery();
        }
    }


    /**
     * Return the query id - this is used to identify where the results of this query should be plugged into
     * the other queries or the formula for the parent derived schema element.
     *
     * @return String query identifier
     */
    public String getQueryId() { return queryId; }


    /**
     * Set up the query id - this is used to identify where the results of this query should be plugged into
     * the other queries or the formula for the parent derived schema element.
     *
     * @param queryId String query identifier
     */
    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }


    /**
     * Return the query string for this element.  The query string may have placeholders for values returned
     * by queries that have a lower queryId than this element.
     *
     * @return String query
     */
    public String getQuery() { return query; }


    /**
     * Set up the query string for this element.  The query string may have placeholders for values returned
     * by queries that have a lower queryId than this element.
     *
     * @param query String query
     */
    public void setQuery(String query)
    {
        this.query = query;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContentForDataSetProperties{" +
                       "queryId='" + queryId + '\'' +
                       ", query='" + query + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        if (!(objectToCompare instanceof DataContentForDataSetProperties))
        {
            return false;
        }
        DataContentForDataSetProperties that = (DataContentForDataSetProperties) objectToCompare;
        return Objects.equals(getQueryId(), that.getQueryId()) &&
                Objects.equals(getQuery(), that.getQuery());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getQueryId(), getQuery());
    }
}