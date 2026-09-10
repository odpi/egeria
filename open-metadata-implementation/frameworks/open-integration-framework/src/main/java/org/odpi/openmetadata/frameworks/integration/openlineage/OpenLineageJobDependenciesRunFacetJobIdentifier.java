/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the identity of a job referenced by the jobDependencies run facet.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageJobDependenciesRunFacetJobIdentifier
{
    private String namespace = null;
    private String name = null;


    /**
     * Default constructor
     */
    public OpenLineageJobDependenciesRunFacetJobIdentifier()
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
     * Return the unique name of the job within that namespace.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the unique name of the job within that namespace.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageJobDependenciesRunFacetJobIdentifier{" +
                       "namespace='" + namespace + '\'' +
                       ", name='" + name + '\'' +
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
        OpenLineageJobDependenciesRunFacetJobIdentifier that = (OpenLineageJobDependenciesRunFacetJobIdentifier) objectToCompare;
        return Objects.equals(namespace, that.namespace) &&
                       Objects.equals(name, that.name);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(namespace, name);
    }
}
