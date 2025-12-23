/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceResultsProperties defines the properties that link a metric to a data set that contains the measurements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceResultsProperties extends RelationshipBeanProperties
{
    private String query = null;
    private String queryType = null;


    /**
     * Default constructor
     */
    public GovernanceResultsProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_RESULTS_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceResultsProperties(GovernanceResultsProperties template)
    {
        super(template);

        if (template != null)
        {
            this.query = getQuery();
            this.queryType = getQueryType();
        }
    }


    /**
     * Return the query that explains how the results are extracted from the data set.
     *
     * @return string description
     */
    public String getQuery()
    {
        return query;
    }


    /**
     * Set up the query that explains how the results are extracted from the data set.
     *
     * @param query string description
     */
    public void setQuery(String query)
    {
        this.query = query;
    }

    /**
     * Return the format/language used for the query.
     *
     * @return string
     */
    public String getQueryType()
    {
        return queryType;
    }


    /**
     * Set up the format/language used for the query.
     *
     * @param queryType string
     */
    public void setQueryType(String queryType)
    {
        this.queryType = queryType;
    }

    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceResultsProperties{" +
                "query='" + query + '\'' +
                ", queryType='" + queryType + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceResultsProperties that = (GovernanceResultsProperties) objectToCompare;
        return Objects.equals(query, that.query) && Objects.equals(queryType, that.queryType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), query, queryType);
    }
}
