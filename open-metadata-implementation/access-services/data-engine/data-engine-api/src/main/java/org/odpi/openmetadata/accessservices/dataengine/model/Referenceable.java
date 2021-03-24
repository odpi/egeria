package org.odpi.openmetadata.accessservices.dataengine.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Referenceable implements Serializable {
    private static final long serialVersionUID = 1L;

    private String qualifiedName;
    private Map<String, String> additionalProperties;
    private Map<String, String> vendorProperties;
    private String typeName;
    private Map<String, Object> extendedProperties;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Referenceable that = (Referenceable) o;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(vendorProperties, that.vendorProperties) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(extendedProperties, that.extendedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, additionalProperties, vendorProperties, typeName, extendedProperties);
    }

    @Override
    public String toString() {
        return "Referenceable{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", vendorProperties=" + vendorProperties +
                ", typeName='" + typeName + '\'' +
                ", extendedProperties=" + extendedProperties +
                '}';
    }
}
