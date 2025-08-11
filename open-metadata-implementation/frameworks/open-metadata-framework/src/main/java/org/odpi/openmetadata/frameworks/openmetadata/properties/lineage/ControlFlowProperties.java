/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.lineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ControlFlowProperties describe the properties for a control flow relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ControlFlowProperties extends RelationshipBeanProperties
{
    private String iscQualifiedName = null;
    private String label            = null;
    private String description      = null;
    private String guard            = null;
    private boolean mandatoryGuard  = false;


    /**
     * Default constructor
     */
    public ControlFlowProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public ControlFlowProperties(ControlFlowProperties template)
    {
        super(template);

        if (template != null)
        {
            iscQualifiedName = template.getISCQualifiedName();
            label            = template.getLabel();
            description      = template.getDescription();
            guard            = template.getGuard();
            mandatoryGuard   = template.getMandatoryGuard();
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
     * Return the label used when displaying this relationship.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the label used when displaying this relationship.
     *
     * @param label string
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the description of the relationship.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the relationship.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the guard of the relationship.
     *
     * @return string guard
     */
    public String getGuard()
    {
        return guard;
    }


    /**
     * Set up the guard of the relationship.
     *
     * @param guard string name
     */
    public void setGuard(String guard)
    {
        this.guard = guard;
    }


    public boolean getMandatoryGuard()
    {
        return mandatoryGuard;
    }

    public void setMandatoryGuard(boolean mandatoryGuard)
    {
        this.mandatoryGuard = mandatoryGuard;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ControlFlowProperties{" +
                "iscQualifiedName='" + iscQualifiedName + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", guard='" + guard + '\'' +
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
        ControlFlowProperties that = (ControlFlowProperties) objectToCompare;
        return Objects.equals(getISCQualifiedName(), that.getISCQualifiedName()) &&
                Objects.equals(getLabel(), that.getLabel()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getGuard(), that.getGuard());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(iscQualifiedName, label, description, guard);
    }
}