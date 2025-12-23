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
 * CloudServiceProperties described a cloud service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CloudServiceProperties extends ClassificationBeanProperties
{
    private String offeringName = null;
    private String serviceType  = null;


    /**
     * Default constructor
     */
    public CloudServiceProperties()
    {
        super();
        super.typeName = OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CloudServiceProperties(CloudServiceProperties template)
    {
        super(template);

        if (template != null)
        {
            offeringName = template.getOfferingName();
            serviceType  = template.getServiceType();
        }
    }


    /**
     * Return the name of the tenant.
     *
     * @return string coordinates
     */
    public String getOfferingName()
    {
        return offeringName;
    }


    /**
     * Set up the name of the tenant.
     *
     * @param offeringName string coordinates
     */
    public void setOfferingName(String offeringName)
    {
        this.offeringName = offeringName;
    }


    /**
     * Return the type of the tenant.
     *
     * @return name
     */
    public String getServiceType()
    {
        return serviceType;
    }


    /**
     * Set up the type of the tenant
     *
     * @param serviceType name
     */
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CloudServiceProperties{" +
                "offeringName='" + offeringName + '\'' +
                ", serviceType='" + serviceType + '\'' +
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
        CloudServiceProperties that = (CloudServiceProperties) objectToCompare;
        return Objects.equals(offeringName, that.offeringName) &&
                       Objects.equals(serviceType, that.serviceType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), offeringName, serviceType);
    }
}
