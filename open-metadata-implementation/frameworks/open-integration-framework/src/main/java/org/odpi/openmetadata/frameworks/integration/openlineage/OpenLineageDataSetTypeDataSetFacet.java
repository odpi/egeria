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
 * This class represents the datasetType dataset facet.  It classifies the dataset, for example TABLE, VIEW, FILE, TOPIC, STREAM or MODEL.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/DatasetTypeDatasetFacet.json#/$defs/DatasetTypeDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataSetTypeDataSetFacet extends OpenLineageDataSetFacet
{
    private String datasetType = null;
    private String subType = null;


    /**
     * Default constructor
     */
    public OpenLineageDataSetTypeDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/DatasetTypeDatasetFacet.json#/$defs/DatasetTypeDatasetFacet"));
    }


    /**
     * Return the dataset type, for example TABLE, VIEW, FILE, TOPIC, STREAM, MODEL or JOB_OUTPUT.
     *
     * @return string
     */
    public String getDatasetType()
    {
        return datasetType;
    }


    /**
     * Set up the dataset type, for example TABLE, VIEW, FILE, TOPIC, STREAM, MODEL or JOB_OUTPUT.
     *
     * @param datasetType string
     */
    public void setDatasetType(String datasetType)
    {
        this.datasetType = datasetType;
    }


    /**
     * Return the optional sub-type within the dataset type, for example MATERIALIZED, EXTERNAL or TEMPORARY.
     *
     * @return string
     */
    public String getSubType()
    {
        return subType;
    }


    /**
     * Set up the optional sub-type within the dataset type, for example MATERIALIZED, EXTERNAL or TEMPORARY.
     *
     * @param subType string
     */
    public void setSubType(String subType)
    {
        this.subType = subType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataSetTypeDataSetFacet{" +
                       "datasetType='" + datasetType + '\'' +
                       ", subType='" + subType + '\'' +
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
        OpenLineageDataSetTypeDataSetFacet that = (OpenLineageDataSetTypeDataSetFacet) objectToCompare;
        return Objects.equals(datasetType, that.datasetType) &&
                       Objects.equals(subType, that.subType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), datasetType, subType);
    }
}
