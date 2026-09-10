/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the lineage dataset facet.  It explicitly declares the dataset- and field-level sources of the dataset.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/LineageFacet.json#/$defs/LineageDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLineageDataSetFacet extends OpenLineageDataSetFacet
{
    private List<OpenLineageLineageInput>             inputs = null;
    private Map<String, OpenLineageLineageFieldEntry> fields = null;


    /**
     * Default constructor
     */
    public OpenLineageLineageDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/LineageFacet.json#/$defs/LineageDatasetFacet"));
    }


    /**
     * Return the dataset-level source inputs.
     *
     * @return list
     */
    public List<OpenLineageLineageInput> getInputs()
    {
        return inputs;
    }


    /**
     * Set up the dataset-level source inputs.
     *
     * @param inputs list
     */
    public void setInputs(List<OpenLineageLineageInput> inputs)
    {
        this.inputs = inputs;
    }


    /**
     * Return the field-level lineage: maps target field names to their source inputs.
     *
     * @return map
     */
    public Map<String, OpenLineageLineageFieldEntry> getFields()
    {
        return fields;
    }


    /**
     * Set up the field-level lineage: maps target field names to their source inputs.
     *
     * @param fields map
     */
    public void setFields(Map<String, OpenLineageLineageFieldEntry> fields)
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
        return "OpenLineageLineageDataSetFacet{" +
                       "inputs=" + inputs +
                       ", fields=" + fields +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", _deleted=" + get_deleted() +
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
        OpenLineageLineageDataSetFacet that = (OpenLineageLineageDataSetFacet) objectToCompare;
        return Objects.equals(inputs, that.inputs) &&
                       Objects.equals(fields, that.fields);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), inputs, fields);
    }
}
