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
 * This class represents the content of an open lineage documentation data set facet as defined in JSON
 * spec https://openlineage.io/spec/facets/1-0-0/DocumentationDatasetFacet.json#/$defs/DocumentationDatasetFacet.
 * It is used internally in Egeria to pass this information to the integration daemon's integration connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageDocumentationDataSetFacet extends OpenLineageDataSetFacet
{
    private String description;


    /**
     * Default constructor
     */
    public OpenLineageDocumentationDataSetFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-0-0/DocumentationDatasetFacet.json#/$defs/DocumentationDatasetFacet"));
    }


    /**
     * Return the description of the data set.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for the data set.
     *
     * @param description string name
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageDocumentationDataSetFacet{" +
                       "description='" + description + '\'' +
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
        OpenLineageDocumentationDataSetFacet that = (OpenLineageDocumentationDataSetFacet) objectToCompare;
        return Objects.equals(description, that.description);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), description);
    }
}
