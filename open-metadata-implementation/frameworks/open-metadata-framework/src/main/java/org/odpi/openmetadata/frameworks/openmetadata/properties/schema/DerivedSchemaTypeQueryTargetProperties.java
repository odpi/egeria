/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DerivedSchemaTypeQueryTargetProperties defines a query on a schema element that returns all or part of the value for a
 * derived schema type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DerivedSchemaTypeQueryTargetProperties
{
    private String queryId         = null;
    private String query           = null;
    private String queryType       = null;
    private String queryTargetGUID = null;


    /**
     * Default constructor
     */
    public DerivedSchemaTypeQueryTargetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema query to copy.
     */
    public DerivedSchemaTypeQueryTargetProperties(DerivedSchemaTypeQueryTargetProperties template)
    {
        super();

        if (template != null)
        {
            queryId = template.getQueryId();
            query = template.getQuery();
            queryType = template.getQueryType();
            queryTargetGUID = template.getQueryTargetGUID();
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
     * Return the name of the query language used in the query.
     *
     * @return queryType String
     */
    public String getQueryType() { return queryType; }


    /**
     * Set up the name of the query language used in the query.
     *
     * @param queryType String name
     */
    public void setQueryType(String queryType)
    {
        this.queryType = queryType;
    }


    /**
     * Return the unique identifier that describes the data source that will be queried to get part of the derived value.
     *
     * @return string guid
     */
    public String getQueryTargetGUID()
    {
        return queryTargetGUID;
    }


    /**
     * Set up the unique identifier that describes the data source that will be queried to get part of the derived value.
     *
     * @param queryTargetGUID string guid
     */
    public void setQueryTargetGUID(String queryTargetGUID)
    {
        this.queryTargetGUID = queryTargetGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DerivedSchemaTypeQueryTargetProperties{" +
                "queryId=" + queryId +
                ", query='" + query + '\'' +
                ", queryType='" + queryType + '\'' +
                ", queryTargetGUID=" + queryTargetGUID +
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
        if (!(objectToCompare instanceof DerivedSchemaTypeQueryTargetProperties))
        {
            return false;
        }
        DerivedSchemaTypeQueryTargetProperties that = (DerivedSchemaTypeQueryTargetProperties) objectToCompare;
        return Objects.equals(getQueryId(), that.getQueryId()) &&
                Objects.equals(getQuery(), that.getQuery()) &&
                Objects.equals(getQueryType(), that.getQueryType()) &&
                Objects.equals(getQueryTargetGUID(), that.getQueryTargetGUID());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getQueryId(), getQuery(), getQueryType(), getQueryTargetGUID());
    }
}