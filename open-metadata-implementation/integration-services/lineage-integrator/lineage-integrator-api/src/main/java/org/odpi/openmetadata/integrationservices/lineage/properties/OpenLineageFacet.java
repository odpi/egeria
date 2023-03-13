/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the Common header for facets in the open lineage standard spec
 * https://github.com/OpenLineage/OpenLineage/blob/main/spec/OpenLineage.json.
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
     * Subclass constructor
     *
     * @param schemaURL default value for schemaURL
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
     * @param producer uri
     */
    public void set_producer(URI producer)
    {
        this._producer = producer;
    }


    /**
     * Return the JSON Pointer (https://tools.ietf.org/html/rfc6901) URL to the corresponding version of the schema definition for this facet.
     *
     * @return uri
     */
    public URI get_schemaURL()
    {
        return _schemaURL;
    }


    /**
     * Set up the JSON Pointer (https://tools.ietf.org/html/rfc6901) URL to the corresponding version of the schema definition for this facet.
     *
     * @param schemaURL uri
     */
    public void set_schemaURL(URI schemaURL)
    {
        this._schemaURL = schemaURL;
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
        return "OpenLineageFacet{" +
                       "producer=" + _producer +
                       ", schemaURL=" + _schemaURL +
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
