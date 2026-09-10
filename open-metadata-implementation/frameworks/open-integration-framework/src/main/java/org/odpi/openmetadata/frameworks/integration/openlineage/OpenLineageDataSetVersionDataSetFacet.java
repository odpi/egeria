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
 * This class represents the version dataset facet.  It identifies the version of the dataset that was read or written.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/DatasetVersionDatasetFacet.json#/$defs/DatasetVersionDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDataSetVersionDataSetFacet extends OpenLineageDataSetFacet
{
    private String datasetVersion = null;


    /**
     * Default constructor
     */
    public OpenLineageDataSetVersionDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/DatasetVersionDatasetFacet.json#/$defs/DatasetVersionDatasetFacet"));
    }


    /**
     * Return the version of the dataset.
     *
     * @return string
     */
    public String getDatasetVersion()
    {
        return datasetVersion;
    }


    /**
     * Set up the version of the dataset.
     *
     * @param datasetVersion string
     */
    public void setDatasetVersion(String datasetVersion)
    {
        this.datasetVersion = datasetVersion;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDataSetVersionDataSetFacet{" +
                       "datasetVersion='" + datasetVersion + '\'' +
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
        OpenLineageDataSetVersionDataSetFacet that = (OpenLineageDataSetVersionDataSetFacet) objectToCompare;
        return Objects.equals(datasetVersion, that.datasetVersion);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), datasetVersion);
    }
}
