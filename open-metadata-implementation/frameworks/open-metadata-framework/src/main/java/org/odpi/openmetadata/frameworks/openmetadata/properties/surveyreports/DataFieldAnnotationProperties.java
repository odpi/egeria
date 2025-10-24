/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFieldAnnotationProperties is a common base class for annotations that are attached to a data field.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ClassificationAnnotationProperties.class, name = "ClassificationAnnotationProperties"),
                @JsonSubTypes.Type(value = DataClassAnnotationProperties.class, name = "DataClassAnnotationProperties"),
                @JsonSubTypes.Type(value = FingerprintAnnotationProperties.class, name = "FingerprintAnnotationProperties"),
                @JsonSubTypes.Type(value = QualityAnnotationProperties.class, name = "QualityAnnotationProperties"),
                @JsonSubTypes.Type(value = ResourceProfileAnnotationProperties.class, name = "DataProfileAnnotation"),
                @JsonSubTypes.Type(value = ResourceProfileLogAnnotationProperties.class, name = "DataProfileLogAnnotation"),
                @JsonSubTypes.Type(value = RelationshipAdviceAnnotationProperties.class, name = "RelationshipAdviceAnnotationProperties"),
                @JsonSubTypes.Type(value = RequestForActionProperties.class, name = "RequestForActionAnnotationProperties"),
                @JsonSubTypes.Type(value = SemanticAnnotationProperties.class, name = "SemanticAnnotationProperties"),
        })
public class DataFieldAnnotationProperties extends AnnotationProperties
{
    /**
     * Default constructor
     */
    public DataFieldAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName);
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataFieldAnnotationProperties(AnnotationProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataFieldAnnotationProperties{} " + super.toString();
    }
}
