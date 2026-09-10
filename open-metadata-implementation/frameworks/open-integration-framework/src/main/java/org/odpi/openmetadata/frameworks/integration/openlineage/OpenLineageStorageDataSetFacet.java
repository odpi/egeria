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
 * This class represents the storage dataset facet.  It identifies the storage layer and file format of the dataset.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/StorageDatasetFacet.json#/$defs/StorageDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageStorageDataSetFacet extends OpenLineageDataSetFacet
{
    private String storageLayer = null;
    private String fileFormat = null;


    /**
     * Default constructor
     */
    public OpenLineageStorageDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/StorageDatasetFacet.json#/$defs/StorageDatasetFacet"));
    }


    /**
     * Return the storage layer provider, for example iceberg or delta.
     *
     * @return string
     */
    public String getStorageLayer()
    {
        return storageLayer;
    }


    /**
     * Set up the storage layer provider, for example iceberg or delta.
     *
     * @param storageLayer string
     */
    public void setStorageLayer(String storageLayer)
    {
        this.storageLayer = storageLayer;
    }


    /**
     * Return the file format, for example parquet, orc, avro, json, csv, text or xml.
     *
     * @return string
     */
    public String getFileFormat()
    {
        return fileFormat;
    }


    /**
     * Set up the file format, for example parquet, orc, avro, json, csv, text or xml.
     *
     * @param fileFormat string
     */
    public void setFileFormat(String fileFormat)
    {
        this.fileFormat = fileFormat;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageStorageDataSetFacet{" +
                       "storageLayer='" + storageLayer + '\'' +
                       ", fileFormat='" + fileFormat + '\'' +
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
        OpenLineageStorageDataSetFacet that = (OpenLineageStorageDataSetFacet) objectToCompare;
        return Objects.equals(storageLayer, that.storageLayer) &&
                       Objects.equals(fileFormat, that.fileFormat);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), storageLayer, fileFormat);
    }
}
