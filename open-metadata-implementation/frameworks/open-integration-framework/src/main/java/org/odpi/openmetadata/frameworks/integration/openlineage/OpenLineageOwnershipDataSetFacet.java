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
 * This class represents the ownership dataset facet.  It captures the owners of the dataset.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-1/OwnershipDatasetFacet.json#/$defs/OwnershipDatasetFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageOwnershipDataSetFacet extends OpenLineageDataSetFacet
{
    private List<OpenLineageOwnershipDataSetFacetOwner> owners = null;


    /**
     * Default constructor
     */
    public OpenLineageOwnershipDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-1/OwnershipDatasetFacet.json#/$defs/OwnershipDatasetFacet"));
    }


    /**
     * Return the owners of the dataset.
     *
     * @return list
     */
    public List<OpenLineageOwnershipDataSetFacetOwner> getOwners()
    {
        return owners;
    }


    /**
     * Set up the owners of the dataset.
     *
     * @param owners list
     */
    public void setOwners(List<OpenLineageOwnershipDataSetFacetOwner> owners)
    {
        this.owners = owners;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageOwnershipDataSetFacet{" +
                       "owners=" + owners +
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
        OpenLineageOwnershipDataSetFacet that = (OpenLineageOwnershipDataSetFacet) objectToCompare;
        return Objects.equals(owners, that.owners);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), owners);
    }
}
