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
 * This class represents the previous identity of a dataset that has been renamed.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier
{
    private String name = null;
    private String namespace = null;


    /**
     * Default constructor
     */
    public OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier()
    {
    }


    /**
     * Return the previous name of the dataset.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the previous name of the dataset.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the previous namespace of the dataset.
     *
     * @return string
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the previous namespace of the dataset.
     *
     * @param namespace string
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier{" +
                       "name='" + name + '\'' +
                       ", namespace='" + namespace + '\'' +
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
        OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier that = (OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(namespace, that.namespace);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, namespace);
    }
}
