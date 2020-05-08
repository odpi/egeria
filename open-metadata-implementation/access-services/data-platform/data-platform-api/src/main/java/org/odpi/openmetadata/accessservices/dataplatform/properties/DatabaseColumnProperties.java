/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseColumnProperties describes the properties of a database column.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class DatabaseColumnProperties extends TabularColumnProperties
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public DatabaseColumnProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DatabaseColumnProperties(DatabaseColumnProperties template)
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
        return "DatabaseColumnProperties{" +
                "dataType='" + getDataType() + '\'' +
                ", defaultValue='" + getDefaultValue() + '\'' +
                ", elementPosition=" + getElementPosition() +
                ", minCardinality=" + getMinCardinality() +
                ", maxCardinality=" + getMaxCardinality() +
                ", allowsDuplicateValues=" + isAllowsDuplicateValues() +
                ", orderedValues=" + isOrderedValues() +
                ", sortOrder=" + getSortOrder() +
                ", minimumLength=" + getMinimumLength() +
                ", length=" + getLength() +
                ", significantDigits=" + getSignificantDigits() +
                ", nullable=" + isNullable() +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", anchorGUID='" + getAnchorGUID() + '\'' +
                ", nativeJavaClass='" + getNativeJavaClass() + '\'' +
                ", aliases=" + getAliases() +
                ", deprecated=" + isDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", classifications=" + getClassifications() +
                ", vendorProperties=" + getVendorProperties() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
