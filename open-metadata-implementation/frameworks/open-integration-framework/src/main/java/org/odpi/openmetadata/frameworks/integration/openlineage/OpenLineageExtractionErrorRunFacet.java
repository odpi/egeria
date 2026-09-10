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
 * This class represents the extractionError run facet.  It reports tasks whose lineage could not be extracted by the OpenLineage integration.
 * It follows the OpenLineage facet spec https://openlineage.io/spec/facets/1-1-2/ExtractionErrorRunFacet.json#/$defs/ExtractionErrorRunFacet.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageExtractionErrorRunFacet extends OpenLineageRunFacet
{
    private Long                                          totalTasks = null;
    private Long                                          failedTasks = null;
    private List<OpenLineageExtractionErrorRunFacetError> errors = null;


    /**
     * Default constructor
     */
    public OpenLineageExtractionErrorRunFacet()
    {
        super(URI.create("https://openlineage.io/spec/facets/1-1-2/ExtractionErrorRunFacet.json#/$defs/ExtractionErrorRunFacet"));
    }


    /**
     * Return the number of distinguishable tasks in the run that were processed by OpenLineage, whether successfully or not.
     *
     * @return long
     */
    public Long getTotalTasks()
    {
        return totalTasks;
    }


    /**
     * Set up the number of distinguishable tasks in the run that were processed by OpenLineage, whether successfully or not.
     *
     * @param totalTasks long
     */
    public void setTotalTasks(Long totalTasks)
    {
        this.totalTasks = totalTasks;
    }


    /**
     * Return the number of distinguishable tasks in the run that OpenLineage could not process successfully.
     *
     * @return long
     */
    public Long getFailedTasks()
    {
        return failedTasks;
    }


    /**
     * Set up the number of distinguishable tasks in the run that OpenLineage could not process successfully.
     *
     * @param failedTasks long
     */
    public void setFailedTasks(Long failedTasks)
    {
        this.failedTasks = failedTasks;
    }


    /**
     * Return the details of each extraction error.
     *
     * @return list
     */
    public List<OpenLineageExtractionErrorRunFacetError> getErrors()
    {
        return errors;
    }


    /**
     * Set up the details of each extraction error.
     *
     * @param errors list
     */
    public void setErrors(List<OpenLineageExtractionErrorRunFacetError> errors)
    {
        this.errors = errors;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageExtractionErrorRunFacet{" +
                       "totalTasks=" + totalTasks +
                       ", failedTasks=" + failedTasks +
                       ", errors=" + errors +
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
        OpenLineageExtractionErrorRunFacet that = (OpenLineageExtractionErrorRunFacet) objectToCompare;
        return Objects.equals(totalTasks, that.totalTasks) &&
                       Objects.equals(failedTasks, that.failedTasks) &&
                       Objects.equals(errors, that.errors);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), totalTasks, failedTasks, errors);
    }
}
