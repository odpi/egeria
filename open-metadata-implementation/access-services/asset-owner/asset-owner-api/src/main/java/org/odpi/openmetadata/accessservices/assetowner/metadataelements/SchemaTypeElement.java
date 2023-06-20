/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.DerivedSchemaTypeQueryTargetProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaTypeElement contains the properties and header for a reference data asset retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaTypeElement implements MetadataElement
{
    private SchemaTypeProperties                         schemaTypeProperties = null;
    private ElementHeader                                elementHeader        = null;

    /*
     * For complex schema types such as StructSchemaType
     */
    private int                                          attributeCount       = 0;

    /*
     * For Map Schema Types
     */
    private SchemaTypeElement                            mapFromElement = null;
    private SchemaTypeElement                            mapToElement   = null;

    /*
     * For External Schema Types
     */
    private SchemaTypeElement                            externalSchemaType = null;

    /*
     * Schema options for SchemaTypeChoice
     */
    private List<SchemaTypeElement>                      schemaOptions = null;

    /*
     * Used when a value, or set of values associated with the schema are derived rather than stored.
     */
    private String                                       formula              = null;
    private String                                       formulaType          = null;
    private List<DerivedSchemaTypeQueryTargetProperties> queries              = null;

    /**
     * Default constructor
     */
    public SchemaTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaTypeElement(SchemaTypeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            schemaTypeProperties = template.getSchemaTypeProperties();

            attributeCount = template.getAttributeCount();

            mapFromElement = template.getMapFromElement();
            mapToElement   = template.getMapToElement();

            externalSchemaType = template.getExternalSchemaType();

            schemaOptions = template.getSchemaOptions();

            formula = template.getFormula();
            formulaType = template.getFormulaType();
            queries = template.getQueries();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties for the schema.
     *
     * @return schema properties (using appropriate subclass)
     */
    public SchemaTypeProperties getSchemaTypeProperties()
    {
        return schemaTypeProperties;
    }


    /**
     * Set up the properties for the schema.
     *
     * @param schemaTypeProperties schema properties
     */
    public void setSchemaTypeProperties(SchemaTypeProperties schemaTypeProperties)
    {
        this.schemaTypeProperties = schemaTypeProperties;
    }



    /**
     * Return the count of attributes in this schema type.
     *
     * @return String data type name
     */
    public int getAttributeCount() { return attributeCount; }


    /**
     * Set up the count of attributes in this schema type
     *
     * @param attributeCount data type name
     */
    public void setAttributeCount(int attributeCount)
    {
        this.attributeCount = attributeCount;
    }



    /**
     * Return the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @return SchemaElement
     */
    public SchemaTypeElement getMapFromElement()
    {
        return mapFromElement;
    }


    /**
     * Set up the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @param mapFromElement SchemaElement
     */
    public void setMapFromElement(SchemaTypeElement mapFromElement)
    {
        this.mapFromElement = mapFromElement;
    }


    /**
     * Return the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @return SchemaElement
     */
    public SchemaTypeElement getMapToElement()
    {
        return mapToElement;
    }


    /**
     * Set up the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @param mapToElement SchemaType
     */
    public void setMapToElement(SchemaTypeElement mapToElement)
    {
        this.mapToElement = mapToElement;
    }


    /**
     * Return the schema type that is reusable amongst assets.
     *
     * @return bean describing external schema
     */
    public SchemaTypeElement getExternalSchemaType()
    {
        return externalSchemaType;
    }


    /**
     * Set up the schema type that is reusable amongst assets.
     *
     * @param externalSchemaType bean describing external schema
     */
    public void setExternalSchemaType(SchemaTypeElement externalSchemaType)
    {
        this.externalSchemaType = externalSchemaType;
    }


    /**
     * Return the list of alternative schema types that this attribute or asset may use.
     *
     * @return list of schema types
     */
    public List<SchemaTypeElement> getSchemaOptions()
    {
        if (schemaOptions == null)
        {
            return null;
        }
        else if (schemaOptions.isEmpty())
        {
            return null;
        }

        return schemaOptions;
    }


    /**
     * Set up the list of alternative schema types that this attribute or asset may use.
     *
     * @param schemaOptions list of schema types
     */
    public void setSchemaOptions(List<SchemaTypeElement> schemaOptions)
    {
        this.schemaOptions = schemaOptions;
    }


    /**
     * Return the formula used to combine the values of the queries.  Each query is has a identifier and the
     * formula has placeholders for these identifiers in it to show how the query results are combined.
     *
     * @return String formula
     */
    public String getFormula() { return formula; }


    /**
     * Set up the formula used to combine the values of the queries.  Each query is has a identifier and the
     * formula has placeholders for these identifiers in it to show how the query results are combined.
     *
     * @param formula String formula
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
     * Return the list of individual query targets for a derived column.
     *
     * @return list of queries and their target element
     */
    public List<DerivedSchemaTypeQueryTargetProperties> getQueries()
    {
        return queries;
    }


    /**
     * Set up the list of individual query targets for a derived column.
     *
     * @param queries list of queries and their target element
     */
    public void setQueries(List<DerivedSchemaTypeQueryTargetProperties> queries)
    {
        this.queries = queries;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SchemaTypeElement{" +
                       "schemaTypeProperties=" + schemaTypeProperties +
                       ", elementHeader=" + elementHeader +
                       ", attributeCount=" + attributeCount +
                       ", mapFromElement=" + mapFromElement +
                       ", mapToElement=" + mapToElement +
                       ", formula='" + formula + '\'' +
                       ", formulaType='" + formulaType + '\'' +
                       ", queries=" + queries +
                       '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        SchemaTypeElement that = (SchemaTypeElement) objectToCompare;
        return attributeCount == that.attributeCount &&
                       Objects.equals(schemaTypeProperties, that.schemaTypeProperties) &&
                       Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(mapFromElement, that.mapFromElement) &&
                       Objects.equals(mapToElement, that.mapToElement) &&
                       Objects.equals(externalSchemaType, that.externalSchemaType) &&
                       Objects.equals(schemaOptions, that.schemaOptions) &&
                       Objects.equals(formula, that.formula) &&
                       Objects.equals(formulaType, that.formulaType) &&
                       Objects.equals(queries, that.queries);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(schemaTypeProperties, elementHeader, attributeCount, mapFromElement, mapToElement, externalSchemaType, schemaOptions,
                            formula, formulaType, queries);
    }
}
