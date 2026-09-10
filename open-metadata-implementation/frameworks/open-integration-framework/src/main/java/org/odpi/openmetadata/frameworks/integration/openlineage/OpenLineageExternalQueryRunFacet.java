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
 * This class represents the externalQuery run facet.  It identifies a query executed in an external system on behalf of the run.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-0-2/ExternalQueryRunFacet.json#/$defs/ExternalQueryRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageExternalQueryRunFacet extends OpenLineageRunFacet
{
    private String externalQueryId = null;
    private String source = null;


    /**
     * Default constructor
     */
    public OpenLineageExternalQueryRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-2/ExternalQueryRunFacet.json#/$defs/ExternalQueryRunFacet"));
    }


    /**
     * Return the identifier of the query in the external system.
     *
     * @return string
     */
    public String getExternalQueryId()
    {
        return externalQueryId;
    }


    /**
     * Set up the identifier of the query in the external system.
     *
     * @param externalQueryId string
     */
    public void setExternalQueryId(String externalQueryId)
    {
        this.externalQueryId = externalQueryId;
    }


    /**
     * Return the source (external system) of the query.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source (external system) of the query.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageExternalQueryRunFacet{" +
                       "externalQueryId='" + externalQueryId + '\'' +
                       ", source='" + source + '\'' +
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
        OpenLineageExternalQueryRunFacet that = (OpenLineageExternalQueryRunFacet) objectToCompare;
        return Objects.equals(externalQueryId, that.externalQueryId) &&
                       Objects.equals(source, that.source);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalQueryId, source);
    }
}
