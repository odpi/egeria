/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CloudTenantProperties described a cloud tenant.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CloudTenantProperties extends ClassificationBeanProperties
{
    private String tenantName    = null;
    private String tenantType    = null;


    /**
     * Default constructor
     */
    public CloudTenantProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CloudTenantProperties(CloudTenantProperties template)
    {
        super(template);

        if (template != null)
        {
            tenantName    = template.getTenantName();
            tenantType    = template.getTenantType();
        }
    }


    /**
     * Return the name of the tenant.
     *
     * @return string coordinates
     */
    public String getTenantName()
    {
        return tenantName;
    }


    /**
     * Set up the name of the tenant.
     *
     * @param tenantName string coordinates
     */
    public void setTenantName(String tenantName)
    {
        this.tenantName = tenantName;
    }


    /**
     * Return the type of the tenant.
     *
     * @return name
     */
    public String getTenantType()
    {
        return tenantType;
    }


    /**
     * Set up the type of the tenant
     *
     * @param tenantType name
     */
    public void setTenantType(String tenantType)
    {
        this.tenantType = tenantType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CloudTenantProperties{" +
                "tenantName='" + tenantName + '\'' +
                ", tenantType='" + tenantType + '\'' +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        CloudTenantProperties that = (CloudTenantProperties) objectToCompare;
        return Objects.equals(tenantName, that.tenantName) &&
                       Objects.equals(tenantType, that.tenantType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), tenantName, tenantType);
    }
}
