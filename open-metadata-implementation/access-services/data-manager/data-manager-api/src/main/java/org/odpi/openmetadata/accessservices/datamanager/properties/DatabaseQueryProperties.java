/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseQueryProperties describes the properties of query that is used to derive a column in a database.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseQueryProperties implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String queryId         = null;
    private String query           = null;
    private String queryTargetGUID = null;


    /**
     * Default constructor
     */
    public DatabaseQueryProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor for the database query.
     *
     * @param template template object to copy.
     */
    public DatabaseQueryProperties(DatabaseQueryProperties template)
    {
        if (template != null)
        {
            queryId         = template.getQueryId();
            query           = template.getQuery();
            queryTargetGUID = template.getQueryTargetGUID();
        }
    }


    /**
     * Return the identifier that is used in the column formula.
     *
     * @return string identifier
     */
    public String getQueryId()
    {
        return queryId;
    }


    /**
     * Set the identifier that is used in the column formula.
     *
     * @param queryId string identifier
     */
    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }


    /**
     * Set up code for query.
     *
     * @param query String logic
     */
    public void setQuery(String query)
    {
        this.query = query;
    }


    /**
     * Return the code for the query.
     *
     * @return String logic
     */
    public String getQuery()
    {
        return query;
    }


    /**
     * Set up the identity of the query target.
     *
     * @param queryTargetGUID schema attribute GUID
     */
    public void setQueryTargetGUID(String queryTargetGUID)
    {
        this.queryTargetGUID = queryTargetGUID;
    }


    /**
     * Returns the identity of the query target.
     *
     * @return schema attribute GUID
     */
    public String getQueryTargetGUID()
    {
        return queryTargetGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DatabaseQueryProperties{" +
                "queryId='" + queryId + '\'' +
                ", query='" + query + '\'' +
                ", queryTargetGUID='" + queryTargetGUID + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DatabaseQueryProperties that = (DatabaseQueryProperties) objectToCompare;
        return Objects.equals(queryId, that.queryId) &&
                Objects.equals(query, that.query) &&
                Objects.equals(queryTargetGUID, that.queryTargetGUID);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(queryId, query, queryTargetGUID);
    }
}
