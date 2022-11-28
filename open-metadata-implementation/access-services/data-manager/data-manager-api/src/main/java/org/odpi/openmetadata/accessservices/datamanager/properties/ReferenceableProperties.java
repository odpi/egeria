/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetProperties.class, name = "AssetProperties"),
                @JsonSubTypes.Type(value = ConnectionProperties.class, name = "ConnectionProperties"),
                @JsonSubTypes.Type(value = ConnectorTypeProperties.class, name = "ConnectorTypeProperties"),
                @JsonSubTypes.Type(value = DatabaseSchemaTypeProperties.class, name = "DatabaseSchemaTypeProperties"),
                @JsonSubTypes.Type(value = EndpointProperties.class, name = "EndpointProperties"),
                @JsonSubTypes.Type(value = SchemaElementProperties.class, name = "SchemaElementProperties"),
                @JsonSubTypes.Type(value = SoftwareCapabilitiesProperties.class, name = "SoftwareCapabilitiesProperties"),
                @JsonSubTypes.Type(value = ValidValueSetProperties.class, name = "ValidValueSetProperties"),
                @JsonSubTypes.Type(value = ValidValueProperties.class, name = "ValidValueProperties"),
        })
public class ReferenceableProperties implements Serializable
{
    private static final long    serialVersionUID = 1L;


    private String               qualifiedName        = null;
    private Map<String, String>  additionalProperties = null;

    private Date                 effectiveFrom        = null;
    private Date                 effectiveTo          = null;

    private Map<String, String>  vendorProperties     = null;

    private String               typeName             = null;
    private Map<String, Object>  extendedProperties   = null;

    /**
     * Default constructor
     */
    public ReferenceableProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves the values from the supplied template
     *
     * @param template element to copy
     */
    public ReferenceableProperties(ReferenceableProperties template)
    {
        if (template != null)
        {
            qualifiedName        = template.getQualifiedName();
            additionalProperties = template.getAdditionalProperties();

            effectiveFrom        = template.getEffectiveFrom();
            effectiveTo          = template.getEffectiveTo();

            vendorProperties     = template.getVendorProperties();

            typeName             = template.getTypeName();
            extendedProperties   = template.getExtendedProperties();
        }
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
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return specific properties for the data manager vendor.
     *
     * @return name value pairs
     */
    public Map<String, String> getVendorProperties()
    {
        if (vendorProperties == null)
        {
            return null;
        }
        else if (vendorProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(vendorProperties);
        }
    }


    /**
     * Set up specific properties for the data manager vendor.
     *
     * @param vendorProperties name value pairs
     */
    public void setVendorProperties(Map<String, String> vendorProperties)
    {
        this.vendorProperties = vendorProperties;
    }


    /**
     * Return the name of the open metadata type for this metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name of the open metadata type for this element.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReferenceableProperties{" +
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", additionalProperties=" + additionalProperties +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", vendorProperties=" + vendorProperties +
                       ", typeName='" + typeName + '\'' +
                       ", extendedProperties=" + extendedProperties +
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
        ReferenceableProperties that = (ReferenceableProperties) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(additionalProperties, that.additionalProperties) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
                       Objects.equals(vendorProperties, that.vendorProperties) &&
                       Objects.equals(typeName, that.typeName) &&
                       Objects.equals(extendedProperties, that.extendedProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, additionalProperties, effectiveFrom, effectiveTo, vendorProperties, typeName, extendedProperties);
    }
}