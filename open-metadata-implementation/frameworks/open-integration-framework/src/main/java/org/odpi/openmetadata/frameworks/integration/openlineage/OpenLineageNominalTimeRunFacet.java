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
 * This class represents the nominalTime run facet.  It captures the nominal (scheduled) start and end time of the run.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/NominalTimeRunFacet.json#/$defs/NominalTimeRunFacet.
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
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/NominalTimeRunFacet.json#/$defs/NominalTimeRunFacet"));
    }


    /**
     * Return the ISO-8601 timestamp representing the nominal start time (included) of the run. AKA the schedule time.
     *
     * @return string
     */
    public String getNominalStartTime()
    {
        return nominalStartTime;
    }


    /**
     * Set up the ISO-8601 timestamp representing the nominal start time (included) of the run. AKA the schedule time.
     *
     * @param nominalStartTime string
     */
    public void setNominalStartTime(String nominalStartTime)
    {
        this.nominalStartTime = nominalStartTime;
    }


    /**
     * Return the ISO-8601 timestamp representing the nominal end time (excluded) of the run. AKA the schedule time.
     *
     * @return string
     */
    public String getNominalEndTime()
    {
        return nominalEndTime;
    }


    /**
     * Set up the ISO-8601 timestamp representing the nominal end time (excluded) of the run. AKA the schedule time.
     *
     * @param nominalEndTime string
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
                       "nominalStartTime='" + nominalStartTime + '\'' +
                       ", nominalEndTime='" + nominalEndTime + '\'' +
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
