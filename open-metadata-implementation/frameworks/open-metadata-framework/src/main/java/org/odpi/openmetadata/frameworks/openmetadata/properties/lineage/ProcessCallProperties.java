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
public class ProcessCallProperties extends LineageRelationshipProperties
{
    private String formula          = null;
    private String formulaType      = null;
    private String lineNumber       = null;


    /**
     * Default constructor
     */
    public ProcessCallProperties()
    {
        super();
        super.typeName = OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName;
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
            formula     = template.getFormula();
            formulaType = template.getFormulaType();
            lineNumber  = template.getLineNumber();
        }
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
     * Return the line number where the process call was made.
     *
     * @return string
     */
    public String getLineNumber()
    {
        return lineNumber;
    }


    /**
     * Set up the line number where the process call was made.
     *
     * @param lineNumber string
     */
    public void setLineNumber(String lineNumber)
    {
        this.lineNumber = lineNumber;
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
                "formula='" + formula + '\'' +
                ", formulaType='" + formulaType + '\'' +
                ", lineNumber='" + lineNumber + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ProcessCallProperties that = (ProcessCallProperties) objectToCompare;
        return Objects.equals(formula, that.formula) &&
                Objects.equals(formulaType, that.formulaType) &&
                Objects.equals(lineNumber, that.lineNumber);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), formula, formulaType, lineNumber);
    }
}