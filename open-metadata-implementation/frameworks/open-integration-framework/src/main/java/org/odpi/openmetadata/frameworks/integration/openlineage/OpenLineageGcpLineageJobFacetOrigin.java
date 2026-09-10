/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the origin of a job in the gcp_lineage job facet.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageGcpLineageJobFacetOrigin
{
    private String sourceType = null;
    private String name = null;


    /**
     * Default constructor
     */
    public OpenLineageGcpLineageJobFacetOrigin()
    {
    }


    /**
     * Return the type of the source system, for example COMPOSER, BIGQUERY_DATA_TRANSFER_SERVICE or CUSTOM.
     *
     * @return string
     */
    public String getSourceType()
    {
        return sourceType;
    }


    /**
     * Set up the type of the source system, for example COMPOSER, BIGQUERY_DATA_TRANSFER_SERVICE or CUSTOM.
     *
     * @param sourceType string
     */
    public void setSourceType(String sourceType)
    {
        this.sourceType = sourceType;
    }


    /**
     * Return the name of the source system.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the source system.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageGcpLineageJobFacetOrigin{" +
                       "sourceType='" + sourceType + '\'' +
                       ", name='" + name + '\'' +
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
        OpenLineageGcpLineageJobFacetOrigin that = (OpenLineageGcpLineageJobFacetOrigin) objectToCompare;
        return Objects.equals(sourceType, that.sourceType) &&
                       Objects.equals(name, that.name);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(sourceType, name);
    }
}
