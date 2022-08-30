/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * <p>
 *     The SchemaElement object provides a base class for the pieces that make up a schema for an asset.
 *     A schema provides information about how the data is structured in the asset.  Schemas are typically
 *     described as nested structures of schema elements.  There are two basic types:
 * </p>
 *     <ul>
 *         <li>SchemaType describes the structure of data.</li>
 *         <li>SchemaAttribute describes the use of another schema as part of the structure within a bigger schema.</li>
 *     </ul>
 * <p>
 *     Assets are linked to a SchemaType.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = SchemaAttribute.class, name = "SchemaAttribute"),
                @JsonSubTypes.Type(value = SchemaType.class, name = "SchemaType")
        })

public abstract class SchemaElement extends GovernedReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected boolean isDeprecated = false;
    protected String  displayName = null;
    protected String  description = null;
    protected String  anchorGUID = null;

    /*
     * Some schema elements are calculated values rather than stored values.  These values are stored in the CalculatedValue
     * classification.  They logically belong to the SchemaType, but they appear on the Schema Attribute if the type information
     * us stored in TypeEmbeddedAttribute classification
     */
    protected boolean isCalculatedValue = false;
    protected String  expression        = null;


    /**
     * Default constructor
     */
    public SchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public SchemaElement(SchemaElement template)
    {
        super(template);

        if (template != null)
        {
            isDeprecated = template.getIsDeprecated();
            displayName = template.getDisplayName();
            description = template.getDescription();
            anchorGUID = template.getAnchorGUID();
            isCalculatedValue = template.isCalculatedValue();
            expression = template.getExpression();
        }
    }


    /**
     * Is the schema element deprecated?
     *
     * @return boolean flag
     */
    public boolean getIsDeprecated()
    {
        return isDeprecated;
    }


    /**
     * Set whether the schema element deprecated or not.  Default is false.
     *
     * @param deprecated boolean flag
     */
    public void setIsDeprecated(boolean deprecated)
    {
        isDeprecated = deprecated;
    }


    /**
     * Return the simple name of the schema element.
     *
     * @return string name
     */
    public String  getDisplayName() { return displayName; }


    /**
     * Set up the simple name of the schema element.
     *
     * @param name String display name
     */
    public void setDisplayName(String   name)
    {
        this.displayName = name;
    }


    /**
     * Returns the stored description property for the schema element.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the schema element.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier of the asset that this schema is ultimately connected to.
     *
     * @return string guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the asset that this schema is ultimately connected to.
     *
     * @param anchorGUID string guid
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return a boolean to indicate if the value for this attribute is stored or calculated.
     * The expression for calculating the value is set if this value is true.
     *
     * @return boolean true for calculated value; false for stored value
     */
    public boolean isCalculatedValue()
    {
        return isCalculatedValue;
    }


    /**
     * Set up a boolean to indicate if the value for this attribute is stored or calculated.
     * The expression for calculating the value is set if this value is true.
     *
     * @param calculatedValue boolean true for calculated value; false for stored value
     */
    public void setCalculatedValue(boolean calculatedValue)
    {
        isCalculatedValue = calculatedValue;
    }


    /**
     * Return the expression used to calculate the value for the schema attribute.
     *
     * @return string expression (any language)
     */
    public String getExpression()
    {
        return expression;
    }


    /**
     * Set up the expression used to calculate the value for the schema attribute.
     *
     * @param expression string expression (any language)
     */
    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    /**
     * Return a clone of this schema element.  This method is needed because schema element
     * is abstract.
     *
     * @return Clone of subclass.
     */
    public abstract SchemaElement cloneSchemaElement();


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaElement{" +
                "isDeprecated=" + isDeprecated +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", anchorGUID='" + anchorGUID + '\'' +
                ", isCalculatedValue=" + isCalculatedValue +
                ", expression='" + expression + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", securityTags=" + getSecurityTags() +
                ", searchKeywords=" + getSearchKeywords() +
                ", latestChange='" + getLatestChange() + '\'' +
                ", latestChangeDetails=" + getLatestChangeDetails() +
                ", confidentialityGovernanceClassification=" + getConfidentialityGovernanceClassification() +
                ", confidenceGovernanceClassification=" + getConfidenceGovernanceClassification() +
                ", criticalityGovernanceClassification=" + getCriticalityGovernanceClassification() +
                ", retentionGovernanceClassification=" + getRetentionGovernanceClassification() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
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
        SchemaElement that = (SchemaElement) objectToCompare;
        return isDeprecated == that.isDeprecated &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(anchorGUID, that.anchorGUID);
    }


    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isDeprecated, displayName, description, anchorGUID);
    }
}
