/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIParameterProperties is a class for representing a parameter in an API specification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIParameterProperties extends SchemaAttributeProperties
{
    private static final long    serialVersionUID = 1L;

    private String parameterType = null;


    /**
     * Default constructor
     */
    public APIParameterProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public APIParameterProperties(APIParameterProperties template)
    {
        super(template);

        if (template != null)
        {
            parameterType = template.getParameterType();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public APIParameterProperties(SchemaAttributeProperties template)
    {
        super(template);

        if (template != null)
        {
            if (getExtendedProperties() != null)
            {
                Map<String, Object> extendedProperties = getExtendedProperties();

                parameterType = extendedProperties.get("parameterType").toString();

                extendedProperties.remove("parameterType");

                super.setExtendedProperties(extendedProperties);
            }
        }
    }


    /**
     * Return the type of parameter - for example for REST APIs, is it a PathVariable or a RequestParameter?
     *
     * @return string name
     */
    public String getParameterType()
    {
        return parameterType;
    }


    /**
     * Set up the type of parameter - for example for REST APIs, is it a PathVariable or a RequestParameter?
     *
     * @param parameterType string name
     */
    public void setParameterType(String parameterType)
    {
        this.parameterType = parameterType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "APIParameterProperties{" +
                       "parameterType='" + parameterType + '\'' +
                       ", elementPosition=" + getElementPosition() +
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
        APIParameterProperties that = (APIParameterProperties) objectToCompare;
        return Objects.equals(parameterType, that.parameterType);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), parameterType);
    }
}
