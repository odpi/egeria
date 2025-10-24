/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssociatedAnnotationProperties is used to link a new annotation to the element it describes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssociatedAnnotationProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor used by subclasses
     */
    public AssociatedAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public AssociatedAnnotationProperties(AssociatedAnnotationProperties template)
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
        return "AssociatedAnnotationProperties{} " + super.toString();
    }
}
