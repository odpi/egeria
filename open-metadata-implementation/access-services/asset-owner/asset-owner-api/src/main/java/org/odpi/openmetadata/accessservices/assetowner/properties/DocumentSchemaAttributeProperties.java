/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A DocumentSchemaAttributeProperties defines an attribute in a hierarchical document structure such as an
 * XML document.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentSchemaAttributeProperties extends SchemaAttributeProperties
{
    private static final long     serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public DocumentSchemaAttributeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DocumentSchemaAttributeProperties(DocumentSchemaAttributeProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DocumentSchemaAttributeProperties{" +
                "elementPosition=" + getElementPosition() +
                ", minCardinality=" + getMinCardinality() +
                ", maxCardinality=" + getMaxCardinality() +
                ", allowsDuplicateValues=" + getAllowsDuplicateValues() +
                ", orderedValues=" + getOrderedValues() +
                ", sortOrder=" + getSortOrder() +
                ", minimumLength=" + getMinimumLength() +
                ", length=" + getLength() +
                ", significantDigits=" + getPrecision() +
                ", nullable=" + getIsNullable() +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", attributeType=" + getAttributeType() +
                ", attributeRelationships=" + getAttributeRelationships() +
                ", nativeJavaClass='" + getNativeJavaClass() + '\'' +
                ", aliases=" + getAliases() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", deprecated=" + getIsDeprecated() +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}