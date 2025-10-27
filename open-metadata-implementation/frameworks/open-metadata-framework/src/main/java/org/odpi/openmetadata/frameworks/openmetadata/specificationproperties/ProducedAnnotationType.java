/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProducedAnnotationType describes an annotation type of survey action service. It is part of the metadata to help
 * tools understand the operations of a service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProducedAnnotationType extends AnnotationTypeType
{
    /**
     * Default constructor
     */
    public ProducedAnnotationType()
    {
        super();
        super.setSpecificationPropertyType(SpecificationPropertyType.PRODUCED_ANNOTATION_TYPE);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProducedAnnotationType(AnnotationTypeType template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProducedAnnotationType{" +
                "} " + super.toString();
    }
}
