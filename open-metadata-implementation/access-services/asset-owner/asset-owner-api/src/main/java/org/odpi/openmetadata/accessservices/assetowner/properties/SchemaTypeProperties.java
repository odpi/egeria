/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

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
        @JsonSubTypes.Type(value = LiteralSchemaTypeProperties.class, name = "LiteralSchemaTypeProperties"),
        @JsonSubTypes.Type(value = SimpleSchemaTypeProperties.class, name = "SimpleSchemaTypeProperties"),
        @JsonSubTypes.Type(value = SchemaTypeChoiceProperties.class, name = "SchemaTypeChoiceProperties"),
              })
public class SchemaTypeProperties extends SchemaElementProperties
{
    private static final long     serialVersionUID = 1L;

    private String versionNumber    = null;
    private String author           = null;
    private String usage            = null;
    private String encodingStandard = null;
    private String namespace        = null;

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
        }
    }


    /**
     * Copy/clone operator.
     *
     * @param objectToFill schema type object
     * @return filled object
     */
    public SchemaType cloneProperties(SchemaType objectToFill)
    {
        SchemaType clone = objectToFill;

        if (clone == null)
        {
            clone = new SchemaType();
        }

        ElementType   type = new ElementType();

        type.setElementTypeName(this.typeName);

        clone.setType(type);
        clone.setClassifications(this.getClassifications());
        clone.setQualifiedName(this.getQualifiedName());
        clone.setMeanings(this.getMeanings());
        clone.setAdditionalProperties(this.getAdditionalProperties());
        clone.setExtendedProperties(this.getExtendedProperties());
        clone.setDisplayName(this.getDisplayName());
        clone.setDescription(getDescription());
        clone.setDeprecated(this.isDeprecated());
        clone.setVersionNumber(this.getVersionNumber());
        clone.setAuthor(this.getAuthor());
        clone.setUsage(this.getUsage());
        clone.setEncodingStandard(this.getEncodingStandard());
        clone.setNamespace(this.getNamespace());

        return clone;
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
                Objects.equals(namespace, that.namespace);
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
                "versionNumber='" + versionNumber + '\'' +
                ", author='" + author + '\'' +
                ", usage='" + usage + '\'' +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", namespace='" + namespace + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}