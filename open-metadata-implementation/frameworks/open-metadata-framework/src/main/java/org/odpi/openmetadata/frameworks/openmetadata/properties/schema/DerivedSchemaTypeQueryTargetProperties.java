/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
public class DerivedSchemaTypeQueryTargetProperties extends RelationshipBeanProperties
{
    private String queryId          = null;
    private String query            = null;
    private String queryType        = null;
    private String iscQualifiedName = null;


    /**
     * Default constructor
     */
    public DerivedSchemaTypeQueryTargetProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema query to copy.
     */
    public DerivedSchemaTypeQueryTargetProperties(DerivedSchemaTypeQueryTargetProperties template)
    {
        super(template);

        if (template != null)
        {
            queryId = template.getQueryId();
            query = template.getQuery();
            queryType = template.getQueryType();
            iscQualifiedName = template.getISCQualifiedName();
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
     * Set up the fully qualified name of the associated information supply chain.
     *
     * @param iscQualifiedName String name
     */
    public void setISCQualifiedName(String iscQualifiedName)
    {
        this.iscQualifiedName = iscQualifiedName;
    }


    /**
     * Returns the stored qualified name of the associated information supply chain.
     *
     * @return qualifiedName
     */
    public String getISCQualifiedName()
    {
        return iscQualifiedName;
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
                "queryId='" + queryId + '\'' +
                ", query='" + query + '\'' +
                ", queryType='" + queryType + '\'' +
                ", iscQualifiedName='" + iscQualifiedName + '\'' +
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
        DerivedSchemaTypeQueryTargetProperties that = (DerivedSchemaTypeQueryTargetProperties) objectToCompare;
        return Objects.equals(queryId, that.queryId) &&
                Objects.equals(query, that.query) &&
                Objects.equals(queryType, that.queryType) &&
                Objects.equals(iscQualifiedName, that.iscQualifiedName);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), queryId, query, queryType, iscQualifiedName);
    }
}