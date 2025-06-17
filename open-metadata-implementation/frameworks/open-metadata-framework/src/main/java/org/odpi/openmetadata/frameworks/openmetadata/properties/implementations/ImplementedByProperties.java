/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.implementations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ImplementedByProperties represents an implementedBy relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImplementedByProperties extends RelationshipProperties
{
    private String designStep       = null;
    private String description      = null;
    private String role             = null;
    private String transformation   = null;
    private String iscQualifiedName = null;

    /**
     * Default constructor
     */
    public ImplementedByProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ImplementedByProperties(ImplementedByProperties template)
    {
        super(template);

        if (template != null)
        {
            this.designStep       = template.getDesignStep();
            this.description      = template.getDescription();
            this.role             = template.getRole();
            this.transformation   = template.getTransformation();
            this.iscQualifiedName = template.getISCQualifiedName();
        }
    }


    /**
     * Return the name of design step that created the implementation.
     *
     * @return string name
     */
    public String getDesignStep()
    {
        return designStep;
    }


    /**
     * Set up the name of design step that created the implementation.
     *
     * @param designStep string name
     */
    public void setDesignStep(String designStep)
    {
        this.designStep = designStep;
    }


    /**
     * Return the description for this element.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this element.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the role of this implementation in supporting the behaviour of element it is derived from.
     *
     * @return String
     */
    public String getRole()
    {
        return role;
    }


    /**
     * Set up the role of this implementation in supporting the behaviour of element it is derived from.
     *
     * @param role String
     */
    public void setRole(String role)
    {
        this.role = role;
    }


    /**
     * Return details of the transformation used to create the implementation.
     *
     * @return string
     */
    public String getTransformation()
    {
        return transformation;
    }


    /**
     * Set up details of the transformation used to create the implementation.
     *
     * @param transformation string
     */
    public void setTransformation(String transformation)
    {
        this.transformation = transformation;
    }


    public String getISCQualifiedName()
    {
        return iscQualifiedName;
    }

    public void setISCQualifiedName(String iscQualifiedName)
    {
        this.iscQualifiedName = iscQualifiedName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ImplementedByProperties{" +
                "designStep='" + designStep + '\'' +
                ", description='" + description + '\'' +
                ", role='" + role + '\'' +
                ", transformation='" + transformation + '\'' +
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
        if (! (objectToCompare instanceof ImplementedByProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(designStep, that.designStep) &&
                Objects.equals(description, that.description) &&
                Objects.equals(transformation, that.transformation) &&
                       Objects.equals(role, that.role);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), designStep, description, transformation, role);
    }
}
