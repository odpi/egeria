/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Process properties defines the properties of a process.  A process is a series of steps and decisions in operation
 * in the organization.  It is typically an automated process but may be performed by a person.
 * Only set the deployedImplementationType or implementationLanguage if the process is automated - ie inherits from DeployedSoftwareComponent.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessProperties extends AssetProperties
{
    private String formula                    = null;
    private String formulaType                = null;
    private String deployedImplementationType = null;
    private String implementationLanguage     = null;

    /**
     * Default constructor
     */
    public ProcessProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ProcessProperties(ProcessProperties template)
    {
        super(template);

        if (template != null)
        {
            formula = template.getFormula();
            formulaType = template.getFormulaType();
            implementationLanguage = template.getImplementationLanguage();
            deployedImplementationType = template.getDeployedImplementationType();
        }
    }


    /**
     * Return the description of the processing performed by this process.
     *
     * @return string description
     */
    public String getFormula() { return formula; }


    /**
     * Set up the description of the processing performed by this process.
     *
     * @param formula string description
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
     * Retrieve the name of the technology used for this process (DeployedComponentType only).
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the name of the technology used for this process (DeployedComponentType only).
     *
     * @param deployedImplementationType string name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return the name of the programming language that this process is implemented in (DeployedComponentType only).
     *
     * @return string name
     */
    public String getImplementationLanguage()
    {
        return implementationLanguage;
    }


    /**
     * Set up the name of the programming language that this process is implemented in (DeployedComponentType only).
     *
     * @param implementationLanguage string name
     */
    public void setImplementationLanguage(String implementationLanguage)
    {
        this.implementationLanguage = implementationLanguage;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProcessProperties{" +
                       ", formula='" + formula + '\'' +
                       ", deployedImplementationType='" + deployedImplementationType + '\'' +
                       ", implementationLanguage='" + implementationLanguage + '\'' +
                       ", technicalName='" + getTechnicalName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", technicalDescription='" + getTechnicalDescription() + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ProcessProperties that = (ProcessProperties) objectToCompare;
        return Objects.equals(formula, that.formula) &&
                       Objects.equals(implementationLanguage, that.implementationLanguage);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getFormula(), getImplementationLanguage());
    }
}
