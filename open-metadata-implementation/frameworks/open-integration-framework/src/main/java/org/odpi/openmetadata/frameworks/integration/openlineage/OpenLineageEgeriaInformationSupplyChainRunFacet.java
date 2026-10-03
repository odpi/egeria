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
 * This class represents Egeria's custom egeria_informationSupplyChain run facet.  It names the information supply
 * chain that a run is part of, so that the lineage Egeria catalogues from the run is tagged with it.  Any
 * OpenLineage producer (for example an Apache Airflow DAG) can add it to its run events.
 * It follows the facet schema at {@value #SCHEMA_URL}.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageEgeriaInformationSupplyChainRunFacet extends OpenLineageRunFacet
{
    /**
     * Key of the facet in the run's facets.
     */
    public static final String FACET_NAME = "egeria_informationSupplyChain";

    /**
     * Location of the facet's schema.
     */
    public static final String SCHEMA_URL = "https://egeria-project.org/openlineage/facets/1-0-0/EgeriaInformationSupplyChainRunFacet.json#/$defs/EgeriaInformationSupplyChainRunFacet";

    private String iscQualifiedName = null;


    /**
     * Default constructor
     */
    public OpenLineageEgeriaInformationSupplyChainRunFacet()
    {
        super(URI.create(SCHEMA_URL));
    }


    /**
     * Return the qualified name of the information supply chain that the run is part of.
     *
     * @return string
     */
    public String getIscQualifiedName()
    {
        return iscQualifiedName;
    }


    /**
     * Set up the qualified name of the information supply chain that the run is part of.
     *
     * @param iscQualifiedName string
     */
    public void setIscQualifiedName(String iscQualifiedName)
    {
        this.iscQualifiedName = iscQualifiedName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageEgeriaInformationSupplyChainRunFacet{" +
                       "iscQualifiedName='" + iscQualifiedName + '\'' +
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
        OpenLineageEgeriaInformationSupplyChainRunFacet that = (OpenLineageEgeriaInformationSupplyChainRunFacet) objectToCompare;
        return Objects.equals(iscQualifiedName, that.iscQualifiedName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), iscQualifiedName);
    }
}
