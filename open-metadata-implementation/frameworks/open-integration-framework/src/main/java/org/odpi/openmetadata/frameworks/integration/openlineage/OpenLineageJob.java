/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an open lineage job as defined in JSON
 * spec https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.  It is used internally in Egeria to pass this information
 * to the integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJob
{
    private String               namespace;
    private String               name;
    private OpenLineageJobFacets facets;
    private Map<String, Object>  additionalProperties = null;


    /**
     * Default constructor
     */
    public OpenLineageJob()
    {
    }


    /**
     * Return the namespace for the job.
     *
     * @return string name
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the namespace for the job.
     *
     * @param namespace string name
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the name of the job.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the job.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the list of job facets (if any).
     *
     * @return job facets containing facet map
     */
    public OpenLineageJobFacets getFacets()
    {
        return facets;
    }


    /**
     * Set up the list of job facets (if any).
     *
     * @param facets job facets containing facet map
     */
    public void setFacets(OpenLineageJobFacets facets)
    {
        this.facets = facets;
    }


    /**
     * Return a map of additional custom facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @return custom facet map (map from string to object)
     */
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up a map of additional custom facets.  The name is the identifier of the facet type and the object is the facet itself.
     *
     * @param additionalProperties custom facet map (map from string to object)
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
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
        return "OpenLineageJob{" +
                       "namespace='" + namespace + '\'' +
                       ", name='" + name + '\'' +
                       ", facets=" + facets +
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
        OpenLineageJob that = (OpenLineageJob) objectToCompare;
        return Objects.equals(namespace, that.namespace) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(facets, that.facets) &&
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
        return Objects.hash(namespace, name, facets, additionalProperties);
    }
}
