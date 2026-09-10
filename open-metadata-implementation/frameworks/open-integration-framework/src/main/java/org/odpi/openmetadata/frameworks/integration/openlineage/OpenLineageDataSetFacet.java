/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a dataset facet from the OpenLineage standard spec
 * https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/DatasetFacet.  It is the superclass of the standard
 * facets of this kind, and is also used directly to hold custom facets (or standard facets that are not yet modelled)
 * whose properties are then found in additionalProperties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataSetFacet extends OpenLineageFacet
{
    private Boolean _deleted = null;


    /**
     * Default constructor
     */
    public OpenLineageDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/DatasetFacet"));
    }


    /**
     * Constructor for subclasses that sets up the schema URL for the facet.
     *
     * @param schemaURL URL of the JSON schema that describes this facet
     */
    public OpenLineageDataSetFacet(URI schemaURL)
    {
        super(schemaURL);
    }


    /**
     * Return whether this facet is being deleted (set to true to delete a facet).
     *
     * @return boolean
     */
    public Boolean get_deleted()
    {
        return _deleted;
    }


    /**
     * Set up whether this facet is being deleted (set to true to delete a facet).
     *
     * @param deleted boolean
     */
    public void set_deleted(Boolean deleted)
    {
        this._deleted = deleted;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataSetFacet{" +
                       "_deleted=" + _deleted +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenLineageDataSetFacet that = (OpenLineageDataSetFacet) objectToCompare;
        return Objects.equals(_deleted, that._deleted);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), _deleted);
    }
}
