/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeEmbeddedAttributeProperties carries the properties for a classification that represents a schema type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeEmbeddedAttributeProperties extends ClassificationBeanProperties
{
    private String              schemaTypeName       = null;
    private String              qualifiedName        = null;
    private String              displayName          = null;
    private String              description          = null;
    private String              versionIdentifier    = null;
    private String              category             = null;
    private Map<String, String> additionalProperties = null;
    private String              usage                = null;
    private String              encodingStandard     = null;
    private String              namespace            = null;
    private String              dataType             = null;
    private String              defaultValue         = null;
    private String              fixedValue           = null;



    /**
     * Default constructor
     */
    public TypeEmbeddedAttributeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public TypeEmbeddedAttributeProperties(TypeEmbeddedAttributeProperties template)
    {
        super(template);

        if (template != null)
        {
            schemaTypeName       = template.getSchemaTypeName();
            qualifiedName        = template.getQualifiedName();
            displayName          = template.getDisplayName();
            description          = template.getDescription();
            versionIdentifier    = template.getVersionIdentifier();
            category             = template.getCategory();
            additionalProperties = template.getAdditionalProperties();
            usage                = template.getUsage();
            encodingStandard     = template.getEncodingStandard();
            namespace            = template.getNamespace();
            dataType             = template.getDataType();
            defaultValue         = template.getDefaultValue();
            fixedValue           = template.getFixedValue();
        }
    }


    /**
     * Return the schema type's type name
     *
     * @return open metadata type name
     */
    public String getSchemaTypeName()
    {
        return schemaTypeName;
    }


    /**
     * Set up the schema type's type name
     *
     * @param schemaTypeName open metadata type name
     */
    public void setSchemaTypeName(String schemaTypeName)
    {
        this.schemaTypeName = schemaTypeName;
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Returns the stored display name property for the element.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the element.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the element.
     * If no description is provided then null is returned.
     *
     * @return  String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the element.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * Return the author-controlled version identifier.
     *
     * @return version identifier
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the author-controlled version identifier.
     *
     * @param versionIdentifier version identifier
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
    }


    /**
     * Return a descriptive name for the element's category.
     *
     * @return string name
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * Set up a descriptive name for the element's category. This field can be used to group related elements together
     * that are used for the same activity, event through they may be different types.
     *
     * @param category string name
     */
    public void setCategory(String category)
    {
        this.category = category;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
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
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return the default value for the element.  Null means no default value set up.
     *
     * @return String containing default value
     */
    public String getDefaultValue() { return defaultValue; }


    /**
     * Set up the default value for the element.  Null means no default value set up.
     *
     * @param defaultValue String containing default value
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    /**
     * Return the fixed value for the element.  Null means fixed value is null.
     *
     * @return String containing fixed value
     */
    public String getFixedValue() { return fixedValue; }


    /**
     * Set up the fixed value for the element.  Null means fixed value is null.
     *
     * @param fixedValue String containing fixed value
     */
    public void setFixedValue(String fixedValue)
    {
        this.fixedValue = fixedValue;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TypeEmbeddedAttributeProperties{" +
                "schemaTypeName='" + schemaTypeName + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", category='" + category + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", usage='" + usage + '\'' +
                ", encodingStandard='" + encodingStandard + '\'' +
                ", namespace='" + namespace + '\'' +
                ", dataType='" + dataType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", fixedValue='" + fixedValue + '\'' +
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
        TypeEmbeddedAttributeProperties that = (TypeEmbeddedAttributeProperties) objectToCompare;
        return Objects.equals(schemaTypeName, that.schemaTypeName) &&
                Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(versionIdentifier, that.versionIdentifier) &&
                Objects.equals(category, that.category) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(encodingStandard, that.encodingStandard) &&
                Objects.equals(namespace, that.namespace) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(fixedValue, that.fixedValue);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), schemaTypeName, qualifiedName, displayName, description, versionIdentifier, category, additionalProperties, usage, encodingStandard, namespace, dataType, defaultValue, fixedValue);
    }
}
