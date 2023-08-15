/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataSetProperties is a class for representing a generic data set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataSetProperties extends DataAssetProperties
{
    private String formula = null;
    private String formulaType = null;


    /**
     * Default constructor
     */
    public DataSetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataSetProperties(DataSetProperties template)
    {
        super(template);

        if (template != null)
        {
            this.formula = template.getFormula();
            this.formulaType = template.getFormulaType();
        }
    }


    /**
     * Return the formula that generates the data set from its source assets.  This formula may have references to query ids that are stored in
     * the DataContentForDataSet relationship.
     *
     * @return string encoded formula
     */
    public String getFormula()
    {
        return formula;
    }


    /**
     * Set up formula that generates the data set from its source assets.  This formula may have references to query ids that are stored in
     * the DataContentForDataSet relationship.
     *
     * @param formula string encoded formula
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Return the format of the expression provided in the formula attribute.
     *
     * @return string name
     */
    public String getFormulaType()
    {
        return formulaType;
    }


    /**
     * Set up the format of the expression provided in the formula attribute.
     *
     * @param formulaType string name
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
        return "DataSetProperties{" +
                       "technicalName='" + getTechnicalName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", technicalDescription='" + getTechnicalDescription() + '\'' +
                       ", isReferenceAsset=" + getIsReferenceAsset() +
                       ", deployedImplementationType=" + getDeployedImplementationType() +
                       ", formula='" + formula + '\'' +
                       ", formulaType='" + formulaType + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
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
        DataSetProperties that = (DataSetProperties) objectToCompare;
        return Objects.equals(formula, that.formula) && Objects.equals(formulaType, that.formulaType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), formula, formulaType);
    }
}
