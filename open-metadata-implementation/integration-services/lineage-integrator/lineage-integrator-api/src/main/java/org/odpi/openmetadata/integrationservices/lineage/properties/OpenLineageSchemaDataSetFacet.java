/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an open lineage schema for a data set facet as defined in JSON
 * spec https://openlineage.io/spec/facets/1-0-0/SchemaDatasetFacet.json#/$defs/SchemaDatasetFacet.
 * It is used internally in Egeria to pass this information to the Lineage Integrator OMIS's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageSchemaDataSetFacet extends OpenLineageDataSetFacet
{
    private List<OpenLineageSchemaDataSetFacetField> fields;


    /**
     * Default constructor
     */
    public OpenLineageSchemaDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/SchemaDatasetFacet.json#/$defs/SchemaDatasetFacet"));
    }


    /**
     * Return the list of data files described by this schema.
     *
     * @return list
     */
    public List<OpenLineageSchemaDataSetFacetField> getFields()
    {
        return fields;
    }


    /**
     * Set up the list of data files described by this schema.
     *
     * @param fields list
     */
    public void setFields(List<OpenLineageSchemaDataSetFacetField> fields)
    {
        this.fields = fields;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageSchemaDataSetFacet{" +
                       "fields=" + fields +
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
        OpenLineageSchemaDataSetFacet that = (OpenLineageSchemaDataSetFacet) objectToCompare;
        return Objects.equals(fields, that.fields);
    }


    /**
     * Return hash code basa``ed on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fields);
    }
}
