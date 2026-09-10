/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a dataset described independently of any run, as carried by an OpenLineage dataset event.
 * It follows https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/StaticDataset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageStaticDataSet
{
    private String                   namespace = null;
    private String                   name = null;
    private OpenLineageDataSetFacets facets = null;
    private Map<String, Object>      additionalProperties = null;


    /**
     * Default constructor
     */
    public OpenLineageStaticDataSet()
    {
    }


    /**
     * Return the namespace containing the dataset.
     *
     * @return value
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the namespace containing the dataset.
     *
     * @param namespace value
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the unique name for the dataset within that namespace.
     *
     * @return value
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the unique name for the dataset within that namespace.
     *
     * @param name value
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the facets describing the dataset.
     *
     * @return value
     */
    public OpenLineageDataSetFacets getFacets()
    {
        return facets;
    }


    /**
     * Set up the facets describing the dataset.
     *
     * @param facets value
     */
    public void setFacets(OpenLineageDataSetFacets facets)
    {
        this.facets = facets;
    }


    /**
     * Return any properties that are not modelled by the bean.  They are serialized as top-level properties
     * alongside the modelled properties.
     *
     * @return map of property name to value
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any properties that are not modelled by the bean.
     *
     * @param additionalProperties map of property name to value
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Add a property that is not modelled by the bean.  Jackson calls this for each unrecognized property found in the JSON.
     *
     * @param propertyName name of the property
     * @param propertyValue value of the property
     */
    @JsonAnySetter
    public void setAdditionalProperty(String propertyName,
                                      Object propertyValue)
    {
        if (additionalProperties == null)
        {
            additionalProperties = new LinkedHashMap<>();
        }

        additionalProperties.put(propertyName, propertyValue);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageStaticDataSet{" +
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
        OpenLineageStaticDataSet that = (OpenLineageStaticDataSet) objectToCompare;
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
