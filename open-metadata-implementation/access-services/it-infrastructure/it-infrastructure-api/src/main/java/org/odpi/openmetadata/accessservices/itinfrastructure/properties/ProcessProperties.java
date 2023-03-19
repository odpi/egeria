/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Process properties defines the properties of a process.  A process is a series of steps and decisions in operation
 * in the organization.  It is typically an automated process but may be performed by a person.
 * Only set the implementationLanguage if the process is automated.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessProperties extends AssetProperties
{
    private static final long     serialVersionUID = 1L;

    private static final String formulaProperty                = "formula";
    private static final String formulaTypeProperty            = "formulaType";
    private static final String implementationLanguageProperty = "implementationLanguage";

    private String        formula                = null;
    private String        formulaType            = null;
    private String        implementationLanguage = null;

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
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProcessProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            Map<String, Object> assetExtendedProperties = template.getExtendedProperties();

            if (assetExtendedProperties != null)
            {
                if (assetExtendedProperties.get(formulaProperty) != null)
                {
                    formula = assetExtendedProperties.get(formulaProperty).toString();
                    assetExtendedProperties.remove(formulaProperty);
                }

                if (assetExtendedProperties.get(formulaTypeProperty) != null)
                {
                    formulaType = assetExtendedProperties.get(formulaTypeProperty).toString();
                    assetExtendedProperties.remove(formulaTypeProperty);
                }

                if (assetExtendedProperties.get(implementationLanguageProperty) != null)
                {
                    implementationLanguage = assetExtendedProperties.get(implementationLanguageProperty).toString();
                    assetExtendedProperties.remove(implementationLanguageProperty);
                }

                super.setExtendedProperties(assetExtendedProperties);
            }
        }
    }


    /**
     * Convert this object into an AssetProperties object.  This involves packing the properties introduced at this level
     * into the extended properties.
     *
     * @return asset properties
     */
    public AssetProperties cloneToAsset()
    {
        AssetProperties assetProperties = super.cloneToAsset("Process");

        Map<String, Object> extendedProperties = assetProperties.getExtendedProperties();

        if (extendedProperties == null)
        {
            extendedProperties = new HashMap<>();
        }

        if (formula != null)
        {
            extendedProperties.put(formulaProperty, formula);
        }

        if (formulaType != null)
        {
            extendedProperties.put(formulaTypeProperty, formulaType);
        }

        if (implementationLanguage != null)
        {
            extendedProperties.put(implementationLanguageProperty, implementationLanguage);
        }

        if (! extendedProperties.isEmpty())
        {
            assetProperties.setExtendedProperties(extendedProperties);
        }

        return assetProperties;
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
     * Return the name of the programming language that this process is implemented in.
     *
     * @return string name
     */
    public String getImplementationLanguage()
    {
        return implementationLanguage;
    }


    /**
     * Set up the name of the programming language that this process is implemented in.
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
                       "name='" + getName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", formula='" + formula + '\'' +
                       ", formulaType='" + formulaType + '\'' +
                       ", implementationLanguage='" + implementationLanguage + '\'' +
                       ", cloneToAsset=" + cloneToAsset() +
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
        return Objects.equals(formula, that.formula) && Objects.equals(formulaType, that.formulaType) &&
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
        return Objects.hash(super.hashCode(), getFormula(), getFormulaType(), getImplementationLanguage());
    }
}
