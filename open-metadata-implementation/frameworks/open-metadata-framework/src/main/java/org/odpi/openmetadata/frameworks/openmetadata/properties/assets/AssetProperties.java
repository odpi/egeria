/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetProperties describes an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataAssetProperties.class, name = "DataAssetProperties"),
        @JsonSubTypes.Type(value = ProcessProperties.class, name = "ProcessProperties"),
        @JsonSubTypes.Type(value = InfrastructureProperties.class, name = "InfrastructureProperties"),
})
public class AssetProperties extends ReferenceableProperties
{
    private String resourceName               = null;
    private String namespace                  = null;
    private String deployedImplementationType = null;
    private String source                     = null;

    /**
     * Default constructor
     */
    public AssetProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ASSET.typeName);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public AssetProperties(AssetProperties template)
    {
        super(template);

        if (template != null)
        {
            resourceName               = template.getResourceName();
            namespace                  = template.getNamespace();
            deployedImplementationType = template.getDeployedImplementationType();
            source                     = template.getSource();
        }
    }

    /**
     * Return the full name of the resource as it is known in its owning technology.
     *
     * @return string
     */
    public String getResourceName()
    {
        return resourceName;
    }

    /**
     * Set up the full name of the resource as it is known in its owning technology.
     *
     * @param resourceName string
     */
    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespace string name
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Retrieve the name of the technology used for this data asset.
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the name of the technology used for this data asset.
     *
     * @param deployedImplementationType string name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }



    /**
     * Return the source (such as author, vendor or operator) of the asset.
     *
     * @return string name
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source (such as author, vendor or operator) of the asset.
     *
     * @param source string name
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetProperties{" +
                "resourceName='" + resourceName + '\'' +
                ", namespace='" + namespace + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
                ", source='" + source + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AssetProperties that = (AssetProperties) objectToCompare;
        return Objects.equals(namespace, that.namespace) &&
                Objects.equals(resourceName, that.resourceName) &&
                Objects.equals(deployedImplementationType, that.deployedImplementationType) &&
                Objects.equals(source, that.source);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), namespace, resourceName, deployedImplementationType, source);
    }
}