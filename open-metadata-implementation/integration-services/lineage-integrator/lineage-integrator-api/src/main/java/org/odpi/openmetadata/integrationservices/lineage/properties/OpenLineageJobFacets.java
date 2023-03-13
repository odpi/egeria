/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the map of job facets in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobFacets
{
    private OpenLineageSourceCodeLocationJobFacet sourceCodeLocation   = null;
    private OpenLineageSQLJobFacet                sql                  = null;
    private OpenLineageDocumentationJobFacet      documentation        = null;
    private Map<String, OpenLineageJobFacet>      additionalProperties = new LinkedHashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageJobFacets()
    {
    }


    public OpenLineageSourceCodeLocationJobFacet getSourceCodeLocation()
    {
        return sourceCodeLocation;
    }


    public void setSourceCodeLocation(OpenLineageSourceCodeLocationJobFacet sourceCodeLocation)
    {
        this.sourceCodeLocation = sourceCodeLocation;
    }


    public OpenLineageSQLJobFacet getSql()
    {
        return sql;
    }


    public void setSql(OpenLineageSQLJobFacet sql)
    {
        this.sql = sql;
    }


    public OpenLineageDocumentationJobFacet getDocumentation()
    {
        return documentation;
    }


    public void setDocumentation(OpenLineageDocumentationJobFacet documentation)
    {
        this.documentation = documentation;
    }


    /**
     * Return a map of additional job facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @return job facet map (map from string to object)
     */
    public Map<String, OpenLineageJobFacet> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of additional job facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @param additionalProperties job facet map (map from string to object)
     */
    public void setAdditionalProperties(Map<String, OpenLineageJobFacet> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobFacets{" +
                       "sourceCodeLocation=" + sourceCodeLocation +
                       ", sql=" + sql +
                       ", documentation=" + documentation +
                       ", additionalProperties=" + additionalProperties +
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
        OpenLineageJobFacets that = (OpenLineageJobFacets) objectToCompare;
        return Objects.equals(sourceCodeLocation, that.sourceCodeLocation) &&
                       Objects.equals(sql, that.sql) &&
                       Objects.equals(documentation, that.documentation) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(sourceCodeLocation, sql, documentation, additionalProperties);
    }
}
