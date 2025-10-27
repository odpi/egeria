/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AnalysisStepType describes an analysis step of a survey action service it is part of the metadata to help
 * tools understand the operations of a service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SupportedAnalysisStep.class, name = "SupportedAnalysisStep"),
        })
public class AnalysisStepType extends SpecificationProperty
{
    /**
     * Default constructor
     */
    public AnalysisStepType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnalysisStepType(SpecificationProperty template)
    {
        super (template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AnalysisStepType{" +
                "} " + super.toString();
    }
}
