/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the sql job facet.  It captures the SQL query executed by the job.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-1-0/SQLJobFacet.json#/$defs/SQLJobFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageSQLJobFacet extends OpenLineageJobFacet
{
    private String query = null;
    private String dialect = null;


    /**
     * Default constructor
     */
    public OpenLineageSQLJobFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-1-0/SQLJobFacet.json#/$defs/SQLJobFacet"));
    }


    /**
     * Return the SQL query.
     *
     * @return string
     */
    public String getQuery()
    {
        return query;
    }


    /**
     * Set up the SQL query.
     *
     * @param query string
     */
    public void setQuery(String query)
    {
        this.query = query;
    }


    /**
     * Return the SQL dialect of the query.
     *
     * @return string
     */
    public String getDialect()
    {
        return dialect;
    }


    /**
     * Set up the SQL dialect of the query.
     *
     * @param dialect string
     */
    public void setDialect(String dialect)
    {
        this.dialect = dialect;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageSQLJobFacet{" +
                       "query='" + query + '\'' +
                       ", dialect='" + dialect + '\'' +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", _deleted=" + get_deleted() +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenLineageSQLJobFacet that = (OpenLineageSQLJobFacet) objectToCompare;
        return Objects.equals(query, that.query) &&
                       Objects.equals(dialect, that.dialect);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), query, dialect);
    }
}
