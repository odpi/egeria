/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a run facet from the OpenLineage standard spec
 * https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/RunFacet.  It is the superclass of the standard
 * facets of this kind, and is also used directly to hold custom facets (or standard facets that are not yet modelled)
 * whose properties are then found in additionalProperties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageRunFacet extends OpenLineageFacet
{
    /**
     * Default constructor
     */
    public OpenLineageRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/RunFacet"));
    }


    /**
     * Constructor for subclasses that sets up the schema URL for the facet.
     *
     * @param schemaURL URL of the JSON schema that describes this facet
     */
    public OpenLineageRunFacet(URI schemaURL)
    {
        super(schemaURL);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageRunFacet{" +
                       "_producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", additionalProperties=" + getAdditionalProperties() +
                       '}';
    }
}
