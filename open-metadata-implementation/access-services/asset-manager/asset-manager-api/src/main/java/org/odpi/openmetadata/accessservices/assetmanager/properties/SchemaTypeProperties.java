/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.ArrayList;
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
                      @JsonSubTypes.Type(value = LiteralSchemaTypeProperties.class, name = "LiteralSchemaTypeProperties"),
                      @JsonSubTypes.Type(value = SimpleSchemaTypeProperties.class, name = "SimpleSchemaTypeProperties"),
                      @JsonSubTypes.Type(value = SchemaTypeChoiceProperties.class, name = "SchemaTypeChoiceProperties"),
                      @JsonSubTypes.Type(value = MapSchemaTypeProperties.class, name = "MapSchemaTypeProperties"),
              })
public class SchemaTypeProperties extends SchemaProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String versionNumber    = null;
    private String author           = null;
    private String encodingStandard = null;
    private String namespace        = null;
    private String usage            = null;

    /*
     * Values for when the schemaType is derived from other values rather than stored
     */
    private String                      formula = null;
    private String                      formulaType = null;
    private List<QueryTargetProperties> queries = null;

    /**
     * Default constructor
     */
    public SchemaTypeProperties()
    {
        super();
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
            versionNumber = template.getVersionNumber();
            author = template.getAuthor();
            usage = template.getUsage();
            encodingStandard = template.getEncodingStandard();
            namespace = template.getNamespace();
            formula = template.getFormula();
            formulaType = template.getFormulaType();
            queries = template.getQueries();
        }
    }


    /**
     * Return the version number of the schema element - null means no version number.
     *
     * @return String version number
     */
    public String getVersionNumber() { return versionNumber; }


    /**
     * Set up the version number of the schema element - null means no version number.
     *
     * @param versionNumber String version number
     */
    public void setVersionNumber(String versionNumber)
    {
        this.versionNumber = versionNumber;
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
     * Return details of the expected usage of this technical element.
     *
     * @return string text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up details of the expected usage of this technical element.
     *
     * @param usage string text
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
    public List<QueryTargetProperties> getQueries()
    {
        if (queries == null)
        {
            return null;
        }
        else if (queries.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(queries);
        }
    }


    /**
     * Set up the list of queries that are used to create the derived schema element.
     *
     * @param queries list of queries
     */
    public void setQueries(List<QueryTargetProperties> queries)
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
                       "qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", versionNumber='" + versionNumber + '\'' +
                       ", author='" + author + '\'' +
                       ", usage='" + usage + '\'' +
                       ", encodingStandard='" + encodingStandard + '\'' +
                       ", namespace='" + namespace + '\'' +
                       ", formula='" + formula + '\'' +
                       ", formulaType='" + formulaType + '\'' +
                       ", queries=" + queries +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaTypeProperties that = (SchemaTypeProperties) objectToCompare;
        return Objects.equals(versionNumber, that.versionNumber) &&
                       Objects.equals(author, that.author) &&
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
        return Objects.hash(super.hashCode(), versionNumber, author, usage, encodingStandard, namespace, formula, formulaType, queries);
    }
}
