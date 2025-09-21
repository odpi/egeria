/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceCodeTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.FormProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.InformationViewProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataSetProperties is a class for representing a generic data set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DeployedDatabaseSchemaProperties.class, name = "DeployedDatabaseSchemaProperties"),
                @JsonSubTypes.Type(value = FormProperties.class, name = "FormProperties"),
                @JsonSubTypes.Type(value = ReferenceCodeTableProperties.class, name = "ReferenceCodeTableProperties"),
                @JsonSubTypes.Type(value = ReportProperties.class, name = "ReportProperties"),
                @JsonSubTypes.Type(value = InformationViewProperties.class, name = "InformationViewProperties"),
                @JsonSubTypes.Type(value = TopicProperties.class, name = "TopicProperties"),
        })
public class DataSetProperties extends DataAssetProperties
{
    private String        formula                = null;
    private String        formulaType            = null;

    /**
     * Default constructor
     */
    public DataSetProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_SET.typeName);
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
            formula = template.getFormula();
            formulaType = template.getFormulaType();
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataSetProperties{" +
                "formula='" + formula + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataSetProperties that = (DataSetProperties) objectToCompare;
        return Objects.equals(formula, that.formula) && Objects.equals(formulaType, that.formulaType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), formula, formulaType);
    }
}
