/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ITInfrastructureProperties is a java bean used to create software servers, hosts and platforms.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = HostProperties.class, name = "HostProperties"),
                @JsonSubTypes.Type(value = SoftwareServerProperties.class, name = "SoftwareServerProperties"),
                @JsonSubTypes.Type(value = SoftwareServerPlatformProperties.class, name = "SoftwareServerPlatformProperties"),
        })
public class ITInfrastructureProperties extends AssetProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String deployedImplementationTypeProperty = OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name;


    private String deployedImplementationType = null;

    /**
     * Default constructor
     */
    public ITInfrastructureProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ITInfrastructureProperties(ITInfrastructureProperties template)
    {
        super(template);

        if (template != null)
        {
            this.deployedImplementationType = template.getDeployedImplementationType();
        }
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ITInfrastructureProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            Map<String, Object> assetExtendedProperties = template.getExtendedProperties();

            if (assetExtendedProperties != null)
            {
                if (assetExtendedProperties.get(deployedImplementationTypeProperty) != null)
                {
                    deployedImplementationType = assetExtendedProperties.get(deployedImplementationTypeProperty).toString();
                    assetExtendedProperties.remove(deployedImplementationTypeProperty);
                }

                super.setExtendedProperties(assetExtendedProperties);
            }
        }
    }


    /**
     * Convert this object into an AssetProperties object.  This involves packing the properties introduced at this level
     * into the extended properties.
     *
     * @param subTypeName subtype name
     * @return asset properties
     */
    @Override
    public AssetProperties cloneToAsset(String subTypeName)
    {
        AssetProperties assetProperties = super.cloneToAsset(subTypeName);

        Map<String, Object> assetExtendedProperties = assetProperties.getExtendedProperties();

        if (assetExtendedProperties == null)
        {
            assetExtendedProperties = new HashMap<>();
        }

        if (deployedImplementationType != null)
        {
            assetExtendedProperties.put(deployedImplementationTypeProperty, deployedImplementationType);
        }

        if (! assetExtendedProperties.isEmpty())
        {
            assetProperties.setExtendedProperties(assetExtendedProperties);
        }

        return assetProperties;
    }



    /**
     * Return the type description for the technology's type.
     *
     * @return type description string
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the type description for the technology's type.
     *
     * @param platformType type description string
     */
    public void setDeployedImplementationType(String platformType)
    {
        this.deployedImplementationType = platformType;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ITInfrastructureProperties{" +
                "deployedImplementationType='" + deployedImplementationType + '\'' +
                "} " + super.toString();
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
        SoftwareServerPlatformProperties that = (SoftwareServerPlatformProperties) objectToCompare;
        return Objects.equals(deployedImplementationType, that.getDeployedImplementationType());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deployedImplementationType);
    }
}
