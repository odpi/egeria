/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
 * This class represents the common header for facets in the OpenLineage standard spec
 * https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/BaseFacet.  Every facet carries the URI of the producer that
 * created it and the URL of the schema that describes it.  Any properties in the JSON that are not modelled by the
 * subclass are held in additionalProperties so they survive a round trip through these beans.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class OpenLineageFacet
{
    private URI                 _producer            = null;
    private URI                 _schemaURL;
    private Map<String, Object> additionalProperties = null;


    /**
     * Constructor that sets up the schema URL for the facet.
     *
     * @param schemaURL URL of the JSON schema that describes this facet
     */
    public OpenLineageFacet(URI schemaURL)
    {
        this._schemaURL = schemaURL;
    }


    /**
     * Return the URI identifying the producer of this metadata. For example this could be a git url with a given tag or sha.
     *
     * @return URI
     */
    public URI get_producer()
    {
        return _producer;
    }


    /**
     * Set up the URI identifying the producer of this metadata. For example this could be a git url with a given tag or sha.
     *
     * @param producer URI
     */
    public void set_producer(URI producer)
    {
        this._producer = producer;
    }


    /**
     * Return the JSON Pointer (https://tools.ietf.org/html/rfc6901) URL to the corresponding version of the schema definition for this facet.
     *
     * @return URI
     */
    public URI get_schemaURL()
    {
        return _schemaURL;
    }


    /**
     * Set up the JSON Pointer (https://tools.ietf.org/html/rfc6901) URL to the corresponding version of the schema definition for this facet.
     *
     * @param schemaURL URI
     */
    public void set_schemaURL(URI schemaURL)
    {
        this._schemaURL = schemaURL;
    }


    /**
     * Return any properties of the facet that are not modelled by the bean.  They are serialized as top-level properties
     * of the facet alongside the modelled properties.
     *
     * @return map of property name to value
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any properties of the facet that are not modelled by the bean.
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
        return "OpenLineageFacet{" +
                       "_producer=" + _producer +
                       ", _schemaURL=" + _schemaURL +
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
        OpenLineageFacet that = (OpenLineageFacet) objectToCompare;
        return Objects.equals(_producer, that._producer) &&
                       Objects.equals(_schemaURL, that._schemaURL) &&
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
        return Objects.hash(_producer, _schemaURL, additionalProperties);
    }
}
