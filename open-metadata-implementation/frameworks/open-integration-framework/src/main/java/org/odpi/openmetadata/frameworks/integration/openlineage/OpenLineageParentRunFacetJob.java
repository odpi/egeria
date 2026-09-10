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
 * This class represents the identity of a parent (or root) job referenced from the parent run facet.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageParentRunFacetJob
{
    private String              namespace = null;
    private String              name = null;
    private Map<String, Object> facets = null;


    /**
     * Default constructor
     */
    public OpenLineageParentRunFacetJob()
    {
    }


    /**
     * Return the namespace containing the job.
     *
     * @return string
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the namespace containing the job.
     *
     * @param namespace string
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the unique name for the job within that namespace.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the unique name for the job within that namespace.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the selected subset of facets of the job, forwarded here for convenience.
     *
     * @return map
     */
    public Map<String, Object> getFacets()
    {
        return facets;
    }


    /**
     * Set up the selected subset of facets of the job, forwarded here for convenience.
     *
     * @param facets map
     */
    public void setFacets(Map<String, Object> facets)
    {
        this.facets = facets;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageParentRunFacetJob{" +
                       "namespace='" + namespace + '\'' +
                       ", name='" + name + '\'' +
                       ", facets=" + facets +
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
        OpenLineageParentRunFacetJob that = (OpenLineageParentRunFacetJob) objectToCompare;
        return Objects.equals(namespace, that.namespace) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(facets, that.facets);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(namespace, name, facets);
    }
}
