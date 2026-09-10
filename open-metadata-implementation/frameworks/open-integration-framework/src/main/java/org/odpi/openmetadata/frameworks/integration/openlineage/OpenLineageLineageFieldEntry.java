/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the source inputs that feed a single target field in the lineage facets.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageLineageFieldEntry
{
    private List<OpenLineageLineageInput> inputs = null;


    /**
     * Default constructor
     */
    public OpenLineageLineageFieldEntry()
    {
    }


    /**
     * Return the source entities and/or fields that feed into this target field.
     *
     * @return list
     */
    public List<OpenLineageLineageInput> getInputs()
    {
        return inputs;
    }


    /**
     * Set up the source entities and/or fields that feed into this target field.
     *
     * @param inputs list
     */
    public void setInputs(List<OpenLineageLineageInput> inputs)
    {
        this.inputs = inputs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageLineageFieldEntry{" +
                       "inputs=" + inputs +
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
        OpenLineageLineageFieldEntry that = (OpenLineageLineageFieldEntry) objectToCompare;
        return Objects.equals(inputs, that.inputs);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(inputs);
    }
}
