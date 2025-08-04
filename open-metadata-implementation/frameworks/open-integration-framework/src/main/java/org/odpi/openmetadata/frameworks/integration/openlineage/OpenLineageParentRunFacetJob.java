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
 * This class represents the content of the parent open lineage job.  It is used internally in Egeria to pass this information
 * to the integration daemon's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageParentRunFacetJob
{
    private String               namespace;
    private String               name;


    /**
     * Default constructor
     */
    public OpenLineageParentRunFacetJob()
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
