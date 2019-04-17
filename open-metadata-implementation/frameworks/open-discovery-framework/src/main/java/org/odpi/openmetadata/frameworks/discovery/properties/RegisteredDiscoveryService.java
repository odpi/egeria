/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredDiscoveryService describes a discovery service that has been registered with a discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredDiscoveryService extends DiscoveryServiceProperties
{
    protected List<String> assetTypes = null;


    /**
     * Default constructor
     */
    public RegisteredDiscoveryService()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredDiscoveryService(RegisteredDiscoveryService  template)
    {
        super(template);

        if (template != null)
        {
            assetTypes = template.getAssetTypes();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredDiscoveryService(DiscoveryServiceProperties  template)
    {
        super(template);
    }


    /**
     * Return the list of asset types that this discovery service supports.
     *
     * @return list of asset type names
     */
    public List<String> getAssetTypes()
    {
        if (assetTypes == null)
        {
            return null;
        }
        else if (assetTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(assetTypes);
        }
    }


    /**
     * Set up the list of asset types that this discovery service supports.
     *
     * @param assetTypes list of asset type names
     */
    public void setAssetTypes(List<String> assetTypes)
    {
        this.assetTypes = assetTypes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RegisteredDiscoveryService{" +
                "assetTypes=" + assetTypes +
                ", displayName='" + displayName + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                ", meanings=" + meanings +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        RegisteredDiscoveryService that = (RegisteredDiscoveryService) objectToCompare;
        return Objects.equals(getAssetTypes(), that.getAssetTypes());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAssetTypes());
    }
}
