/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the content of an open lineage schema for the nominal (expected) time of a job run facet as defined in JSON
 * spec https://openlineage.io/spec/facets/1-0-0/NominalTimeRunFacet.json#/$defs/NominalTimeRunFacet.
 * It is used internally in Egeria to pass this information to the Lineage Integrator OMIS's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageNominalTimeRunFacet extends OpenLineageRunFacet
{
    private String nominalStartTime = null;
    private String nominalEndTime = null;

    /**
     * Default constructor
     */
    public OpenLineageNominalTimeRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/NominalTimeRunFacet.json#/$defs/NominalTimeRunFacet"));
    }


    /**
     * Return an [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601) timestamp representing the nominal start time (included) of the run.
     * AKA the schedule time.
     *
     * @return zoned time stamp
     */
    public String getNominalStartTime()
    {
        return nominalStartTime;
    }


    /**
     * Set up an [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601) timestamp representing the nominal start time (included) of the run.
     * AKA the schedule time.
     *
     * @param nominalStartTime zoned time stamp
     */
    public void setNominalStartTime(String nominalStartTime)
    {
        this.nominalStartTime = nominalStartTime;
    }


    /**
     * Return an [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601) timestamp representing the nominal end time (excluded) of the run.
     * (Should be the nominal start time of next run.)
     *
     * @return zoned time stamp
     */
    public String getNominalEndTime()
    {
        return nominalEndTime;
    }


    /**
     * Set up an [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601) timestamp representing the nominal end time (excluded) of the run.
     * (Should be the nominal start time of next run.)
     *
     * @param nominalEndTime zoned time stamp
     */
    public void setNominalEndTime(String nominalEndTime)
    {
        this.nominalEndTime = nominalEndTime;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageNominalTimeRunFacet{" +
                       "nominalStartTime=" + nominalStartTime +
                       ", nominalEndTime=" + nominalEndTime +
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
        OpenLineageNominalTimeRunFacet that = (OpenLineageNominalTimeRunFacet) objectToCompare;
        return Objects.equals(nominalStartTime, that.nominalStartTime) &&
                       Objects.equals(nominalEndTime, that.nominalEndTime);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), nominalStartTime, nominalEndTime);
    }
}
