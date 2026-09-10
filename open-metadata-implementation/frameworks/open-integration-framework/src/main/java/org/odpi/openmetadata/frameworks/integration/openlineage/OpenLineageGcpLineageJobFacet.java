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
 * This class represents the gcp_lineage job facet registered by Google Cloud Data Lineage.  It supplies the display name and origin of the job for the Data Lineage UI.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/GcpLineageJobFacet.json#/$defs/GcpLineageJobFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageGcpLineageJobFacet extends OpenLineageJobFacet
{
    private String                              displayName = null;
    private OpenLineageGcpLineageJobFacetOrigin origin = null;


    /**
     * Default constructor
     */
    public OpenLineageGcpLineageJobFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/GcpLineageJobFacet.json#/$defs/GcpLineageJobFacet"));
    }


    /**
     * Return the name of the job to be used on the UI.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the name of the job to be used on the UI.
     *
     * @param displayName string
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the origin (source system) of the job.
     *
     * @return bean
     */
    public OpenLineageGcpLineageJobFacetOrigin getOrigin()
    {
        return origin;
    }


    /**
     * Set up the origin (source system) of the job.
     *
     * @param origin bean
     */
    public void setOrigin(OpenLineageGcpLineageJobFacetOrigin origin)
    {
        this.origin = origin;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageGcpLineageJobFacet{" +
                       "displayName='" + displayName + '\'' +
                       ", origin=" + origin +
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
        OpenLineageGcpLineageJobFacet that = (OpenLineageGcpLineageJobFacet) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(origin, that.origin);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, origin);
    }
}
