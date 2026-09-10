/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the lineage job facet.  It explicitly declares the dataset-, field- and job-level relationships produced by the job.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-0/LineageFacet.json#/$defs/LineageJobFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLineageJobFacet extends OpenLineageJobFacet
{
    private List<OpenLineageLineageEntry> entries = null;


    /**
     * Default constructor
     */
    public OpenLineageLineageJobFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/LineageFacet.json#/$defs/LineageJobFacet"));
    }


    /**
     * Return the lineage entries describing target entities and the source entities that feed them.
     *
     * @return list
     */
    public List<OpenLineageLineageEntry> getEntries()
    {
        return entries;
    }


    /**
     * Set up the lineage entries describing target entities and the source entities that feed them.
     *
     * @param entries list
     */
    public void setEntries(List<OpenLineageLineageEntry> entries)
    {
        this.entries = entries;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageLineageJobFacet{" +
                       "entries=" + entries +
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
        OpenLineageLineageJobFacet that = (OpenLineageLineageJobFacet) objectToCompare;
        return Objects.equals(entries, that.entries);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), entries);
    }
}
