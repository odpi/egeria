/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Maps an element as an execution point for a governance definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ControlPointProperties.class, name = "ControlPointProperties"),
                @JsonSubTypes.Type(value = EnforcementPointProperties.class, name = "EnforcementPointProperties"),
                @JsonSubTypes.Type(value = VerificationPointProperties.class, name = "VerificationPointProperties"),
        })
public class ExecutionPointProperties extends ClassificationBeanProperties
{
    private String qualifiedName = null;


    /**
     * Default constructor
     */
    public ExecutionPointProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXECUTION_POINT_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExecutionPointProperties(ExecutionPointProperties template)
    {
        super(template);

        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
        }
    }


    /**
     * Return the qualified name of the associated governance definition.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the qualified name of the associated governance definition.
     *
     * @param qualifiedName string
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EnforcementPointProperties{" +
                "qualifiedName='" + qualifiedName + '\'' +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ExecutionPointProperties that = (ExecutionPointProperties) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualifiedName);
    }
}
