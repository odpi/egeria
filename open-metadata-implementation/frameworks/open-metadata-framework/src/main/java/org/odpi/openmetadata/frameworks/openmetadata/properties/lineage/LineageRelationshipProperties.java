/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.lineage;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ITProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.TeamProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductDependencyProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LineageRelationshipProperties describe the common properties for a lineage relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DataFlowProperties.class, name = "DataFlowProperties"),
                @JsonSubTypes.Type(value = ControlFlowProperties.class, name = "ControlFlowProperties"),
                @JsonSubTypes.Type(value = ProcessCallProperties.class, name = "ProcessCallProperties"),
                @JsonSubTypes.Type(value = LineageMappingProperties.class, name = "LineageMappingProperties"),
                @JsonSubTypes.Type(value = DataMappingProperties.class, name = "DataMappingProperties"),
                @JsonSubTypes.Type(value = DigitalProductDependencyProperties.class, name = "DigitalProductDependencyProperties"),
        })
public class LineageRelationshipProperties extends LabeledRelationshipProperties
{
    private String iscQualifiedName = null;



    /**
     * Default constructor
     */
    public LineageRelationshipProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public LineageRelationshipProperties(LineageRelationshipProperties template)
    {
        super(template);

        if (template != null)
        {
            iscQualifiedName = template.getISCQualifiedName();
        }
    }


    /**
     * Set up the fully qualified name of the associated information supply chain.
     *
     * @param iscQualifiedName String name
     */
    public void setISCQualifiedName(String iscQualifiedName)
    {
        this.iscQualifiedName = iscQualifiedName;
    }


    /**
     * Returns the stored qualified name of the associated information supply chain.
     *
     * @return qualifiedName
     */
    public String getISCQualifiedName()
    {
        return iscQualifiedName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LineageRelationshipProperties{" +
                "iscQualifiedName='" + iscQualifiedName + '\'' +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        LineageRelationshipProperties that = (LineageRelationshipProperties) objectToCompare;
        return Objects.equals(getISCQualifiedName(), that.getISCQualifiedName());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), iscQualifiedName);
    }
}