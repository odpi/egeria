/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * <p>
 *     The SchemaType object provides a base class for the pieces that make up a schema for a data asset.
 *     A schema provides information about how the data is structured in the asset.  Schemas are typically
 *     described as nested structures of linked schema elements.  Schemas can also be reused in other schemas.
 * </p>
 * <p>
 *     Schema type has a number of subtypes that hold additional properties.
 *     <ul>
 *         <li>PrimitiveSchemaType is for a leaf element in a schema.</li>
 *         <li>MapSchemaType is for an attribute of type Map</li>
 *         <li>APIOperation is for operations in an API</li>
 *     </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ComplexSchemaType.class, name = "ComplexSchemaType"),
                @JsonSubTypes.Type(value = MapSchemaType.class, name = "MapSchemaType"),
                @JsonSubTypes.Type(value = APISchemaType.class, name = "APISchemaType"),
                @JsonSubTypes.Type(value = APIOperation.class, name = "APIOperation"),
                @JsonSubTypes.Type(value = LiteralSchemaType.class, name = "LiteralSchemaType"),
                @JsonSubTypes.Type(value = SimpleSchemaType.class, name = "SimpleSchemaType"),
                @JsonSubTypes.Type(value = SchemaTypeChoice.class, name = "SchemaTypeChoice"),
                @JsonSubTypes.Type(value = ExternalSchemaType.class, name = "ExternalSchemaType"),
        })
@SuppressWarnings(value = "deprecation")
public class SchemaType extends SchemaElement
{
    protected String              versionNumber    = null;
    protected String              author           = null;
    protected String              usage            = null;
    protected String              encodingStandard = null;
    protected String              namespace        = null;

    /*
     * Values for when the schemaType is derived from other values rather than stored
     */
    protected String                             formula = null;
    protected String                             formulaType = null;
    protected List<DerivedSchemaTypeQueryTarget> queries = null;


    /**
     * Default constructor
     */
    public SchemaType()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public SchemaType(SchemaType template)
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
    public List<DerivedSchemaTypeQueryTarget> getQueries()
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
    public void setQueries(List<DerivedSchemaTypeQueryTarget> queries)
    {
        this.queries = queries;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new SchemaType(this);
    }


    /**
     * Return a clone of this schema type.
     *
     * @return Clone of subclass.
     */
    public  SchemaType cloneSchemaType()
    {
        return new SchemaType(this);
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaType{" +
                "versionNumber='" + versionNumber + '\'' +
                ", author='" + author + '\'' +
                ", usage='" + usage + '\'' +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", namespace='" + namespace + '\'' +
                ", formula='" + formula + '\'' +
                ", formulaType='" + formulaType + '\'' +
                ", queries=" + queries +
                ", isDeprecated=" + isDeprecated +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", isCalculatedValue=" + isCalculatedValue +
                ", expression='" + expression + '\'' +
                ", expressionType='" + expressionType + '\'' +
                ", meanings=" + meanings +
                ", searchKeywords=" + searchKeywords +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", url='" + url + '\'' +
                ", extendedProperties=" + extendedProperties +
                ", cloneSchemaElement=" + cloneSchemaElement() +
                ", cloneSchemaType=" + cloneSchemaType() +
                ", isDeprecated=" + getIsDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", calculatedValue=" + getIsCalculatedValue() +
                ", expression='" + getExpression() + '\'' +
                ", meanings=" + getMeanings() +
                ", searchKeywords=" + getSearchKeywords() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", URL='" + getURL() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                ", GUID='" + getGUID() + '\'' +
                ", classifications=" + getClassifications() +
                ", status=" + getStatus() +
                ", type=" + getType() +
                ", origin=" + getOrigin() +
                ", versions=" + getVersions() +
                ", headerVersion=" + getHeaderVersion() +
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
        SchemaType that = (SchemaType) objectToCompare;
        return Objects.equals(getVersionNumber(), that.getVersionNumber()) &&
                       Objects.equals(getAuthor(), that.getAuthor()) &&
                       Objects.equals(getUsage(), that.getUsage()) &&
                       Objects.equals(getEncodingStandard(), that.getEncodingStandard()) &&
                       Objects.equals(getNamespace(), that.getNamespace()) &&
                       Objects.equals(getFormula(), that.getFormula()) &&
                       Objects.equals(getFormulaType(), that.getFormulaType()) &&
                       Objects.equals(getQueries(), that.getQueries());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getVersionNumber(), getAuthor(), getUsage(), getEncodingStandard(), getNamespace(), getFormula(),
                            getFormulaType(), getQueries());
    }
}
