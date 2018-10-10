/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * <p>
 *     The SchemaElement object provides a base class for the pieces that make up a schema for a data asset.
 *     A schema provides information about how the data is structured in the asset.  Schemas are typically
 *     described as nested structures of linked schema elements.  Schemas can also be reused in other schemas.
 * </p>
 *     SchemaElement is an abstract class - used to enable the most accurate and precise mapping of the
 *     elements in a schema to the asset.
 *     <ul>
 *         <li>PrimitiveSchemaElement is for a leaf element in a schema.</li>
 *         <li>Schema is a complex structure of nested schema elements.</li>
 *         <li>MapSchemaElement is for an attribute of type Map</li>
 *         <li>DerivedSchemaElement is for an attribute that is derived from other schema attributes</li>
 *     </ul>
 *     Most assets will be linked to a Schema.
 * <p>
 *     Schema elements can be linked to one another using SchemaLink.
 * </p>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Schema.class, name = "Schema"),
                @JsonSubTypes.Type(value = MapSchemaElement.class, name = "MapSchemaElement"),
                @JsonSubTypes.Type(value = PrimitiveSchemaElement.class, name = "PrimitiveSchemaElement")
        })

public abstract class SchemaElement extends Referenceable
{
    protected String              versionNumber    = null;
    protected String              author           = null;
    protected String              usage            = null;
    protected String              encodingStandard = null;
    protected Map<String, Object> schemaProperties = null;


    /**
     * Default constructor
     */
    public SchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateSchema template object to copy.
     */
    public SchemaElement(SchemaElement templateSchema)
    {
        super(templateSchema);

        if (templateSchema != null)
        {
            versionNumber = templateSchema.getVersionNumber();
            author = templateSchema.getAuthor();
            usage = templateSchema.getUsage();
            encodingStandard = templateSchema.getEncodingStandard();
            schemaProperties = templateSchema.getSchemaProperties();
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because schema element
     * is abstract.
     *
     * @return Either a Schema or a PrimitiveSchemaElement depending on the type of the template.
     */
    public abstract SchemaElement cloneSchemaElement();


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
     * Set up schema properties - these are properties introduced in the implementation specific subclasses of
     * schema element.
     *
     * @param schemaProperties  properties map
     */
    public void setSchemaProperties(Map<String,Object> schemaProperties)
    {
        this.schemaProperties = schemaProperties;
    }


    /**
     * Return Set up schema properties - these are properties introduced in the implementation specific subclasses of
     * schema element.  Null means no properties are available.
     *
     * @return  property map
     */
    public Map<String,Object> getSchemaProperties()
    {
        if (schemaProperties == null)
        {
            return null;
        }
        else if (schemaProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(schemaProperties);
        }
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
        if (!(objectToCompare instanceof SchemaElement))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaElement that = (SchemaElement) objectToCompare;
        return Objects.equals(getVersionNumber(), that.getVersionNumber()) &&
                Objects.equals(getAuthor(), that.getAuthor()) &&
                Objects.equals(getUsage(), that.getUsage()) &&
                Objects.equals(getEncodingStandard(), that.getEncodingStandard());
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaElement{" +
                "versionNumber='" + versionNumber + '\'' +
                ", author='" + author + '\'' +
                ", usage='" + usage + '\'' +
                ", schemaProperties=" + schemaProperties +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
                '}';
    }
}