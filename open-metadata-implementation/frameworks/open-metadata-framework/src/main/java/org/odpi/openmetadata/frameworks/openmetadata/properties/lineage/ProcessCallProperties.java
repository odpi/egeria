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
 * ProcessCallProperties describe the properties for a process call relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessCallProperties extends RelationshipBeanProperties
{
    private String iscQualifiedName = null;
    private String label            = null;
    private String description      = null;
    private String formula          = null;
    private String formulaType      = null;


    /**
     * Default constructor
     */
    public ProcessCallProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public ProcessCallProperties(ProcessCallProperties template)
    {
        super(template);

        if (template != null)
        {
            iscQualifiedName = template.getISCQualifiedName();
            label            = template.getLabel();
            description      = template.getDescription();
            formula          = template.getFormula();
            formulaType      = template.getFormulaType();
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
     * Return the formula of the relationship.
     *
     * @return string formula
     */
    public String getFormula()
    {
        return formula;
    }


    /**
     * Set up the formula of the relationship.
     *
     * @param formula string name
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Return the specification language for the formula.
     *
     * @return string description
     */
    public String getFormulaType()
    {
        return formulaType;
    }


    /**
     * Set up  the specification language for the formula.
     *
     * @param formulaType string description
     */
    public void setFormulaType(String formulaType)
    {
        this.formulaType = formulaType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProcessCallProperties{" +
                "iscQualifiedName='" + iscQualifiedName + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", formula='" + formula + '\'' +
                ", formulaType='" + formulaType + '\'' +
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
        ProcessCallProperties that = (ProcessCallProperties) objectToCompare;
        return Objects.equals(getISCQualifiedName(), that.getISCQualifiedName()) &&
                Objects.equals(getLabel(), that.getLabel()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getFormula(), that.getFormula()) &&
                Objects.equals(getFormulaType(), that.getFormulaType());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(iscQualifiedName, label, description, formula, formulaType);
    }
}