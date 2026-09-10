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
 * This class represents the gcp_composer_run run facet registered by Google Cloud Composer.  It identifies the DAG run.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/GcpComposerRunFacet.json#/$defs/GcpComposerRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageGcpComposerRunFacet extends OpenLineageRunFacet
{
    private String dagRunId = null;


    /**
     * Default constructor
     */
    public OpenLineageGcpComposerRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/GcpComposerRunFacet.json#/$defs/GcpComposerRunFacet"));
    }


    /**
     * Return the id of the DAG run.
     *
     * @return string
     */
    public String getDagRunId()
    {
        return dagRunId;
    }


    /**
     * Set up the id of the DAG run.
     *
     * @param dagRunId string
     */
    public void setDagRunId(String dagRunId)
    {
        this.dagRunId = dagRunId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageGcpComposerRunFacet{" +
                       "dagRunId='" + dagRunId + '\'' +
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
        OpenLineageGcpComposerRunFacet that = (OpenLineageGcpComposerRunFacet) objectToCompare;
        return Objects.equals(dagRunId, that.dagRunId);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dagRunId);
    }
}
