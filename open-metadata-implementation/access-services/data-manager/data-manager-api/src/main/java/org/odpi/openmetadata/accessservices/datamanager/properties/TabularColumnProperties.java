/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TabularColumnProperties is a class for representing a column within a table type structure.
 * Tabular columns are schema attributes with a simple type attached
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DatabaseColumnProperties.class, name = "DatabaseColumnProperties"),
        })
public class TabularColumnProperties extends SchemaAttributeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public TabularColumnProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public TabularColumnProperties(TabularColumnProperties template)
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
        return "TabularColumnProperties{" +
                       "elementPosition=" + getElementPosition() +
                       ", minCardinality=" + getMinCardinality() +
                       ", maxCardinality=" + getMaxCardinality() +
                       ", allowsDuplicateValues=" + getAllowsDuplicateValues() +
                       ", orderedValues=" + getOrderedValues() +
                       ", sortOrder=" + getSortOrder() +
                       ", minimumLength=" + getMinimumLength() +
                       ", length=" + getLength() +
                       ", precision=" + getPrecision() +
                       ", significantDigits=" + getSignificantDigits() +
                       ", isNullable=" + getIsNullable() +
                       ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                       ", nativeJavaClass='" + getNativeJavaClass() + '\'' +
                       ", aliases=" + getAliases() +
                       ", dataType='" + getDataType() + '\'' +
                       ", defaultValue='" + getDefaultValue() + '\'' +
                       ", fixedValue='" + getFixedValue() + '\'' +
                       ", externalTypeGUID='" + getExternalTypeGUID() + '\'' +
                       ", validValuesSetGUID='" + getValidValuesSetGUID() + '\'' +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}
