/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ServerAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerAssetUseProperties describes the properties for the ServerAssetUse relationship between a software
 * server capability and an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerAssetUseProperties extends RelationshipProperties
{
    private ServerAssetUseType useType     = null;
    private String             description = null;
    private boolean            minimumInstancesSet = false;
    private int                minimumInstances = 0;
    private boolean            maximumInstancesSet = false;
    private int                maximumInstances = 0;


    /**
     * Default constructor
     */
    public ServerAssetUseProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerAssetUseProperties(ServerAssetUseProperties template)
    {
        super(template);

        if (template != null)
        {
            this.useType = template.getUseType();
            this.description = template.getDescription();
            this.minimumInstancesSet = template.getMinimumInstancesSet();
            this.minimumInstances = template.getMinimumInstances();
            this.maximumInstancesSet = template.getMaximumInstancesSet();
            this.maximumInstances = template.getMaximumInstances();
        }
    }


    /**
     * Return the types of interactions that the software server capability may have with the asset.
     *
     * @return ServerAssetUseType enum
     */
    public ServerAssetUseType getUseType()
    {
        return useType;
    }


    /**
     * Set up the types of interactions that the software server capability may have with the asset.
     *
     * @param useType ServerAssetUseType enum
     */
    public void setUseType(ServerAssetUseType useType)
    {
        this.useType = useType;
    }


    /**
     * Return the description of the relationship between the software server capability and the asset.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the relationship between the software server capability and the asset.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return whether the minimum instances value is set up or just default.
     *
     * @return flag
     */
    public boolean getMinimumInstancesSet()
    {
        return minimumInstancesSet;
    }


    /**
     * Set up whether the minimum instances value is set up or just default.
     *
     * @param minimumInstancesSet flag
     */
    public void setMinimumInstancesSet(boolean minimumInstancesSet)
    {
        this.minimumInstancesSet = minimumInstancesSet;
    }


    /**
     * Return the minimum number of running asset instances controlled by the software server capability.
     *
     * @return integer
     */
    public int getMinimumInstances()
    {
        return minimumInstances;
    }


    /**
     * Set up the minimum number of running asset instances controlled by the software server capability.
     *
     * @param minimumInstances integer
     */
    public void setMinimumInstances(int minimumInstances)
    {
        this.minimumInstances = minimumInstances;
    }


    /**
     * Return whether the maximum instances value is set up or just default.
     *
     * @return flag
     */
    public boolean getMaximumInstancesSet()
    {
        return maximumInstancesSet;
    }


    /**
     * Set up whether the maximum instances value is set up or just default.
     *
     * @param maximumInstancesSet flag
     */
    public void setMaximumInstancesSet(boolean maximumInstancesSet)
    {
        this.maximumInstancesSet = maximumInstancesSet;
    }


    /**
     * Return the maximum number of running asset instances controlled by the software server capability.
     *
     * @return integer
     */
    public int getMaximumInstances()
    {
        return maximumInstances;
    }


    /**
     * Set up the maximum number of running asset instances controlled by the software server capability.
     *
     * @param maximumInstances integer
     */
    public void setMaximumInstances(int maximumInstances)
    {
        this.maximumInstances = maximumInstances;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ServerAssetUseProperties{" +
                       "useType=" + useType +
                       ", description='" + description + '\'' +
                       ", minimumInstancesSet=" + minimumInstancesSet +
                       ", minimumInstances=" + minimumInstances +
                       ", maximumInstancesSet=" + maximumInstancesSet +
                       ", maximumInstances=" + maximumInstances +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ServerAssetUseProperties that = (ServerAssetUseProperties) objectToCompare;
        return minimumInstancesSet == that.minimumInstancesSet &&
                       minimumInstances == that.minimumInstances &&
                       maximumInstancesSet == that.maximumInstancesSet &&
                       maximumInstances == that.maximumInstances &&
                       useType == that.useType &&
                       Objects.equals(description, that.description);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), useType, description, minimumInstancesSet, minimumInstances, maximumInstancesSet, maximumInstances);
    }
}
