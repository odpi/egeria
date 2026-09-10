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
 * This class represents the processing_engine run facet.  It identifies the engine (and OpenLineage adapter) that executed the run.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-1-1/ProcessingEngineRunFacet.json#/$defs/ProcessingEngineRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageProcessingEngineRunFacet extends OpenLineageRunFacet
{
    private String version = null;
    private String name = null;
    private String openlineageAdapterVersion = null;


    /**
     * Default constructor
     */
    public OpenLineageProcessingEngineRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-1-1/ProcessingEngineRunFacet.json#/$defs/ProcessingEngineRunFacet"));
    }


    /**
     * Return the processing engine version, for example the Airflow or Spark version.
     *
     * @return string
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the processing engine version, for example the Airflow or Spark version.
     *
     * @param version string
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the processing engine name, for example Airflow or Spark.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the processing engine name, for example Airflow or Spark.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the version of the OpenLineage adapter package used by the engine.
     *
     * @return string
     */
    public String getOpenlineageAdapterVersion()
    {
        return openlineageAdapterVersion;
    }


    /**
     * Set up the version of the OpenLineage adapter package used by the engine.
     *
     * @param openlineageAdapterVersion string
     */
    public void setOpenlineageAdapterVersion(String openlineageAdapterVersion)
    {
        this.openlineageAdapterVersion = openlineageAdapterVersion;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageProcessingEngineRunFacet{" +
                       "version='" + version + '\'' +
                       ", name='" + name + '\'' +
                       ", openlineageAdapterVersion='" + openlineageAdapterVersion + '\'' +
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
        OpenLineageProcessingEngineRunFacet that = (OpenLineageProcessingEngineRunFacet) objectToCompare;
        return Objects.equals(version, that.version) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(openlineageAdapterVersion, that.openlineageAdapterVersion);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), version, name, openlineageAdapterVersion);
    }
}
