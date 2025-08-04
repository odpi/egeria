/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaTypeProperties carries the common parameters for creating or updating schema types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ComplexSchemaTypeProperties.class, name = "ComplexSchemaTypeProperties"),
        @JsonSubTypes.Type(value = ExternalSchemaTypeProperties.class, name = "ExternalSchemaTypeProperties"),
        @JsonSubTypes.Type(value = LiteralSchemaTypeProperties.class, name = "LiteralSchemaTypeProperties"),
        @JsonSubTypes.Type(value = MapSchemaTypeProperties.class, name = "MapSchemaTypeProperties"),
        @JsonSubTypes.Type(value = SchemaTypeChoiceProperties.class, name = "SchemaTypeChoiceProperties"),
        @JsonSubTypes.Type(value = SimpleSchemaTypeProperties.class, name = "SimpleSchemaTypeProperties"),
})
public class SchemaTypeProperties extends SchemaElementProperties
{
    private String author            = null;
    private String usage            = null;
    private String encodingStandard = null;
    private String namespace        = null;

    /*
     * Values for when the schemaType is derived from other values rather than stored
     */
    private String                                       formula     = null;
    private String                                       formulaType = null;
    private List<DerivedSchemaTypeQueryTargetProperties> queries     = null;

    /**
     * Default constructor
     */
    public SchemaTypeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SCHEMA_TYPE.typeName);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public SchemaTypeProperties(SchemaTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            author            = template.getAuthor();
            usage             = template.getUsage();
            encodingStandard  = template.getEncodingStandard();
            namespace         = template.getNamespace();
            formula           = template.getFormula();
            formulaType       = template.getFormulaType();
            queries           = template.getQueries();
        }
    }


    /**
     * Return the name of the author of the schema element.  Null means the author is unknown.
     *
     * @return String author name
     */
    public String getAuthor() { return author; }


    /**
     * Set up the name of the author of the schema element.  Null means the author is unknown.
     *
     * @param author String author name
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }


    /**
     * Return the usage guidance for this schema element. Null means no guidance available.
     *
     * @return String usage guidance
     */
    public String getUsage() { return usage; }


    /**
     * Set up the usage guidance for this schema element. Null means no guidance available.
     *
     * @param usage String usage guidance
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the format (encoding standard) used for this schema.  It may be XML, JSON, SQL DDL or something else.
     * Null means the encoding standard is unknown or there are many choices.
     *
     * @return String encoding standard
     */
    public String getEncodingStandard() { return encodingStandard; }


    /**
     * Set up the format (encoding standard) used for this schema.  It may be XML, JSON, SQL DDL or something else.
     * Null means the encoding standard is unknown or there are many choices.
     *
     * @param encodingStandard String encoding standard
     */
    public void setEncodingStandard(String encodingStandard)
    {
        this.encodingStandard = encodingStandard;
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespace string name
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
     *
     * @return String formula
     */
    public String getFormula() { return formula; }


    /**
     * Set up the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
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
     * Return the list of queries that are used to create the derived schema element.
     *
     * @return list of queries
     */
    public List<DerivedSchemaTypeQueryTargetProperties> getQueries()
    {
        return queries;
    }


    /**
     * Set up the list of queries that are used to create the derived schema element.
     *
     * @param queries list of queries
     */
    public void setQueries(List<DerivedSchemaTypeQueryTargetProperties> queries)
    {
        this.queries = queries;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaTypeProperties{" +
                "author='" + author + '\'' +
                ", usage='" + usage + '\'' +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", namespace='" + namespace + '\'' +
                ", formula='" + formula + '\'' +
                ", formulaType='" + formulaType + '\'' +
                ", queries=" + queries +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaTypeProperties that = (SchemaTypeProperties) objectToCompare;
        return Objects.equals(author, that.author) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(encodingStandard, that.encodingStandard) &&
                Objects.equals(namespace, that.namespace) &&
                Objects.equals(formula, that.formula) &&
                Objects.equals(formulaType, that.formulaType) &&
                Objects.equals(queries, that.queries);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), author, usage, encodingStandard, namespace, formula, formulaType, queries);
    }
}
