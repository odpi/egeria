/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Returns lineage node names.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageNodeNamesResponse extends FFDCResponseBase
{
    List<String> names;

    /**
     * Default constructor
     */
    public LineageNodeNamesResponse()
    {
        super();
    }

    /**
     * Return the list of node names;
     *
     * @return list of strings
     */
    public List<String> getNames()
    {
        return names;
    }


    /**
     * Set up the list of node names.
     *
     * @param names list of strings
     */
    public void setNames(List<String> names)
    {
        this.names = names;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "LineageNodeNamesResponse{" +
                "names=" + names +
                "} " + super.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        LineageNodeNamesResponse that = (LineageNodeNamesResponse) objectToCompare;
        return Objects.equals(names, that.names);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), names);
    }
}
