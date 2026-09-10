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
 * This class represents the columnLineage dataset facet.  It maps each output field of the dataset to the input fields used to evaluate it.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-2-0/ColumnLineageDatasetFacet.json#/$defs/ColumnLineageDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageColumnLineageDataSetFacet extends OpenLineageDataSetFacet
{
    private Map<String, OpenLineageColumnLineageDataSetFacetField> fields = null;
    private List<OpenLineageColumnLineageDataSetFacetInputField>   dataset = null;


    /**
     * Default constructor
     */
    public OpenLineageColumnLineageDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-2-0/ColumnLineageDatasetFacet.json#/$defs/ColumnLineageDatasetFacet"));
    }


    /**
     * Return the column level lineage that maps output fields (the map keys) to the input fields used to evaluate them.
     *
     * @return map
     */
    public Map<String, OpenLineageColumnLineageDataSetFacetField> getFields()
    {
        return fields;
    }


    /**
     * Set up the column level lineage that maps output fields (the map keys) to the input fields used to evaluate them.
     *
     * @param fields map
     */
    public void setFields(Map<String, OpenLineageColumnLineageDataSetFacetField> fields)
    {
        this.fields = fields;
    }


    /**
     * Return the column level lineage that affects the whole dataset, for example filtering, sorting, grouping or joining.
     *
     * @return list
     */
    public List<OpenLineageColumnLineageDataSetFacetInputField> getDataset()
    {
        return dataset;
    }


    /**
     * Set up the column level lineage that affects the whole dataset, for example filtering, sorting, grouping or joining.
     *
     * @param dataset list
     */
    public void setDataset(List<OpenLineageColumnLineageDataSetFacetInputField> dataset)
    {
        this.dataset = dataset;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageColumnLineageDataSetFacet{" +
                       "fields=" + fields +
                       ", dataset=" + dataset +
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
        OpenLineageColumnLineageDataSetFacet that = (OpenLineageColumnLineageDataSetFacet) objectToCompare;
        return Objects.equals(fields, that.fields) &&
                       Objects.equals(dataset, that.dataset);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fields, dataset);
    }
}
