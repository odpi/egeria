/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFieldProperties is a class for representing a data field within a Form, Report or Que
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFieldProperties extends SchemaAttributeProperties
{
    /**
     * Default constructor used by subclasses
     */
    public DataFieldProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DataFieldProperties(DataFieldProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DataFieldProperties(SchemaAttributeProperties template)
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
        return "DataFieldProperties{" +
                       "elementPosition=" + getElementPosition() +
                       ", minCardinality=" + getMinCardinality() +
                       ", maxCardinality=" + getMaxCardinality() +
                       ", allowsDuplicateValues=" + getAllowsDuplicateValues() +
                       ", orderedValues=" + getOrderedValues() +
                       ", sortOrder=" + getSortOrder() +
                       ", minimumLength=" + getMinimumLength() +
                       ", length=" + getLength() +
                       ", precision=" + getPrecision() +
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
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }
}
