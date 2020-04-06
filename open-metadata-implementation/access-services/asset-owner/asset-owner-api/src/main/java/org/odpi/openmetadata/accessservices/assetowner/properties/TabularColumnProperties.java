/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A TabularColumnProperties defines a column in a simple table.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TabularColumnProperties extends SchemaAttributeProperties
{
    private static final long     serialVersionUID = 1L;


    /**
     * Default constructor
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
                "attributeName='" + getAttributeName() + '\'' +
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
                ", attributeType=" + getAttributeType() +
                ", externalAttributeType=" + getExternalAttributeType() +
                ", attributeRelationships=" + getAttributeRelationships() +
                ", nativeJavaClass='" + getNativeJavaClass() + '\'' +
                ", aliases=" + getAliases() +
                ", deprecated=" + isDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}