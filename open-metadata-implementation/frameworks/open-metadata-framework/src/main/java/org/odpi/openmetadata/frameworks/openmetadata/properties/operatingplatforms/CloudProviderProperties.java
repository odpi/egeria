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
 * CloudProviderProperties describes a cloud provider.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CloudProviderProperties extends ClassificationBeanProperties
{
    private String providerName = null;


    /**
     * Default constructor
     */
    public CloudProviderProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CLOUD_PROVIDER_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CloudProviderProperties(CloudProviderProperties template)
    {
        super(template);

        if (template != null)
        {
            providerName = template.getProviderName();
        }
    }


    /**
     * Return the name of the cloud provider.
     *
     * @return string name - often a URL
     */
    public String getProviderName()
    {
        return providerName;
    }


    /**
     * Set up the name of the cloud provider.
     *
     * @param providerName string name - often a URL
     */
    public void setProviderName(String providerName)
    {
        this.providerName = providerName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CloudProviderProperties{" +
                "providerName='" + providerName + '\'' +
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
        CloudProviderProperties that = (CloudProviderProperties) objectToCompare;
        return Objects.equals(providerName, that.providerName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), providerName);
    }
}
